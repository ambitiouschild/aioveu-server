package com.aioveu.common.rabbitmq.producer.model.vo;


/**
 * @ClassName: RetryCheckResult
 * @Description TODO 重试检查结果
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 21:42
 * @Version 1.0
 **/

import com.aioveu.common.rabbitmq.enums.SendStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 重试检查结果
 * 用于判断消息是否可以重试
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryCheckResult {

    /** 是否可以重试 */
    private boolean canRetry;

    /** 原因（不能重试时） */
    private String reason;

    /** 错误码 */
    private String errorCode;

    /** 是否达到最大重试次数 */
    private boolean maxRetryReached;

    /** 下次重试时间 */
    private LocalDateTime nextRetryTime;

    /** 当前重试次数 */
    private Integer currentRetryCount;

    /** 最大重试次数 */
    private Integer maxRetryCount;

    /** 消息状态 */
    private SendStatusEnum sendStatusEnum;

    /** 消息ID */
    private String messageId;

    // ========== 静态工厂方法 ==========

    /**
     * 可以重试
     */
    public static RetryCheckResult canRetry(String messageId,
                                            Integer currentRetryCount,
                                            SendStatusEnum sendStatusEnum) {
        return RetryCheckResult.builder()
                .canRetry(true)
                .messageId(messageId)
                .currentRetryCount(currentRetryCount)
                .sendStatusEnum(sendStatusEnum)
                .maxRetryCount(3) // 默认3次
                .build();
    }

    /**
     * 消息为空
     */
    public static RetryCheckResult messageNull() {
        return RetryCheckResult.builder()
                .canRetry(false)
                .reason("消息记录为空")
                .errorCode("MESSAGE_NULL")
                .build();
    }

    /**
     * 消息已成功
     */
    public static RetryCheckResult alreadySuccess(String messageId) {
        return RetryCheckResult.builder()
                .canRetry(false)
                .reason("消息已发送成功，无需重试")
                .errorCode("ALREADY_SUCCESS")
                .messageId(messageId)
                .sendStatusEnum(SendStatusEnum.SUCCESS)
                .build();
    }

    /**
     * 不可重试的状态
     */
    public static RetryCheckResult invalidStatus(String messageId, SendStatusEnum status) {
        return RetryCheckResult.builder()
                .canRetry(false)
                .reason("消息状态不可重试: " + status.getLabel())
                .errorCode("INVALID_STATUS")
                .messageId(messageId)
                .sendStatusEnum(status)
                .build();
    }

    /**
     * 达到最大重试次数
     */
    public static RetryCheckResult maxRetryReached(String messageId,
                                                   Integer currentRetryCount,
                                                   Integer maxRetryCount) {
        return RetryCheckResult.builder()
                .canRetry(false)
                .reason("已达到最大重试次数: " + currentRetryCount + "/" + maxRetryCount)
                .errorCode("MAX_RETRY_REACHED")
                .maxRetryReached(true)
                .messageId(messageId)
                .currentRetryCount(currentRetryCount)
                .maxRetryCount(maxRetryCount)
                .build();
    }

    /**
     * 未到重试时间
     */
    public static RetryCheckResult retryNotDue(String messageId,
                                               LocalDateTime nextRetryTime) {
        return RetryCheckResult.builder()
                .canRetry(false)
                .reason("未到重试时间，下次重试: " + nextRetryTime)
                .errorCode("RETRY_NOT_DUE")
                .nextRetryTime(nextRetryTime)
                .messageId(messageId)
                .build();
    }

    /**
     * 路由失败（这种错误重试也没用）
     */
    public static RetryCheckResult routingFailed(String messageId) {
        return RetryCheckResult.builder()
                .canRetry(false)
                .reason("路由失败，重试无效")
                .errorCode("ROUTING_FAILED")
                .messageId(messageId)
                .sendStatusEnum(SendStatusEnum.ROUTING_FAILED)
                .build();
    }

    // ========== 业务方法 ==========

    /**
     * 获取剩余重试次数
     */
    public Integer getRemainingRetryCount() {
        if (currentRetryCount == null || maxRetryCount == null) {
            return 0;
        }
        return Math.max(0, maxRetryCount - currentRetryCount);
    }

    /**
     * 获取重试摘要
     */
    public String getSummary() {
        if (canRetry) {
            return String.format("可以重试: messageId=%s, 当前第%d次, 剩余%d次",
                    messageId, currentRetryCount, getRemainingRetryCount());
        } else {
            return String.format("不可重试: messageId=%s, 原因=%s",
                    messageId, reason);
        }
    }
}
