package com.aioveu.oms.aioveu11MqConsumer.listener;


/**
 * @ClassName: AuditEventListener
 * @Description TODO 审计事件监听器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 20:39
 * @Version 1.0
 **/

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 审计事件监听器
 * 记录所有消息发送的审计日志
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final AuditLogService auditLogService;

    @EventListener
    public void handleMessageSent(MessageSentEvent event) {
        try {
            // 创建审计日志
            AuditLog auditLog = AuditLog.builder()
                    .eventType("MESSAGE_SENT")
                    .eventId(event.getEventId())
                    .tenantId(event.getTenantId())
                    .userId(getCurrentUserId())
                    .resourceId(event.getMessageId())
                    .resourceType("MESSAGE")
                    .action("SEND")
                    .actionTime(event.getEventTime())
                    .result("SUCCESS")
                    .detail(buildAuditDetail(event))
                    .ipAddress(getClientIp())
                    .userAgent(getUserAgent())
                    .build();

            // 保存审计日志
            auditLogService.save(auditLog);

            log.debug("审计日志已记录: eventId={}", event.getEventId());

        } catch (Exception e) {
            log.error("记录审计日志失败", e);
        }
    }

    @EventListener
    public void handleMessageSendFailed(MessageSendFailedEvent event) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .eventType("MESSAGE_SEND_FAILED")
                    .tenantId(event.getTenantId())
                    .resourceId(event.getMessageId())
                    .resourceType("MESSAGE")
                    .action("SEND")
                    .actionTime(event.getEventTime())
                    .result("FAILED")
                    .errorCode(event.getErrorCode())
                    .errorMessage(event.getErrorMessage())
                    .detail(buildFailedAuditDetail(event))
                    .build();

            auditLogService.save(auditLog);

        } catch (Exception e) {
            log.error("记录失败审计日志失败", e);
        }
    }

    private String buildAuditDetail(MessageSentEvent event) {
        return String.format(
                "消息发送成功审计 - 消息ID: %s, 租户: %s, 类型: %s, 交换机: %s, 路由键: %s, 耗时: %dms, 大小: %dbytes",
                event.getMessageId(), event.getTenantId(), event.getMessageType(),
                event.getExchange(), event.getRoutingKey(), event.getCostTime(),
                event.getMessageSize() != null ? event.getMessageSize() : 0
        );
    }

    private String buildFailedAuditDetail(MessageSendFailedEvent event) {
        return String.format(
                "消息发送失败审计 - 消息ID: %s, 租户: %s, 类型: %s, 错误: %s, 状态: %s",
                event.getMessageId(), event.getTenantId(), event.getMessageType(),
                event.getErrorMessage(), event.getSendStatus()
        );
    }

    private String getCurrentUserId() {
        // 从SecurityContext获取当前用户
        return "system"; // 示例
    }

    private String getClientIp() {
        // 从请求上下文中获取
        return "127.0.0.1"; // 示例
    }

    private String getUserAgent() {
        // 从请求上下文中获取
        return "RabbitMQ-Sender"; // 示例
    }
}
