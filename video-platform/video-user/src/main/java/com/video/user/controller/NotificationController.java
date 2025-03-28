package com.video.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.model.Result;
import com.video.user.dto.ReadNotificationDTO;
import com.video.user.service.NotificationService;
import com.video.user.vo.NotificationVO;
import com.video.user.vo.UnreadCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "通知接口")
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @ApiOperation("获取通知列表")
    @GetMapping("/list")
    public Result<Page<NotificationVO>> getNotificationList(
            @RequestAttribute Long userId,
            @ApiParam("通知类型") @RequestParam(required = false) Integer type,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(notificationService.getNotificationList(userId, type, pageNum, pageSize));
    }

    @ApiOperation("标记通知为已读")
    @PostMapping("/read")
    public Result<Void> markAsRead(
            @RequestAttribute Long userId,
            @RequestBody ReadNotificationDTO readNotificationDTO) {
        notificationService.markAsRead(userId, readNotificationDTO);
        return Result.success();
    }

    @ApiOperation("获取未读通知数量")
    @GetMapping("/unread/count")
    public Result<UnreadCountVO> getUnreadCount(@RequestAttribute Long userId) {
        return Result.success(notificationService.getUnreadCount(userId));
    }
} 