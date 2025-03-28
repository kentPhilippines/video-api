package com.video.user.dto;

import lombok.Data;

@Data
public class CreateFolderDTO {
    /**
     * 收藏夹名称
     */
    private String name;
    
    /**
     * 收藏夹描述
     */
    private String description;
    
    /**
     * 是否公开（0-私密 1-公开）
     */
    private Integer isPublic;
} 