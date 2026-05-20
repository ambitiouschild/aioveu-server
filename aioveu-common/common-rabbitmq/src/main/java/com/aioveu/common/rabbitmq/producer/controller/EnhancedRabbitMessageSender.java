package com.aioveu.common.rabbitmq.producer.controller;


import com.aioveu.common.rabbitmq.producer.monitor.RabbitSendResultMonitor;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendRequest;
import com.aioveu.common.rabbitmq.producer.model.vo.RabbitSendResult;
import com.aioveu.common.rabbitmq.producer.model.vo.SendReport;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: EnhancedRabbitMessageSender
 * @Description TODO 增强的RabbitMQ发送服务
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 17:03
 * @Version 1.0
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class EnhancedRabbitMessageSender {

    private final RabbitTemplate rabbitTemplate;

    private final RabbitSendResultMonitor resultMonitor;


    /**
     * 发送消息并返回详细结果
     */
    public RabbitSendResult sendWithDetailedResult(RabbitSendRequest request) {
        long startTime = System.currentTimeMillis();
        String messageId = request.getMessageId() != null ?
                request.getMessageId() : UUID.randomUUID().toString();

        try {
            // 构建关联数据
            CorrelationData correlationData = new CorrelationData(messageId);

            // 发送消息
            rabbitTemplate.convertAndSend(
                    request.getExchange(),
                    request.getRoutingKey(),
                    request.getBody(),
                    message -> {
                        // 设置消息属性
                        MessageProperties properties = message.getMessageProperties();
                        properties.setMessageId(messageId);
                        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

                        if (request.getTenantId() != null) {
                            properties.setHeader("tenantId", request.getTenantId());
                        }
                        if (request.getMessageType() != null) {
                            properties.setHeader("messageType", request.getMessageType());
                        }

                        return message;
                    },
                    correlationData
            );

            // 等待确认
            CorrelationData.Confirm confirm = correlationData.getFuture()
                    .get(request.getTimeout(), TimeUnit.MILLISECONDS);

            // 构建结果
            RabbitSendResult result = RabbitSendResult.builder()
                    .messageId(messageId)
                    .correlationId(correlationData.getId())
                    .exchange(request.getExchange())
                    .routingKey(request.getRoutingKey())
                    .tenantId(request.getTenantId())
                    .messageType(request.getMessageType())
                    .sendThread(Thread.currentThread().getName())
                    .withCostTime(startTime)
                    .withAck(confirm.isAck(), confirm.getReason())
                    .build();

            if (confirm.isAck()) {
                log.info("消息发送成功: {}", result.getSimpleInfo());
            } else {
                log.error("消息发送被Broker拒绝: {}", result.getSimpleInfo());
            }

            return result;

        } catch (TimeoutException e) {
            return RabbitSendResult.timeout(messageId,
                            System.currentTimeMillis() - startTime,
                            request.getExchange(), request.getRoutingKey())
                    .withTenant(request.getTenantId())
                    .withMessageType(request.getMessageType());

        } catch (Exception e) {
            return RabbitSendResult.failure(messageId, e.getMessage(),
                            System.currentTimeMillis() - startTime,
                            request.getExchange(), request.getRoutingKey())
                    .withTenant(request.getTenantId())
                    .withMessageType(request.getMessageType());
        }
    }

    /**
     * 批量发送并生成报告
     */
    public SendReport sendBatchWithReport(List<RabbitSendRequest> requests) {
        List<RabbitSendResult> results = new ArrayList<>();
        List<CompletableFuture<RabbitSendResult>> futures = new ArrayList<>();

        for (RabbitSendRequest request : requests) {
            CompletableFuture<RabbitSendResult> future = CompletableFuture
                    .supplyAsync(() -> sendWithDetailedResult(request))
                    .exceptionally(ex -> {
                        log.error("异步发送异常", ex);
                        return RabbitSendResult.failure(
                                request.getMessageId(), ex.getMessage(), 0,
                                request.getExchange(), request.getRoutingKey()
                        );
                    });
            futures.add(future);
        }

        // 等待所有完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenAccept(v -> {
                    for (CompletableFuture<RabbitSendResult> future : futures) {
                        try {
                            results.add(future.get());
                        } catch (Exception e) {
                            log.error("获取发送结果失败", e);
                        }
                    }
                })
                .join();

        // 生成报告
        return resultMonitor.generateReport(results);
    }


}
