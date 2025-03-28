package com.video.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FavoriteVideoVO {
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
     * 视频作者ID
     */
    private Long authorId;
    
    /**
     * 视频作者名称
     */
    private String authorName;
    
    /**
     * 收藏时间
     */
    private LocalDateTime favoriteTime;
} 