package com.video.common.websocket;

import com.video.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final RedisUtils redisUtils;
    private static final Map<String, WebSocketSession> SESSIONS = new ConcurrentHashMap<>();
    private static final String WEBSOCKET_SESSION_PREFIX = "ws:session:";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = getUserId(session);
        SESSIONS.put(userId, session);
        // 将会话信息保存到Redis，实现分布式会话管理
        redisUtils.hset(WEBSOCKET_SESSION_PREFIX + userId, "serverId", getServerId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String userId = getUserId(session);
        String payload = message.getPayload();
        
        // 发布消息到Redis，实现分布式消息广播
        redisUtils.publish("ws:message", Map.of(
            "userId", userId,
            "message", payload
        ));
        
        // 处理消息
        session.sendMessage(new TextMessage("Server received: " + payload));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserId(session);
        SESSIONS.remove(userId);
        // 从Redis中移除会话信息
        redisUtils.delete(WEBSOCKET_SESSION_PREFIX + userId);
    }

    public void sendMessageToUser(String userId, String message) throws IOException {
        WebSocketSession session = SESSIONS.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    private String getUserId(WebSocketSession session) {
        return (String) session.getAttributes().get("userId");
    }

    private String getServerId() {
        // 返回当前服务器的唯一标识，可以是IP:PORT
        return "server-1";
    }
} 