package com.video.common.websocket;

import lombok.Data;

@Data
public class WebSocketMessage {
    private MessageType type;
    private String targetId;
    private Object payload;
    
    public enum MessageType {
        SINGLE,     // 单播消息
        BROADCAST   // 广播消息
    }
    
    public static WebSocketMessage createSingleMessage(String targetId, Object payload) {
        WebSocketMessage message = new WebSocketMessage();
        message.setType(MessageType.SINGLE);
        message.setTargetId(targetId);
        message.setPayload(payload);
        return message;
    }
    
    public static WebSocketMessage createBroadcastMessage(Object payload) {
        WebSocketMessage message = new WebSocketMessage();
        message.setType(MessageType.BROADCAST);
        message.setPayload(payload);
        return message;
    }
} 