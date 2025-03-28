package com.video.auth.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_login_log")
public class UserLoginLog {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 登录IP
     */
    private String loginIp;
    
    /**
     * 登录地点
     */
    private String loginLocation;
    
    /**
     * 登录设备
     */
    private String loginDevice;
    
    /**
     * 登录方式：1-账号密码 2-邮箱验证码 3-手机验证码 4-第三方登录
     */
    private Integer loginType;
    
    /**
     * 登录状态：0-失败 1-成功
     */
    private Integer loginStatus;
    
    /**
     * 登录消息（失败原因等）
     */
    private String loginMsg;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
} 