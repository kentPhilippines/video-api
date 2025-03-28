package com.video.common.response;

import lombok.Getter;

/**
 * 响应状态码
 */
@Getter
public enum ResultCode {
    
    SUCCESS(200, "操作成功"),
    ERROR(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "请求的资源不存在"),
    
    // 用户相关：1000-1999
    USER_NOT_EXIST(1000, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(1001, "用户名或密码错误"),
    USER_ACCOUNT_EXPIRED(1002, "账号已过期"),
    USER_CREDENTIALS_ERROR(1003, "用户凭证错误"),
    USER_CREDENTIALS_EXPIRED(1004, "用户凭证已过期"),
    USER_ACCOUNT_DISABLE(1005, "账号已被禁用"),
    USER_ACCOUNT_LOCKED(1006, "账号已被锁定"),
    USER_ACCOUNT_NOT_EXIST(1007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(1008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(1009, "账号下线"),
    
    // 业务相关：2000-2999
    NO_PERMISSION(2001, "没有权限操作"),
    
    // 文件相关：3000-3999
    FILE_NOT_EXIST(3000, "文件不存在"),
    FILE_UPLOAD_ERROR(3001, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(3002, "文件下载失败");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 