package com.video.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Data
@TableName("users")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String phone;
    
    private String avatar;
    
    @TableField("vip_status")
    private Integer vipStatus;
    
    @TableField("vip_expire_time")
    private LocalDateTime vipExpireTime;
    
    private Integer status;
    
    @TableField("last_login_time")
    private LocalDateTime lastLoginTime;
    
    @TableField("last_login_ip")
    private String lastLoginIp;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    @TableLogic
    private Integer deleted;
} 