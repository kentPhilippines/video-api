package com.video.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.user.entity.UserNotification;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserNotificationMapper extends BaseMapper<UserNotification> {

    /**
     * 获取指定类型的未读通知数量
     */
    @Select("SELECT COUNT(*) FROM user_notification " +
            "WHERE user_id = #{userId} AND type = #{type} " +
            "AND is_read = 0 AND deleted = 0")
    int getUnreadCountByType(@Param("userId") Long userId, @Param("type") Integer type);
    
    /**
     * 获取所有未读通知数量
     */
    @Select("SELECT COUNT(*) FROM user_notification " +
            "WHERE user_id = #{userId} AND is_read = 0 AND deleted = 0")
    int getTotalUnreadCount(@Param("userId") Long userId);
    
    /**
     * 批量标记通知为已读
     */
    @Update("<script>" +
            "UPDATE user_notification SET is_read = 1 " +
            "WHERE id IN " +
            "<foreach collection='ids' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            " AND user_id = #{userId} AND deleted = 0" +
            "</script>")
    int markAsRead(@Param("userId") Long userId, @Param("ids") List<Long> ids);
} 