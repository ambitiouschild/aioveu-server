package com.aioveu.pay.aioveu12MqProducerPayment.utils;


import com.aioveu.common.rabbitmq.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.common.rabbitmq.producer.model.vo.RetryCheckResult;
import com.aioveu.common.rabbitmq.producer.model.vo.RetryResult;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

/**
 * @ClassName: CreateResult
 * @Description TODO  创建结果工具
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 21:28
 * @Version 1.0
 **/

@Slf4j
@Component
@RequiredArgsConstructor
public class RetryResultCreator {

    private final RabbitTemplate rabbitTemplate;

    // ========== 辅助方法 ==========

    /**
     * 创建无效ID的结果
     */
    public RetryResult createInvalidIdResult(long startTime) {
        long costTime = System.currentTimeMillis() - startTime;
        log.info("创建无效ID的结果");
        return RetryResult.failure(
                        "unknown",
                        "消息ID不能为空",
                        costTime,
                        null
                )
                .toBuilder()
                .errorCode("INVALID_ID")
                .maxRetryReached(false)
                .sendStatus(SendStatus.FAILED)
                .build();
    }

    /**
     * 创建重复重试的结果
     */
    public RetryResult createDuplicateRetryResult(String messageId, long startTime) {
        long costTime = System.currentTimeMillis() - startTime;
        log.info("创建重复重试的结果");
        return RetryResult.failure(
                        messageId,
                        "消息正在重试中",
                        costTime,
                        null
                )
                .toBuilder()
                .errorCode("DUPLICATE_RETRY")
                .maxRetryReached(false)
                .sendStatus(SendStatus.FAILED)
                .build();
    }

    /**
     * 创建未找到的结果
     */
    public RetryResult createNotFoundResult(String messageId, long startTime) {
        long costTime = System.currentTimeMillis() - startTime;
        log.info("消息记录不存在");
        return RetryResult.failure(
                        messageId,
                        "消息记录不存在",
                        costTime,
                        null
                )
                .toBuilder()
                .errorCode("NOT_FOUND")
                .maxRetryReached(false)
                .sendStatus(SendStatus.FAILED)
                .build();
    }

    /**
     * 创建不能重试的结果
     */
    public RetryResult createCannotRetryResult(MqSendRecord entity,
                                               RetryCheckResult checkResult,
                                               long startTime) {
        long costTime = System.currentTimeMillis() - startTime;

        Integer statusValue = entity.getSendStatus(); // 假设这是从数据库读取的值
        SendStatus status = SendStatus.fromValue(statusValue);

        return RetryResult.failure(
                        entity.getMessageId(),
                        checkResult.getReason(),
                        costTime,
                        entity.getRetryCount()
                )
                .toBuilder()
                .errorCode(checkResult.getErrorCode())
                .maxRetryReached(checkResult.isMaxRetryReached())
                .sendStatus(status)
//                .exchange(entity.getExchange())
//                .routingKey(entity.getRoutingKey())
//                .addExtra("currentStatus", entity.getSendStatus().getLabel())
//                .addExtra("retryCount", entity.getRetryCount())
//                .addExtra("maxRetryCount", 3)
                .build();
    }

    /**
     * 创建异常结果
     */
    public RetryResult createExceptionResult(String messageId, Exception e, long startTime) {
        long costTime = System.currentTimeMillis() - startTime;
        return RetryResult.failure(
                        messageId,
                        "RETRY_EXCEPTION",
                        "重试异常: " + e.getMessage(),
                        costTime,
                        null
                )
                .toBuilder()
                .maxRetryReached(false)
                .sendStatus(SendStatus.FAILED)
                .build()   // 先 build 再 addExtra
                .addExtra("exceptionClass", e.getClass().getName())
                .addExtra("stackTrace", getStackTrace(e));

    }

