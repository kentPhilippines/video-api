package com.video.user.dto;

import lombok.Data;

@Data
public class FollowUserDTO {
    /**
     * 被关注用户ID
     */
    private Long followedId;
} 