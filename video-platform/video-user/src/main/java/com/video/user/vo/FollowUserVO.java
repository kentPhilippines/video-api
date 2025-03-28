package com.video.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FollowUserVO {
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 头像
     */
    private String avatar;
    
    /**
     * 关注时间
     */
    private LocalDateTime followTime;
    
    /**
     * 是否互相关注
     */
    private Boolean isFollowEachOther;
} 