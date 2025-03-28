package com.video.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 配置本地缓存管理器（一级缓存）
     */
    @Bean("caffeineCacheManager")
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caches = new ArrayList<>();
        
        // 用户信息缓存
        caches.add(new CaffeineCache("user", 
            Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build()));
        
        // 视频信息缓存
        caches.add(new CaffeineCache("video", 
            Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build()));
        
        // 热门视频缓存
        caches.add(new CaffeineCache("hot_video", 
            Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build()));
        
        // 分类信息缓存
        caches.add(new CaffeineCache("category", 
            Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build()));
        
        // 标签信息缓存
        caches.add(new CaffeineCache("tag", 
            Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(6, TimeUnit.HOURS)
                .build()));
                
        cacheManager.setCaches(caches);
        return cacheManager;
    }
    
    /**
     * 配置Redis缓存管理器（二级缓存）
     * 已在RedisConfig中配置
     */
} 