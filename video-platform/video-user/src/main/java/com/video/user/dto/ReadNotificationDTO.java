package com.video.user.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReadNotificationDTO {
    
    /**
     * 通知ID列表
     */
    private List<Long> ids;
} 