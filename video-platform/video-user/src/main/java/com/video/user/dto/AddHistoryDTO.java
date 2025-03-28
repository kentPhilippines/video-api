package com.video.user.dto;

import lombok.Data;

@Data
public class AddHistoryDTO {
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
} 