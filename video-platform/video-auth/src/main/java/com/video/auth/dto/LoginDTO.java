package com.video.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
@Schema(description = "登录请求DTO")
public class LoginDTO {
    
    @Schema(description = "用户名/邮箱/手机号")
    @NotBlank(message = "账号不能为空")
    private String account;
    
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    
    @Schema(description = "验证码（需要时必填）")
    private String code;
    
    @Schema(description = "客户端类型：1-Web 2-iOS 3-Android")
    private Integer clientType;
    
    @Schema(description = "客户端设备ID")
    private String clientId;
} 