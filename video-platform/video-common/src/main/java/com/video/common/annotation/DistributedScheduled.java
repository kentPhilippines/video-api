package com.video.common.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedScheduled {
    
    /**
     * cron表达式
     */
    String cron() default "";
    
    /**
     * 任务名称
     */
    String name() default "";
    
    /**
     * 等待获取锁的时间（秒）
     */
    long waitTime() default 3;
    
    /**
     * 持有锁的时间（秒）
     */
    long leaseTime() default 30;

    /**
     * 是否允许并行执行
     * false: 必须等待上一次执行完成才能执行下一次
     * true: 允许并行执行（默认）
     */
    boolean parallel() default true;

    /**
     * 任务组名称
     * 同一组的任务将串行执行
     */
    String group() default "";

    /**
     * 等待上一个任务完成的超时时间（秒）
     * 仅在parallel=false时生效
     */
    long waitPreviousTimeout() default 300;
}