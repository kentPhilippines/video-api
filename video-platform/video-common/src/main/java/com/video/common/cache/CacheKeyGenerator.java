package com.video.common.cache;

import java.lang.reflect.Method;

/**
 * 缓存key生成器接口
 */
public interface CacheKeyGenerator {
    
    /**
     * 生成缓存名称
     * @param targetClass 目标类
     * @param method 方法
     * @param params 参数
     * @return 缓存名称
     */
    String generateCacheName(Class<?> targetClass, Method method, Object[] params);
    
    /**
     * 生成缓存key
     * @param targetClass 目标类
     * @param method 方法
     * @param params 参数
     * @return 缓存key
     */
    String generateKey(Class<?> targetClass, Method method, Object[] params);
}