package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;

import com.aioveu.pay.aioveu10MqSendRecord.enums.AckType;
import com.aioveu.pay.aioveu10MqSendRecord.enums.ErrorCategory;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * @ClassName: MessageSendResult
 * @Description TODO RabbitMQ消息发送结果
 *                  包含RabbitMQ特有的返回信息和确认机制
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 17:48
 * @Version 1.0
 **/



/**
 * RabbitMQ消息发送结果
 * 包含RabbitMQ特有的返回信息和确认机制
 * 修复了withTenant等方法
 */
@Data
@Builder(toBuilder = true)  // 支持toBuilder()方法
@NoArgsConstructor
@AllArgsConstructor
public class RabbitSendResult {

    // ========== 基础信息 ==========
    /** 消息ID（业务生成） */
    private String messageId;

    /** RabbitMQ关联ID（CorrelationData ID） */
    private String correlationId;


    /** 发送状态（SUCCESS/FAILURE） */
    private SendStatus sendStatus;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMessage;


    /** 发送耗时（毫秒） */
    private long costTime;


    /** 发送时间戳 */
    @Builder.Default
    private Date sendTime = new Date();


    /** 确认时间戳（收到Broker确认的时间） */
    private Date confirmTime;



    // ========== 消息路由信息 ==========
    /** 交换机名称 */
    private String exchange;

    /** 路由键 */
    private String routingKey;

    /** 是否强制返回（mandatory） */
    private boolean mandatory;

    /** 消息是否成功路由到队列 */
    private boolean routedToQueue;


    // ========== Broker确认信息 ==========
    /** 是否收到Broker确认（publisher confirm） */
    private boolean ackReceived;

    /** Broker确认类型（ack/nack） */
    private AckType ackType;

    /** Broker确认原因（如果是nack） */
    private String ackCause;

    /** Broker确认时间戳 */
    private Date ackTime;


    // ========== 消息返回信息（路由失败时） ==========
    /** 是否触发消息返回（ReturnCallback） */
    private boolean messageReturned;

    /** 返回码（AMQP协议返回码） */
    private Integer replyCode;

    /** 返回文本 */
    private String replyText;

    /** 返回的交换机 */
    private String returnedExchange;

    /** 返回的路由键 */
    private String returnedRoutingKey;



    // ========== 消息属性 ==========
    /** 消息大小（字节） */
    private Integer messageSize;

    /** 消息属性（headers） */
    @Builder.Default
    private Map<String, Object> messageProperties = new HashMap<>();

    /** 租户ID（多租户场景） */
    private Long tenantId;

    /** 消息类型 */
    private String messageType;

    // ========== 扩展信息 ==========
    /** 扩展字段 */
    @Builder.Default
    private Map<String, Object> extraInfo = new HashMap<>();

    /** 是否重试发送 */
    private boolean retried;

    /** 重试次数 */
    @Builder.Default
    private int retryCount = 0;

    /** 发送线程名 */
    private String sendThread;

    /** 客户端IP */
    private String clientIp;

    /** 客户端应用名称 */
    private String clientApp;

    // ========== 枚举定义 ==========


    // ========== 静态工厂方法 ==========

    /**
     * 创建成功结果
     */
    public static RabbitSendResult success(String messageId, String correlationId,
                                           long costTime, String exchange, String routingKey) {
        return RabbitSendResult.builder()
                .messageId(messageId)
                .correlationId(correlationId)
                .sendStatus(SendStatus.SUCCESS)
                .costTime(costTime)
                .exchange(exchange)
                .routingKey(routingKey)
                .ackReceived(true)
                .ackType(AckType.ACK)
                .routedToQueue(true)
                .sendTime(new Date())
                .ackTime(new Date())
                .confirmTime(new Date())
                .build();
    }

    /**
     * 创建失败结果
     */
    public static RabbitSendResult failure(String messageId, String errorMessage,
                                           long costTime, String exchange, String routingKey) {
        return RabbitSendResult.builder()
                .messageId(messageId)
                .sendStatus(SendStatus.FAILED)
                .errorMessage(errorMessage)
                .costTime(costTime)
                .exchange(exchange)
                .routingKey(routingKey)
                .sendTime(new Date())
                .build();
    }

