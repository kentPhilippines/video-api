package com.video.common.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RPermitExpirableSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLockUtils {

    private final RedissonClient redissonClient;
    private final RedisUtils redisUtils;

    private static final String TASK_STATUS_PREFIX = "task:status:";
    private static final String TASK_LOCK_PREFIX = "task:lock:";
    private static final String GROUP_SEMAPHORE_PREFIX = "group:semaphore:";
    private static final String RUNNING_FLAG = "RUNNING";
    private static final String COMPLETED_FLAG = "COMPLETED";

    /**
     * 检查任务是否正在运行
     *
     * @param taskName 任务名称
     * @return 是否正在运行
     */
    public boolean isTaskRunning(String taskName) {
        try {
            String status = (String) redisUtils.get(TASK_STATUS_PREFIX + taskName);
            return RUNNING_FLAG.equals(status);
        } catch (Exception e) {
            log.error("检查任务状态失败: {}", taskName, e);
            return false;
        }
    }

    /**
     * 标记任务为运行状态
     *
     * @param taskName 任务名称
     * @param timeout 超时时间（秒）
     */
    public void markTaskRunning(String taskName, long timeout) {
        try {
            String key = TASK_STATUS_PREFIX + taskName;
            redisUtils.set(key, RUNNING_FLAG, timeout, TimeUnit.SECONDS);
            log.debug("标记任务运行状态: {}, 超时时间: {}秒", taskName, timeout);
        } catch (Exception e) {
            log.error("标记任务运行状态失败: {}", taskName, e);
            throw new RuntimeException("标记任务运行状态失败", e);
        }
    }

    /**
     * 标记任务为完成状态
     *
     * @param taskName 任务名称
     */
    public void markTaskCompleted(String taskName) {
        try {
            String key = TASK_STATUS_PREFIX + taskName;
            redisUtils.set(key, COMPLETED_FLAG, 60, TimeUnit.SECONDS); // 保留完成状态1分钟
            log.debug("标记任务完成状态: {}", taskName);
        } catch (Exception e) {
            log.error("标记任务完成状态失败: {}", taskName, e);
            throw new RuntimeException("标记任务完成状态失败", e);
        }
    }

    /**
     * 获取任务锁
     *
     * @param taskName 任务名称
     * @param waitTime 等待时间
     * @param leaseTime 租约时间
     * @return 是否获取成功
     */
    public boolean tryLock(String taskName, long waitTime, long leaseTime) {
        try {
            RLock lock = redissonClient.getLock(TASK_LOCK_PREFIX + taskName);
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("获取任务锁失败: {}", taskName, e);
            return false;
        }
    }

    /**
     * 释放任务锁
     *
     * @param taskName 任务名称
     */
    public void unlock(String taskName) {
        try {
            RLock lock = redissonClient.getLock(TASK_LOCK_PREFIX + taskName);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("释放任务锁: {}", taskName);
            }
        } catch (Exception e) {
            log.error("释放任务锁失败: {}", taskName, e);
            throw new RuntimeException("释放任务锁失败", e);
        }
    }

    /**
     * 尝试获取组信号量
     *
     * @param group 组名
     * @param permits 许可数
     * @param timeout 超时时间（秒）
     * @return 获取到的许可ID，如果获取失败则返回null
     */
    public String tryAcquireGroupSemaphore(String group, long permits, long timeout) {
        try {
            RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore(GROUP_SEMAPHORE_PREFIX + group);
            // 设置最大许可数
            semaphore.trySetPermits((int) permits);
            // 尝试获取许可
            String permitId = semaphore.tryAcquire(timeout, timeout, TimeUnit.SECONDS);
            if (permitId != null) {
                log.debug("获取组信号量成功: {}, permitId: {}", group, permitId);
            } else {
                log.debug("获取组信号量失败: {}, 等待超时", group);
            }
            return permitId;
        } catch (Exception e) {
            log.error("获取组信号量失败: {}", group, e);
            return null;
        }
    }

    /**
     * 释放组信号量
     *
     * @param group 组名
     * @param permitId 许可ID
     */
    public void releaseGroupSemaphore(String group, String permitId) {
        try {
            if (permitId == null) {
                log.warn("释放组信号量失败: {}, permitId为空", group);
                return;
            }
            RPermitExpirableSemaphore semaphore = redissonClient.getPermitExpirableSemaphore(GROUP_SEMAPHORE_PREFIX + group);
            semaphore.release(permitId);
            log.debug("释放组信号量成功: {}, permitId: {}", group, permitId);
        } catch (Exception e) {
            log.error("释放组信号量失败: {}, permitId: {}", group, permitId, e);
            throw new RuntimeException("释放组信号量失败", e);
        }
    }

    /**
     * 获取任务状态
     *
     * @param taskName 任务名称
     * @return 任务状态
     */
    public String getTaskStatus(String taskName) {
        try {
            return (String) redisUtils.get(TASK_STATUS_PREFIX + taskName);
        } catch (Exception e) {
            log.error("获取任务状态失败: {}", taskName, e);
            return null;
        }
    }

    /**
     * 清除任务状态
     *
     * @param taskName 任务名称
     */
    public void clearTaskStatus(String taskName) {
        try {
            redisUtils.delete(TASK_STATUS_PREFIX + taskName);
            log.debug("清除任务状态: {}", taskName);
        } catch (Exception e) {
            log.error("清除任务状态失败: {}", taskName, e);
            throw new RuntimeException("清除任务状态失败", e);
        }
    }
}