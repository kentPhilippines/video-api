-- 用户表
CREATE TABLE `users` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `email` varchar(100) NOT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
    `vip_status` tinyint NOT NULL DEFAULT '0' COMMENT 'VIP状态：0-普通用户 1-VIP用户',
    `vip_expire_time` datetime DEFAULT NULL COMMENT 'VIP过期时间',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-正常',
    `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 用户关注表
CREATE TABLE `user_follow` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `follower_id` bigint NOT NULL COMMENT '关注者ID',
    `followed_id` bigint NOT NULL COMMENT '被关注者ID',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '关注状态：0-取消关注 1-已关注',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_follower_followed` (`follower_id`,`followed_id`),
    KEY `idx_followed_id` (`followed_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注表';

-- 收藏夹表
CREATE TABLE `favorite_folder` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `name` varchar(50) NOT NULL COMMENT '收藏夹名称',
    `description` varchar(200) DEFAULT NULL COMMENT '收藏夹描述',
    `is_public` tinyint NOT NULL DEFAULT '1' COMMENT '是否公开：0-私密 1-公开',
    `count` int NOT NULL DEFAULT '0' COMMENT '收藏数量',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏夹表';

-- 用户收藏表
CREATE TABLE `user_favorite` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `video_id` bigint NOT NULL COMMENT '视频ID',
    `folder_id` bigint NOT NULL COMMENT '收藏夹ID',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '收藏状态：0-取消收藏 1-已收藏',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_video_folder` (`user_id`,`video_id`,`folder_id`),
    KEY `idx_folder_id` (`folder_id`),
    KEY `idx_video_id` (`video_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- 用户历史记录表
CREATE TABLE `user_history` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `video_id` bigint NOT NULL COMMENT '视频ID',
    `progress` int NOT NULL DEFAULT '0' COMMENT '观看进度（秒）',
    `duration` int NOT NULL DEFAULT '0' COMMENT '视频总时长（秒）',
    `finished` tinyint NOT NULL DEFAULT '0' COMMENT '是否看完：0-未完成 1-已完成',
    `watched_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后观看时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_video` (`user_id`,`video_id`),
    KEY `idx_user_watched` (`user_id`,`watched_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户历史记录表';

-- 用户通知表
CREATE TABLE `user_notification` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '接收用户ID',
    `from_user_id` bigint DEFAULT NULL COMMENT '发送用户ID',
    `type` tinyint NOT NULL COMMENT '通知类型：1-关注 2-点赞 3-评论 4-回复 5-系统通知',
    `content` varchar(500) NOT NULL COMMENT '通知内容',
    `resource_id` bigint DEFAULT NULL COMMENT '关联资源ID',
    `is_read` tinyint NOT NULL DEFAULT '0' COMMENT '是否已读：0-未读 1-已读',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_type` (`user_id`,`type`),
    KEY `idx_user_read` (`user_id`,`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户通知表'; 