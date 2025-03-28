package com.video.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                 WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            
            // 从请求参数或header中获取用户标识
            String userId = getUserIdentifier(servletRequest);
            
            if (!StringUtils.hasText(userId)) {
                log.warn("WebSocket连接被拒绝：未提供用户标识");
                return false;
            }

            // 将用户标识存储在WebSocket会话属性中
            attributes.put("userId", userId);
            log.info("WebSocket握手 - 用户ID: {}", userId);
            return true;
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                             WebSocketHandler wsHandler, Exception exception) {
        // 握手后的处理，通常为空
    }

    private String getUserIdentifier(HttpServletRequest request) {
        // 首先尝试从请求参数中获取
        String userId = request.getParameter("userId");
        
        // 如果请求参数中没有，尝试从header中获取
        if (!StringUtils.hasText(userId)) {
            userId = request.getHeader("X-User-Id");
        }
        
        // 如果header中也没有，尝试从认证信息中获取
        if (!StringUtils.hasText(userId)) {
            // 这里可以根据实际的认证方式获取用户标识
            // 例如：从JWT token中解析
            String token = request.getHeader("Authorization");
            if (StringUtils.hasText(token)) {
                // 解析token获取用户标识
                userId = extractUserIdFromToken(token);
            }
        }
        
        return userId;
    }

    private String extractUserIdFromToken(String token) {
        // 这里实现从token中提取用户标识的逻辑
        // 示例：简单地移除"Bearer "前缀
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
} 