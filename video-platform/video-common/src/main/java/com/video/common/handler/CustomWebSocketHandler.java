package com.video.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class CustomWebSocketHandler implements WebSocketHandler {
    
    private final ObjectMapper objectMapper;
    // 存储所有活跃的WebSocket会话
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserId(session);
        sessions.put(userId, session);
        log.info("WebSocket连接建立成功 - 用户ID: {}", userId);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws IOException {
        String userId = getUserId(session);
        String payload = message.getPayload().toString();
        log.debug("收到用户 {} 的消息: {}", userId, payload);
        
        // 处理消息
        handleWebSocketMessage(session, payload);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        String userId = getUserId(session);
        log.error("WebSocket传输错误 - 用户ID: {}", userId, exception);
        closeSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        String userId = getUserId(session);
        sessions.remove(userId);
        log.info("WebSocket连接关闭 - 用户ID: {}, 状态: {}", userId, closeStatus);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 发送消息给指定用户
    public boolean sendMessageToUser(String userId, Object message) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonMessage));
                return true;
            } catch (IOException e) {
                log.error("发送消息给用户 {} 失败", userId, e);
            }
        }
        return false;
    }

    // 广播消息给所有用户
    public void broadcastMessage(Object message) {
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(message);
        } catch (IOException e) {
            log.error("消息序列化失败", e);
            return;
        }

        sessions.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(jsonMessage));
                } catch (IOException e) {
                    log.error("广播消息失败 - sessionId: {}", session.getId(), e);
                }
            }
        });
    }

    private void handleWebSocketMessage(WebSocketSession session, String message) {
        try {
            // 这里可以根据业务需求处理不同类型的消息
            // 例如：心跳检测、聊天消息、通知推送等
            
            // 示例：简单地回显消息
            session.sendMessage(new TextMessage("服务器收到消息：" + message));
        } catch (IOException e) {
            log.error("处理WebSocket消息失败", e);
        }
    }

    private String getUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }

    private void closeSession(WebSocketSession session) {
        try {
            String userId = getUserId(session);
            sessions.remove(userId);
            if (session.isOpen()) {
                session.close();
            }
        } catch (IOException e) {
            log.error("关闭WebSocket会话失败", e);
        }
    }
} 