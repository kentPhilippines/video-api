package com.video.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistoryVideoVO {
    /**
     * 视频ID
     */
    private Long videoId;
    
    /**
     * 视频标题
     */
    private String title;
    
    /**
     * 视频封面
     */
    private String cover;
    
    /**
     * 视频时长（秒）
     */
    private Integer duration;
    
    /**
     * 观看进度（秒）
     */
    private Integer progress;
    
    /**
     * 是否看完（0-未完成 1-已完成）
     */
    private Integer finished;
    
    /**
     * 视频作者ID
     */
    private Long authorId;
    
    /**
     * 视频作者名称
     */
    private String authorName;
    
    /**
     * 最后观看时间
     */
    private LocalDateTime watchedAt;
} 