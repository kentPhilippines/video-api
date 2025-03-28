package com.video.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.common.exception.BusinessException;
import com.video.common.response.ResultCode;
import com.video.common.utils.JwtUtils;
import com.video.user.entity.User;
import com.video.user.mapper.UserMapper;
import com.video.user.service.UserService;
import com.video.user.dto.UserDTO;
import com.video.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(User user) {
        // 检查用户名是否可用
        if (!checkUsernameAvailable(user.getUsername())) {
            throw new BusinessException(ResultCode.USER_ACCOUNT_ALREADY_EXIST);
        }
        
        // 检查邮箱是否可用
        if (!checkEmailAvailable(user.getEmail())) {
            throw new BusinessException(ResultCode.USER_ACCOUNT_ALREADY_EXIST);
        }
        
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置默认头像
        if (!StringUtils.hasText(user.getAvatar())) {
            user.setAvatar("https://example.com/default-avatar.png");
        }
        // 设置默认状态
        user.setStatus(1);
        user.setVipStatus(0);
        
        // 保存用户
        save(user);
    }

    @Override
    public String login(String username, String password) {
        // 查询用户
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .one();
                
        // 验证用户是否存在
        if (user == null) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        
        // 验证用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_ACCOUNT_DISABLE);
        }
        
        // 更新登录信息
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);
        
        // 生成token
        return jwtUtils.generateToken(user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        // 不允许修改用户名和密码
        user.setUsername(null);
        user.setPassword(null);
        
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        // 查询用户
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String email) {
        // TODO: 实现邮箱验证和密码重置逻辑
    }

    @Override
    public Page<User> getUserList(Integer pageNum, Integer pageSize, String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getEmail, keyword)
                    .or()
                    .like(User::getPhone, keyword);
        }
        return page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public User getUserVipInfo(Long userId) {
        return getById(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void subscribeVip(Long userId, Integer months) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = user.getVipExpireTime();
        
        // 如果是首次开通或已过期，从当前时间开始计算
        if (expireTime == null || expireTime.isBefore(now)) {
            expireTime = now;
        }
        
        // 更新VIP状态和到期时间
        user.setVipStatus(1);
        user.setVipExpireTime(expireTime.plusMonths(months));
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelVipAutoRenew(Long userId) {
        // TODO: 实现取消自动续费逻辑
    }

    @Override
    public boolean checkUsernameAvailable(String username) {
        return lambdaQuery()
                .eq(User::getUsername, username)
                .count() == 0;
    }

    @Override
    public boolean checkEmailAvailable(String email) {
        return lambdaQuery()
                .eq(User::getEmail, email)
                .count() == 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserDTO userDTO) {
        // 检查用户名是否存在
        if (checkUsernameExists(userDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (userDTO.getEmail() != null && checkEmailExists(userDTO.getEmail())) {
            throw new BusinessException("邮箱已被使用");
        }
        
        // 检查手机号是否存在
        if (userDTO.getPhone() != null && checkPhoneExists(userDTO.getPhone())) {
            throw new BusinessException("手机号已被使用");
        }
        
        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        // 加密密码
        if (userDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        
        // 设置默认值
        user.setStatus(1);
        user.setVipStatus(0);
        
        // 保存用户
        save(user);
        
        return user.getId();
    }

    @Override
    public User createUserFromOAuth(Map<String, Object> oauthInfo) {
        String username = generateUsername(String.valueOf(oauthInfo.get("login")));
        String email = (String) oauthInfo.get("email");
        String avatar = (String) oauthInfo.get("avatar_url");
        
        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setAvatar(avatar);
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // 随机密码
        user.setStatus(1);
        user.setVipStatus(0);
        
        // 保存用户
        save(user);
        
        return user;
    }

    @Override
    public UserVO getUserInfo(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public User getUserByAccount(String account) {
        return lambdaQuery()
                .eq(User::getUsername, account)
                .or()
                .eq(User::getEmail, account)
                .or()
                .eq(User::getPhone, account)
                .one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO userDTO) {
        User user = getById(userDTO.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 检查用户名是否被其他用户使用
        if (userDTO.getUsername() != null && !user.getUsername().equals(userDTO.getUsername())) {
            if (checkUsernameExists(userDTO.getUsername())) {
                throw new BusinessException("用户名已存在");
            }
        }
        
        // 检查邮箱是否被其他用户使用
        if (userDTO.getEmail() != null && !user.getEmail().equals(userDTO.getEmail())) {
            if (checkEmailExists(userDTO.getEmail())) {
                throw new BusinessException("邮箱已被使用");
            }
        }
        
        // 检查手机号是否被其他用户使用
        if (userDTO.getPhone() != null && !user.getPhone().equals(userDTO.getPhone())) {
            if (checkPhoneExists(userDTO.getPhone())) {
                throw new BusinessException("手机号已被使用");
            }
        }
        
        // 更新用户信息
        BeanUtils.copyProperties(userDTO, user);
        updateById(user);
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return lambdaQuery().eq(User::getUsername, username).exists();
    }

    @Override
    public boolean checkEmailExists(String email) {
        return lambdaQuery().eq(User::getEmail, email).exists();
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        return lambdaQuery().eq(User::getPhone, phone).exists();
    }

    @Override
    public void updateLastLogin(Long userId, String ip) {
        lambdaUpdate()
                .eq(User::getId, userId)
                .set(User::getLastLoginTime, LocalDateTime.now())
                .set(User::getLastLoginIp, ip)
                .update();
    }

    /**
     * 生成唯一用户名
     */
    private String generateUsername(String base) {
        String username = base;
        int suffix = 1;
        
        while (checkUsernameExists(username)) {
            username = base + suffix++;
        }
        
        return username;
    }
} 