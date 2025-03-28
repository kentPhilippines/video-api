package com.video.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.model.Result;
import com.video.user.dto.AddHistoryDTO;
import com.video.user.service.UserHistoryService;
import com.video.user.vo.HistoryVideoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户历史记录接口")
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    @ApiOperation("添加观看历史")
    @PostMapping
    public Result<Void> addHistory(
            @RequestAttribute Long userId,
            @RequestBody AddHistoryDTO addHistoryDTO) {
        userHistoryService.addHistory(userId, addHistoryDTO);
        return Result.success();
    }

    @ApiOperation("获取观看历史列表")
    @GetMapping
    public Result<Page<HistoryVideoVO>> getHistoryList(
            @RequestAttribute Long userId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(userHistoryService.getHistoryList(userId, pageNum, pageSize));
    }

    @ApiOperation("删除单条历史记录")
    @DeleteMapping("/{videoId}")
    public Result<Void> deleteHistory(
            @RequestAttribute Long userId,
            @ApiParam("视频ID") @PathVariable Long videoId) {
        userHistoryService.deleteHistory(userId, videoId);
        return Result.success();
    }

    @ApiOperation("清空观看历史")
    @DeleteMapping
    public Result<Void> clearHistory(@RequestAttribute Long userId) {
        userHistoryService.clearHistory(userId);
        return Result.success();
    }

    @ApiOperation("获取历史记录数量")
    @GetMapping("/count")
    public Result<Integer> getHistoryCount(@RequestAttribute Long userId) {
        return Result.success(userHistoryService.getHistoryCount(userId));
    }

    @ApiOperation("获取视频观看进度")
    @GetMapping("/progress/{videoId}")
    public Result<Integer> getVideoProgress(
            @RequestAttribute Long userId,
            @ApiParam("视频ID") @PathVariable Long videoId) {
        return Result.success(userHistoryService.getVideoProgress(userId, videoId));
    }
} 