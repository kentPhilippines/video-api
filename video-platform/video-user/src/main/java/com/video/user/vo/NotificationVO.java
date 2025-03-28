package com.video.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationVO {
    
    /**
     * 通知ID
     */
    private Long id;
    
    /**
     * 通知类型（1-关注 2-点赞 3-评论 4-回复 5-系统通知）
     */
    private Integer type;
    
    /**
     * 通知内容
     */
    private String content;
    
    /**
     * 发送用户信息
     */
    private UserVO fromUser;
    
    /**
     * 关联资源ID（视频ID/评论ID等）
     */
    private Long resourceId;
    
    /**
     * 是否已读（0-未读 1-已读）
     */
    private Integer isRead;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    @Data
    public static class UserVO {
        private Long id;
        private String username;
        private String avatar;
    }
} 