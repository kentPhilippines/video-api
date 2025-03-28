package com.video.common.aspect;

import com.video.common.annotation.MultiLevelCache;
import com.video.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class MultiLevelCacheAspect {

    private final CacheManager caffeineCacheManager;
    private final RedisUtils redisUtils;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(com.video.common.annotation.MultiLevelCache)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        MultiLevelCache annotation = method.getAnnotation(MultiLevelCache.class);

        // 生成缓存key
        String cacheKey = getCacheKey(point, method, annotation);
        
        // 检查缓存条件
        if (!checkCondition(point, method, annotation.condition())) {
            return point.proceed();
        }

        // 尝试从一级缓存获取
        Object result = null;
        if (annotation.enableLocalCache()) {
            result = getFromLocalCache(annotation.cacheName(), cacheKey);
            if (result != null) {
                log.debug("从一级缓存获取数据, key: {}", cacheKey);
                return result;
            }
        }

        // 尝试从二级缓存获取
        if (annotation.enableRedisCache()) {
            result = getFromRedisCache(annotation.cacheName(), cacheKey);
            if (result != null) {
                log.debug("从二级缓存获取数据, key: {}", cacheKey);
                // 如果启用了一级缓存，则将数据放入一级缓存
                if (annotation.enableLocalCache()) {
                    putToLocalCache(annotation.cacheName(), cacheKey, result, annotation.localExpire(), annotation.timeUnit());
                }
                return result;
            }
        }

        // 执行方法获取结果
        result = point.proceed();

        // 检查是否需要缓存
        if (result == null && !annotation.cacheNull()) {
            return null;
        }
        if (!checkUnless(point, method, annotation.unless(), result)) {
            return result;
        }

        // 放入缓存
        if (annotation.enableLocalCache()) {
            putToLocalCache(annotation.cacheName(), cacheKey, result, annotation.localExpire(), annotation.timeUnit());
        }
        if (annotation.enableRedisCache()) {
            putToRedisCache(annotation.cacheName(), cacheKey, result, annotation.redisExpire(), annotation.timeUnit());
        }

        return result;
    }

    private String getCacheKey(ProceedingJoinPoint point, Method method, MultiLevelCache annotation) {
        String key = annotation.key();
        if (StringUtils.hasText(key)) {
            return annotation.cacheName() + ":" + parseKey(key, method, point.getArgs());
        }
        // 默认使用方法名和参数作为key
        return annotation.cacheName() + ":" + method.getName() + ":" + String.join(":", toStringArray(point.getArgs()));
    }

    private String parseKey(String key, Method method, Object[] args) {
        Expression expression = parser.parseExpression(key);
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = discoverer.getParameterNames(method);
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        return Objects.requireNonNull(expression.getValue(context)).toString();
    }

    private boolean checkCondition(ProceedingJoinPoint point, Method method, String condition) {
        if (!StringUtils.hasText(condition)) {
            return true;
        }
        Expression expression = parser.parseExpression(condition);
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = discoverer.getParameterNames(method);
        Object[] args = point.getArgs();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        return Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
    }

    private boolean checkUnless(ProceedingJoinPoint point, Method method, String unless, Object result) {
        if (!StringUtils.hasText(unless)) {
            return true;
        }
        Expression expression = parser.parseExpression(unless);
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = discoverer.getParameterNames(method);
        Object[] args = point.getArgs();
        if (paramNames != null) {
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }
        context.setVariable("result", result);
        return !Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
    }

    private Object getFromLocalCache(String cacheName, String key) {
        Cache cache = caffeineCacheManager.getCache(cacheName);
        return cache != null ? cache.get(key) : null;
    }

    private Object getFromRedisCache(String cacheName, String key) {
        return redisUtils.get(cacheName + ":" + key);
    }

    private void putToLocalCache(String cacheName, String key, Object value, long expire, TimeUnit timeUnit) {
        Cache cache = caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    private void putToRedisCache(String cacheName, String key, Object value, long expire, TimeUnit timeUnit) {
        redisUtils.set(cacheName + ":" + key, value, expire, timeUnit);
    }

    private String[] toStringArray(Object[] args) {
        String[] result = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = String.valueOf(args[i]);
        }
        return result;
    }
} 