    /**
     * 创建超时结果
     */
    public static RabbitSendResult timeout(String messageId, long costTime,
                                           String exchange, String routingKey) {
        return RabbitSendResult.builder()
                .messageId(messageId)
                .sendStatus(SendStatus.TIMEOUT)
                .errorMessage("发送超时")
                .costTime(costTime)
                .exchange(exchange)
                .routingKey(routingKey)
                .sendTime(new Date())
                .build();
    }

    /**
     * 创建路由失败结果
     */
    public static RabbitSendResult routingFailed(String messageId, ReturnedMessage returnedMessage) {
        return RabbitSendResult.builder()
                .messageId(messageId)
                .sendStatus(SendStatus.ROUTING_FAILED)
                .errorMessage("消息路由失败")
                .messageReturned(true)
                .replyCode(returnedMessage.getReplyCode())
                .replyText(returnedMessage.getReplyText())
                .returnedExchange(returnedMessage.getExchange())
                .returnedRoutingKey(returnedMessage.getRoutingKey())
                .sendTime(new Date())
                .build();
    }

    /**
     * 创建NACK结果
     */
    public static RabbitSendResult nack(String messageId, String correlationId,
                                        String cause, long costTime) {
        return RabbitSendResult.builder()
                .messageId(messageId)
                .correlationId(correlationId)
                .sendStatus(SendStatus.CONFIRM_NACK)
                .ackReceived(true)
                .ackType(AckType.NACK)
                .ackCause(cause)
                .costTime(costTime)
                .errorMessage("Broker返回NACK: " + cause)
                .sendTime(new Date())
                .ackTime(new Date())
                .build();
    }

    /**
     * 从CorrelationData创建结果
     */
    public static RabbitSendResult fromCorrelationData(CorrelationData correlationData,
                                                       long startTime, String exchange,
                                                       String routingKey) {
        RabbitSendResult result = RabbitSendResult.builder()
                .messageId(correlationData.getId())
                .correlationId(correlationData.getId())
                .exchange(exchange)
                .routingKey(routingKey)
                .costTime(System.currentTimeMillis() - startTime)
                .sendTime(new Date(startTime))
                .build();

        if (correlationData.getFuture() != null && correlationData.getFuture().isDone()) {
            try {
                CorrelationData.Confirm confirm = correlationData.getFuture().get();
                if (confirm != null) {
                    result.setAckReceived(true);
                    result.setAckTime(new Date());
                    result.setConfirmTime(new Date());

                    if (confirm.isAck()) {
                        result.setSendStatus(SendStatus.SUCCESS);
                        result.setAckType(AckType.ACK);
                        result.setRoutedToQueue(true);
                    } else {
                        result.setSendStatus(SendStatus.CONFIRM_NACK);
                        result.setAckType(AckType.NACK);
                        result.setAckCause(confirm.getReason());
                        result.setErrorMessage("Broker NACK: " + confirm.getReason());
                    }
                } else {
                    result.setSendStatus(SendStatus.UNKNOWN);
                }
            } catch (Exception e) {
                result.setSendStatus(SendStatus.FAILED);
                result.setErrorMessage("获取确认结果异常: " + e.getMessage());
            }
        } else {
            result.setSendStatus(SendStatus.CONFIRM_TIMEOUT);
            result.setErrorMessage("确认超时");
        }

        return result;
    }


    /**
     * 从RabbitMQ Message创建结果
     */
    public static RabbitSendResult fromMessage(Message message, String exchange,
                                               String routingKey, long costTime) {
        MessageProperties properties = message.getMessageProperties();

        return RabbitSendResult.builder()
                .messageId(properties.getMessageId())
                .exchange(exchange)
                .routingKey(routingKey)
                .costTime(costTime)
                .sendTime(new Date())
                .tenantId((Long) properties.getHeader("tenantId"))
                .messageType((String) properties.getHeader("messageType"))
                .messageSize(message.getBody().length)
                .build();
    }



    // ========== 业务方法 ==========