    /**
     * 检查是否可以重试
     */
    public RetryCheckResult checkIfCanRetry(MqSendRecord entity) {
        RetryCheckResult result = new RetryCheckResult();

        // 检查状态

        Integer statusValue = entity.getSendStatus(); // 假设这是从数据库读取的值
        SendStatus status = SendStatus.fromValue(statusValue);

        if (!isRetryableStatus(status)) {
            result.setCanRetry(false);
            result.setReason("消息不可重试，状态: " + status.getLabel());
            result.setErrorCode("INVALID_STATUS");
            return result;
        }

        // 检查重试次数
        int retryCount = entity.getRetryCount() != null ? entity.getRetryCount() : 0;
        int maxRetryCount = 3; // 最大重试次数

        if (retryCount >= maxRetryCount) {
            result.setCanRetry(false);
            result.setReason("已达到最大重试次数: " + maxRetryCount);
            result.setErrorCode("MAX_RETRY_REACHED");
            result.setMaxRetryReached(true);
            return result;
        }

        // 检查重试时间间隔
        if (entity.getUpdateTime() != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastUpdate = entity.getUpdateTime();

            // 指数退避：第一次5秒，第二次10秒，第三次20秒
            long delaySeconds = (long) Math.pow(2, retryCount) * 5;
            LocalDateTime nextRetryTime = lastUpdate.plusSeconds(delaySeconds);

            if (now.isBefore(nextRetryTime)) {
                result.setCanRetry(false);
                result.setReason("未到重试时间，下次重试: " + nextRetryTime);
                result.setErrorCode("RETRY_NOT_DUE");
                result.setNextRetryTime(nextRetryTime);
                return result;
            }
        }

        result.setCanRetry(true);
        return result;
    }

    /**
     * 判断状态是否可重试
     */
    public boolean isRetryableStatus(SendStatus status) {
        return status == SendStatus.FAILED ||
                status == SendStatus.TIMEOUT ||
                status == SendStatus.CONFIRM_TIMEOUT ||
                status == SendStatus.CONFIRM_NACK ||
                status == SendStatus.ROUTING_FAILED;
    }

    /**
     * 更新实体为重试中状态
     */
    public void updateEntityAsRetrying(MqSendRecord entity) {
        entity.setSendStatus(SendStatus.SENDING.getValue());
        entity.setRetryCount(entity.getRetryCount() != null ? entity.getRetryCount() + 1 : 1);
//        entity.setRetried(true);
        entity.setUpdateTime(LocalDateTime.now());
        entity.setSendTime(LocalDateTime.now()); // 重置发送时间
    }

    /**
     * 执行重试
     */
    public RetryResult executeRetry(MqSendRecord entity, long startTime) {
        try {
            // 1. 构建消息
            Message message = buildRetryMessage(entity);
            CorrelationData correlationData = new CorrelationData(entity.getMessageId());

            // 2. 发送消息
            rabbitTemplate.send(
                    entity.getExchange(),
                    entity.getRoutingKey(),
                    message,
                    correlationData
            );

            // 3. 等待确认
            CorrelationData.Confirm confirm = correlationData.getFuture()
                    .get(5000, TimeUnit.MILLISECONDS);

            long costTime = System.currentTimeMillis() - startTime;

            if (confirm.isAck()) {
                // 生成新的消息ID
                String newMessageId = entity.getMessageId() + "-retry-" + System.currentTimeMillis();

                return RetryResult.success(
                                entity.getMessageId(), // originalMessageId
                                newMessageId,          // newMessageId
                                costTime,              // costTime
                                entity.getRetryCount() // retryCount
                        )
                        .toBuilder()
                        .exchange(entity.getExchange())
                        .routingKey(entity.getRoutingKey())
                        .sendStatus(SendStatus.SUCCESS)
                        .build()
                        .addExtra("ackTime", LocalDateTime.now())
                        .addExtra("correlationId", correlationData.getId());

            } else {
                return RetryResult.failure(
                                entity.getMessageId(),
                                "BROKER_NACK",
                                "Broker返回NACK: " + confirm.getReason(),
                                costTime,
                                entity.getRetryCount()
                        )
                        .toBuilder()
                        .exchange(entity.getExchange())
                        .routingKey(entity.getRoutingKey())
                        .sendStatus(SendStatus.CONFIRM_NACK)
                        .build()
                        .addExtra("ackCause", confirm.getReason());

            }

        } catch (TimeoutException e) {
            long costTime = System.currentTimeMillis() - startTime;
            return RetryResult.failure(
                            entity.getMessageId(),
                            "TIMEOUT",
                            "重试超时: " + e.getMessage(),
                            costTime,
                            entity.getRetryCount()
                    )
                    .toBuilder()
                    .exchange(entity.getExchange())
                    .routingKey(entity.getRoutingKey())
                    .sendStatus(SendStatus.TIMEOUT)
                    .build();

        } catch (Exception e) {
            long costTime = System.currentTimeMillis() - startTime;
            return RetryResult.failure(
                            entity.getMessageId(),
                            "SEND_ERROR",
                            "发送异常: " + e.getMessage(),
                            costTime,
                            entity.getRetryCount()
                    )
                    .toBuilder()
                    .exchange(entity.getExchange())
                    .routingKey(entity.getRoutingKey())
                    .sendStatus(SendStatus.FAILED)
                    .build()
                    .addExtra("exception", e.getClass().getName());

        }
    }

