package com.video.user.dto;

import lombok.Data;

@Data
public class AddFavoriteDTO {
    /**
     * 视频ID
     */
    private Long videoId;
    
    /**
     * 收藏夹ID
     */
    private Long folderId;
} 