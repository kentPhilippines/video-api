package com.video.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.user.entity.UserHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserHistoryMapper extends BaseMapper<UserHistory> {
    
    /**
     * 获取用户观看历史记录数量
     *
     * @param userId 用户ID
     * @return 历史记录数量
     */
    @Select("SELECT COUNT(DISTINCT video_id) FROM user_history " +
            "WHERE user_id = #{userId} AND deleted = 0")
    int getHistoryCount(@Param("userId") Long userId);
    
    /**
     * 获取视频的观看进度
     *
     * @param userId 用户ID
     * @param videoId 视频ID
     * @return 观看进度（秒）
     */
    @Select("SELECT progress FROM user_history " +
            "WHERE user_id = #{userId} AND video_id = #{videoId} " +
            "AND deleted = 0 ORDER BY watched_at DESC LIMIT 1")
    Integer getVideoProgress(@Param("userId") Long userId, @Param("videoId") Long videoId);
    
    /**
     * 清空用户的观看历史
     *
     * @param userId 用户ID
     * @return 影响的行数
     */
    @Update("UPDATE user_history SET deleted = 1 " +
            "WHERE user_id = #{userId} AND deleted = 0")
    int clearHistory(@Param("userId") Long userId);
} 