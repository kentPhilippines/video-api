package com.video.common.websocket;

import com.video.common.handler.CustomWebSocketHandler;
import com.video.common.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributedWebSocketService {
    
    private static final String WS_CHANNEL = "ws:message:channel";
    private final RedisUtils redisUtils;
    private final CustomWebSocketHandler webSocketHandler;
    
    /**
     * 发送消息给指定用户
     * 1. 尝试在本地发送
     * 2. 通过Redis发布消息到其他节点
     */
    public void sendMessageToUser(String userId, Object message) {
        // 先尝试在本地发送
        boolean localSent = webSocketHandler.sendMessageToUser(userId, message);
        
        // 如果本地发送失败（用户不在本节点），则通过Redis广播到其他节点
        if (!localSent) {
            WebSocketMessage wsMessage = WebSocketMessage.createSingleMessage(userId, message);
            redisUtils.publish(WS_CHANNEL, wsMessage);
            log.debug("消息通过Redis发送到其他节点 - 用户ID: {}", userId);
        }
    }
    
    /**
     * 广播消息给所有用户
     * 1. 在本地广播
     * 2. 通过Redis发布消息到其他节点
     */
    public void broadcastMessage(Object message) {
        // 本地广播
        webSocketHandler.broadcastMessage(message);
        
        // 通过Redis广播到其他节点
        WebSocketMessage wsMessage = WebSocketMessage.createBroadcastMessage(message);
        redisUtils.publish(WS_CHANNEL, wsMessage);
        log.debug("广播消息通过Redis发送到其他节点");
    }
} 