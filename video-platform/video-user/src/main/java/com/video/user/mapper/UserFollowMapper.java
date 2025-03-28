package com.video.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.video.user.entity.UserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserFollowMapper extends BaseMapper<UserFollow> {
    
    /**
     * 检查是否互相关注
     *
     * @param userId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 是否互相关注
     */
    @Select("SELECT COUNT(*) > 0 FROM user_follow " +
            "WHERE follower_id = #{targetUserId} AND followed_id = #{userId} " +
            "AND status = 1 AND deleted = 0")
    boolean checkFollowEachOther(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);
    
    /**
     * 获取用户的关注数量
     *
     * @param userId 用户ID
     * @return 关注数量
     */
    @Select("SELECT COUNT(*) FROM user_follow " +
            "WHERE follower_id = #{userId} AND status = 1 AND deleted = 0")
    int getFollowingCount(@Param("userId") Long userId);
    
    /**
     * 获取用户的粉丝数量
     *
     * @param userId 用户ID
     * @return 粉丝数量
     */
    @Select("SELECT COUNT(*) FROM user_follow " +
            "WHERE followed_id = #{userId} AND status = 1 AND deleted = 0")
    int getFollowerCount(@Param("userId") Long userId);
} 