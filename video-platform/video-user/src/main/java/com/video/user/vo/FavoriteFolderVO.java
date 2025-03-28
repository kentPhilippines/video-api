package com.video.user.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FavoriteFolderVO {
    /**
     * 收藏夹ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
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
    
    /**
     * 收藏数量
     */
    private Integer count;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 