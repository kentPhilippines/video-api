-- 任务类别表
CREATE TABLE `task_category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_name` varchar(50) NOT NULL COMMENT '类别名称',
    `category_code` varchar(50) NOT NULL COMMENT '类别编码',
    `description` varchar(200) DEFAULT NULL COMMENT '类别描述',
    `icon_url` varchar(255) DEFAULT NULL COMMENT '类别图标',
    `display_order` int NOT NULL DEFAULT '0' COMMENT '显示顺序',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_code` (`category_code`),
    KEY `idx_status_order` (`status`, `display_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务类别表';

-- 任务子类别表
CREATE TABLE `task_subcategory` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_id` bigint NOT NULL COMMENT '所属类别ID',
    `subcategory_name` varchar(50) NOT NULL COMMENT '子类别名称',
    `subcategory_code` varchar(50) NOT NULL COMMENT '子类别编码',
    `description` varchar(200) DEFAULT NULL COMMENT '子类别描述',
    `icon_url` varchar(255) DEFAULT NULL COMMENT '子类别图标',
    `display_order` int NOT NULL DEFAULT '0' COMMENT '显示顺序',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_subcategory_code` (`subcategory_code`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_status_order` (`status`, `display_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务子类别表';

-- 修改任务表结构
DROP TABLE IF EXISTS `user_task`;
CREATE TABLE `task` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `category_id` bigint NOT NULL COMMENT '任务类别ID',
    `subcategory_id` bigint NOT NULL COMMENT '任务子类别ID',
    `task_code` varchar(50) NOT NULL COMMENT '任务编码',
    `task_name` varchar(100) NOT NULL COMMENT '任务名称',
    `task_description` varchar(500) NOT NULL COMMENT '任务描述',
    `task_type` tinyint NOT NULL COMMENT '任务类型：1-每日任务 2-周常任务 3-成就任务 4-新手任务 5-活动任务',
    `completion_type` tinyint NOT NULL COMMENT '完成类型：1-观看视频 2-点赞 3-评论 4-分享 5-上传视频 6-连续签到 7-消费达标',
    `completion_condition` varchar(255) NOT NULL COMMENT '完成条件（JSON格式，如：{"videoCount": 5, "duration": 300}）',
    `reward_rules` text NOT NULL COMMENT '奖励规则（JSON格式，支持多级奖励）',
    `time_limit` int DEFAULT NULL COMMENT '时间限制（小时）',
    `reset_type` tinyint DEFAULT NULL COMMENT '重置类型：1-每日重置 2-每周重置 3-每月重置 4-不重置',
    `reset_time` varchar(50) DEFAULT NULL COMMENT '重置时间（cron表达式）',
    `task_level` int NOT NULL DEFAULT '1' COMMENT '任务等级',
    `prerequisites` varchar(255) DEFAULT NULL COMMENT '前置任务要求（JSON格式，如：[{"taskCode": "xxx", "status": 2}]）',
    `display_order` int NOT NULL DEFAULT '0' COMMENT '显示顺序',
    `start_time` datetime DEFAULT NULL COMMENT '任务开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下线 1-上线',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_code` (`task_code`),
    KEY `idx_category` (`category_id`, `subcategory_id`),
    KEY `idx_task_type` (`task_type`),
    KEY `idx_status_order` (`status`, `display_order`),
    KEY `idx_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- 修改任务进度表结构
DROP TABLE IF EXISTS `user_task_progress`;
CREATE TABLE `user_task_progress` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `task_id` bigint NOT NULL COMMENT '任务ID',
    `task_code` varchar(50) NOT NULL COMMENT '任务编码',
    `current_level` int NOT NULL DEFAULT '1' COMMENT '当前任务等级',
    `progress_data` text NOT NULL COMMENT '进度数据（JSON格式，记录详细进度信息）',
    `current_stage` int NOT NULL DEFAULT '1' COMMENT '当前阶段（多级任务使用）',
    `total_stages` int NOT NULL DEFAULT '1' COMMENT '总阶段数',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '完成状态：0-进行中 1-已完成 2-已领取奖励 3-已过期',
    `reset_count` int NOT NULL DEFAULT '0' COMMENT '重置次数',
    `last_reset_time` datetime DEFAULT NULL COMMENT '上次重置时间',
    `completed_at` datetime DEFAULT NULL COMMENT '完成时间',
    `reward_received_at` datetime DEFAULT NULL COMMENT '领取奖励时间',
    `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_task` (`user_id`, `task_id`, `current_level`),
    KEY `idx_task_code` (`task_code`),
    KEY `idx_user_status` (`user_id`, `status`),
    KEY `idx_reset_time` (`last_reset_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户任务进度表';

-- 任务奖励记录表
CREATE TABLE `task_reward_record` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `task_id` bigint NOT NULL COMMENT '任务ID',
    `task_code` varchar(50) NOT NULL COMMENT '任务编码',
    `progress_id` bigint NOT NULL COMMENT '任务进度ID',
    `reward_level` int NOT NULL DEFAULT '1' COMMENT '奖励等级',
    `reward_data` text NOT NULL COMMENT '奖励数据（JSON格式，包含所有奖励信息）',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待发放 1-已发放 2-发放失败',
    `fail_reason` varchar(255) DEFAULT NULL COMMENT '失败原因',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_task` (`user_id`, `task_id`),
    KEY `idx_progress_id` (`progress_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务奖励记录表'; 