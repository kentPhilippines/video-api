package com.video.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.video.user.dto.AddFavoriteDTO;
import com.video.user.dto.CreateFolderDTO;
import com.video.user.entity.FavoriteFolder;
import com.video.user.vo.FavoriteFolderVO;
import com.video.user.vo.FavoriteVideoVO;

public interface FavoriteService extends IService<FavoriteFolder> {
    
    /**
     * 创建收藏夹
     *
     * @param userId 用户ID
     * @param createFolderDTO 创建收藏夹DTO
     * @return 收藏夹ID
     */
    Long createFolder(Long userId, CreateFolderDTO createFolderDTO);
    
    /**
     * 删除收藏夹
     *
     * @param userId 用户ID
     * @param folderId 收藏夹ID
     */
    void deleteFolder(Long userId, Long folderId);
    
    /**
     * 更新收藏夹信息
     *
     * @param userId 用户ID
     * @param folderId 收藏夹ID
     * @param createFolderDTO 更新收藏夹DTO
     */
    void updateFolder(Long userId, Long folderId, CreateFolderDTO createFolderDTO);
    
    /**
     * 获取用户的收藏夹列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 收藏夹列表
     */
    Page<FavoriteFolderVO> getFolderList(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 获取收藏夹中的视频列表
     *
     * @param userId 用户ID
     * @param folderId 收藏夹ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 视频列表
     */
    Page<FavoriteVideoVO> getFolderVideos(Long userId, Long folderId, Integer pageNum, Integer pageSize);
    
    /**
     * 添加视频到收藏夹
     *
     * @param userId 用户ID
     * @param addFavoriteDTO 添加收藏DTO
     */
    void addToFolder(Long userId, AddFavoriteDTO addFavoriteDTO);
    
    /**
     * 从收藏夹移除视频
     *
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param folderId 收藏夹ID
     */
    void removeFromFolder(Long userId, Long videoId, Long folderId);
    
    /**
     * 检查视频是否已收藏到指定收藏夹
     *
     * @param userId 用户ID
     * @param videoId 视频ID
     * @param folderId 收藏夹ID
     * @return 是否已收藏
     */
    boolean checkVideoFavorited(Long userId, Long videoId, Long folderId);
    
    /**
     * 获取用户收藏视频的总数
     *
     * @param userId 用户ID
     * @return 收藏总数
     */
    int getFavoriteCount(Long userId);
} 