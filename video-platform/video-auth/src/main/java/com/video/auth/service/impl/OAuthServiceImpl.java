package com.video.auth.service.impl;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.auth.config.OAuthConfig;
import com.video.auth.dto.OAuthLoginDTO;
import com.video.auth.entity.UserOAuth;
import com.video.auth.entity.UserToken;
import com.video.auth.mapper.UserOAuthMapper;
import com.video.auth.mapper.UserTokenMapper;
import com.video.auth.service.OAuthService;
import com.video.auth.vo.LoginVO;
import com.video.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final OAuthConfig oAuthConfig;
    private final UserOAuthMapper userOAuthMapper;
    private final UserTokenMapper userTokenMapper;

    @Override
    public String getAuthorizeUrl(Integer type, String redirectUri) {
        OAuthConfig.OAuthClientConfig config = getOAuthConfig(type);
        
        return UriComponentsBuilder.fromHttpUrl(config.getAuthorizeUrl())
                .queryParam("client_id", config.getClientId())
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", config.getScope())
                .queryParam("response_type", "code")
                .build().toUriString();
    }

    @Override
    public LoginVO oauthLogin(OAuthLoginDTO oAuthLoginDTO) {
        // 获取OAuth用户信息
        Map<String, Object> oauthUserInfo = getOAuthUserInfo(oAuthLoginDTO.getOauthType(), oAuthLoginDTO.getCode());
        String oauthId = String.valueOf(oauthUserInfo.get("id"));
        
        // 查找或创建OAuth用户
        UserOAuth userOAuth = userOAuthMapper.getByTypeAndOAuthId(oAuthLoginDTO.getOauthType(), oauthId);
        if (userOAuth == null) {
            userOAuth = new UserOAuth();
            userOAuth.setOauthType(oAuthLoginDTO.getOauthType());
            userOAuth.setOauthId(oauthId);
            userOAuth.setOauthInfo(JSONUtil.parseObj(oauthUserInfo).toString());
            
            // TODO: 创建用户并关联OAuth账号（需要用户服务）
            // User user = userService.createUserFromOAuth(oauthUserInfo);
            // userOAuth.setUserId(user.getId());
            
            userOAuthMapper.insert(userOAuth);
        }
        
        // 生成token
        UserToken userToken = generateToken(userOAuth.getUserId(), oAuthLoginDTO.getClientType(), oAuthLoginDTO.getClientId());
        
        // 返回登录结果
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(userOAuth.getUserId());
        loginVO.setAccessToken(userToken.getAccessToken());
        loginVO.setRefreshToken(userToken.getRefreshToken());
        loginVO.setAccessTokenExpireIn(7200L);
        loginVO.setRefreshTokenExpireIn(604800L);
        return loginVO;
    }

    @Override
    public Map<String, Object> getOAuthUserInfo(Integer type, String code) {
        OAuthConfig.OAuthClientConfig config = getOAuthConfig(type);
        
        // 获取访问令牌
        String accessToken = getAccessToken(type, code);
        
        // 获取用户信息
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        ResponseEntity<String> response = restTemplate.exchange(
                config.getUserInfoUrl(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );
        
        try {
            return objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new BusinessException("获取OAuth用户信息失败");
        }
    }

    private String getAccessToken(Integer type, String code) {
        OAuthConfig.OAuthClientConfig config = getOAuthConfig(type);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", config.getClientId());
        params.add("client_secret", config.getClientSecret());
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", config.getRedirectUri());
        
        ResponseEntity<Map> response = restTemplate.postForEntity(
                config.getTokenUrl(),
                new HttpEntity<>(params, headers),
                Map.class
        );
        
        if (response.getBody() == null || !response.getBody().containsKey("access_token")) {
            throw new BusinessException("获取OAuth访问令牌失败");
        }
        
        return (String) response.getBody().get("access_token");
    }

    private OAuthConfig.OAuthClientConfig getOAuthConfig(Integer type) {
        String platform = switch (type) {
            case 1 -> "github";
            case 2 -> "google";
            case 3 -> "wechat";
            default -> throw new BusinessException("不支持的OAuth类型");
        };
        
        OAuthConfig.OAuthClientConfig config = oAuthConfig.getClients().get(platform);
        if (config == null) {
            throw new BusinessException("OAuth配置不存在");
        }
        
        return config;
    }

    private UserToken generateToken(Long userId, Integer clientType, String clientId) {
        // 清除旧的令牌
        UserToken oldToken = userTokenMapper.getByUserAndClient(userId, clientType, clientId);
        if (oldToken != null) {
            userTokenMapper.invalidateToken(oldToken.getId());
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
        userTokenMapper.insert(userToken);

        return userToken;
    }
} 