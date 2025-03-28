package com.video.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.user.dto.AddFavoriteDTO;
import com.video.user.dto.CreateFolderDTO;
import com.video.user.entity.FavoriteFolder;
import com.video.user.entity.UserFavorite;
import com.video.user.mapper.FavoriteFolderMapper;
import com.video.user.mapper.UserFavoriteMapper;
import com.video.user.service.FavoriteService;
import com.video.user.vo.FavoriteFolderVO;
import com.video.user.vo.FavoriteVideoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteFolderMapper, FavoriteFolder> implements FavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFolder(Long userId, CreateFolderDTO createFolderDTO) {
        // 检查收藏夹名称是否已存在
        LambdaQueryWrapper<FavoriteFolder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FavoriteFolder::getUserId, userId)
                .eq(FavoriteFolder::getName, createFolderDTO.getName())
                .eq(FavoriteFolder::getDeleted, 0);
        if (this.count(queryWrapper) > 0) {
            throw new RuntimeException("收藏夹名称已存在");
        }

        // 创建收藏夹
        FavoriteFolder folder = new FavoriteFolder();
        folder.setUserId(userId);
        folder.setName(createFolderDTO.getName());
        folder.setDescription(createFolderDTO.getDescription());
        folder.setIsPublic(createFolderDTO.getIsPublic());
        folder.setCount(0);
        folder.setCreatedAt(LocalDateTime.now());
        folder.setUpdatedAt(LocalDateTime.now());

        this.save(folder);
        return folder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFolder(Long userId, Long folderId) {
        // 检查收藏夹是否存在且属于当前用户
        FavoriteFolder folder = checkFolderOwnership(userId, folderId);

        // 逻辑删除收藏夹
        folder.setDeleted(1);
        folder.setUpdatedAt(LocalDateTime.now());
        this.updateById(folder);

        // 逻辑删除收藏夹中的所有收藏记录
        LambdaQueryWrapper<UserFavorite> favoriteWrapper = new LambdaQueryWrapper<>();
        favoriteWrapper.eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getFolderId, folderId)
                .eq(UserFavorite::getDeleted, 0);

        UserFavorite updateFavorite = new UserFavorite();
        updateFavorite.setDeleted(1);
        updateFavorite.setUpdatedAt(LocalDateTime.now());
        userFavoriteMapper.update(updateFavorite, favoriteWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFolder(Long userId, Long folderId, CreateFolderDTO createFolderDTO) {
        // 检查收藏夹是否存在且属于当前用户
        FavoriteFolder folder = checkFolderOwnership(userId, folderId);

        // 检查新名称是否与其他收藏夹重复
        if (!folder.getName().equals(createFolderDTO.getName())) {
            LambdaQueryWrapper<FavoriteFolder> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FavoriteFolder::getUserId, userId)
                    .eq(FavoriteFolder::getName, createFolderDTO.getName())
                    .eq(FavoriteFolder::getDeleted, 0);
            if (this.count(queryWrapper) > 0) {
                throw new RuntimeException("收藏夹名称已存在");
            }
        }

        // 更新收藏夹信息
        folder.setName(createFolderDTO.getName());
        folder.setDescription(createFolderDTO.getDescription());
        folder.setIsPublic(createFolderDTO.getIsPublic());
        folder.setUpdatedAt(LocalDateTime.now());
        this.updateById(folder);
    }

    @Override
    public Page<FavoriteFolderVO> getFolderList(Long userId, Integer pageNum, Integer pageSize) {
        // 分页查询收藏夹列表
        Page<FavoriteFolder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<FavoriteFolder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FavoriteFolder::getUserId, userId)
                .eq(FavoriteFolder::getDeleted, 0)
                .orderByDesc(FavoriteFolder::getCreatedAt);

        Page<FavoriteFolder> folderPage = this.page(page, queryWrapper);
        
        // 转换为VO
        Page<FavoriteFolderVO> voPage = new Page<>(folderPage.getCurrent(), folderPage.getSize(), folderPage.getTotal());
        List<FavoriteFolderVO> voList = folderPage.getRecords().stream().map(folder -> {
            FavoriteFolderVO vo = new FavoriteFolderVO();
            BeanUtils.copyProperties(folder, vo);
            return vo;
        }).collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }

    @Override
    public Page<FavoriteVideoVO> getFolderVideos(Long userId, Long folderId, Integer pageNum, Integer pageSize) {
        // 检查收藏夹是否存在且有权限访问
        FavoriteFolder folder = this.getById(folderId);
        if (folder == null || folder.getDeleted() == 1) {
            throw new RuntimeException("收藏夹不存在");
        }
        if (!folder.getUserId().equals(userId) && folder.getIsPublic() == 0) {
            throw new RuntimeException("没有权限访问该收藏夹");
        }

        // 分页查询收藏的视频
        Page<UserFavorite> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFavorite::getUserId, folder.getUserId())
                .eq(UserFavorite::getFolderId, folderId)
                .eq(UserFavorite::getStatus, 1)
                .eq(UserFavorite::getDeleted, 0)
                .orderByDesc(UserFavorite::getCreatedAt);

        Page<UserFavorite> favoritePage = userFavoriteMapper.selectPage(page, queryWrapper);
        
        // 转换为VO（需要调用视频服务获取视频信息）
        Page<FavoriteVideoVO> voPage = new Page<>(favoritePage.getCurrent(), favoritePage.getSize(), favoritePage.getTotal());
        if (favoritePage.getRecords().isEmpty()) {
            voPage.setRecords(new ArrayList<>());
            return voPage;
        }

        // TODO: 调用视频服务获取视频详细信息
        // List<Long> videoIds = favoritePage.getRecords().stream()
        //         .map(UserFavorite::getVideoId)
        //         .collect(Collectors.toList());
        // Map<Long, VideoInfo> videoInfoMap = videoService.getVideoInfoBatch(videoIds);

        // 临时返回空列表
        voPage.setRecords(new ArrayList<>());
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToFolder(Long userId, AddFavoriteDTO addFavoriteDTO) {
        // 检查收藏夹是否存在且属于当前用户
        FavoriteFolder folder = checkFolderOwnership(userId, addFavoriteDTO.getFolderId());

        // TODO: 调用视频服务检查视频是否存在
        // VideoInfo videoInfo = videoService.getVideoInfo(addFavoriteDTO.getVideoId());
        // if (videoInfo == null) {
        //     throw new RuntimeException("视频不存在");
        // }

        // 检查是否已经收藏
        if (checkVideoFavorited(userId, addFavoriteDTO.getVideoId(), addFavoriteDTO.getFolderId())) {
            throw new RuntimeException("视频已在收藏夹中");
        }

        // 添加收藏记录
        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setVideoId(addFavoriteDTO.getVideoId());
        favorite.setFolderId(addFavoriteDTO.getFolderId());
        favorite.setStatus(1);
        favorite.setCreatedAt(LocalDateTime.now());
        favorite.setUpdatedAt(LocalDateTime.now());
        userFavoriteMapper.insert(favorite);

        // 更新收藏夹视频数量
        baseMapper.incrementCount(addFavoriteDTO.getFolderId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFromFolder(Long userId, Long videoId, Long folderId) {
        // 检查收藏夹是否存在且属于当前用户
        checkFolderOwnership(userId, folderId);

        // 查找收藏记录
        LambdaQueryWrapper<UserFavorite> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getVideoId, videoId)
                .eq(UserFavorite::getFolderId, folderId)
                .eq(UserFavorite::getStatus, 1)
                .eq(UserFavorite::getDeleted, 0);

        UserFavorite favorite = userFavoriteMapper.selectOne(queryWrapper);
        if (favorite == null) {
            throw new RuntimeException("视频不在收藏夹中");
        }

        // 更新收藏状态
        favorite.setStatus(0);
        favorite.setUpdatedAt(LocalDateTime.now());
        userFavoriteMapper.updateById(favorite);

        // 更新收藏夹视频数量
        baseMapper.decrementCount(folderId);
    }

    @Override
    public boolean checkVideoFavorited(Long userId, Long videoId, Long folderId) {
        return userFavoriteMapper.checkVideoFavorited(userId, videoId, folderId);
    }

    @Override
    public int getFavoriteCount(Long userId) {
        return userFavoriteMapper.getFavoriteCount(userId);
    }

    /**
     * 检查收藏夹是否存在且属于当前用户
     *
     * @param userId 用户ID
     * @param folderId 收藏夹ID
     * @return 收藏夹信息
     */
    private FavoriteFolder checkFolderOwnership(Long userId, Long folderId) {
        FavoriteFolder folder = this.getById(folderId);
        if (folder == null || folder.getDeleted() == 1) {
            throw new RuntimeException("收藏夹不存在");
        }
        if (!folder.getUserId().equals(userId)) {
            throw new RuntimeException("没有权限操作该收藏夹");
        }
        return folder;
    }
} 