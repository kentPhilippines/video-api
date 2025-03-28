package com.video.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "oauth")
public class OAuthConfig {
    
    private Map<String, OAuthClientConfig> clients;
    
    @Data
    public static class OAuthClientConfig {
        private String clientId;
        private String clientSecret;
        private String authorizeUrl;
        private String tokenUrl;
        private String userInfoUrl;
        private String redirectUri;
        private String scope;
    }
} 