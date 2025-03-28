package com.video.common.annotation.demo.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class VideoVO implements Serializable {
    private Long id;
    private String title;
    private String description;
    private String coverUrl;
    private String videoUrl;
    private Long userId;
    private Long categoryId;
    private Integer status;
    private Long viewCount;
    private Long likeCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 