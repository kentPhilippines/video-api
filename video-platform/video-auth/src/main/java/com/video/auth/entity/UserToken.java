package com.video.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_token")
public class UserToken {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 访问令牌
     */
    private String accessToken;
    
    /**
     * 刷新令牌
     */
    private String refreshToken;
    
    /**
     * 客户端类型：1-Web 2-iOS 3-Android
     */
    private Integer clientType;
    
    /**
     * 客户端设备ID
     */
    private String clientId;
    
    /**
     * 访问令牌过期时间
     */
    private LocalDateTime accessTokenExpiredAt;
    
    /**
     * 刷新令牌过期时间
     */
    private LocalDateTime refreshTokenExpiredAt;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    /**
     * 是否删除：0-未删除 1-已删除
     */
    @TableLogic
    private Integer deleted;
} 