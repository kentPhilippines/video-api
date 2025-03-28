package com.video.user.vo;

import lombok.Data;

@Data
public class UnreadCountVO {
    
    /**
     * 总未读数量
     */
    private Integer total;
    
    /**
     * 关注通知未读数量
     */
    private Integer follow;
    
    /**
     * 点赞通知未读数量
     */
    private Integer like;
    
    /**
     * 评论通知未读数量
     */
    private Integer comment;
    
    /**
     * 回复通知未读数量
     */
    private Integer reply;
    
    /**
     * 系统通知未读数量
     */
    private Integer system;
} 