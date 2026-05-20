package com.aioveu.common.rabbitmq.producer.model.vo;


/**
 * @ClassName: RetryResult
 * @Description TODO
 *                      使用@Accessors(chain = true)支持链式调用
 *                      使用@Builder(toBuilder = true)
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/14 21:15
 * @Version 1.0
 **/

import com.aioveu.common.rabbitmq.enums.SendStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息重试结果
 * 自定义实现，更符合业务需求
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RetryResult {

    /** 重试是否成功 */
    private boolean success;

    /** 错误信息（失败时） */
    private String error;

    /** 错误码 */
    private String errorCode;

    /** 重试耗时（毫秒） */
    private Long costTime;

    /** 重试时间 */
    @Builder.Default
    private LocalDateTime retryTime = LocalDateTime.now();

    /** 重试后的消息ID */
    private String newMessageId;

    /** 原始消息ID */
    private String originalMessageId;

    /** 重试次数（第几次重试） */
    private Integer retryCount;

    /** 是否达到最大重试次数 */
    private boolean maxRetryReached;

    /** 扩展信息 */
    @Builder.Default
    private Map<String, Object> extraInfo = new HashMap<>();

    /** 重试后的消息状态 */
    private SendStatus sendStatus;

    /** 交换机 */
    private String exchange;

    /** 路由键 */
    private String routingKey;

    // ========== 静态工厂方法 ==========

    /**
     * 重试成功
     */
    public static RetryResult success(String originalMessageId, String newMessageId,
                                      Long costTime, Integer retryCount) {
        return RetryResult.builder()
                .success(true)
                .costTime(costTime)
                .retryTime(LocalDateTime.now())
                .originalMessageId(originalMessageId)
                .newMessageId(newMessageId)
                .retryCount(retryCount)
                .sendStatus(SendStatus.SUCCESS)
                .build();
    }

    /**
     * 重试失败
     */
    public static RetryResult failure(String originalMessageId, String error,
                                      Long costTime, Integer retryCount) {
        return RetryResult.builder()
                .success(false)
                .error(error)
                .costTime(costTime)
                .retryTime(LocalDateTime.now())
                .originalMessageId(originalMessageId)
                .retryCount(retryCount)
                .sendStatus(SendStatus.FAILED)
                .build();
    }

    /**
     * 重试失败（带错误码）
     */
    public static RetryResult failure(String originalMessageId, String errorCode,
                                      String error, Long costTime, Integer retryCount) {
        return RetryResult.builder()
                .success(false)
                .errorCode(errorCode)
                .error(error)
                .costTime(costTime)
                .retryTime(LocalDateTime.now())
                .originalMessageId(originalMessageId)
                .retryCount(retryCount)
                .sendStatus(SendStatus.FAILED)
                .build();
    }

    /**
     * 达到最大重试次数
     */
    public static RetryResult maxRetryReached(String originalMessageId,
                                              Integer maxRetryCount, Long costTime) {
        return RetryResult.builder()
                .success(false)
                .error("已达到最大重试次数: " + maxRetryCount)
                .errorCode("MAX_RETRY_REACHED")
                .costTime(costTime)
                .retryTime(LocalDateTime.now())
                .originalMessageId(originalMessageId)
                .retryCount(maxRetryCount)
                .maxRetryReached(true)
                .sendStatus(SendStatus.FAILED)
                .build();
    }

    /**
     * 从RabbitSendResult创建
     * 添加链式调用方法（推荐）
     */
    public static RetryResult fromRabbitResultChain(String originalMessageId,
                                               RabbitSendResult rabbitResult,
                                               Integer retryCount) {
        if (rabbitResult.isSuccess()) {
            return success(originalMessageId,
                    rabbitResult.getMessageId(),
                    rabbitResult.getCostTime(),
                    retryCount)
                    .setExchange(rabbitResult.getExchange())
                    .setRoutingKey(rabbitResult.getRoutingKey());
        } else {
            return failure(originalMessageId,
                    rabbitResult.getErrorMessage(),
                    rabbitResult.getCostTime(),
                    retryCount)
                    .setExchange(rabbitResult.getExchange())
                    .setRoutingKey(rabbitResult.getRoutingKey());
        }
    }

    /**
     * 从RabbitSendResult创建
     * 使用toBuilder()方法（最灵活）
     */
    public static RetryResult fromRabbitResult(String originalMessageId,
                                               RabbitSendResult rabbitResult,
                                               Integer retryCount) {
        if (rabbitResult.isSuccess()) {
            return success(originalMessageId,
                    rabbitResult.getMessageId(),
                    rabbitResult.getCostTime(),
                    retryCount)
                    .toBuilder()  // 转换为Builder
                    .exchange(rabbitResult.getExchange())
                    .routingKey(rabbitResult.getRoutingKey())
                    .build();  // 重新构建
        } else {
            return failure(originalMessageId,
                    rabbitResult.getErrorMessage(),
                    rabbitResult.getCostTime(),
                    retryCount)
                    .toBuilder()  // 转换为Builder
                    .exchange(rabbitResult.getExchange())
                    .routingKey(rabbitResult.getRoutingKey())
                    .build();  // 重新构建
        }
    }


    // ========== 业务方法 ==========

    /**
     * 添加扩展信息
     */
    public RetryResult addExtraInfo(String key, Object value) {
        if (this.extraInfo == null) {
            this.extraInfo = new HashMap<>();
        }
        this.extraInfo.put(key, value);
        return this;
    }

    /**
     * 获取扩展信息
     */
    public Object getExtraInfo(String key) {
        return this.extraInfo != null ? this.extraInfo.get(key) : null;
    }

    /**
     * 获取结果摘要
     */
    public String getSummary() {
        if (success) {
            return String.format("重试成功: 原消息=%s, 新消息=%s, 耗时=%dms, 第%d次重试",
                    originalMessageId, newMessageId, costTime, retryCount);
        } else {
            return String.format("重试失败: 消息=%s, 错误=%s, 耗时=%dms, 第%d次重试",
                    originalMessageId, error, costTime, retryCount);
        }
    }

    /**
     * 转换为Map
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", success);
        map.put("error", error);
        map.put("errorCode", errorCode);
        map.put("costTime", costTime);
        map.put("retryTime", retryTime);
        map.put("originalMessageId", originalMessageId);
        map.put("newMessageId", newMessageId);
        map.put("retryCount", retryCount);
        map.put("maxRetryReached", maxRetryReached);
        map.put("sendStatus", sendStatus != null ? sendStatus.name() : null);
        map.put("exchange", exchange);
        map.put("routingKey", routingKey);
        if (extraInfo != null && !extraInfo.isEmpty()) {
            map.put("extraInfo", extraInfo);
        }
        return map;
    }

    /**
     * 转换为JSON
     */
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(toMap());
        } catch (Exception e) {
            return "{\"error\":\"转换失败: " + e.getMessage() + "\"}";
        }
    }

    /**
     * 是否应该继续重试
     */
    public boolean shouldContinueRetry(int maxRetryCount) {
        if (success) {
            return false; // 成功了就不重试了
        }

        if (maxRetryReached) {
            return false; // 已达到最大重试次数
        }

        if (retryCount != null && retryCount >= maxRetryCount) {
            return false; // 达到最大重试次数
        }

        // 根据错误类型判断是否应该继续重试
        if (errorCode != null) {
            switch (errorCode) {
                case "NO_ROUTE":
                case "ACCESS_REFUSED":
                case "MESSAGE_INVALID":
                    return false; // 这些错误重试也没用
                default:
                    return true; // 其他错误可以继续重试
            }
        }

        return true;
    }


    // ========== 链式调用方法 ==========

    public RetryResult withExchange(String exchange) {
        this.exchange = exchange;
        return this;
    }

    public RetryResult withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public RetryResult withErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public RetryResult withExtraInfo(Map<String, Object> extraInfo) {
        this.extraInfo = extraInfo;
        return this;
    }

    public RetryResult addExtra(String key, Object value) {
        if (this.extraInfo == null) {
            this.extraInfo = new HashMap<>();
        }
        this.extraInfo.put(key, value);
        return this;
    }
}
