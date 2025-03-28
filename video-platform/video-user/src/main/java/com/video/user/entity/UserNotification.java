package com.video.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_notification")
public class UserNotification {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 接收用户ID
     */
    private Long userId;
    
    /**
     * 发送用户ID
     */
    private Long fromUserId;
    
    /**
     * 通知类型（1-关注 2-点赞 3-评论 4-回复 5-系统通知）
     */
    private Integer type;
    
    /**
     * 通知内容
     */
    private String content;
    
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    /**
     * 逻辑删除标记
     */
    @TableLogic
    private Integer deleted;
} 