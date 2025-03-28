package com.video.common.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.video.common.handler.CustomWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisMessageListener implements MessageListener {
    
    private final ObjectMapper objectMapper;
    private final CustomWebSocketHandler webSocketHandler;
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(pattern);
            String messageBody = new String(message.getBody());
            
            WebSocketMessage wsMessage = objectMapper.readValue(messageBody, WebSocketMessage.class);
            
            switch (wsMessage.getType()) {
                case SINGLE:
                    webSocketHandler.sendMessageToUser(wsMessage.getTargetId(), wsMessage.getPayload());
                    break;
                case BROADCAST:
                    webSocketHandler.broadcastMessage(wsMessage.getPayload());
                    break;
                default:
                    log.warn("未知的消息类型: {}", wsMessage.getType());
            }
            
        } catch (Exception e) {
            log.error("处理Redis消息失败", e);
        }
    }
} 