package com.video.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.video.user.dto.ReadNotificationDTO;
import com.video.user.entity.UserNotification;
import com.video.user.vo.NotificationVO;
import com.video.user.vo.UnreadCountVO;

public interface NotificationService extends IService<UserNotification> {
    
    /**
     * 创建通知
     *
     * @param userId 接收用户ID
     * @param fromUserId 发送用户ID
     * @param type 通知类型
     * @param content 通知内容
     * @param resourceId 关联资源ID
     */
    void createNotification(Long userId, Long fromUserId, Integer type, String content, Long resourceId);
    
    /**
     * 获取通知列表
     *
     * @param userId 用户ID
     * @param type 通知类型，可选
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 通知列表
     */
    Page<NotificationVO> getNotificationList(Long userId, Integer type, Integer pageNum, Integer pageSize);
    
    /**
     * 标记通知为已读
     *
     * @param userId 用户ID
     * @param readNotificationDTO 标记已读DTO
     */
    void markAsRead(Long userId, ReadNotificationDTO readNotificationDTO);
    
    /**
     * 获取未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    UnreadCountVO getUnreadCount(Long userId);
} 