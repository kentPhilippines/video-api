package com.video.common.annotation.demo;

import com.video.common.annotation.CacheIgnore;
import com.video.common.annotation.MultiLevelCache;
import com.video.common.annotation.demo.model.UserVO;
import com.video.common.annotation.demo.model.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 动态缓存key生成示例
 */
@Slf4j
@Service
public class DynamicCacheDemo {

    /**
     * 自动生成缓存名称和key
     * 缓存名称: dynamiccachedemo:userinfo
     * 缓存key: userId:123
     */
    @MultiLevelCache(
        enableLocalCache = true,
        enableRedisCache = true,
        localExpire = 300
    )
    public UserVO getUserInfo(Long userId) {
        log.info("从数据库获取用户信息: {}", userId);
        return new UserVO();
    }

    /**
     * 忽略部分参数
     * 缓存名称: dynamiccachedemo:videolist
     * 缓存key: userId:123:categoryId:456
     * page和size参数不参与key生成
     */
    @MultiLevelCache(
        enableLocalCache = true,
        enableRedisCache = true
    )
    public List<VideoVO> getVideoList(
        Long userId, 
        Long categoryId, 
        @CacheIgnore int page, 
        @CacheIgnore int size
    ) {
        log.info("从数据库获取视频列表: userId={}, categoryId={}, page={}, size={}", 
            userId, categoryId, page, size);
        return List.of(new VideoVO());
    }

    /**
     * 自定义缓存名称，自动生成key
     * 缓存名称: custom:video:hot
     * 缓存key: categoryId:789:limit:10
     */
    @MultiLevelCache(
        cacheName = "custom:video:hot",
        enableLocalCache = true,
        enableRedisCache = true,
        localExpire = 60
    )
    public List<VideoVO> getHotVideos(Long categoryId, int limit) {
        log.info("从数据库获取热门视频: categoryId={}, limit={}", categoryId, limit);
        return List.of(new VideoVO());
    }

    /**
     * 复杂对象参数
     * 缓存名称: dynamiccachedemo:searchvideos
     * 缓存key: keyword:test:tags:list:3:userId:123
     */
    @MultiLevelCache(
        enableLocalCache = true,
        enableRedisCache = true
    )
    public List<VideoVO> searchVideos(String keyword, List<String> tags, Long userId) {
        log.info("搜索视频: keyword={}, tags={}, userId={}", keyword, tags, userId);
        return List.of(new VideoVO());
    }

    /**
     * 自定义key生成器示例
     * 使用自定义的key生成规则
     */
    @MultiLevelCache(
        keyGenerator = CustomCacheKeyGenerator.class,
        enableLocalCache = true,
        enableRedisCache = true
    )
    public List<VideoVO> getRecommendVideos(Long userId, String deviceId, String ip) {
        log.info("获取推荐视频: userId={}, deviceId={}, ip={}", userId, deviceId, ip);
        return List.of(new VideoVO());
    }
} 