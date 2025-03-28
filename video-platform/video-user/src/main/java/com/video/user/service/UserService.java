package com.video.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.video.user.entity.User;
import com.video.user.dto.UserDTO;
import com.video.user.vo.UserVO;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 创建用户
     *
     * @param userDTO 用户信息
     * @return 用户ID
     */
    Long createUser(UserDTO userDTO);
    
    /**
     * 从OAuth信息创建用户
     *
     * @param oauthInfo OAuth用户信息
     * @return 用户信息
     */
    User createUserFromOAuth(Map<String, Object> oauthInfo);
    
    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserInfo(Long userId);
    
    /**
     * 通过账号获取用户
     *
     * @param account 账号（用户名/邮箱/手机号）
     * @return 用户信息
     */
    User getUserByAccount(String account);
    
    /**
     * 更新用户信息
     *
     * @param userDTO 用户信息
     */
    void updateUser(UserDTO userDTO);
    
    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);
    
    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean checkEmailExists(String email);
    
    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean checkPhoneExists(String phone);
    
    /**
     * 更新用户最后登录信息
     *
     * @param userId 用户ID
     * @param ip IP地址
     */
    void updateLastLogin(Long userId, String ip);
    
    /**
     * 用户注册
     */
    void register(User user);
    
    /**
     * 用户登录
     */
    String login(String username, String password);
    
    /**
     * 更新用户信息
     */
    void updateUserInfo(User user);
    
    /**
     * 修改密码
     */
    void updatePassword(Long userId, String oldPassword, String newPassword);
    
    /**
     * 重置密码
     */
    void resetPassword(String email);
    
    /**
     * 分页查询用户列表
     */
    Page<User> getUserList(Integer pageNum, Integer pageSize, String keyword);
    
    /**
     * 获取用户VIP信息
     */
    User getUserVipInfo(Long userId);
    
    /**
     * 开通/续费VIP
     */
    void subscribeVip(Long userId, Integer months);
    
    /**
     * 取消VIP自动续费
     */
    void cancelVipAutoRenew(Long userId);
    
    /**
     * 检查用户名是否可用
     */
    boolean checkUsernameAvailable(String username);
    
    /**
     * 检查邮箱是否可用
     */
    boolean checkEmailAvailable(String email);
} 