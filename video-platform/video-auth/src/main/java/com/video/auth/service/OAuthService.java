package com.video.auth.service;

import com.video.auth.dto.OAuthLoginDTO;
import com.video.auth.vo.LoginVO;

import java.util.Map;

public interface OAuthService {
    
    /**
     * 获取OAuth授权URL
     *
     * @param type OAuth类型：1-GitHub 2-Google 3-微信
     * @param redirectUri 重定向URI
     * @return 授权URL
     */
    String getAuthorizeUrl(Integer type, String redirectUri);
    
    /**
     * OAuth登录
     *
     * @param oAuthLoginDTO OAuth登录信息
     * @return 登录结果
     */
    LoginVO oauthLogin(OAuthLoginDTO oAuthLoginDTO);
    
    /**
     * 获取OAuth用户信息
     *
     * @param type OAuth类型
     * @param code 授权码
     * @return OAuth用户信息
     */
    Map<String, Object> getOAuthUserInfo(Integer type, String code);
} 