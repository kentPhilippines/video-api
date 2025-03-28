package com.video.auth.service.impl;

import com.video.auth.dto.*;
import com.video.auth.entity.UserLoginLog;
import com.video.auth.entity.UserToken;
import com.video.auth.entity.UserVerificationCode;
import com.video.auth.mapper.UserLoginLogMapper;
import com.video.auth.mapper.UserOAuthMapper;
import com.video.auth.mapper.UserTokenMapper;
import com.video.auth.mapper.UserVerificationCodeMapper;
import com.video.auth.service.AuthService;
import com.video.auth.vo.LoginVO;
import com.video.common.exception.BusinessException;
import com.video.common.utils.JwtUtils;
import com.video.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final String VERIFICATION_CODE_KEY = "verification_code:%s:%s";
    private static final long VERIFICATION_CODE_EXPIRE = 300; // 5分钟过期
    private static final int MAX_SEND_PER_DAY = 10; // 每天最多发送次数

    private final UserLoginLogMapper loginLogMapper;
    private final UserVerificationCodeMapper verificationCodeMapper;
    private final UserOAuthMapper oAuthMapper;
    private final UserTokenMapper tokenMapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtils redisUtils;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        String password = registerDTO.getPassword();
        String email = registerDTO.getEmail();
        String phone = registerDTO.getPhone();
        String code = registerDTO.getCode();
        
        // 验证验证码
        String account = email != null ? email : phone;
        if (!verifyCode(account, code, 1)) { // 1表示注册验证码类型
            throw new BusinessException("验证码无效或已过期");
        }
        
        // TODO: 检查用户名/邮箱/手机号是否已存在（需要用户服务）
        // if (userService.checkUsernameExists(username)) {
        //     throw new BusinessException("用户名已存在");
        // }
        // if (email != null && userService.checkEmailExists(email)) {
        //     throw new BusinessException("邮箱已被使用");
        // }
        // if (phone != null && userService.checkPhoneExists(phone)) {
        //     throw new BusinessException("手机号已被使用");
        // }
        
        // TODO: 创建用户（需要用户服务）
        // User user = new User();
        // user.setUsername(username);
        // user.setPassword(passwordEncoder.encode(password));
        // user.setEmail(email);
        // user.setPhone(phone);
        // user.setStatus(1);
        // userService.createUser(user);
        
        // 记录注册日志
        UserLoginLog loginLog = new UserLoginLog();
        loginLog.setUserId(1L); // 临时使用固定值，等待用户服务实现
        loginLog.setLoginIp("127.0.0.1");
        loginLog.setLocation("本地");
        loginLog.setDevice("未知");
        loginLog.setLoginType(0); // 0表示注册
        loginLog.setLoginStatus(1);
        loginLog.setLoginMessage("注册成功");
        loginLogMapper.insert(loginLog);
    }

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        String account = loginDTO.getAccount();
        String password = loginDTO.getPassword();
        String code = loginDTO.getCode();
        Integer clientType = loginDTO.getClientType();
        String clientId = loginDTO.getClientId();
        
        // TODO: 获取用户信息（需要用户服务）
        // User user = userService.getUserByAccount(account);
        // if (user == null) {
        //     throw new BusinessException("账号或密码错误");
        // }
        Long userId = 1L; // 临时使用固定值，等待用户服务实现
        String encodedPassword = "$2a$10$..."; // 临时使用固定值，等待用户服务实现
        
        // 检查是否需要验证码
        String today = LocalDateTime.now().toString().substring(0, 10);
        Integer failCount = loginLogMapper.getLoginFailCount(userId, today);
        if (failCount >= 3) {
            if (code == null || !verifyCode(account, code, 2)) {
                throw new BusinessException("请输入正确的验证码");
            }
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, encodedPassword)) {
            // 记录登录失败日志
            saveLoginLog(userId, "127.0.0.1", "本地", "未知", 1, 0, "密码错误");
            
            // 判断是否需要显示验证码
            failCount = loginLogMapper.getLoginFailCount(userId, today);
            if (failCount >= 3) {
                throw new BusinessException("密码错误，请使用验证码登录");
            } else {
                throw new BusinessException("账号或密码错误");
            }
        }
        
        // 生成令牌
        UserToken userToken = generateToken(userId, clientType, clientId);
        
        // 记录登录成功日志
        saveLoginLog(userId, "127.0.0.1", "本地", "未知", 1, 1, "登录成功");
        
        // 返回登录结果
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(userId);
        loginVO.setUsername("user"); // 临时使用固定值，等待用户服务实现
        loginVO.setAccessToken(userToken.getAccessToken());
        loginVO.setRefreshToken(userToken.getRefreshToken());
        loginVO.setAccessTokenExpireIn(7200L);
        loginVO.setRefreshTokenExpireIn(604800L);
        return loginVO;
    }

    @Override
    public LoginVO oauthLogin(OAuthLoginDTO oAuthLoginDTO) {
        // TODO: 实现OAuth登录逻辑
        // 1. 获取OAuth用户信息
        // 2. 关联或创建用户
        // 3. 生成令牌
        // 4. 记录登录日志
        return null;
    }

    @Override
    public void sendVerificationCode(VerificationCodeDTO verificationCodeDTO) {
        String account = verificationCodeDTO.getPhone();
        String type = verificationCodeDTO.getType();

        // 验证账号格式
        boolean isEmail = EMAIL_PATTERN.matcher(account).matches();
        boolean isPhone = PHONE_PATTERN.matcher(account).matches();
        if (!isEmail && !isPhone) {
            throw new BusinessException("账号格式不正确");
        }
        
        // 检查发送频率限制
        String today = LocalDateTime.now().toString().substring(0, 10);
        Integer count = verificationCodeMapper.getCodeCount(account, today);
        if (count >= MAX_SEND_PER_DAY) {
            throw new BusinessException("发送次数超过限制，请明天再试");
        }
        
        // 生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        LocalDateTime now = LocalDateTime.now();
        
        // 保存验证码记录
        UserVerificationCode verificationCode = new UserVerificationCode();
        if (isEmail) {
            verificationCode.setEmail(account);
        } else {
            verificationCode.setPhone(account);
        }
        verificationCode.setCode(code);
        verificationCode.setType(type);
        verificationCode.setExpiredAt(now.plusMinutes(5));
        verificationCode.setVerified(0);
        verificationCodeMapper.insert(verificationCode);
        
        // 将验证码保存到Redis，设置5分钟过期
        String redisKey = String.format(VERIFICATION_CODE_KEY, type, account);
        redisUtils.setEx(redisKey, code, VERIFICATION_CODE_EXPIRE);
        
        // 发送验证码
        try {
            if (isEmail) {
                // TODO: 调用邮件服务发送验证码
                // emailService.sendVerificationCode(account, code);
            } else {
                // TODO: 调用短信服务发送验证码
                // smsService.sendVerificationCode(account, code);
            }
        } catch (Exception e) {
            throw new BusinessException("验证码发送失败：" + e.getMessage());
        }
    }

    @Override
    public LoginVO verificationCodeLogin(String account, String code, Integer clientType, String clientId) {
        // 验证验证码
        if (!verifyCode(account, code, 2)) { // 2表示登录验证码类型
            throw new BusinessException("验证码无效或已过期");
        }
        
        // TODO: 获取或创建用户（需要用户服务）
        // User user = userService.getOrCreateUserByAccount(account);
        Long userId = 1L; // 临时使用固定值，等待用户服务实现
        
        // 生成令牌
        UserToken userToken = generateToken(userId, clientType, clientId);
        
        // 记录登录日志
        saveLoginLog(userId, "127.0.0.1", "本地", "未知", 2, 1, "验证码登录成功");
        
        // 返回登录结果
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(userId);
        loginVO.setUsername("user"); // 临时使用固定值，等待用户服务实现
        loginVO.setAccessToken(userToken.getAccessToken());
        loginVO.setRefreshToken(userToken.getRefreshToken());
        loginVO.setAccessTokenExpireIn(7200L);
        loginVO.setRefreshTokenExpireIn(604800L);
        return loginVO;
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        // 验证刷新令牌
        UserToken userToken = tokenMapper.getByRefreshToken(refreshToken);
        if (userToken == null || userToken.getRefreshTokenExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("刷新令牌无效或已过期");
        }

        // 生成新的令牌
        String newAccessToken = UUID.randomUUID().toString();
        String newRefreshToken = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        
        // 更新令牌信息
        userToken.setAccessToken(newAccessToken);
        userToken.setRefreshToken(newRefreshToken);
        userToken.setAccessTokenExpiredAt(now.plusHours(2));
        userToken.setRefreshTokenExpiredAt(now.plusDays(7));
        tokenMapper.updateById(userToken);

        // 返回新的令牌信息
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(userToken.getUserId());
        loginVO.setAccessToken(newAccessToken);
        loginVO.setRefreshToken(newRefreshToken);
        loginVO.setAccessTokenExpireIn(7200L);
        loginVO.setRefreshTokenExpireIn(604800L);
        return loginVO;
    }

    @Override
    public void logout(Long userId, Integer clientType, String clientId) {
        // 清除用户令牌
        UserToken userToken = tokenMapper.getByUserAndClient(userId, clientType, clientId);
        if (userToken != null) {
            tokenMapper.invalidateToken(userToken.getId());
        }
    }

    @Override
    public Long validateToken(String accessToken) {
        // 验证访问令牌
        UserToken userToken = tokenMapper.getByAccessToken(accessToken);
        if (userToken == null || userToken.getAccessTokenExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("访问令牌无效或已过期");
        }
        return userToken.getUserId();
    }

    /**
     * 记录登录日志
     */
    private void saveLoginLog(Long userId, String ip, String location, String device, Integer loginType, 
                            Integer status, String message) {
        UserLoginLog loginLog = new UserLoginLog();
        loginLog.setUserId(userId);
        loginLog.setLoginIp(ip);
        loginLog.setLoginType(loginType);
        loginLog.setLoginStatus(status);
        loginLogMapper.insert(loginLog);
    }

    /**
     * 生成用户令牌
     */
    private UserToken generateToken(Long userId, Integer clientType, String clientId) {
        // 清除旧的令牌
        UserToken oldToken = tokenMapper.getByUserAndClient(userId, clientType, clientId);
        if (oldToken != null) {
            tokenMapper.invalidateToken(oldToken.getId());
        }

        // 生成新的令牌
        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        UserToken userToken = new UserToken();
        userToken.setUserId(userId);
        userToken.setClientType(clientType);
        userToken.setClientId(clientId);
        userToken.setAccessToken(accessToken);
        userToken.setRefreshToken(refreshToken);
        userToken.setAccessTokenExpiredAt(now.plusHours(2));
        userToken.setRefreshTokenExpiredAt(now.plusDays(7));
        tokenMapper.insert(userToken);

        return userToken;
    }

    /**
     * 验证验证码
     */
    private boolean verifyCode(String account, String code, Integer type) {
        // 从Redis获取验证码
        String redisKey = String.format(VERIFICATION_CODE_KEY, type, account);
        String savedCode = redisUtils.get(redisKey);
        
        if (savedCode == null) {
            return false;
        }
        
        // 验证成功后删除验证码
        if (savedCode.equals(code)) {
            redisUtils.delete(redisKey);
            
            // 更新验证码记录状态
            UserVerificationCode verificationCode = verificationCodeMapper.getLatestValidCode(account, type);
            if (verificationCode != null) {
                verificationCodeMapper.markAsVerified(verificationCode.getId());
            }
            
            return true;
        }
        
        return false;
    }
} 