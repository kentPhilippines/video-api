package com.video.common.annotation.demo;

import com.video.common.annotation.demo.model.UserDTO;
import com.video.common.annotation.demo.model.UserVO;
import com.video.common.annotation.demo.model.VideoStatsVO;
import com.video.common.annotation.demo.model.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存使用示例
 * 一级缓存（Caffeine）：本地缓存，适用于高频访问、数据量较小的场景
 * 二级缓存（Redis）：分布式缓存，适用于分布式环境、数据量大的场景
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "demo")
public class CacheDemo {

    /**
     * 使用一级缓存（Caffeine）示例
     * 用户信息缓存，过期时间1小时
     */
    @Cacheable(
        cacheNames = "user",
        key = "#userId",
        unless = "#result == null"
    )
    public UserVO getUserInfo(Long userId) {
        log.info("从数据库获取用户信息: {}", userId);
        return getUserFromDb(userId);
    }

    /**
     * 使用二级缓存（Redis）示例
     * 视频信息缓存，过期时间30分钟
     */
    @Cacheable(
        cacheNames = "video",
        key = "#videoId",
        unless = "#result == null",
        cacheManager = "redisCacheManager"
    )
    public VideoVO getVideoInfo(Long videoId) {
        log.info("从数据库获取视频信息: {}", videoId);
        return getVideoFromDb(videoId);
    }

    /**
     * 组合使用一二级缓存示例
     * 热门视频列表，一级缓存5分钟，二级缓存30分钟
     */
    @Caching(cacheable = {
        @Cacheable(
            cacheNames = "hot_video",
            key = "#categoryId",
            unless = "#result == null"
        ),
        @Cacheable(
            cacheNames = "redis_hot_video",
            key = "#categoryId",
            unless = "#result == null",
            cacheManager = "redisCacheManager"
        )
    })
    public List<VideoVO> getHotVideos(Long categoryId) {
        log.info("从数据库获取热门视频列表: {}", categoryId);
        return getHotVideosFromDb(categoryId);
    }

    /**
     * 缓存更新示例
     * 更新用户信息时同时更新缓存
     */
    @CachePut(
        cacheNames = "user",
        key = "#user.id",
        unless = "#result == null"
    )
    public UserVO updateUserInfo(UserDTO user) {
        log.info("更新用户信息: {}", user.getId());
        return updateUserInDb(user);
    }

    /**
     * 删除缓存示例
     * 删除用户时同时删除相关缓存
     */
    @Caching(evict = {
        @CacheEvict(
            cacheNames = "user",
            key = "#userId"
        ),
        @CacheEvict(
            cacheNames = "user_videos",
            key = "#userId"
        )
    })
    public void deleteUser(Long userId) {
        log.info("删除用户: {}", userId);
        deleteUserFromDb(userId);
    }

    /**
     * 条件缓存示例
     * 只有非VIP用户的信息才缓存
     */
    @Cacheable(
        cacheNames = "user",
        key = "#userId",
        unless = "#result == null || #result.vip",
        condition = "#userId != null"
    )
    public UserVO getNonVipUserInfo(Long userId) {
        log.info("获取非VIP用户信息: {}", userId);
        return getUserFromDb(userId);
    }

    /**
     * 批量缓存操作示例
     * 获取多个视频信息
     */
    @Caching(cacheable = {
        @Cacheable(
            cacheNames = "video",
            key = "#videoIds",
            unless = "#result == null"
        ),
        @Cacheable(
            cacheNames = "redis_video",
            key = "#videoIds",
            unless = "#result == null",
            cacheManager = "redisCacheManager"
        )
    })
    public List<VideoVO> getVideoList(List<Long> videoIds) {
        log.info("批量获取视频信息: {}", videoIds);
        return getVideosFromDb(videoIds);
    }

    /**
     * 全局缓存清理示例
     * 清理所有用户相关缓存
     */
    @CacheEvict(
        cacheNames = "user",
        allEntries = true,
        beforeInvocation = true
    )
    public void clearAllUserCache() {
        log.info("清理所有用户缓存");
    }

    /**
     * 同步缓存示例
     * 确保缓存操作的原子性
     */
    @Caching(cacheable = {
        @Cacheable(
            cacheNames = "video_stats",
            key = "#videoId",
            sync = true
        )
    })
    public VideoStatsVO getVideoStats(Long videoId) {
        log.info("获取视频统计信息: {}", videoId);
        return getVideoStatsFromDb(videoId);
    }

    // 模拟数据库操作
    private UserVO getUserFromDb(Long userId) {
        return new UserVO();
    }

    private VideoVO getVideoFromDb(Long videoId) {
        return new VideoVO();
    }

    private List<VideoVO> getHotVideosFromDb(Long categoryId) {
        return new ArrayList<>();
    }

    private UserVO updateUserInDb(UserDTO user) {
        return new UserVO();
    }

    private void deleteUserFromDb(Long userId) {
        // 删除用户
    }

    private List<VideoVO> getVideosFromDb(List<Long> videoIds) {
        return new ArrayList<>();
    }

    private VideoStatsVO getVideoStatsFromDb(Long videoId) {
        return new VideoStatsVO();
    }
} 