package com.video.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.common.handler.CustomWebSocketHandler;
import com.video.common.interceptor.WebSocketInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws")
                .setAllowedOrigins("*")
                .addInterceptors(webSocketInterceptor());
    }

    @Bean
    public CustomWebSocketHandler webSocketHandler() {
        return new CustomWebSocketHandler(objectMapper());
    }

    @Bean
    public WebSocketInterceptor webSocketInterceptor() {
        return new WebSocketInterceptor();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
} 