    /**
     * 判断是否发送成功
     */
    public boolean isSuccess() {
        return SendStatus.SUCCESS == sendStatus;
    }

    /**
     * 判断是否需要重试
     */
    public boolean shouldRetry() {
        return sendStatus == SendStatus.FAILED ||
                sendStatus == SendStatus.TIMEOUT ||
                sendStatus == SendStatus.CONFIRM_TIMEOUT ||
                (sendStatus == SendStatus.CONFIRM_NACK && isRetryableNack());
    }

    /**
     * 判断是否为可重试的NACK
     */
    private boolean isRetryableNack() {
        if (ackCause == null) {
            return true;
        }
        // 不可重试的NACK原因
        String lowerCause = ackCause.toLowerCase();
        return !lowerCause.contains("no route") &&
                !lowerCause.contains("access refused");
    }

    /**
     * 获取完整的错误信息
     */
    public String getFullErrorMessage() {
        StringBuilder sb = new StringBuilder();
        if (errorMessage != null) {
            sb.append(errorMessage);
        }
        if (ackCause != null) {
            sb.append(" [ACK Cause: ").append(ackCause).append("]");
        }
        if (replyText != null) {
            sb.append(" [Reply: ").append(replyText).append("]");
        }
        return sb.toString();
    }

    /**
     * 获取消息位置标识
     */
    public String getMessagePosition() {
        if (exchange != null && routingKey != null) {
            return exchange + " -> " + routingKey;
        }
        return "Unknown";
    }

    /**
     * 获取确认延迟（毫秒）
     */
    public Long getConfirmDelay() {
        if (ackTime != null && sendTime != null) {
            return ackTime.getTime() - sendTime.getTime();
        }
        return null;
    }

    /**
     * 添加扩展信息
     */
    public RabbitSendResult addExtraInfo(String key, Object value) {
        if (this.extraInfo == null) {
            this.extraInfo = new HashMap<>();
        }
        this.extraInfo.put(key, value);
        return this;
    }

    /**
     * 添加消息属性
     */
    public RabbitSendResult addMessageProperty(String key, Object value) {
        if (this.messageProperties == null) {
            this.messageProperties = new HashMap<>();
        }
        this.messageProperties.put(key, value);
        return this;
    }


    // ========== 链式调用方法（修复withTenant等）==========

