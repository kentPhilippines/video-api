package com.video.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class VideoGatewayApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(VideoGatewayApplication.class, args);
    }
} 