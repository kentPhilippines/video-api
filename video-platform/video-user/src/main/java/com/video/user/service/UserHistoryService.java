package com.video.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.video.user.dto.AddHistoryDTO;
import com.video.user.entity.UserHistory;
import com.video.user.vo.HistoryVideoVO;

public interface UserHistoryService extends IService<UserHistory> {
    
    /**
     * 添加观看历史记录
     *
     * @param userId 用户ID
     * @param addHistoryDTO 添加历史记录DTO
     */
    void addHistory(Long userId, AddHistoryDTO addHistoryDTO);
    
    /**
     * 获取用户的观看历史列表
     *
     * @param userId 用户ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 历史记录列表
     */
    Page<HistoryVideoVO> getHistoryList(Long userId, Integer pageNum, Integer pageSize);
    
    /**
     * 删除单条历史记录
     *
     * @param userId 用户ID
     * @param videoId 视频ID
     */
    void deleteHistory(Long userId, Long videoId);
    
    /**
     * 清空用户的观看历史
     *
     * @param userId 用户ID
     */
    void clearHistory(Long userId);
    
    /**
     * 获取用户观看历史记录数量
     *
     * @param userId 用户ID
     * @return 历史记录数量
     */
    int getHistoryCount(Long userId);
    
    /**
     * 获取视频的观看进度
     *
     * @param userId 用户ID
     * @param videoId 视频ID
     * @return 观看进度（秒）
     */
    Integer getVideoProgress(Long userId, Long videoId);
} 