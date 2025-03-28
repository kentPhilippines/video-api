package com.video.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {
    
    /**
     * 用户ID
     */
    private Long id;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号（脱敏）
     */
    private String phone;
    
    /**
     * 头像URL
     */
    private String avatar;
    
    /**
     * 用户状态（0-禁用 1-正常）
     */
    private Integer status;
    
    /**
     * VIP状态（0-普通用户 1-VIP用户）
     */
    private Integer vipStatus;
    
    /**
     * VIP到期时间
     */
    private LocalDateTime vipExpireTime;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;
    
    /**
     * 最后登录IP
     */
    private String lastLoginIp;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 