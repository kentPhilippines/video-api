package com.video.common.aspect;

import com.video.common.annotation.DistributedScheduled;
import com.video.common.utils.DistributedLockUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DistributedScheduledAspect {

    private final DistributedLockUtils distributedLockUtils;

    @Around("@annotation(com.video.common.annotation.DistributedScheduled)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        DistributedScheduled annotation = method.getAnnotation(DistributedScheduled.class);
        String taskName = annotation.name();
        
        if (taskName.isEmpty()) {
            taskName = method.getDeclaringClass().getName() + "." + method.getName();
        }

        // 检查是否允许并行执行
        if (!annotation.parallel()) {
            // 检查任务是否正在运行
            if (distributedLockUtils.isTaskRunning(taskName)) {
                log.debug("任务正在运行中，跳过本次执行：{}", taskName);
                return null;
            }
        }

        // 如果有任务组，需要获取组信号量
        String groupPermitId = null;
        if (!annotation.group().isEmpty()) {
            groupPermitId = distributedLockUtils.tryAcquireGroupSemaphore(
                annotation.group(), 
                annotation.waitPreviousTimeout(),
                annotation.leaseTime()
            );
            if (groupPermitId == null) {
                log.warn("无法获取组信号量，跳过任务执行: {}", taskName);
                return null;
            }
        }

        boolean locked = false;
        try {
            // 尝试获取分布式锁
            locked = distributedLockUtils.tryLock(taskName, annotation.waitTime(), annotation.leaseTime());
            if (locked) {
                log.debug("获取分布式锁成功，任务：{}", taskName);
                
                // 如果不允许并行执行，标记任务开始运行
                if (!annotation.parallel()) {
                    distributedLockUtils.markTaskRunning(taskName, annotation.leaseTime());
                }
                
                try {
                    return point.proceed();
                } finally {
                    // 如果不允许并行执行，标记任务完成
                    if (!annotation.parallel()) {
                        distributedLockUtils.markTaskCompleted(taskName);
                    }
                }
            } else {
                log.debug("获取分布式锁失败，任务：{}", taskName);
                return null;
            }
        } finally {
            if (locked) {
                distributedLockUtils.unlock(taskName);
                log.debug("释放分布式锁，任务：{}", taskName);
            }
            // 释放任务组信号量
            if (groupPermitId != null) {
                distributedLockUtils.releaseGroupSemaphore(annotation.group(), groupPermitId);
            }
        }
    }
} 