package com.video.common.annotation.demo;

import com.video.common.annotation.DistributedScheduled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 分布式定时任务使用示例
 */
@Slf4j
@Service
public class ScheduledTaskDemo {

    /**
     * 基础定时任务示例
     * 每5分钟执行一次，允许并行执行
     */
    @DistributedScheduled(
        cron = "0 */5 * * * ?",
        name = "basicTask"
    )
    public void basicTask() {
        log.info("执行基础定时任务");
    }

    /**
     * 不允许并行执行的任务示例
     * 每小时执行一次，必须等待上一次执行完成
     * 任务最长执行时间为30分钟
     */
    @DistributedScheduled(
        cron = "0 0 * * * ?",
        name = "nonParallelTask",
        parallel = false,
        leaseTime = 1800  // 30分钟
    )
    public void nonParallelTask() {
        log.info("执行非并行任务");
        // 模拟耗时操作
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 任务组示例 - 数据统计任务1
     * 每天凌晨1点执行
     * 同组任务按顺序执行
     */
    @DistributedScheduled(
        cron = "0 0 1 * * ?",
        name = "statsTask1",
        group = "dailyStats",
        waitPreviousTimeout = 1800  // 等待上一个任务最多30分钟
    )
    public void dailyStatsTask1() {
        log.info("执行每日统计任务1");
        // 处理用户相关统计
    }

    /**
     * 任务组示例 - 数据统计任务2
     * 每天凌晨1点执行
     * 等待task1完成后执行
     */
    @DistributedScheduled(
        cron = "0 0 1 * * ?",
        name = "statsTask2",
        group = "dailyStats"
    )
    public void dailyStatsTask2() {
        log.info("执行每日统计任务2");
        // 处理视频相关统计
    }

    /**
     * 长时间运行任务示例
     * 每天凌晨3点执行
     * 任务最长执行时间为2小时
     */
    @DistributedScheduled(
        cron = "0 0 3 * * ?",
        name = "longRunningTask",
        parallel = false,
        leaseTime = 7200,  // 2小时
        waitTime = 10      // 等待获取锁10秒
    )
    public void longRunningTask() {
        log.info("执行长时间运行任务");
        // 处理大量数据的任务
    }

    /**
     * 快速执行任务示例
     * 每分钟执行一次
     * 允许并行执行
     */
    @DistributedScheduled(
        cron = "0 * * * * ?",
        name = "quickTask",
        leaseTime = 30    // 30秒
    )
    public void quickTask() {
        log.info("执行快速任务");
        // 处理实时数据
    }

    /**
     * 视频处理任务组 - 转码任务
     * 每5分钟执行一次
     */
    @DistributedScheduled(
        cron = "0 */5 * * * ?",
        name = "videoTranscode",
        group = "videoProcessing",
        waitPreviousTimeout = 600  // 等待上一个任务最多10分钟
    )
    public void videoTranscodeTask() {
        log.info("执行视频转码任务");
        // 处理视频转码
    }

    /**
     * 视频处理任务组 - 封面生成任务
     * 每5分钟执行一次
     * 在转码任务后执行
     */
    @DistributedScheduled(
        cron = "0 */5 * * * ?",
        name = "videoThumbnail",
        group = "videoProcessing"
    )
    public void videoThumbnailTask() {
        log.info("执行视频封面生成任务");
        // 生成视频封面
    }

    /**
     * 系统维护任务示例
     * 每天凌晨2点执行
     * 不允许并行执行
     * 执行超时时间为1小时
     */
    @DistributedScheduled(
        cron = "0 0 2 * * ?",
        name = "systemMaintenance",
        parallel = false,
        leaseTime = 3600,         // 1小时
        waitPreviousTimeout = 600  // 等待10分钟
    )
    public void systemMaintenanceTask() {
        log.info("执行系统维护任务");
        // 系统维护操作
    }
} 