    /**
     * 设置租户ID（链式调用）
     */
    public RabbitSendResult withTenant(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * 设置消息类型（链式调用）
     */
    public RabbitSendResult withMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    /**
     * 设置重试次数（链式调用）
     */
    public RabbitSendResult withRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    /**
     * 设置发送线程（链式调用）
     */
    public RabbitSendResult withSendThread(String sendThread) {
        this.sendThread = sendThread;
        return this;
    }

    /**
     * 标记为重试（链式调用）
     */
    public RabbitSendResult markAsRetried() {
        this.retried = true;
        this.retryCount++;
        return this;
    }

    /**
     * 设置客户端信息（链式调用）
     */
    public RabbitSendResult withClientInfo(String clientIp, String clientApp) {
        this.clientIp = clientIp;
        this.clientApp = clientApp;
        return this;
    }

    /**
     * 使用toBuilder()创建新实例（不可变对象模式）
     */
    public RabbitSendResult toBuilderTenant(Long tenantId) {
        return this.toBuilder().tenantId(tenantId).build();
    }

    public RabbitSendResult toBuilderMessageType(String messageType) {
        return this.toBuilder().messageType(messageType).build();
    }

    public RabbitSendResult toBuilderWithStatus(SendStatus sendStatus) {
        return this.toBuilder().sendStatus(sendStatus).build();
    }


    // ========== 转换方法 ==========
    /**
     * 转换为Map（用于日志或监控）
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("correlationId", correlationId);
        map.put("sendStatus", sendStatus != null ? sendStatus.name() : null);
        map.put("errorCode", errorCode);
        map.put("errorMessage", errorMessage);
        map.put("costTime", costTime);
        map.put("sendTime", sendTime);
        map.put("confirmTime", confirmTime);
        map.put("exchange", exchange);
        map.put("routingKey", routingKey);
        map.put("mandatory", mandatory);
        map.put("routedToQueue", routedToQueue);
        map.put("ackReceived", ackReceived);
        map.put("ackType", ackType != null ? ackType.name() : null);
        map.put("ackCause", ackCause);
        map.put("ackTime", ackTime);
        map.put("messageReturned", messageReturned);
        map.put("replyCode", replyCode);
        map.put("replyText", replyText);
        map.put("returnedExchange", returnedExchange);
        map.put("returnedRoutingKey", returnedRoutingKey);
        map.put("messageSize", messageSize);
        map.put("tenantId", tenantId);
        map.put("messageType", messageType);
        map.put("retried", retried);
        map.put("retryCount", retryCount);
        return map;
    }

    /**
     * 转换为JSON字符串
     */
    /**
     * 转换为JSON字符串
     */
    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(toMap());
        } catch (Exception e) {
            return "{\"error\":\"转换JSON失败: " + e.getMessage() + "\"}";
        }
    }

    /**
     * 获取简化的结果信息
     */
    /**
     * 获取简化的结果信息
     */
    public String getSimpleInfo() {
        return String.format("RabbitSendResult{messageId=%s, status=%s, exchange=%s, routingKey=%s, cost=%dms, success=%s, tenant=%s}",
                messageId,
                sendStatus,
                exchange,
                routingKey,
                costTime,
                isSuccess(),
                tenantId
        );
    }


    /**
     * 获取详细的结果信息
     */
    public String getDetailInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("RabbitSendResult:\n");
        sb.append("  messageId: ").append(messageId).append("\n");
        sb.append("  status: ").append(sendStatus).append("\n");
        sb.append("  exchange: ").append(exchange).append("\n");
        sb.append("  routingKey: ").append(routingKey).append("\n");
        sb.append("  tenantId: ").append(tenantId).append("\n");
        sb.append("  messageType: ").append(messageType).append("\n");
        sb.append("  costTime: ").append(costTime).append("ms\n");
        sb.append("  success: ").append(isSuccess()).append("\n");

        if (!isSuccess()) {
            sb.append("  error: ").append(getFullErrorMessage()).append("\n");
        }

        if (ackReceived) {
            sb.append("  ackType: ").append(ackType).append("\n");
            if (ackCause != null) {
                sb.append("  ackCause: ").append(ackCause).append("\n");
            }
        }

        if (messageReturned) {
            sb.append("  returned: ").append(replyText)
                    .append(" (code: ").append(replyCode).append(")\n");
        }

        return sb.toString();
    }


    // ========== 工具方法 ==========

    /**
     * 判断是否为路由失败
     */
    public boolean isRoutingFailed() {
        return sendStatus == SendStatus.ROUTING_FAILED;
    }

    /**
     * 判断是否为超时
     */
    public boolean isTimeout() {
        return sendStatus == SendStatus.TIMEOUT || sendStatus == SendStatus.CONFIRM_TIMEOUT;
    }

    /**
     * 判断是否为Broker拒绝
     */
    public boolean isNack() {
        return sendStatus == SendStatus.CONFIRM_NACK;
    }

    /**
     * 获取错误分类
     */
    public ErrorCategory getErrorCategory() {
        if (isSuccess()) {
            return ErrorCategory.SUCCESS;
        }

        if (isTimeout()) {
            return ErrorCategory.TIMEOUT;
        }

        if (isRoutingFailed()) {
            return ErrorCategory.ROUTING;
        }

        if (isNack()) {
            return ErrorCategory.BROKER;
        }

        return ErrorCategory.OTHER;
    }

    /**
     * 创建深度拷贝
     */
    public RabbitSendResult deepCopy() {
        RabbitSendResult copy = new RabbitSendResult();
        copy.messageId = this.messageId;
        copy.correlationId = this.correlationId;
        copy.sendStatus = this.sendStatus;
        copy.errorCode = this.errorCode;
        copy.errorMessage = this.errorMessage;
        copy.costTime = this.costTime;
        copy.sendTime = this.sendTime != null ? (Date) this.sendTime.clone() : null;
        copy.confirmTime = this.confirmTime != null ? (Date) this.confirmTime.clone() : null;
        copy.exchange = this.exchange;
        copy.routingKey = this.routingKey;
        copy.mandatory = this.mandatory;
        copy.routedToQueue = this.routedToQueue;
        copy.ackReceived = this.ackReceived;
        copy.ackType = this.ackType;
        copy.ackCause = this.ackCause;
        copy.ackTime = this.ackTime != null ? (Date) this.ackTime.clone() : null;
        copy.messageReturned = this.messageReturned;
        copy.replyCode = this.replyCode;
        copy.replyText = this.replyText;
        copy.returnedExchange = this.returnedExchange;
        copy.returnedRoutingKey = this.returnedRoutingKey;
        copy.messageSize = this.messageSize;

        if (this.messageProperties != null) {
            copy.messageProperties = new HashMap<>(this.messageProperties);
        }

        copy.tenantId = this.tenantId;
        copy.messageType = this.messageType;

        if (this.extraInfo != null) {
            copy.extraInfo = new HashMap<>(this.extraInfo);
        }

        copy.retried = this.retried;
        copy.retryCount = this.retryCount;
        copy.sendThread = this.sendThread;
        copy.clientIp = this.clientIp;
        copy.clientApp = this.clientApp;

        return copy;
    }

    // ========== Builder扩展方法 ==========

    /**
     * 自定义Builder，支持链式调用
     */
    public static class RabbitSendResultBuilder {
        private String messageId;
        private String correlationId;
        private SendStatus sendStatus;
        private String errorCode;
        private String errorMessage;
        private long costTime;
        private Date sendTime = new Date();
        private Date confirmTime;
        private String exchange;
        private String routingKey;
        private boolean mandatory;
        private boolean routedToQueue;
        private boolean ackReceived;
        private AckType ackType;
        private String ackCause;
        private Date ackTime;
        private boolean messageReturned;
        private Integer replyCode;
        private String replyText;
        private String returnedExchange;
        private String returnedRoutingKey;
        private Integer messageSize;
        private Map<String, Object> messageProperties = new HashMap<>();
        private Long tenantId;
        private String messageType;
        private Map<String, Object> extraInfo = new HashMap<>();
        private boolean retried;
        private int retryCount = 0;
        private String sendThread = Thread.currentThread().getName();
        private String clientIp;
        private String clientApp;

        public RabbitSendResultBuilder withTenant(Long tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public RabbitSendResultBuilder withMessageType(String messageType) {
            this.messageType = messageType;
            return this;
        }

        public RabbitSendResultBuilder withCostTime(long startTime) {
            this.costTime = System.currentTimeMillis() - startTime;
            return this;
        }

        public RabbitSendResultBuilder withAck(boolean ack, String cause) {
            this.ackReceived = true;
            this.ackTime = new Date();
            this.confirmTime = new Date();
            this.ackType = ack ? AckType.ACK : AckType.NACK;
            this.ackCause = cause;
            this.sendStatus = ack ? SendStatus.SUCCESS : SendStatus.CONFIRM_NACK;
            if (!ack) {
                this.errorMessage = "Broker返回NACK: " + cause;
            }
            return this;
        }

        public RabbitSendResultBuilder withReturnedMessage(ReturnedMessage returnedMessage) {
            this.messageReturned = true;
            this.sendStatus = SendStatus.ROUTING_FAILED;
            this.replyCode = returnedMessage.getReplyCode();
            this.replyText = returnedMessage.getReplyText();
            this.returnedExchange = returnedMessage.getExchange();
            this.returnedRoutingKey = returnedMessage.getRoutingKey();
            this.errorMessage = "消息路由失败: " + returnedMessage.getReplyText();
            return this;
        }

        public RabbitSendResultBuilder addMessageProperty(String key, Object value) {
            this.messageProperties.put(key, value);
            return this;
        }

        public RabbitSendResultBuilder addExtraInfo(String key, Object value) {
            this.extraInfo.put(key, value);
            return this;
        }
    }
}
