# 在线电影观看平台

## 项目简介
这是一个基于Java Spring Boot开发的分布式在线电影观看平台，采用微服务架构设计，支持高并发访问，提供电影播放、用户管理、评论系统等功能。平台支持Web端、移动端（iOS/Android）和小程序端多端访问。

## 技术栈
### 后端技术
- Java 17
- Spring Boot 3.x
- Spring Cloud Alibaba
- MyBatis-Plus (ORM框架)
- MySQL 8.0 (主从复制)
- Redis (缓存)
- Nacos (注册中心)
- RocketMQ (消息队列)
- JWT (认证)
- Docker (容器化)

### 前端技术

#### PC端 (Web)
- 技术框架：
  - Vue 3.x
  - TypeScript
  - Vite
  - Pinia (状态管理)
  - Vue Router
  
- UI组件：
  - Element Plus
  - TailwindCSS
  
- 视频播放：
  - Video.js
  - HLS.js (流媒体支持)
  
- 开发工具：
  - ESLint
  - Prettier
  - Jest
  - Cypress (E2E测试)

#### 移动端 (App)
- 技术框架：
  - Flutter 3.x
  - Dart
  - GetX (状态管理)
  
- UI组件：
  - Material Design 3
  - Custom Widgets
  
- 视频播放：
  - video_player
  - chewie (播放器UI)
  
- 原生集成：
  - 支付模块
  - 推送通知
  - 缓存管理
  - 生物识别

#### 开发规范
- PC端
  - Vue 3 组合式API
  - TypeScript 类型约束
  - 模块化设计
  - 响应式布局

- 移动端
  - Flutter Clean Architecture
  - BLoC 模式
  - 原生性能优化
  - 离线优先设计

## 系统架构
```
video-platform/
├── video-gateway/           # API网关服务
├── video-auth/             # 认证服务
├── video-user/             # 用户服务
├── video-movie/            # 电影服务
├── video-comment/          # 评论服务
├── video-storage/          # 视频存储服务
└── video-common/           # 公共模块
```

## 分布式架构设计

### 1. 微服务划分
- 网关服务 (Gateway)：统一接入层，负责路由转发、认证等
- 认证服务 (Auth)：处理用户认证、授权
- 用户服务 (User)：用户管理、会员系统
- 电影服务 (Movie)：电影信息管理、播放管理
- 评论服务 (Comment)：评论管理、互动功能
- 存储服务 (Storage)：视频文件存储、转码、分发
  - 视频上传与分片上传
  - 视频转码与格式处理
  - CDN分发管理
  - 视频加密与防盗链
  - 存储空间管理
  - 视频备份与容灾

### 2. 高可用设计
- 服务注册与发现：Nacos
- 负载均衡：Nginx
- 分布式缓存：Redis主从
- 消息队列：RocketMQ
- 数据库架构：MySQL主从复制

### 3. 高并发解决方案
- 缓存策略
  - Redis缓存
  - 热点数据缓存
  
- 数据库优化
  - 读写分离
  - 分库分表
  - 索引优化
  
- 流量控制
  - 接口限流
  - 熔断降级
  
- 异步处理
  - 消息队列
  - 异步任务

### 4. 监控告警
- Spring Boot Admin
- ELK日志系统

## 核心功能模块
1. 用户管理模块
   - 用户注册/登录
   - 个人信息管理
   - 会员系统
     - VIP等级体系
     - 等级特权
     - 成长值系统
     - 等级专属福利
   - 观影权限管理
   - 观影时长统计
   - 主播认证
   - 主播等级体系

2. 创作者中心
   - 视频上传管理
   - 视频定价设置
   - 收益统计
   - 粉丝管理
   - 数据分析
   - 视频审核状态

3. 订单支付模块
   - 电影单片购买
   - 会员订阅购买
   - 优惠券管理
   - 支付管理
   - 订单管理

4. 电影管理模块
   - 电影信息管理
   - 分类管理
   - 标签管理
   - 定价管理
   - 播放权限控制

5. 播放模块
   - 视频播放
   - 播放进度保存
   - 清晰度切换
   - 付费验证
   - 剩余观看时长显示