    /**
     * 更新重试后的实体
     */
    public void updateEntityAfterRetry(MqSendRecord entity, RetryResult retryResult) {
        if (retryResult.isSuccess()) {
            entity.setSendStatus(SendStatus.SUCCESS.getValue());
            entity.setErrorMsg(null);
//            entity.setErrorCode(null);
            entity.setConfirmTime(LocalDateTime.now());
            entity.setCostTime(retryResult.getCostTime());

            // 保存新消息ID到扩展信息
            if (retryResult.getNewMessageId() != null) {
                Map<String, Object> extraInfo = new HashMap<>();
                if (entity.getExtraInfo() != null) {
                    extraInfo = entity.getExtraInfo();
                }
                extraInfo.put("retryNewMessageId", retryResult.getNewMessageId());
                entity.setExtraInfo(extraInfo);
            }

        } else {
            entity.setSendStatus(SendStatus.FAILED.getValue());
            entity.setErrorMsg("重试失败: " + retryResult.getError());
//            entity.setErrorCode(retryResult.getErrorCode());

            // 保存重试信息到扩展信息
            Map<String, Object> extraInfo = new HashMap<>();
            if (entity.getExtraInfo() != null) {
                extraInfo = entity.getExtraInfo();
            }
            extraInfo.put("lastRetryTime", LocalDateTime.now());
            extraInfo.put("lastRetryError", retryResult.getError());
            extraInfo.put("lastRetryErrorCode", retryResult.getErrorCode());
            entity.setExtraInfo(extraInfo);
        }

        entity.setUpdateTime(LocalDateTime.now());
    }

    /**
     * 发布重试事件
     */
    private void publishRetryEvent(MqSendRecord entity, RetryResult retryResult) {
        try {
            // 可以集成Spring Event或直接记录日志
            if (retryResult.isSuccess()) {
                log.info("消息重试成功: messageId={}, retryCount={}, cost={}ms",
                        entity.getMessageId(), retryResult.getRetryCount(), retryResult.getCostTime());
            } else {
                log.warn("消息重试失败: messageId={}, error={}, retryCount={}",
                        entity.getMessageId(), retryResult.getError(), retryResult.getRetryCount());
            }
        } catch (Exception e) {
            log.error("发布重试事件失败", e);
        }
    }


    private Message buildRetryMessage(MqSendRecord entity) {
        MessageProperties properties = new MessageProperties();
        properties.setMessageId(entity.getMessageId());
        properties.setContentType("application/json");
        properties.setTimestamp(new Date());

        if (entity.getTenantId() != null) {
            properties.setHeader("tenantId", entity.getTenantId());
        }

//        if (entity.getMessageType() != null) {
//            properties.setHeader("messageType", entity.getMessageType());
//        }
//
//        if (entity.getCorrelationId() != null) {
//            properties.setCorrelationId(entity.getCorrelationId());
//        }

        // 添加重试标记
        properties.setHeader("isRetry", true);
        properties.setHeader("retryCount", entity.getRetryCount());

        // 消息体
        byte[] body = entity.getMessageBody() != null ?
                entity.getMessageBody().getBytes(StandardCharsets.UTF_8) :
                "{}".getBytes(StandardCharsets.UTF_8);

        return new Message(body, properties);
    }

}
