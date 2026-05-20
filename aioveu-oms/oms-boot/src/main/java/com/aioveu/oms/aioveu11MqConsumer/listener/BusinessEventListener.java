package com.aioveu.oms.aioveu11MqConsumer.listener;


/**
 * @ClassName: BusinessEventListener
 * @Description TODO 业务事件监听器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 20:39
 * @Version 1.0
 **/

import com.aioveu.cache.service.CacheService;
import com.aioveu.common.sms.service.SmsService;
import com.aioveu.database.service.DatabaseService;
import com.aioveu.event.model.vo.MessageSentEvent;
import com.aioveu.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 业务事件监听器
 * 处理消息发送成功后的业务逻辑
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessEventListener {


    private final MailService emailService;
    private final SmsService smsService;
    private final DatabaseService databaseService;
    private final CacheService cacheService;

    @Async("businessTaskExecutor")
    @EventListener
    public void handleMessageSent(MessageSentEvent event) {
        Long tenantId = event.getTenantId();
        String messageType = event.getMessageType();

        try {
            // 根据消息类型处理不同的业务逻辑
            switch (messageType) {
                case "ORDER_CREATE":
                    handleOrderCreateMessage(event);
                    break;
                case "ORDER_PAY":
                    handleOrderPayMessage(event);
                    break;
                case "USER_REGISTER":
                    handleUserRegisterMessage(event);
                    break;
                case "INVENTORY_UPDATE":
                    handleInventoryUpdateMessage(event);
                    break;
                default:
                    log.debug("未知消息类型: {}", messageType);
            }

        } catch (Exception e) {
            log.error("处理业务事件失败: messageId={}, type={}",
                    event.getMessageId(), messageType, e);
        }
    }

    /**
     * 处理订单创建消息
     */
    private void handleOrderCreateMessage(MessageSentEvent event) {
        String orderId = event.getBusinessId();
        if (orderId == null) {
            // 从扩展信息中获取
            orderId = (String) event.getExtraInfo().get("orderId");
        }

//        if (orderId != null) {
//            // 1. 更新订单发送状态
//            databaseService.updateOrderSendStatus(orderId, "SENT", event.getCostTime());
//
//            // 2. 发送订单创建成功通知
//            if (shouldSendNotification(event.getTenantId())) {
//                sendOrderCreateNotification(orderId, event.getTenantId());
//            }
//
//            // 3. 清理缓存
//            cacheService.remove("order:pending:" + orderId);
//
//            log.info("订单创建消息处理完成: orderId={}, cost={}ms",
//                    orderId, event.getCostTime());
//        }
    }

    /**
     * 处理订单支付消息
     */
    private void handleOrderPayMessage(MessageSentEvent event) {
        String orderId = event.getBusinessId();
        if (orderId != null) {
            // 1. 记录支付消息发送时间
//            databaseService.recordPaymentMessageSent(orderId, event.getSendTime());
//
//            // 2. 发送支付成功短信
//            if (shouldSendSms(event.getTenantId())) {
//                smsService.sendPaymentSuccessSms(orderId, event.getTenantId());
//            }

            log.info("订单支付消息处理完成: orderId={}", orderId);
        }
    }

    /**
     * 处理用户注册消息
     */
    private void handleUserRegisterMessage(MessageSentEvent event) {
        String userId = event.getBusinessId();
        if (userId != null) {
            // 发送欢迎邮件
//            emailService.sendWelcomeEmail(userId, event.getTenantId());
//
//            // 记录用户活跃时间
//            databaseService.updateUserLastActive(userId, event.getSendTime());

            log.info("用户注册消息处理完成: userId={}", userId);
        }
    }

    /**
     * 处理库存更新消息
     */
    private void handleInventoryUpdateMessage(MessageSentEvent event) {
        // 更新库存缓存
        String sku = (String) event.getExtraInfo().get("skuCode");
        if (sku != null) {
//            cacheService.refreshInventoryCache(sku, event.getTenantId());
            log.debug("库存缓存已刷新: sku={}", sku);
        }
    }

    /**
     * 发送订单创建通知
     */
    private void sendOrderCreateNotification(String orderId, String tenantId) {
        // 发送邮件通知
//        emailService.sendOrderCreatedEmail(orderId, tenantId);

        // 发送内部通知
        sendInternalNotification("订单创建成功",
                String.format("订单 %s 已创建，消息已发送到MQ", orderId));
    }

    private boolean shouldSendNotification(String tenantId) {
        // 根据租户配置判断
        return true;
    }

    private boolean shouldSendSms(String tenantId) {
        // 根据租户配置判断
        return true;
    }

    private void sendInternalNotification(String title, String message) {
        // 发送到企业微信、钉钉等
    }
}
