package com.video.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.video.user.dto.ReadNotificationDTO;
import com.video.user.entity.User;
import com.video.user.entity.UserNotification;
import com.video.user.mapper.UserMapper;
import com.video.user.mapper.UserNotificationMapper;
import com.video.user.service.NotificationService;
import com.video.user.vo.NotificationVO;
import com.video.user.vo.UnreadCountVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends ServiceImpl<UserNotificationMapper, UserNotification> implements NotificationService {

    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createNotification(Long userId, Long fromUserId, Integer type, String content, Long resourceId) {
        UserNotification notification = new UserNotification();
        notification.setUserId(userId);
        notification.setFromUserId(fromUserId);
        notification.setType(type);
        notification.setContent(content);
        notification.setResourceId(resourceId);
        notification.setIsRead(0);
        
        this.save(notification);
    }

    @Override
    public Page<NotificationVO> getNotificationList(Long userId, Integer type, Integer pageNum, Integer pageSize) {
        // 构建查询条件
        LambdaQueryWrapper<UserNotification> wrapper = new LambdaQueryWrapper<UserNotification>()
                .eq(UserNotification::getUserId, userId)
                .eq(UserNotification::getDeleted, false)
                .eq(type != null, UserNotification::getType, type)
                .orderByDesc(UserNotification::getCreatedAt);
        
        // 分页查询
        Page<UserNotification> page = new Page<>(pageNum, pageSize);
        Page<UserNotification> notificationPage = this.page(page, wrapper);
        
        // 获取发送者用户信息
        List<Long> fromUserIds = notificationPage.getRecords().stream()
                .map(UserNotification::getFromUserId)
                .collect(Collectors.toList());
        
        List<User> fromUsers = userMapper.selectBatchIds(fromUserIds);
        Map<Long, User> userMap = fromUsers.stream()
                .collect(Collectors.toMap(User::getId, user -> user));
        
        // 转换为VO
        List<NotificationVO> records = notificationPage.getRecords().stream()
                .map(notification -> {
                    NotificationVO vo = new NotificationVO();
                    vo.setId(notification.getId());
                    vo.setType(notification.getType());
                    vo.setContent(notification.getContent());
                    vo.setResourceId(notification.getResourceId());
                    vo.setIsRead(notification.getIsRead());
                    vo.setCreatedAt(notification.getCreatedAt());
                    
                    // 设置发送者信息
                    User fromUser = userMap.get(notification.getFromUserId());
                    if (fromUser != null) {
                        NotificationVO.UserVO userVO = new NotificationVO.UserVO();
                        userVO.setId(fromUser.getId());
                        userVO.setUsername(fromUser.getUsername());
                        userVO.setAvatar(fromUser.getAvatar());
                        vo.setFromUser(userVO);
                    }
                    
                    return vo;
                })
                .collect(Collectors.toList());
        
        // 构建返回结果
        Page<NotificationVO> voPage = new Page<>(notificationPage.getCurrent(), notificationPage.getSize(), notificationPage.getTotal());
        voPage.setRecords(records);
        
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long userId, ReadNotificationDTO readNotificationDTO) {
        this.baseMapper.markAsRead(userId, readNotificationDTO.getIds());
    }

    @Override
    public UnreadCountVO getUnreadCount(Long userId) {
        UnreadCountVO vo = new UnreadCountVO();
        
        // 获取各类型未读数量
        vo.setFollow(this.baseMapper.getUnreadCountByType(userId, 1));
        vo.setLike(this.baseMapper.getUnreadCountByType(userId, 2));
        vo.setComment(this.baseMapper.getUnreadCountByType(userId, 3));
        vo.setReply(this.baseMapper.getUnreadCountByType(userId, 4));
        vo.setSystem(this.baseMapper.getUnreadCountByType(userId, 5));
        
        // 计算总未读数量
        vo.setTotal(vo.getFollow() + vo.getLike() + vo.getComment() + vo.getReply() + vo.getSystem());
        
        return vo;
    }
} 