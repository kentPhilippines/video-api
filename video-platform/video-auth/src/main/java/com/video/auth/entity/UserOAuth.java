package com.video.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_oauth")
public class UserOAuth {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * OAuth类型：1-GitHub 2-Google 3-微信
     */
    private Integer oauthType;
    
    /**
     * OAuth平台用户ID
     */
    private String oauthId;
    
    /**
     * 访问令牌
     */
    private String accessToken;
    
    /**
     * 令牌过期时间
     */
    private LocalDateTime tokenExpiredAt;
    
    /**
     * OAuth用户信息(JSON)
     */
    private String oauthInfo;
    
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