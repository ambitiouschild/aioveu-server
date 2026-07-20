package com.aioveu.common.rabbitmq.producer.service.impl;

import com.aioveu.common.rabbitmq.enums.SendStatusEnum;
import com.aioveu.common.rabbitmq.producer.monitor.ProducerMetricsCollector;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import com.aioveu.common.rabbitmq.producer.service.RabbitMessageService;
import com.aioveu.common.rabbitmq.producer.util.MessageIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName: MqSendRecordServiceImpl
 * @Description TODO RabbitMQ专属消息发送服务
 *                    消息发送服务（使用自定义Request）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:48
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMessageServiceImpl implements RabbitMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MessageIdGenerator messageIdGenerator;

    private final ProducerMetricsCollector producerMetricsCollector;

    /**
     * 同步发送并获取结果
     *
     * @param exchange 交换机
     * @param routingKey 路由键
     * @param message 消息体
     * @param tenantId 租户ID
     * @return 发送结果
     */
    public RabbitSendResult sendMessageSync(String exchange, String routingKey, Object message, Long tenantId) {
        long startTime = System.currentTimeMillis();
        String messageId = UUID.randomUUID().toString();

        try {
            // 构建消息
            CorrelationData correlationData = new CorrelationData(messageId);

            // 发送消息
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);

            // 等待确认
            correlationData.getFuture().get(5000, TimeUnit.MILLISECONDS);

            // 构建成功结果
            return RabbitSendResult.success(messageId, correlationData.getId(),
                            System.currentTimeMillis() - startTime, exchange, routingKey)
                    .withTenant(tenantId)  // 使用传入的 tenantId
                    .withMessageType("ORDER_CREATE")
                    .addExtraInfo("bizId", "order_1001");

        } catch (TimeoutException e) {
            // 超时结果
            return RabbitSendResult.timeout(messageId,
                            System.currentTimeMillis() - startTime, exchange, routingKey)
                    .withTenant(tenantId);  // 使用传入的 tenantId

        } catch (Exception e) {
            // 失败结果
            return RabbitSendResult.failure(messageId, e.getMessage(),
                            System.currentTimeMillis() - startTime, exchange, routingKey)
                    .withTenant(tenantId);  // 使用传入的 tenantId
        }
    }

    /**
     * 处理发送结果
     */
    public void handleSendResult(RabbitSendResult result) {
        // 记录日志
        log.info("消息发送结果: {}", result.getSimpleInfo());

        // 判断是否成功
        if (result.isSuccess()) {
            log.info("消息发送成功: messageId={}, exchange={}, cost={}ms",
                    result.getMessageId(), result.getExchange(), result.getCostTime());

            // 成功后的业务处理


        } else {
            log.error("消息发送失败: messageId={}, error={}",
                    result.getMessageId(), result.getFullErrorMessage());

            // 失败处理
            if (result.shouldRetry()) {
                log.warn("消息发送失败，准备重试: messageId={}, retryCount={}",
                        result.getMessageId(), result.getRetryCount());
                retrySend(result);
            } else {

            }
        }

        // 监控指标
        recordMetrics(result);
    }

    /**
     * 批量发送结果处理
     */
    public void handleBatchResults(List<RabbitSendResult> results) {
        int successCount = 0;
        int failureCount = 0;

        for (RabbitSendResult result : results) {
            if (result.isSuccess()) {
                successCount++;
            } else {
                failureCount++;
                log.error("批量发送失败项: {}", result.getSimpleInfo());
            }
        }

        log.info("批量发送完成: 总数={}, 成功={}, 失败={}",
                results.size(), successCount, failureCount);
    }


    /**
     * 正确使用withTenant方法
     */
    public RabbitSendResult createResult(Long tenantId) {
        // 方法1：使用静态工厂方法 + 链式调用
        RabbitSendResult result = RabbitSendResult.success(
                        "msg-001", "corr-001", 150, "order.exchange", "order.create"
                )
                .withTenant(tenantId)  // 使用传入的 tenantId  // ✅ 正确：使用链式调用
                .withMessageType("ORDER_CREATE")
                .withRetryCount(0)
                .withSendThread(Thread.currentThread().getName());

        return result;
    }

    /**
     * 批量创建结果
     */
    public List<RabbitSendResult> createBatchResults(Long tenantId) {
        List<RabbitSendResult> results = new ArrayList<>();

        // 创建多个结果
        for (int i = 1; i <= 5; i++) {
            RabbitSendResult result = RabbitSendResult.success(
                            "msg-" + i,
                            "corr-" + i,
                            100 + i * 50,
                            "order.exchange",
                            "order.create"
                    )
                    .withTenant(tenantId)
                    .withMessageType("ORDER_CREATE")
                    .addExtraInfo("orderId", "order_" + i)
                    .addExtraInfo("userId", "user_" + i);

            results.add(result);
        }

        return results;
    }


    /**
     * 处理失败结果
     */
    public RabbitSendResult createFailureResult(Long tenantId) {
        // 创建失败结果
        RabbitSendResult result = RabbitSendResult.failure(
                        "msg-fail-001",
                        "网络连接失败",
                        5000,
                        "order.exchange",
                        "order.create"
                )
                .withTenant(tenantId)
                .withMessageType("ORDER_CREATE")
                .withRetryCount(1)
                .addExtraInfo("retryAt", new Date());

        return result;
    }


    /**
     * 使用toBuilder模式
     */
    public RabbitSendResult updateResultWithToBuilder(RabbitSendResult original , Long tenantId) {
        // 使用toBuilder创建新实例
        return original.toBuilder()
                .tenantId(tenantId)  // 修改租户
                .messageType("ORDER_UPDATE")  // 修改消息类型
                .retryCount(original.getRetryCount() + 1)  // 增加重试次数
                .sendTime(new Date())  // 更新发送时间
                .build();
    }


    /**
     * 使用toBuilder便捷方法
     */
    public RabbitSendResult updateResultWithConvenienceMethods(RabbitSendResult original, Long tenantId) {
        return original.toBuilderTenant(tenantId)
                .toBuilderMessageType("ORDER_DELETE")
                .toBuilderWithStatus(SendStatusEnum.SUCCESS);
    }


    /**
     * 从ReturnedMessage构建结果
     */
    public RabbitSendResult createResultFromReturned(ReturnedMessage returnedMessage, Long tenantId) {
        if (returnedMessage == null) {
            return RabbitSendResult.failure(
                    messageIdGenerator.generateMessageId(),
                    "ReturnedMessage为空",
                    0L,
                    "unknown",
                    "unknown"
            ).withTenant(tenantId).withMessageType("UNKNOWN");
        }


        String messageId = extractMessageId(returnedMessage.getMessage());

        return RabbitSendResult.routingFailed(messageId, returnedMessage)
                .withTenant(tenantId)
                .withMessageType("ORDER_NOTIFY")
                .addExtraInfo("originalExchange", returnedMessage.getExchange())
                .addExtraInfo("originalRoutingKey", returnedMessage.getRoutingKey())
                .addExtraInfo("replyCode", returnedMessage.getReplyCode())
                .addExtraInfo("replyText", returnedMessage.getReplyText());
    }


    /**
     * 增强的从ReturnedMessage构建结果
     */
    public RabbitSendResult createEnhancedResultFromReturned(ReturnedMessage returnedMessage,
                                                             Long tenantId, String messageType) {
        if (returnedMessage == null) {
            return createNullReturnedResult(tenantId, messageType);
        }

        Message message = returnedMessage.getMessage();
        MessageProperties properties = message != null ? message.getMessageProperties() : null;

        // 提取各种可能的信息
        String messageId = extractMessageIdFromReturned(returnedMessage);
        Long originalTenantId = extractTenantId(properties);
        String originalMessageType = extractMessageType(properties);

        RabbitSendResult result = RabbitSendResult.routingFailed(messageId, returnedMessage)
                .withTenant(originalTenantId != null ? originalTenantId : tenantId)
                .withMessageType(originalMessageType != null ? originalMessageType : messageType)
                .withSendThread(Thread.currentThread().getName());

        // 添加额外的诊断信息
        addDiagnosticInfo(result, returnedMessage, properties);

        return result;
    }


    /**
     * 从消息属性提取租户ID
     */
    public Long extractTenantId(MessageProperties properties) {
        if (properties == null) {
            return null;
        }

        // 1. 从header中获取
        Long tenantIdFromHeader = extractTenantIdFromHeader(properties);
        if (tenantIdFromHeader != null) {
            return tenantIdFromHeader;
        }

        // 从userId获取
        // 2. 从userId中获取
        Long tenantIdFromUserId = extractTenantIdFromUserId(properties);
        if (tenantIdFromUserId != null) {
            return tenantIdFromUserId;
        }

        return null;
    }


    /**
     * 从消息头提取租户ID
     */
    private Long extractTenantIdFromHeader(MessageProperties properties) {
        try {
            Object tenantIdObj = properties.getHeader("tenantId");
            if (tenantIdObj == null) {
                return null;
            }

            if (tenantIdObj instanceof Long) {
                return (Long) tenantIdObj;
            } else if (tenantIdObj instanceof Integer) {
                return ((Integer) tenantIdObj).longValue();
            } else if (tenantIdObj instanceof String) {
                String tenantIdStr = (String) tenantIdObj;
                if (!tenantIdStr.trim().isEmpty()) {
                    return Long.parseLong(tenantIdStr.trim());
                }
            } else if (tenantIdObj instanceof Number) {
                return ((Number) tenantIdObj).longValue();
            }
        } catch (Exception e) {
            log.warn("从header解析tenantId失败", e);
        }
        return null;
    }

    /**
     * 从userId提取租户ID
     */
    private Long extractTenantIdFromUserId(MessageProperties properties) {
        try {
            String userId = properties.getUserId();
            if (userId == null || userId.trim().isEmpty()) {
                return null;
            }

            userId = userId.trim();

            // 如果userId以"tenant_"开头，尝试提取后面的数字
            if (userId.startsWith("tenant_")) {
                String tenantPart = userId.substring("tenant_".length());
                if (!tenantPart.isEmpty()) {
                    return Long.parseLong(tenantPart);
                }
            } else {
                // 直接尝试解析为Long
                return Long.parseLong(userId);
            }
        } catch (NumberFormatException e) {
            log.debug("userId无法转换为Long: {}", properties.getUserId(), e);
        } catch (Exception e) {
            log.warn("从userId提取tenantId失败", e);
        }
        return null;
    }

    /**
     * 从消息属性提取消息类型
     */
    public String extractMessageType(MessageProperties properties) {
        if (properties == null) {
            return null;
        }

        Object messageTypeObj = properties.getHeader("messageType");
        if (messageTypeObj instanceof String) {
            return (String) messageTypeObj;
        }

        Object typeObj = properties.getHeader("type");
        if (typeObj instanceof String) {
            return (String) typeObj;
        }

        return null;
    }

    /**
     * 从CorrelationData构建结果
     */
    public RabbitSendResult createResultFromCorrelation(CorrelationData correlationData,
                                                        long startTime, String exchange,
                                                        String routingKey, Long tenantId) {  // 添加参数) {
        RabbitSendResult result = RabbitSendResult.fromCorrelationData(
                correlationData, startTime, exchange, routingKey);

        // 添加业务信息
        result.setTenantId(tenantId);
        result.setMessageType("ORDER_CREATE");
        result.setSendThread(Thread.currentThread().getName());

        return result;
    }

    /**
     * 实际业务使用示例
     */
    public void businessExample() {
        // 模拟发送消息
        long startTime = System.currentTimeMillis();
        String messageId = UUID.randomUUID().toString();
        Long tenantId = 0l;
        String exchange = "order.exchange";
        String routingKey = "order.create";

        try {
            // 模拟发送消息
            Thread.sleep(100);

            // 创建成功结果
            RabbitSendResult result = RabbitSendResult.success(
                            messageId,
                            messageId,  // correlationId 使用 messageId
                            System.currentTimeMillis() - startTime,
                            exchange,
                            routingKey
                    )
                    .withTenant(tenantId)
                    .withMessageType("ORDER_CREATE")
                    .withSendThread(Thread.currentThread().getName())
                    .addExtraInfo("bizId", "order_1001")
                    .addExtraInfo("amount", 99.9);

            // 处理结果
            handleSendResult(result);

        } catch (Exception e) {
            // 创建失败结果
            RabbitSendResult result = RabbitSendResult.failure(
                            messageId,
                            e.getMessage(),
                            System.currentTimeMillis() - startTime,
                            exchange,
                            routingKey
                    )
                    .withTenant(tenantId)
                    .withMessageType("ORDER_CREATE");

            handleSendResult(result);
        }
    }


    /**
     * 添加诊断信息
     */
    public void addDiagnosticInfo(RabbitSendResult result, ReturnedMessage returnedMessage,
                                   MessageProperties properties) {
        if (properties != null) {
            result.addExtraInfo("contentType", properties.getContentType());
            result.addExtraInfo("contentEncoding", properties.getContentEncoding());
            result.addExtraInfo("priority", properties.getPriority());
            result.addExtraInfo("deliveryMode", properties.getDeliveryMode());
            result.addExtraInfo("timestamp", properties.getTimestamp());
            result.addExtraInfo("appId", properties.getAppId());
            result.addExtraInfo("clusterId", properties.getClusterId());
            result.addExtraInfo("correlationId", properties.getCorrelationId());
        }

        if (returnedMessage.getMessage() != null) {
            byte[] body = returnedMessage.getMessage().getBody();
            if (body != null) {
                result.addExtraInfo("bodySize", body.length);
                // 尝试解析body内容
                try {
                    String bodyStr = new String(body, StandardCharsets.UTF_8);
                    if (bodyStr.length() > 500) {
                        bodyStr = bodyStr.substring(0, 500) + "...";
                    }
                    result.addExtraInfo("bodyPreview", bodyStr);
                } catch (Exception e) {
                    result.addExtraInfo("bodyError", "解析失败: " + e.getMessage());
                }
            }
        }

        result.addExtraInfo("diagnosticTime", new java.util.Date());
        result.addExtraInfo("thread", Thread.currentThread().getName());
    }

    /**
     * 处理空的ReturnedMessage
     */
    public RabbitSendResult createNullReturnedResult(Long tenantId, String messageType) {
        return RabbitSendResult.failure(
                        messageIdGenerator.generateMessageId(),
                        "ReturnedMessage为null",
                        0L,
                        "unknown",
                        "unknown"
                )
                .withTenant(tenantId != null ? tenantId : 0)
                .withMessageType(messageType != null ? messageType : "UNKNOWN")
                .addExtraInfo("errorType", "NULL_RETURNED_MESSAGE")
                .addExtraInfo("diagnostic", "接收到的ReturnedMessage参数为null");
    }



    private void retrySend(RabbitSendResult result) {
        // 实现重试逻辑
        result.setRetried(true);
        result.setRetryCount(result.getRetryCount() + 1);

        // 重新发送消息
        // sendMessageSync(...);
    }


    private void recordMetrics(RabbitSendResult result) {
        // 记录监控指标
        producerMetricsCollector.getMetrics();
    }


    /**
     * 修复的extractMessageId方法
     * 支持多种方式提取消息ID
     */
    public String extractMessageId(Message message) {

        if (message == null) {
            log.warn("消息为空，无法提取messageId");
            return messageIdGenerator.generateMessageId();
        }

        MessageProperties properties = message.getMessageProperties();
        if (properties == null) {
            log.warn("消息属性为空，生成新的messageId");
            return messageIdGenerator.generateMessageId();
        }


        // 方法1：从消息属性中获取messageId
        String messageId = properties.getMessageId();
        if (messageId != null && !messageId.trim().isEmpty()) {
            return messageId;
        }

        // 方法2：从消息头中获取
        Object headerMessageId = properties.getHeader("messageId");
        if (headerMessageId instanceof String) {
            String id = (String) headerMessageId;
            if (!id.trim().isEmpty()) {
                return id;
            }
        }


        // 方法3：从消息体JSON中解析
//        try {
//            String body = new String(message.getBody(), StandardCharsets.UTF_8);
//            Map<String, Object> bodyMap = objectMapper.readValue(body,
//                    new TypeReference<Map<String, Object>>() {});
//
//            if (bodyMap.containsKey("messageId")) {
//                Object idObj = bodyMap.get("messageId");
//                if (idObj != null) {
//                    return idObj.toString();
//                }
//            }
//
//            if (bodyMap.containsKey("id")) {
//                Object idObj = bodyMap.get("id");
//                if (idObj != null) {
//                    return idObj.toString();
//                }
//            }
//
//        } catch (Exception e) {
//            log.debug("从消息体解析messageId失败: {}", e.getMessage());
//        }

        // 方法4：从关联ID中获取
        String correlationId = properties.getCorrelationId();
        if (correlationId != null && !correlationId.trim().isEmpty()) {
            return correlationId;
        }

        // 方法5：生成新的messageId
        log.debug("无法从消息中提取messageId，生成新的ID");
        return messageIdGenerator.generateMessageId();
    }


    /**
     * 专门处理ReturnedMessage的提取方法
     */
    public String extractMessageIdFromReturned(ReturnedMessage returnedMessage) {
        if (returnedMessage == null) {
            return messageIdGenerator.generateMessageId();
        }

        return extractMessageId(returnedMessage.getMessage());
    }



    /**
     * 分析结果列表
     */
    public void analyzeResults(List<RabbitSendResult> results) {
        int total = results.size();
        int success = 0;
        int failure = 0;
        Map<Long, Integer> tenantStats = new HashMap<>();
        Map<SendStatusEnum, Integer> statusStats = new HashMap<>();

        for (RabbitSendResult result : results) {
            if (result.isSuccess()) {
                success++;
            } else {
                failure++;
            }

            // 租户统计
            if (result.getTenantId() != null) {
                tenantStats.merge(result.getTenantId(), 1, Integer::sum);
            }

            // 状态统计
            if (result.getSendStatusEnum() != null) {
                statusStats.merge(result.getSendStatusEnum(), 1, Integer::sum);
            }
        }

        log.info("分析结果: 总数={}, 成功={}, 失败={}, 成功率={}%",
                total, success, failure, total > 0 ? (success * 100.0 / total) : 0);

        log.info("租户分布: {}", tenantStats);
        log.info("状态分布: {}", statusStats);
    }


    /**
     * 创建路由失败结果示例
     */
    public RabbitSendResult createRoutingFailedResult(Long tenantId) {
        // 模拟ReturnedMessage
        ReturnedMessage returnedMessage = new ReturnedMessage(
                new org.springframework.amqp.core.Message("test".getBytes(), new MessageProperties()),
                312,  // NO_ROUTE
                "NO_ROUTE",
                "order.exchange",
                "order.invalid.routing"
        );

        return RabbitSendResult.routingFailed("msg-routing-001", returnedMessage)
                .withTenant(tenantId)
                .withMessageType("ORDER_NOTIFY");
    }




    /**
     * 批量处理ReturnedMessages
     */
    public List<RabbitSendResult> createResultsFromReturnedList(List<ReturnedMessage> returnedMessages,
                                                                Long defaultTenant, String defaultMessageType) {
        List<RabbitSendResult> results = new ArrayList<>();

        if (returnedMessages == null || returnedMessages.isEmpty()) {
            log.warn("ReturnedMessages列表为空");
            return results;
        }

        for (ReturnedMessage returnedMessage : returnedMessages) {
            try {
                RabbitSendResult result = createEnhancedResultFromReturned(
                        returnedMessage, defaultTenant, defaultMessageType
                );
                results.add(result);
            } catch (Exception e) {
                log.error("处理ReturnedMessage失败", e);
                // 创建错误结果
                results.add(createErrorResult(e, defaultTenant, defaultMessageType));
            }
        }

        return results;
    }


    /**
     * 创建错误结果
     */
    private RabbitSendResult createErrorResult(Exception e, Long tenantId, String messageType) {
        return RabbitSendResult.failure(
                        messageIdGenerator.generateMessageId(),
                        "处理ReturnedMessage异常: " + e.getMessage(),
                        0L,
                        "error",
                        "error"
                )
                .withTenant(tenantId)
                .withMessageType(messageType)
                .addExtraInfo("errorClass", e.getClass().getName())
                .addExtraInfo("stackTrace", getStackTrace(e));
    }


    /**
     * 获取堆栈跟踪
     */
    private String getStackTrace(Exception e) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }


    /**
     * 分析ReturnedMessage的原因
     */
    public String analyzeReturnedCause(ReturnedMessage returnedMessage) {
        if (returnedMessage == null) {
            return "ReturnedMessage为空";
        }

        int replyCode = returnedMessage.getReplyCode();
        String replyText = returnedMessage.getReplyText();

        switch (replyCode) {
            case 312: // NO_ROUTE
                return String.format("路由失败(312): 交换机[%s]没有找到匹配队列的路由键[%s]",
                        returnedMessage.getExchange(), returnedMessage.getRoutingKey());

            case 313: // NO_CONSUMERS
                return String.format("无消费者(313): 交换机[%s]路由键[%s]的队列没有消费者",
                        returnedMessage.getExchange(), returnedMessage.getRoutingKey());

            case 403: // ACCESS_REFUSED
                return String.format("访问拒绝(403): 对交换机[%s]的访问被拒绝",
                        returnedMessage.getExchange());

            case 404: // NOT_FOUND
                return String.format("资源不存在(404): 交换机[%s]或队列不存在",
                        returnedMessage.getExchange());

            default:
                return String.format("未知错误(%d): %s", replyCode, replyText);
        }
    }


    /**
     * 发送消息并处理返回
     */
    public void sendMessageWithReturnHandling() {
        String exchange = "order.exchange";
        String routingKey = "order.create";
        String message = "{\"orderId\": \"1001\", \"amount\": 99.9}";
        Long tenantId = 1l;
        String messageType = "ORDER_CREATE";

        // 生成消息ID
        String messageId = "msg-" + UUID.randomUUID().toString();

        try {
            // 设置消息属性
            MessageProperties properties = new MessageProperties();
            properties.setMessageId(messageId);
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            properties.setHeader("tenantId", tenantId);
            properties.setHeader("messageType", messageType);

            // 创建消息
            Message rabbitMessage = new Message(
                    message.getBytes(StandardCharsets.UTF_8),
                    properties
            );

            // 发送消息
            rabbitTemplate.send(exchange, routingKey, rabbitMessage);

            log.info("消息发送成功: {}", messageId);

        } catch (Exception e) {
            log.error("消息发送失败: {}", messageId, e);

            // 创建失败结果
            RabbitSendResult result = RabbitSendResult.failure(
                    messageId, e.getMessage(), 0L, exchange, routingKey
            ).withTenant(tenantId).withMessageType(messageType);

            // 处理失败结果
            handleSendResult(result);
        }
    }

    /**
     * 测试 ReturnCallback
     */
    public void testReturnCallback() {
        // 发送一个会路由失败的消息
        String invalidExchange = "nonexistent.exchange";
        String invalidRoutingKey = "invalid.routing.key";
        String message = "{\"test\": \"message\"}";

        MessageProperties properties = new MessageProperties();
        properties.setMessageId("test-msg-001");
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setHeader("tenantId", "test_tenant");
        properties.setHeader("messageType", "TEST");

        Message rabbitMessage = new Message(
                message.getBytes(StandardCharsets.UTF_8),
                properties
        );

        // 这会触发 ReturnCallback
        rabbitTemplate.send(invalidExchange, invalidRoutingKey, rabbitMessage);

        log.info("测试消息已发送，应该会触发ReturnCallback");
    }


    /**
     * 模拟处理 ReturnedMessage
     */
    public void processReturnedMessageExample(Long tenantId) {
        // 创建一个模拟的 ReturnedMessage
        MessageProperties properties = new MessageProperties();
        properties.setMessageId("test-returned-001");
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setHeader("tenantId", tenantId);
        properties.setHeader("messageType", "ORDER_CREATE");

        Message message = new Message(
                "{\"orderId\": \"1001\"}".getBytes(StandardCharsets.UTF_8),
                properties
        );

        ReturnedMessage returnedMessage = new ReturnedMessage(
                message,
                312, // NO_ROUTE
                "NO_ROUTE to queue 'order.queue' from exchange 'order.exchange'",
                "order.exchange",
                "order.invalid.routing"
        );

        // 使用方法1：基本的创建结果
        RabbitSendResult result1 = createResultFromReturned(returnedMessage, tenantId);
        log.info("基本结果: {}", result1.getSimpleInfo());

        // 使用方法2：增强的创建结果
        RabbitSendResult result2 = createEnhancedResultFromReturned(
                returnedMessage, tenantId, "ORDER_CREATE"
        );
        log.info("增强结果: {}", result2.getDetailInfo());

        // 分析方法原因
        String cause = analyzeReturnedCause(returnedMessage);
        log.info("失败原因分析: {}", cause);
    }





}
