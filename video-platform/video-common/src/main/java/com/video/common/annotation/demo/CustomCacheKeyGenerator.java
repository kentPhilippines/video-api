package com.video.common.annotation.demo;

import com.video.common.cache.CacheKeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 自定义缓存key生成器示例
 */
@Component
public class CustomCacheKeyGenerator implements CacheKeyGenerator {

    @Override
    public String generateCacheName(Class<?> targetClass, Method method, Object[] params) {
        return null;
    }

    @Override
    public String generateKey(Class<?> targetClass, Method method, Object[] params) {
        return null;
    }
}