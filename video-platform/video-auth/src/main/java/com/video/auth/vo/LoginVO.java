package com.video.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录响应VO")
public class LoginVO {
    
    @Schema(description = "用户ID")
    private Long userId;
    
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "访问令牌")
    private String accessToken;
    
    @Schema(description = "刷新令牌")
    private String refreshToken;
    
    @Schema(description = "访问令牌过期时间（秒）")
    private Long accessTokenExpireIn;
    
    @Schema(description = "刷新令牌过期时间（秒）")
    private Long refreshTokenExpireIn;
} 