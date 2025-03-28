package com.video.common.annotation;

import java.lang.annotation.*;

/**
 * 标记不需要作为缓存key的参数
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheIgnore {
} 