package com.video.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_verification_code")
public class UserVerificationCode {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID（未注册用户为空）
     */
    private Long userId;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 验证码
     */
    private String code;
    
    /**
     * 验证码类型：1-注册 2-登录 3-重置密码
     */
    private Integer type;
    
    /**
     * 过期时间
     */
    private LocalDateTime expiredAt;
    
    /**
     * 是否已验证：0-未验证 1-已验证
     */
    private Integer verified;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
} 