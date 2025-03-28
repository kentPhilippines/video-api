-- 用户登录记录表
CREATE TABLE `user_login_log` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `login_ip` varchar(50) NOT NULL COMMENT '登录IP',
    `login_location` varchar(100) DEFAULT NULL COMMENT '登录地点',
    `login_device` varchar(200) DEFAULT NULL COMMENT '登录设备',
    `login_type` tinyint NOT NULL DEFAULT '1' COMMENT '登录方式：1-账号密码 2-邮箱验证码 3-手机验证码 4-第三方登录',
    `login_status` tinyint NOT NULL COMMENT '登录状态：0-失败 1-成功',
    `login_msg` varchar(200) DEFAULT NULL COMMENT '登录消息（失败原因等）',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户登录记录表';

-- 用户验证码表
CREATE TABLE `user_verification_code` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint DEFAULT NULL COMMENT '用户ID（未注册用户为空）',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `code` varchar(10) NOT NULL COMMENT '验证码',
    `type` tinyint NOT NULL COMMENT '验证码类型：1-注册 2-登录 3-重置密码',
    `expired_at` datetime NOT NULL COMMENT '过期时间',
    `verified` tinyint NOT NULL DEFAULT '0' COMMENT '是否已验证：0-未验证 1-已验证',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_email_type` (`email`, `type`),
    KEY `idx_phone_type` (`phone`, `type`),
    KEY `idx_expired_at` (`expired_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户验证码表';

-- 用户第三方登录表
CREATE TABLE `user_oauth` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `oauth_type` varchar(20) NOT NULL COMMENT '第三方类型：GITHUB、GOOGLE、WECHAT等',
    `oauth_id` varchar(100) NOT NULL COMMENT '第三方账号ID',
    `oauth_access_token` varchar(500) DEFAULT NULL COMMENT '访问令牌',
    `oauth_expires_in` int DEFAULT NULL COMMENT '令牌有效期',
    `oauth_scope` varchar(200) DEFAULT NULL COMMENT '授权范围',
    `oauth_info` json DEFAULT NULL COMMENT '第三方账号信息',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_id` (`oauth_type`, `oauth_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户第三方登录表';

-- 用户令牌表
CREATE TABLE `user_token` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `access_token` varchar(100) NOT NULL COMMENT '访问令牌',
    `refresh_token` varchar(100) NOT NULL COMMENT '刷新令牌',
    `token_type` varchar(20) NOT NULL DEFAULT 'Bearer' COMMENT '令牌类型',
    `client_id` varchar(50) DEFAULT NULL COMMENT '客户端ID',
    `client_ip` varchar(50) DEFAULT NULL COMMENT '客户端IP',
    `client_device` varchar(200) DEFAULT NULL COMMENT '客户端设备信息',
    `expired_at` datetime NOT NULL COMMENT '过期时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_access_token` (`access_token`),
    UNIQUE KEY `uk_refresh_token` (`refresh_token`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_expired_at` (`expired_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户令牌表'; 