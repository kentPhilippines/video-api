package com.video.common.annotation.demo.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class VideoStatsVO implements Serializable {
    private Long videoId;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
    private Long shareCount;
    private Long favoriteCount;
    private LocalDateTime statsTime;
} 