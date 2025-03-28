package com.video.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("favorite_folder")
public class FavoriteFolder {
    
    @TableId(type = IdType.AUTO)
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