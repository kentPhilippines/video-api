package com.video.common.annotation.demo;

import com.video.common.annotation.CacheIgnore;
import com.video.common.annotation.MultiLevelCache;
import com.video.common.annotation.demo.model.UserVO;
import com.video.common.annotation.demo.model.VideoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存使用示例
 * 展示各种缓存使用场景和配置方式
 */
@Slf4j
@Service
public class MultiLevelCacheDemo {

    /**
     * 1. 基础用法 - 默认配置
     * - 使用默认的key生成器
     * - 同时启用一级和二级缓存
     * - 默认过期时间：一级缓存1小时，二级缓存2小时
     */
    @MultiLevelCache
    public UserVO getUser(Long userId) {
        log.info("从数据库获取用户: {}", userId);
        return new UserVO();
    }

    /**
     * 2. 自定义缓存名称和key
     * - 使用自定义的缓存名称
     * - 使用SpEL表达式定义key
     */
    @MultiLevelCache(
        cacheName = "custom:video",
        key = "#userId + ':' + #categoryId",
        localExpire = 300,
        redisExpire = 600
    )
    public List<VideoVO> getVideosByCategory(Long userId, Long categoryId) {
        log.info("从数据库获取分类视频: userId={}, categoryId={}", userId, categoryId);
        return List.of(new VideoVO());
    }

    /**
     * 3. 只使用一级缓存（本地缓存）
     * - 禁用二级缓存
     * - 自定义过期时间
     */
    @MultiLevelCache(
        enableRedisCache = false,
        localExpire = 60,
        timeUnit = TimeUnit.SECONDS
    )
    public Map<String, Object> getSystemConfig() {
        log.info("从数据库获取系统配置");
        return Map.of("key", "value");
    }

    /**
     * 4. 只使用二级缓存（Redis缓存）
     * - 禁用一级缓存
     * - 启用同步加载防止缓存击穿
     */
    @MultiLevelCache(
        enableLocalCache = false,
        sync = true,
        redisExpire = 1800
    )
    public List<VideoVO> getHotVideos(String region) {
        log.info("从数据库获取热门视频: region={}", region);
        return List.of(new VideoVO());
    }

    /**
     * 5. 条件缓存
     * - 使用SpEL表达式控制是否缓存
     * - 允许缓存null值
     */
    @MultiLevelCache(
        condition = "#userId > 0",
        unless = "#result == null",
        cacheNull = true
    )
    public UserVO getUserIfExists(Long userId) {
        log.info("从数据库查询用户是否存在: {}", userId);
        return null;
    }

    /**
     * 6. 复杂参数处理
     * - 使用@CacheIgnore忽略不需要的参数
     * - 处理集合类型参数
     */
    @MultiLevelCache(
        cacheName = "video:search",
        localExpire = 300,
        redisExpire = 600
    )
    public List<VideoVO> searchVideos(
        String keyword,
        List<String> tags,
        @CacheIgnore Integer page,
        @CacheIgnore Integer size
    ) {
        log.info("搜索视频: keyword={}, tags={}, page={}, size={}", 
            keyword, tags, page, size);
        return List.of(new VideoVO());
    }

    /**
     * 7. 使用自定义key生成器
     * - 通过keyGenerator指定自定义的key生成器
     */
    @MultiLevelCache(
        keyGenerator = CustomCacheKeyGenerator.class,
        enableLocalCache = true,
        enableRedisCache = true
    )
    public List<VideoVO> getRecommendVideos(Long userId, String deviceId, String ip) {
        log.info("获取推荐视频: userId={}, deviceId={}, ip={}", 
            userId, deviceId, ip);
        return List.of(new VideoVO());
    }

    /**
     * 8. 不同时间单位示例
     * - 使用分钟作为时间单位
     * - 一级缓存5分钟，二级缓存10分钟
     */
    @MultiLevelCache(
        localExpire = 5,
        redisExpire = 10,
        timeUnit = TimeUnit.MINUTES
    )
    public List<VideoVO> getLatestVideos(Long userId) {
        log.info("获取最新视频: userId={}", userId);
        return List.of(new VideoVO());
    }

    /**
     * 9. 组合使用多个特性
     * - 自定义缓存名称和key
     * - 设置过期时间
     * - 条件缓存
     * - 同步加载
     */
    @MultiLevelCache(
        cacheName = "video:trending",
        key = "#category + ':' + #period",
        localExpire = 300,
        redisExpire = 900,
        condition = "#category != null",
        sync = true
    )
    public List<VideoVO> getTrendingVideos(String category, String period) {
        log.info("获取趋势视频: category={}, period={}", category, period);
        return List.of(new VideoVO());
    }

    /**
     * 10. 缓存预热示例
     * - 不使用condition和unless
     * - 强制缓存所有结果
     * - 较长的过期时间
     */
    @MultiLevelCache(
        cacheName = "video:categories",
        localExpire = 3600,
        redisExpire = 7200,
        cacheNull = true
    )
    public Map<String, List<String>> getAllCategories() {
        log.info("加载所有视频分类");
        return Map.of("game", List.of("RPG", "FPS"));
    }
} 