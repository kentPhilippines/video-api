package com.video.user.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.common.response.R;
import com.video.user.dto.AddFavoriteDTO;
import com.video.user.dto.CreateFolderDTO;
import com.video.user.service.FavoriteService;
import com.video.user.vo.FavoriteFolderVO;
import com.video.user.vo.FavoriteVideoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "收藏接口")
@RestController
@RequestMapping("/user/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @ApiOperation("创建收藏夹")
    @PostMapping("/folder")
    public R<Long> createFolder(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("创建收藏夹信息") @RequestBody CreateFolderDTO createFolderDTO) {
        return R.ok(favoriteService.createFolder(userId, createFolderDTO));
    }

    @ApiOperation("删除收藏夹")
    @DeleteMapping("/folder/{folderId}")
    public R<Void> deleteFolder(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("收藏夹ID") @PathVariable Long folderId) {
        favoriteService.deleteFolder(userId, folderId);
        return R.ok();
    }

    @ApiOperation("更新收藏夹")
    @PutMapping("/folder/{folderId}")
    public R<Void> updateFolder(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("收藏夹ID") @PathVariable Long folderId,
            @ApiParam("更新收藏夹信息") @RequestBody CreateFolderDTO createFolderDTO) {
        favoriteService.updateFolder(userId, folderId, createFolderDTO);
        return R.ok();
    }

    @ApiOperation("获取收藏夹列表")
    @GetMapping("/folder/list")
    public R<Page<FavoriteFolderVO>> getFolderList(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok(favoriteService.getFolderList(userId, pageNum, pageSize));
    }

    @ApiOperation("获取收藏夹中的视频列表")
    @GetMapping("/folder/{folderId}/videos")
    public R<Page<FavoriteVideoVO>> getFolderVideos(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("收藏夹ID") @PathVariable Long folderId,
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok(favoriteService.getFolderVideos(userId, folderId, pageNum, pageSize));
    }

    @ApiOperation("添加视频到收藏夹")
    @PostMapping("/add")
    public R<Void> addToFolder(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("添加收藏信息") @RequestBody AddFavoriteDTO addFavoriteDTO) {
        favoriteService.addToFolder(userId, addFavoriteDTO);
        return R.ok();
    }

    @ApiOperation("从收藏夹移除视频")
    @DeleteMapping("/{folderId}/{videoId}")
    public R<Void> removeFromFolder(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("收藏夹ID") @PathVariable Long folderId,
            @ApiParam("视频ID") @PathVariable Long videoId) {
        favoriteService.removeFromFolder(userId, videoId, folderId);
        return R.ok();
    }

    @ApiOperation("检查视频是否已收藏")
    @GetMapping("/check")
    public R<Boolean> checkVideoFavorited(
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("视频ID") @RequestParam Long videoId,
            @ApiParam("收藏夹ID") @RequestParam Long folderId) {
        return R.ok(favoriteService.checkVideoFavorited(userId, videoId, folderId));
    }

    @ApiOperation("获取用户收藏总数")
    @GetMapping("/count")
    public R<Integer> getFavoriteCount(
            @ApiParam("用户ID") @RequestParam Long userId) {
        return R.ok(favoriteService.getFavoriteCount(userId));
    }
} 