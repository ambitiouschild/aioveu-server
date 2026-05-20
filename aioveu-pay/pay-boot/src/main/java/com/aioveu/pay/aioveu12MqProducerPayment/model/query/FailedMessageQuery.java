package com.aioveu.pay.aioveu12MqProducerPayment.model.query;


import com.aioveu.common.rabbitmq.enums.SendStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: FailedMessageQuery
 * @Description TODO 查询失败消息的条件参数
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/15 18:58
 * @Version 1.0
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedMessageQuery {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 关联ID
     */
    private String correlationId;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 交换机名称
     */
    private String exchange;

    /**
     * 路由键
     */
    private String routingKey;

    /**
     * 业务ID
     */
    private String businessId;

    /**
     * 错误码
     */
    private String errorCode;

    /**
     * 发送状态（多个状态）
     */
    private List<Integer> sendStatuses;

    /**
     * 最小重试次数
     */
    private Integer minRetryCount;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 是否包含成功消息
     */
    @Builder.Default
    private Boolean includeSuccess = false;

    /**
     * 是否包含可重试消息
     */
    @Builder.Default
    private Boolean retryableOnly = false;

    /**
     * 关键字搜索（消息ID、错误信息等）
     */
    private String keyword;

    // ========== 业务方法 ==========

    /**
     * 获取失败的状态列表
     */
    public List<Integer> getFailedStatuses() {
        List<Integer> statuses = new ArrayList<>();
        statuses.add(SendStatus.FAILED.getValue());
        statuses.add(SendStatus.TIMEOUT.getValue());
        statuses.add(SendStatus.CONFIRM_TIMEOUT.getValue());
        statuses.add(SendStatus.CONFIRM_NACK.getValue());
        statuses.add(SendStatus.ROUTING_FAILED.getValue());
        return statuses;
    }

    /**
     * 是否需要包含成功状态
     */
    public boolean shouldIncludeSuccess() {
        return includeSuccess != null && includeSuccess;
    }

    /**
     * 是否只查询可重试的消息
     */
    public boolean isRetryableOnly() {
        return retryableOnly != null && retryableOnly;
    }

    /**
     * 是否为空查询
     */
    public boolean isEmptyQuery() {
        return messageId == null && correlationId == null && tenantId == null
                && messageType == null && exchange == null && routingKey == null
                && businessId == null && errorCode == null && sendStatuses == null
                && startTime == null && endTime == null && keyword == null;
    }

    /**
     * 获取查询条件描述
     */
    public String getQueryDescription() {
        StringBuilder sb = new StringBuilder("查询条件: ");

        if (messageId != null) sb.append("消息ID=").append(messageId).append(", ");
        if (tenantId != null) sb.append("租户=").append(tenantId).append(", ");
        if (messageType != null) sb.append("消息类型=").append(messageType).append(", ");
        if (startTime != null) sb.append("开始时间=").append(startTime).append(", ");
        if (endTime != null) sb.append("结束时间=").append(endTime).append(", ");
        if (isRetryableOnly()) sb.append("仅可重试, ");

        return sb.toString();
    }
}
