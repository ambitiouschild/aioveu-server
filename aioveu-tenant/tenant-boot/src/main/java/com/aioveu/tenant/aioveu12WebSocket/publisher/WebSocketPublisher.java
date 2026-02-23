package com.aioveu.tenant.aioveu12WebSocket.publisher;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @ClassName: WebSocketPublisher
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 12:59
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketPublisher {

    private SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @Autowired(required = false)
    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publish(String destination, Object payload) {
        if (messagingTemplate == null) {
            log.warn("消息模板尚未初始化，无法发送消息: destination={}", destination);
            return;
        }

        try {
            Object body = serializeIfNeeded(payload);
            messagingTemplate.convertAndSend(destination, body);
        } catch (Exception e) {
            log.error("发送消息失败: destination={}", destination, e);
        }
    }

    public void publishToUser(String username, String destination, Object payload) {
        if (messagingTemplate == null) {
            log.warn("消息模板尚未初始化，无法发送用户消息: username={}, destination={}", username, destination);
            return;
        }

        try {
            Object body = serializeIfNeeded(payload);
            messagingTemplate.convertAndSendToUser(username, destination, body);
        } catch (Exception e) {
            log.error("发送用户消息失败: username={}, destination={}", username, destination, e);
        }
    }

    private Object serializeIfNeeded(Object payload) throws JacksonException {
        if (payload == null) {
            return null;
        }
        if (payload instanceof String || payload instanceof Number || payload instanceof Boolean) {
            return payload;
        }
        return objectMapper.writeValueAsString(payload);
    }
}
