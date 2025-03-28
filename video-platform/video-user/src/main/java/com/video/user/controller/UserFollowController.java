package com.video.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.response.Result;
import com.video.user.dto.FollowUserDTO;
import com.video.user.service.UserFollowService;
import com.video.user.vo.FollowUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户关注接口")
@RestController
@RequestMapping("/user/follow")
public class UserFollowController {

    @Autowired
    private UserFollowService userFollowService;

    @ApiOperation("关注用户")
    @PostMapping
    public Result<Void> followUser(
            @ApiParam("当前用户ID") @RequestParam Long userId,
            @ApiParam("关注用户信息") @RequestBody FollowUserDTO followUserDTO) {
        userFollowService.followUser(userId, followUserDTO);
        return Result.success();
    }

    @ApiOperation("取消关注用户")
    @DeleteMapping("/{targetUserId}")
    public Result<Void> unfollowUser(
            @ApiParam("当前用户ID") @RequestParam Long userId,
            @ApiParam("目标用户ID") @PathVariable Long targetUserId) {
        userFollowService.unfollowUser(userId, targetUserId);
        return Result.success();
    }

    @ApiOperation("获取关注列表")
    @GetMapping("/following")
    public Result<Page<FollowUserVO>> getFollowingList(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(userFollowService.getFollowingList(userId, pageNum, pageSize));
    }

    @ApiOperation("获取粉丝列表")
    @GetMapping("/followers")
    public Result<Page<FollowUserVO>> getFollowerList(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(userFollowService.getFollowerList(userId, pageNum, pageSize));
    }

    @ApiOperation("获取关注数量")
    @GetMapping("/following/count")
    public Result<Integer> getFollowingCount(
            @ApiParam("用户ID") @RequestParam Long userId) {
        return Result.success(userFollowService.getFollowingCount(userId));
    }

    @ApiOperation("获取粉丝数量")
    @GetMapping("/followers/count")
    public Result<Integer> getFollowerCount(
            @ApiParam("用户ID") @RequestParam Long userId) {
        return Result.success(userFollowService.getFollowerCount(userId));
    }

    @ApiOperation("检查是否已关注用户")
    @GetMapping("/check")
    public Result<Boolean> isFollowing(
            @ApiParam("当前用户ID") @RequestParam Long userId,
            @ApiParam("目标用户ID") @RequestParam Long targetUserId) {
        return Result.success(userFollowService.isFollowing(userId, targetUserId));
    }
} 