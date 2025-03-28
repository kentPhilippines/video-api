package com.video.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.user.entity.FavoriteFolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FavoriteFolderMapper extends BaseMapper<FavoriteFolder> {
    
    /**
     * 增加收藏夹中的视频数量
     *
     * @param folderId 收藏夹ID
     * @return 影响的行数
     */
    @Update("UPDATE favorite_folder SET count = count + 1 " +
            "WHERE id = #{folderId} AND deleted = 0")
    int incrementCount(@Param("folderId") Long folderId);
    
    /**
     * 减少收藏夹中的视频数量
     *
     * @param folderId 收藏夹ID
     * @return 影响的行数
     */
    @Update("UPDATE favorite_folder SET count = count - 1 " +
            "WHERE id = #{folderId} AND deleted = 0 AND count > 0")
    int decrementCount(@Param("folderId") Long folderId);
} 