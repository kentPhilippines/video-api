package com.video.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.response.R;
import com.video.user.entity.User;
import com.video.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 用户控制器
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody User user) {
        userService.register(user);
        return R.ok();
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public R<String> login(@Parameter(description = "用户名") @RequestParam String username,
                          @Parameter(description = "密码") @RequestParam String password) {
        String token = userService.login(username, password);
        return R.ok(token);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/info")
    public R<Void> updateUserInfo(@Valid @RequestBody User user) {
        userService.updateUserInfo(user);
        return R.ok();
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public R<Void> updatePassword(@Parameter(description = "用户ID") @RequestParam Long userId,
                                @Parameter(description = "旧密码") @RequestParam String oldPassword,
                                @Parameter(description = "新密码") @RequestParam String newPassword) {
        userService.updatePassword(userId, oldPassword, newPassword);
        return R.ok();
    }

    @Operation(summary = "重置密码")
    @PostMapping("/reset-password")
    public R<Void> resetPassword(@Parameter(description = "邮箱") @RequestParam String email) {
        userService.resetPassword(email);
        return R.ok();
    }

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/list")
    public R<Page<User>> getUserList(@Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
                                   @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") Integer pageSize,
                                   @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        return R.ok(userService.getUserList(pageNum, pageSize, keyword));
    }

    @Operation(summary = "获取用户VIP信息")
    @GetMapping("/vip-info/{userId}")
    public R<User> getUserVipInfo(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return R.ok(userService.getUserVipInfo(userId));
    }

    @Operation(summary = "开通/续费VIP")
    @PostMapping("/subscribe-vip")
    public R<Void> subscribeVip(@Parameter(description = "用户ID") @RequestParam Long userId,
                               @Parameter(description = "月数") @RequestParam Integer months) {
        userService.subscribeVip(userId, months);
        return R.ok();
    }

    @Operation(summary = "取消VIP自动续费")
    @PostMapping("/cancel-vip-auto-renew")
    public R<Void> cancelVipAutoRenew(@Parameter(description = "用户ID") @RequestParam Long userId) {
        userService.cancelVipAutoRenew(userId);
        return R.ok();
    }

    @Operation(summary = "检查用户名是否可用")
    @GetMapping("/check-username")
    public R<Boolean> checkUsernameAvailable(@Parameter(description = "用户名") @RequestParam String username) {
        return R.ok(userService.checkUsernameAvailable(username));
    }

    @Operation(summary = "检查邮箱是否可用")
    @GetMapping("/check-email")
    public R<Boolean> checkEmailAvailable(@Parameter(description = "邮箱") @RequestParam String email) {
        return R.ok(userService.checkEmailAvailable(email));
    }
} 