package com.video.auth.controller;

import com.video.auth.dto.*;
import com.video.auth.service.AuthService;
import com.video.auth.vo.LoginVO;
import com.video.common.response.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(name = "认证接口", description = "提供登录、注册、验证码等认证服务")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public R<Void> register(@RequestBody @Valid RegisterDTO registerDTO) {
        authService.register(registerDTO);
        return R.ok();
    }

    @Operation(summary = "账号密码登录")
    @PostMapping("/login")
    public R<LoginVO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return R.ok(authService.login(loginDTO));
    }

    @Operation(summary = "验证码登录")
    @PostMapping("/login/code")
    public R<LoginVO> loginByCode(
            @Parameter(description = "账号（邮箱/手机号）") @RequestParam String account,
            @Parameter(description = "验证码") @RequestParam String code,
            @Parameter(description = "客户端类型：1-Web 2-iOS 3-Android") @RequestParam(required = false) Integer clientType,
            @Parameter(description = "客户端设备ID") @RequestParam(required = false) String clientId) {
        return R.ok(authService.verificationCodeLogin(account, code, clientType, clientId));
    }

    @Operation(summary = "OAuth登录")
    @PostMapping("/login/oauth")
    public R<LoginVO> oauthLogin(@RequestBody @Valid OAuthLoginDTO oAuthLoginDTO) {
        return R.ok(authService.oauthLogin(oAuthLoginDTO));
    }

    @Operation(summary = "发送验证码")
    @PostMapping("/code/send")
    public R<Void> sendVerificationCode(@RequestBody @Valid VerificationCodeDTO verificationCodeDTO) {
        authService.sendVerificationCode(verificationCodeDTO);
        return R.ok();
    }

    @Operation(summary = "刷新令牌")
    @PostMapping("/token/refresh")
    public R<LoginVO> refreshToken(
            @Parameter(description = "刷新令牌") @RequestParam String refreshToken) {
        return R.ok(authService.refreshToken(refreshToken));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<Void> logout(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "客户端类型：1-Web 2-iOS 3-Android") @RequestParam(required = false) Integer clientType,
            @Parameter(description = "客户端设备ID") @RequestParam(required = false) String clientId) {
        authService.logout(userId, clientType, clientId);
        return R.ok();
    }

    @Operation(summary = "验证令牌")
    @GetMapping("/token/validate")
    public R<Long> validateToken(
            @Parameter(description = "访问令牌") @RequestParam String accessToken) {
        return R.ok(authService.validateToken(accessToken));
    }
} 