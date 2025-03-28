package com.video.common.config;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;

/**
 * Nacos配置类
 */
@Configuration
@EnableDiscoveryClient
public class NacosConfig {
    // Nacos的配置都在配置文件中完成，这里只需要开启服务发现功能
} 