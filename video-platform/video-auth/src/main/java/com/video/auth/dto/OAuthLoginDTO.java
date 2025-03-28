package com.video.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "OAuth登录请求DTO")
public class OAuthLoginDTO {
    
    @Schema(description = "OAuth类型：1-GitHub 2-Google 3-微信")
    @NotNull(message = "OAuth类型不能为空")
    private Integer oauthType;
    
    @Schema(description = "OAuth授权码")
    @NotBlank(message = "授权码不能为空")
    private String code;
    
    @Schema(description = "客户端类型：1-Web 2-iOS 3-Android")
    private Integer clientType;
    
    @Schema(description = "客户端设备ID")
    private String clientId;
    
    @Schema(description = "重定向URI")
    private String redirectUri;
} 