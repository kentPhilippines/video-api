package com.video.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.common.exception.BusinessException;
import com.video.user.dto.AddHistoryDTO;
import com.video.user.entity.UserHistory;
import com.video.user.mapper.UserHistoryMapper;
import com.video.user.service.UserHistoryService;
import com.video.user.vo.HistoryVideoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserHistoryServiceImpl extends ServiceImpl<UserHistoryMapper, UserHistory> implements UserHistoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addHistory(Long userId, AddHistoryDTO addHistoryDTO) {
        // TODO: 检查视频是否存在
        
        // 查找是否已有该视频的历史记录
        LambdaQueryWrapper<UserHistory> wrapper = new LambdaQueryWrapper<UserHistory>()
                .eq(UserHistory::getUserId, userId)
                .eq(UserHistory::getVideoId, addHistoryDTO.getVideoId())
                .eq(UserHistory::getDeleted, false);
        
        UserHistory history = this.getOne(wrapper);
        
        if (history == null) {
            // 创建新的历史记录
            history = new UserHistory();
            history.setUserId(userId);
            history.setVideoId(addHistoryDTO.getVideoId());
        }
        
        // 更新观看进度和时长
        history.setProgress(addHistoryDTO.getProgress());
        history.setDuration(addHistoryDTO.getDuration());
        history.setFinished(addHistoryDTO.getFinished());
        history.setWatchedAt(LocalDateTime.now());
        
        this.saveOrUpdate(history);
    }

    @Override
    public Page<HistoryVideoVO> getHistoryList(Long userId, Integer pageNum, Integer pageSize) {
        Page<UserHistory> page = new Page<>(pageNum, pageSize);
        
        LambdaQueryWrapper<UserHistory> wrapper = new LambdaQueryWrapper<UserHistory>()
                .eq(UserHistory::getUserId, userId)
                .eq(UserHistory::getDeleted, false)
                .orderByDesc(UserHistory::getWatchedAt);
        
        Page<UserHistory> historyPage = this.page(page, wrapper);
        
        // 转换为VO
        List<HistoryVideoVO> records = historyPage.getRecords().stream()
                .map(history -> {
                    HistoryVideoVO vo = new HistoryVideoVO();
                    vo.setVideoId(history.getVideoId());
                    vo.setProgress(history.getProgress());
                    vo.setDuration(history.getDuration());
                    vo.setFinished(history.getFinished());
                    vo.setWatchedAt(history.getWatchedAt());
                    
                    // TODO: 从视频服务获取视频详细信息
                    // vo.setTitle();
                    // vo.setCover();
                    // vo.setAuthorId();
                    // vo.setAuthorName();
                    
                    return vo;
                })
                .collect(Collectors.toList());
        
        Page<HistoryVideoVO> voPage = new Page<>(historyPage.getCurrent(), historyPage.getSize(), historyPage.getTotal());
        voPage.setRecords(records);
        
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHistory(Long userId, Long videoId) {
        LambdaQueryWrapper<UserHistory> wrapper = new LambdaQueryWrapper<UserHistory>()
                .eq(UserHistory::getUserId, userId)
                .eq(UserHistory::getVideoId, videoId)
                .eq(UserHistory::getDeleted, false);
        
        UserHistory history = this.getOne(wrapper);
        if (history == null) {
            throw new BusinessException("历史记录不存在");
        }
        
        // 逻辑删除
        history.setDeleted(true);
        this.updateById(history);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearHistory(Long userId) {
        this.baseMapper.clearHistory(userId);
    }

    @Override
    public int getHistoryCount(Long userId) {
        return this.baseMapper.getHistoryCount(userId);
    }

    @Override
    public Integer getVideoProgress(Long userId, Long videoId) {
        return this.baseMapper.getVideoProgress(userId, videoId);
    }
} 