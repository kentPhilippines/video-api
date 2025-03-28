package com.video.common.annotation;

import com.video.common.cache.CacheKeyGenerator;
import com.video.common.cache.DefaultCacheKeyGenerator;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 多级缓存注解
 * 支持同时配置一级缓存（Caffeine）和二级缓存（Redis）
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MultiLevelCache {
    
    /**
     * 缓存名称
     * 如果为空，则使用key生成器生成
     */
    String cacheName() default "";
    
    /**
     * 缓存key，支持SpEL表达式
     * 如果为空，则使用key生成器生成
     */
    String key() default "";
    
    /**
     * 自定义key生成器
     */
    Class<? extends CacheKeyGenerator> keyGenerator() default DefaultCacheKeyGenerator.class;
    
    /**
     * 是否启用一级缓存（Caffeine）
     */
    boolean enableLocalCache() default true;
    
    /**
     * 是否启用二级缓存（Redis）
     */
    boolean enableRedisCache() default true;
    
    /**
     * 一级缓存过期时间
     */
    long localExpire() default 3600;
    
    /**
     * 二级缓存过期时间
     */
    long redisExpire() default 7200;
    
    /**
     * 时间单位，默认秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    
    /**
     * 是否同步加载，防止缓存击穿
     */
    boolean sync() default false;
    
    /**
     * 是否允许缓存null值
     */
    boolean cacheNull() default false;
    
    /**
     * 缓存条件，支持SpEL表达式
     */
    String condition() default "";
    
    /**
     * 不缓存的条件，支持SpEL表达式
     */
    String unless() default "";
} 