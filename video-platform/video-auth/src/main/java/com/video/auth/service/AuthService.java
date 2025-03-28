package com.video.auth.service;

import com.video.auth.dto.*;
import com.video.auth.vo.LoginVO;

public interface AuthService {
    
    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     */
    void register(RegisterDTO registerDTO);
    
    /**
     * 账号密码登录
     *
     * @param loginDTO 登录信息
     * @return 登录结果
     */
    LoginVO login(LoginDTO loginDTO);
    
    /**
     * OAuth登录
     *
     * @param oAuthLoginDTO OAuth登录信息
     * @return 登录结果
     */
    LoginVO oauthLogin(OAuthLoginDTO oAuthLoginDTO);
    
    /**
     * 发送验证码
     *
     * @param verificationCodeDTO 验证码请求信息
     */
    void sendVerificationCode(VerificationCodeDTO verificationCodeDTO);
    
    /**
     * 验证码登录
     *
     * @param account 账号（邮箱/手机号）
     * @param code 验证码
     * @param clientType 客户端类型
     * @param clientId 客户端ID
     * @return 登录结果
     */
    LoginVO verificationCodeLogin(String account, String code, Integer clientType, String clientId);
    
    /**
     * 刷新令牌
     *
     * @param refreshToken 刷新令牌
     * @return 新的令牌信息
     */
    LoginVO refreshToken(String refreshToken);
    
    /**
     * 退出登录
     *
     * @param userId 用户ID
     * @param clientType 客户端类型
     * @param clientId 客户端ID
     */
    void logout(Long userId, Integer clientType, String clientId);
    
    /**
     * 验证令牌
     *
     * @param accessToken 访问令牌
     * @return 用户ID
     */
    Long validateToken(String accessToken);
} 