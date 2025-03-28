package com.video.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.user.entity.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {
    
    /**
     * 检查视频是否已收藏到指定收藏夹
     *
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param folderId 收藏夹ID
     * @return 是否已收藏
     */
    @Select("SELECT COUNT(*) > 0 FROM user_favorite " +
            "WHERE user_id = #{userId} AND video_id = #{videoId} " +
            "AND folder_id = #{folderId} AND status = 1 AND deleted = 0")
    boolean checkVideoFavorited(@Param("userId") Long userId, 
                              @Param("videoId") Long videoId,
                              @Param("folderId") Long folderId);
    
    /**
     * 获取用户收藏视频的总数
     *
     * @param userId 用户ID
     * @return 收藏总数
     */
    @Select("SELECT COUNT(DISTINCT video_id) FROM user_favorite " +
            "WHERE user_id = #{userId} AND status = 1 AND deleted = 0")
    int getFavoriteCount(@Param("userId") Long userId);
} 