6. 营销活动模块
   - 优惠券发放
   - 观影时长赠送
   - 会员特权
   - 活动管理
   - 新用户福利

7. 互动模块
   - 评论系统
   - 收藏功能
   - 点赞功能
   - 观看历史

8. 搜索模块
   - 电影搜索
   - 高级筛选
   - 智能推荐

## 数据库设计
主要数据表设计（使用MyBatis-Plus）：

### users（用户表）
```sql
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `email` varchar(100) NOT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `vip_status` tinyint NOT NULL DEFAULT '0' COMMENT '会员状态：0-普通用户 1-VIP用户',
  `vip_expire_time` datetime DEFAULT NULL COMMENT 'VIP过期时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-正常',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_vip_status` (`vip_status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### movies（电影表）
```sql
CREATE TABLE `movies` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) NOT NULL COMMENT '电影名称',
  `original_title` varchar(100) DEFAULT NULL COMMENT '原始名称',
  `description` text COMMENT '描述',
  `cover_url` varchar(255) NOT NULL COMMENT '封面图片URL',
  `video_url` varchar(255) NOT NULL COMMENT '视频地址',
  `duration` int NOT NULL COMMENT '时长(秒)',
  `release_date` date NOT NULL COMMENT '发布日期',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `director` varchar(50) DEFAULT NULL COMMENT '导演',
  `actors` varchar(255) DEFAULT NULL COMMENT '演员',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签',
  `language` varchar(50) NOT NULL COMMENT '语言',
  `area` varchar(50) NOT NULL COMMENT '地区',
  `year` int NOT NULL COMMENT '年份',
  `views` bigint NOT NULL DEFAULT '0' COMMENT '观看次数',
  `likes` bigint NOT NULL DEFAULT '0' COMMENT '点赞数',
  `rating` decimal(2,1) DEFAULT NULL COMMENT '评分',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架 1-上架',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_views` (`views`),
  KEY `idx_rating` (`rating`),
  KEY `idx_release_date` (`release_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电影表';
```

### movie_prices（电影定价表）
```sql
CREATE TABLE `movie_prices` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `movie_id` bigint NOT NULL COMMENT '电影ID',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `discount_price` decimal(10,2) DEFAULT NULL COMMENT '折扣价',
  `discount_start` datetime DEFAULT NULL COMMENT '折扣开始时间',
  `discount_end` datetime DEFAULT NULL COMMENT '折扣结束时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架 1-上架',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_movie_id` (`movie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电影定价表';
```

### orders（订单表）
```sql
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `movie_id` bigint DEFAULT NULL COMMENT '电影ID',
  `order_type` tinyint NOT NULL COMMENT '订单类型：1-单片购买 2-会员订阅',
  `amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `coupon_id` bigint DEFAULT NULL COMMENT '优惠券ID',
  `pay_type` tinyint DEFAULT NULL COMMENT '支付方式：1-支付宝 2-微信 3-苹果支付',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待支付 1-已支付 2-已取消',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_movie_id` (`movie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';
```

### watch_rights（观看权限表）
```sql
CREATE TABLE `watch_rights` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `movie_id` bigint DEFAULT NULL COMMENT '电影ID',
  `right_type` tinyint NOT NULL COMMENT '权限类型：1-单片购买 2-会员观看',
  `watch_time` int DEFAULT NULL COMMENT '可观看时长(分钟)',
  `remaining_time` int DEFAULT NULL COMMENT '剩余观看时长(分钟)',
  `start_time` datetime NOT NULL COMMENT '生效时间',
  `end_time` datetime DEFAULT NULL COMMENT '失效时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-已失效 1-生效中',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_movie` (`user_id`,`movie_id`),
  KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='观看权限表';
```

### coupons（优惠券表）
```sql
CREATE TABLE `coupons` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `type` tinyint NOT NULL COMMENT '类型：1-满减券 2-折扣券 3-时长券',
  `value` decimal(10,2) NOT NULL COMMENT '优惠值：满减金额/折扣率/观影时长',
  `min_amount` decimal(10,2) DEFAULT NULL COMMENT '最低使用金额',
  `start_time` datetime NOT NULL COMMENT '生效时间',
  `end_time` datetime NOT NULL COMMENT '失效时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-已停用 1-启用中',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';
```

### user_coupons（用户优惠券表）
```sql
CREATE TABLE `user_coupons` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未使用 1-已使用 2-已过期',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint DEFAULT NULL COMMENT '使用订单ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';
```

### creators（创作者表）
```sql
CREATE TABLE `creators` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `creator_name` varchar(50) NOT NULL COMMENT '创作者名称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `introduction` text COMMENT '个人简介',
  `certification_status` tinyint NOT NULL DEFAULT '0' COMMENT '认证状态：0-未认证 1-认证中 2-已认证 3-认证失败',
  `level` int NOT NULL DEFAULT '1' COMMENT '创作者等级',
  `fans_count` bigint NOT NULL DEFAULT '0' COMMENT '粉丝数',
  `total_income` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '总收入',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-正常',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='创作者表';
```

### creator_videos（创作者视频表）
```sql
CREATE TABLE `creator_videos` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `creator_id` bigint NOT NULL COMMENT '创作者ID',
  `title` varchar(100) NOT NULL COMMENT '视频标题',
  `description` text COMMENT '视频描述',
  `cover_url` varchar(255) NOT NULL COMMENT '封面图片URL',
  `video_url` varchar(255) NOT NULL COMMENT '视频地址',
  `duration` int NOT NULL COMMENT '时长(秒)',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签',
  `price_type` tinyint NOT NULL DEFAULT '1' COMMENT '定价类型：1-免费 2-付费 3-会员免费',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `audit_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核状态：0-待审核 1-审核通过 2-审核拒绝',
  `audit_remark` varchar(255) DEFAULT NULL COMMENT '审核备注',
  `views` bigint NOT NULL DEFAULT '0' COMMENT '观看次数',
  `likes` bigint NOT NULL DEFAULT '0' COMMENT '点赞数',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-下架 1-上架',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_audit_status` (`audit_status`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='创作者视频表';
```

### creator_incomes（创作者收益表）
```sql
CREATE TABLE `creator_incomes` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `creator_id` bigint NOT NULL COMMENT '创作者ID',
  `video_id` bigint NOT NULL COMMENT '视频ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `amount` decimal(10,2) NOT NULL COMMENT '收益金额',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-待结算 1-已结算',
  `settle_time` datetime DEFAULT NULL COMMENT '结算时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_creator_id` (`creator_id`),
  KEY `idx_video_id` (`video_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='创作者收益表';
```

### creator_fans（创作者粉丝表）
```sql
CREATE TABLE `creator_fans` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `creator_id` bigint NOT NULL COMMENT '创作者ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_creator_user` (`creator_id`,`user_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='创作者粉丝表';
```

### vip_levels（VIP等级表）
```sql
CREATE TABLE `vip_levels` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `level` int NOT NULL COMMENT 'VIP等级',
  `name` varchar(50) NOT NULL COMMENT '等级名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '等级图标',
  `growth_value` int NOT NULL COMMENT '所需成长值',
  `monthly_price` decimal(10,2) NOT NULL COMMENT '月度价格',
  `yearly_price` decimal(10,2) NOT NULL COMMENT '年度价格',
  `max_quality` varchar(20) NOT NULL COMMENT '最高观看画质',
  `concurrent_devices` int NOT NULL COMMENT '同时在线设备数',
  `download_rights` tinyint NOT NULL DEFAULT '0' COMMENT '下载权限：0-无 1-有',
  `ad_free` tinyint NOT NULL DEFAULT '0' COMMENT '是否无广告：0-否 1-是',
  `exclusive_content` tinyint NOT NULL DEFAULT '0' COMMENT '专属内容：0-无 1-有',
  `customer_service` varchar(20) NOT NULL COMMENT '客服等级',
  `discount_rate` decimal(3,2) DEFAULT NULL COMMENT '购买折扣率',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='VIP等级表';
```

### user_vip_info（用户VIP信息表）
```sql
CREATE TABLE `user_vip_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `vip_level` int NOT NULL DEFAULT '0' COMMENT 'VIP等级',
  `growth_value` int NOT NULL DEFAULT '0' COMMENT '成长值',
  `total_growth_value` int NOT NULL DEFAULT '0' COMMENT '历史总成长值',
  `expire_time` datetime DEFAULT NULL COMMENT '会员过期时间',
  `auto_renew` tinyint NOT NULL DEFAULT '0' COMMENT '是否自动续费：0-否 1-是',
  `renew_type` tinyint DEFAULT NULL COMMENT '续费类型：1-月度 2-年度',
  `last_renew_time` datetime DEFAULT NULL COMMENT '上次续费时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_vip_level` (`vip_level`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户VIP信息表';
```

### growth_records（成长值记录表）
```sql
CREATE TABLE `growth_records` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `type` tinyint NOT NULL COMMENT '类型：1-观影 2-评论 3-购买 4-签到 5-活动',
  `growth_value` int NOT NULL COMMENT '成长值变化',
  `description` varchar(255) NOT NULL COMMENT '变化说明',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='成长值记录表';
```

### video_storage（视频存储记录表）
```sql
CREATE TABLE `video_storage` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `video_id` bigint NOT NULL COMMENT '视频ID',
  `storage_type` tinyint NOT NULL COMMENT '存储类型：1-本地 2-云存储 3-CDN',
  `file_key` varchar(255) NOT NULL COMMENT '文件唯一标识',
  `file_path` varchar(255) NOT NULL COMMENT '存储路径',
  `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
  `mime_type` varchar(50) NOT NULL COMMENT '文件类型',
  `resolution` varchar(20) NOT NULL COMMENT '视频分辨率',
  `bitrate` int NOT NULL COMMENT '码率(Kbps)',
  `duration` int NOT NULL COMMENT '时长(秒)',
  `encryption_key` varchar(255) DEFAULT NULL COMMENT '加密密钥',
  `transcoding_status` tinyint NOT NULL DEFAULT '0' COMMENT '转码状态：0-待转码 1-转码中 2-转码完成 3-转码失败',
  `cdn_status` tinyint NOT NULL DEFAULT '0' COMMENT 'CDN状态：0-未分发 1-分发中 2-已分发',
  `cdn_url` varchar(255) DEFAULT NULL COMMENT 'CDN访问地址',
  `backup_status` tinyint NOT NULL DEFAULT '0' COMMENT '备份状态：0-未备份 1-已备份',
  `backup_path` varchar(255) DEFAULT NULL COMMENT '备份路径',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-不可用 1-可用',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_video_id` (`video_id`),
  KEY `idx_file_key` (`file_key`),
  KEY `idx_transcoding_status` (`transcoding_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频存储记录表';
```

## API接口设计
详细的API文档将使用Swagger生成，主要接口包括：

### 用户相关
- POST /api/auth/register - 用户注册
- POST /api/auth/login - 用户登录
- GET /api/users/profile - 获取用户信息
- PUT /api/users/profile - 更新用户信息

### 电影相关
- GET /api/movies - 获取电影列表
- GET /api/movies/{id} - 获取电影详情
- GET /api/movies/categories - 获取分类列表
- GET /api/movies/search - 搜索电影

### 互动相关
- POST /api/movies/{id}/comments - 发表评论
- GET /api/movies/{id}/comments - 获取评论列表
- POST /api/movies/{id}/favorite - 收藏电影
- GET /api/users/history - 获取观看历史

### 订单支付相关
- POST /api/orders - 创建订单
- GET /api/orders/{id} - 获取订单详情
- POST /api/orders/{id}/pay - 订单支付
- GET /api/orders/list - 获取订单列表

### 优惠券相关
- GET /api/coupons - 获取可用优惠券列表
- POST /api/coupons/{id}/receive - 领取优惠券
- GET /api/coupons/my - 获取我的优惠券
- POST /api/coupons/verify - 验证优惠券

### 观看权限相关
- GET /api/movies/{id}/watch-right - 检查观看权限
- GET /api/users/watch-rights - 获取观看权限列表
- GET /api/users/remaining-time - 获取剩余观看时长

### 创作者相关
- POST /api/creators/apply - 申请成为创作者
- GET /api/creators/profile - 获取创作者信息
- PUT /api/creators/profile - 更新创作者信息
- GET /api/creators/statistics - 获取创作者数据统计
- GET /api/creators/incomes - 获取收益明细
- GET /api/creators/fans - 获取粉丝列表

### 视频管理相关
- POST /api/creator/videos - 上传视频
- PUT /api/creator/videos/{id} - 更新视频信息
- DELETE /api/creator/videos/{id} - 删除视频
- GET /api/creator/videos - 获取视频列表
- GET /api/creator/videos/{id}/statistics - 获取视频统计数据
- POST /api/creator/videos/{id}/price - 设置视频价格
- GET /api/creator/videos/audit-status - 获取审核状态列表

### 粉丝互动相关
- POST /api/creators/{id}/follow - 关注创作者
- DELETE /api/creators/{id}/follow - 取消关注
- GET /api/creators/{id}/videos - 获取创作者视频列表

### VIP相关接口
- GET /api/vip/levels - 获取VIP等级列表
- GET /api/vip/benefits - 获取VIP权益列表
- GET /api/vip/my-info - 获取我的VIP信息
- POST /api/vip/subscribe - 开通/续费VIP
- POST /api/vip/cancel-auto-renew - 取消自动续费
- GET /api/vip/growth-records - 获取成长值记录
- GET /api/vip/growth-rules - 获取成长值规则

### 视频存储相关接口
- POST /api/storage/upload/init - 初始化分片上传
- POST /api/storage/upload/part - 上传视频分片
- POST /api/storage/upload/complete - 完成视频上传
- GET /api/storage/video/{id}/status - 获取视频处理状态
- GET /api/storage/video/{id}/play-url - 获取视频播放地址
- POST /api/storage/video/{id}/transcode - 手动触发视频转码
- GET /api/storage/video/{id}/qualities - 获取视频清晰度列表
- POST /api/storage/video/{id}/cdn-refresh - 刷新CDN缓存
- GET /api/storage/statistics - 获取存储使用统计

## 部署架构

### PC端部署
- Nginx集群
- CDN加速
- 静态资源优化
- 前端监控
- 性能优化：
  - 路由懒加载
  - 组件按需加载
  - 图片懒加载
  - 资源预加载
  - 浏览器缓存策略

### 移动端部署
- 应用市场发布：
  - App Store
  - Google Play
  - 国内安卓市场
- 热更新方案：
  - Flutter 资源热更新
  - 动态化方案
- 性能优化：
  - 启动优化
  - 包体积优化
  - 内存优化
  - 网络优化
- 监控体系：
  - 性能监控
  - 崩溃监控
  - 用户行为分析

### 后端部署
- JDK 17或以上
- MySQL 8.0或以上（主从复制）
- Redis 6.0或以上（集群模式）
- RocketMQ 4.x
- Nacos 2.x
- Sentinel
- Docker 20.x
- Kubernetes 1.20+
- Nginx（用于静态资源和反向代理）

### 存储服务部署
- 分布式存储系统
  - 主存储节点
  - 备份存储节点
  - 对象存储服务
- 视频处理集群
  - 转码服务器集群
  - 任务调度系统
- CDN分发
  - 全球CDN节点
  - 边缘节点缓存
  - 智能调度系统
- 监控系统
  - 存储容量监控
  - 转码队列监控
  - CDN状态监控
  - 服务质量监控

## 安全考虑
- 使用Spring Security进行身份认证和授权
- 实现JWT token机制
- 密码加密存储
- 防止SQL注入
- XSS防护
- CORS配置
- 接口限流
- 分布式session管理
- 分布式锁实现
- 全链路加密
- 服务间认证
- 限流防刷
- 防止重放攻击
- 敏感数据加密
- 文件上传安全
- 移动端安全
  - SSL Pinning
  - 代码混淆
  - 本地存储加密
- 小程序安全
  - 敏感信息加密
  - 签名验证 