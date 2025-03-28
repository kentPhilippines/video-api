package com.video.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.video.user.dto.FollowUserDTO;
import com.video.user.entity.UserFollow;
import com.video.user.vo.FollowUserVO;

public interface UserFollowService extends IService<UserFollow> {
    
    /**
     * 关注用户
     *
     * @param userId 当前用户ID
     * @param followUserDTO 关注用户DTO
     */
    void followUser(Long userId, FollowUserDTO followUserDTO);
    
    /**
     * 取消关注用户
     *
     * @param userId 当前用户ID
     * @param targetUserId 目标用户ID
     */
    void unfollowUser(Long userId, Long targetUserId);
    
    /**
     * 获取用户的关注列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 关注用户列表
     */
    Page<FollowUserVO> getFollowingList(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取用户的粉丝列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 粉丝用户列表
     */
    Page<FollowUserVO> getFollowerList(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取用户的关注数量
     *
     * @param userId 用户ID
     * @return 关注数量
     */
    int getFollowingCount(Long userId);
    
    /**
     * 获取用户的粉丝数量
     *
     * @param userId 用户ID
     * @return 粉丝数量
     */
    int getFollowerCount(Long userId);
    
    /**
     * 检查是否已关注用户
     *
     * @param userId 当前用户ID
     * @param targetUserId 目标用户ID
     * @return 是否已关注
     */
    boolean isFollowing(Long userId, Long targetUserId);
} 