-- 用户钱包表
CREATE TABLE `user_wallet` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
    `points` bigint NOT NULL DEFAULT '0' COMMENT '积分',
    `coins` bigint NOT NULL DEFAULT '0' COMMENT '平台币',
    `total_recharge` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总充值金额',
    `total_consume` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总消费金额',
    `wallet_status` tinyint NOT NULL DEFAULT '1' COMMENT '钱包状态：0-冻结 1-正常',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_wallet_status` (`wallet_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包表';

-- 交易记录表
CREATE TABLE `transaction_record` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `transaction_no` varchar(64) NOT NULL COMMENT '交易流水号',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `type` tinyint NOT NULL COMMENT '交易类型：1-充值 2-消费 3-退款 4-提现 5-收入',
    `amount` decimal(10,2) NOT NULL COMMENT '交易金额',
    `points_amount` bigint DEFAULT '0' COMMENT '积分变动数量',
    `coins_amount` bigint DEFAULT '0' COMMENT '平台币变动数量',
    `payment_type` tinyint DEFAULT NULL COMMENT '支付方式：1-支付宝 2-微信 3-银行卡 4-平台币',
    `payment_status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态：0-待支付 1-支付成功 2-支付失败 3-已退款',
    `description` varchar(255) NOT NULL COMMENT '交易描述',
    `order_id` varchar(64) DEFAULT NULL COMMENT '关联订单号',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_transaction_no` (`transaction_no`),
    KEY `idx_user_type` (`user_id`, `type`),
    KEY `idx_payment_status` (`payment_status`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录表';

-- 用户任务表
CREATE TABLE `user_task` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `task_name` varchar(50) NOT NULL COMMENT '任务名称',
    `task_type` tinyint NOT NULL COMMENT '任务类型：1-每日任务 2-成长任务 3-成就任务 4-活动任务',
    `task_description` varchar(255) NOT NULL COMMENT '任务描述',
    `reward_type` tinyint NOT NULL COMMENT '奖励类型：1-积分 2-平台币 3-会员天数 4-实物',
    `reward_amount` int NOT NULL COMMENT '奖励数量',
    `task_status` tinyint NOT NULL DEFAULT '1' COMMENT '任务状态：0-下线 1-上线',
    `start_time` datetime DEFAULT NULL COMMENT '任务开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '任务结束时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_task_status` (`task_status`),
    KEY `idx_task_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户任务表';

-- 用户任务进度表
CREATE TABLE `user_task_progress` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `task_id` bigint NOT NULL COMMENT '任务ID',
    `progress` int NOT NULL DEFAULT '0' COMMENT '当前进度',
    `target` int NOT NULL COMMENT '目标进度',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '完成状态：0-进行中 1-已完成 2-已领取奖励',
    `completed_at` datetime DEFAULT NULL COMMENT '完成时间',
    `reward_received_at` datetime DEFAULT NULL COMMENT '领取奖励时间',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_task` (`user_id`, `task_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户任务进度表';

-- 用户签到表
CREATE TABLE `user_checkin` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `checkin_date` date NOT NULL COMMENT '签到日期',
    `checkin_days` int NOT NULL DEFAULT '1' COMMENT '连续签到天数',
    `reward_points` int NOT NULL COMMENT '获得积分',
    `reward_coins` int NOT NULL DEFAULT '0' COMMENT '获得平台币',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_date` (`user_id`, `checkin_date`),
    KEY `idx_checkin_date` (`checkin_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户签到表';

-- 抽奖活动表
CREATE TABLE `lucky_draw` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `title` varchar(100) NOT NULL COMMENT '活动标题',
    `description` varchar(500) NOT NULL COMMENT '活动描述',
    `draw_type` tinyint NOT NULL COMMENT '抽奖类型：1-积分抽奖 2-平台币抽奖 3-免费抽奖',
    `cost_amount` int NOT NULL DEFAULT '0' COMMENT '抽奖消耗数量',
    `daily_limit` int NOT NULL DEFAULT '0' COMMENT '每日抽奖次数限制',
    `total_limit` int NOT NULL DEFAULT '0' COMMENT '总抽奖次数限制',
    `start_time` datetime NOT NULL COMMENT '活动开始时间',
    `end_time` datetime NOT NULL COMMENT '活动结束时间',
    `status` tinyint NOT NULL DEFAULT '0' COMMENT '活动状态：0-未开始 1-进行中 2-已结束',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_status_time` (`status`, `start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖活动表';

-- 抽奖奖品表
CREATE TABLE `lucky_draw_prize` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `draw_id` bigint NOT NULL COMMENT '抽奖活动ID',
    `prize_name` varchar(100) NOT NULL COMMENT '奖品名称',
    `prize_type` tinyint NOT NULL COMMENT '奖品类型：1-积分 2-平台币 3-会员天数 4-实物',
    `prize_amount` int NOT NULL COMMENT '奖品数量',
    `probability` decimal(5,4) NOT NULL COMMENT '中奖概率',
    `total_stock` int NOT NULL COMMENT '奖品总库存',
    `remain_stock` int NOT NULL COMMENT '剩余库存',
    `daily_stock` int DEFAULT NULL COMMENT '每日库存限制',
    `image_url` varchar(255) DEFAULT NULL COMMENT '奖品图片',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_draw_id` (`draw_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抽奖奖品表';

-- 用户抽奖记录表
CREATE TABLE `user_lucky_draw_record` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `draw_id` bigint NOT NULL COMMENT '抽奖活动ID',
    `prize_id` bigint NOT NULL COMMENT '奖品ID',
    `prize_type` tinyint NOT NULL COMMENT '奖品类型：1-积分 2-平台币 3-会员天数 4-实物',
    `prize_amount` int NOT NULL COMMENT '奖品数量',
    `cost_type` tinyint NOT NULL COMMENT '消耗类型：1-积分 2-平台币 3-免费',
    `cost_amount` int NOT NULL COMMENT '消耗数量',
    `receive_status` tinyint NOT NULL DEFAULT '0' COMMENT '领取状态：0-未领取 1-已领取',
    `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
    `shipping_status` tinyint DEFAULT NULL COMMENT '发货状态：0-未发货 1-已发货 2-已签收',
    `shipping_time` datetime DEFAULT NULL COMMENT '发货时间',
    `shipping_info` varchar(500) DEFAULT NULL COMMENT '发货信息',
    `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_draw` (`user_id`, `draw_id`),
    KEY `idx_receive_status` (`receive_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户抽奖记录表'; 