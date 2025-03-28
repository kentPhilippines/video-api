package com.video.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_history")
public class UserHistory {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 视频ID
     */
    private Long videoId;
    
    /**
     * 观看进度（秒）
     */
    private Integer progress;
    
    /**
     * 视频总时长（秒）
     */
    private Integer duration;
    
    /**
     * 是否看完（0-未完成 1-已完成）
     */
    private Integer finished;
    
    /**
     * 最后观看时间
     */
    private LocalDateTime watchedAt;
    
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