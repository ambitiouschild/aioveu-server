package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;

import com.aioveu.pay.aioveu12MqProducerPayment.util.JsonUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.amqp.core.MessageProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: SendMessageRequest
 * @Description TODO RabbitMQ专属发送请求
 *                          发送请求参数 核心业务层（最推荐）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 17:47
 * @Version 1.0
 **/

/**
 * 消息发送请求（业务层DTO）
 * 统一封装各种MQ的发送参数
 */
@Data
@Builder
@AllArgsConstructor
public class RabbitSendRequest {

    // ========== 必填字段 ==========

    /** 交换机名称 */
    @NotBlank(message = "交换机不能为空")
    private String exchange;


    /** 路由键（支持租户ID路由，如：order.create.tenant_001） */
    @NotBlank(message = "路由键不能为空")
    private String routingKey;

    /** 主题/交换机 */
    @NotBlank(message = "主题不能为空")
    private String topic;

    /** 消息体（JSON/XML/String） */
    @NotNull(message = "消息体不能为空")
    private Object body;


    // ========== 业务标识 ==========

    // ========== 业务标识 ==========
    /** 租户ID（多租户商城必须） */
    private Long tenantId;

    /** 消息类型：ORDER_CREATE-订单创建, ORDER_PAY-订单支付, INVENTORY-库存等 */
    @NotBlank(message = "消息类型不能为空")
    private String messageType;


    /** 业务ID（订单号、用户ID等） */
    private String bizId;

    /** 业务类型（ORDER-订单, USER-用户, NOTIFY-通知等） */
    private String bizType;

    /** 消息标签（用于消费过滤） */
    private String tag;

    /** 消息键（用于顺序消息/分区路由） */
    private String messageKey;

    // ========== 消息控制 ==========

    /** 延迟时间（毫秒，0表示不延迟） */
    @Builder.Default
    private long delayMillis = 0;

    /** 优先级（0-9，数字越大优先级越高） */
    @Builder.Default
    private int priority = 0;

    /** 是否持久化（true-服务重启不丢失） */
    @Builder.Default
    private boolean persistent = true;

    /** 过期时间（毫秒，0表示永不过期） */
    @Builder.Default
    private long ttl = 0;

    /** 发送超时时间（毫秒） */
    @Builder.Default
    private long timeout = 3000;

    /** 是否异步发送 */
    @Builder.Default
    private boolean async = false;

    // ========== 事务/顺序消息 ==========
    /** 是否为事务消息 */
    private Boolean transactional = false;

    /** 是否为顺序消息 */
    private Boolean ordered = false;

    /** 分区键（用于顺序消息） */
    private String shardingKey;

    // ========== 重试策略 ==========
    /** 重试次数（0表示不重试） */
    @Builder.Default
    private int retryTimes = 3;

    /** 重试间隔（毫秒） */
    @Builder.Default
    private long retryInterval = 1000;

    /** 是否启用退避策略 */
    private Boolean backoffEnabled = true;

    // ========== 扩展属性 ==========
    /** 消息属性（扩展字段） */
    private Map<String, String> properties = new HashMap<>();

    /** 消息属性（会设置到RabbitMQ Message Properties中） */
    @Builder.Default
    private Map<String, Object> headers = new HashMap<>();

    /** 消息ID（不传则自动生成） */
    private String messageId;

    // ========== 追踪信息 ==========
    /** 追踪ID（用于全链路追踪） */
    private String traceId;

    /** 跨度ID */
    private String spanId;

    /** 调用方应用 */
    private String callerApp;

    /** 调用方IP */
    private String callerIp;

    // ========== 构造方法 ==========
    public RabbitSendRequest() {
    }

    public static RabbitSendRequest of(String exchange, String routingKey, Object body, String messageType) {
        return RabbitSendRequest.builder()
                .exchange(exchange)
                .routingKey(routingKey)
                .body(body)
                .messageType(messageType)
                .build();
    }

    public static RabbitSendRequest of(String exchange, String routingKey, Object body,
                                       String messageType, Long tenantId) {
        return RabbitSendRequest.builder()
                .exchange(exchange)
                .routingKey(routingKey)
                .body(body)
                .messageType(messageType)
                .tenantId(tenantId)
                .build();
    }


    // ========== 链式调用方法 ==========
    /** 添加租户ID */
    public RabbitSendRequest withTenant(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /** 添加业务ID */
    public RabbitSendRequest withBizId(String bizId) {
        this.bizId = bizId;
        return this;
    }

    /** 设置延迟 */
    public RabbitSendRequest withDelay(long delayMillis) {
        this.delayMillis = delayMillis;
        return this;
    }

    /** 启用异步发送 */
    public RabbitSendRequest async() {
        this.async = true;
        return this;
    }

    /** 添加消息头 */
    public RabbitSendRequest addHeader(String key, Object value) {
        this.headers.put(key, value);
        return this;
    }

    /** 设置优先级 */
    public RabbitSendRequest withPriority(int priority) {
        this.priority = priority;
        return this;
    }


    // ========== 工具方法 ==========
    /** 获取消息体字符串 */
    public String getBodyAsString() {
        if (body == null) {
            return null;
        }
        if (body instanceof String) {
            return (String) body;
        }
        // 使用JSON序列化
        return JsonUtils.toJson(body);
    }

    /** 构建RabbitMQ的MessageProperties */
    public MessageProperties buildMessageProperties() {
        MessageProperties properties = new MessageProperties();

        // 基础属性
        properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        properties.setContentEncoding("UTF-8");
        properties.setPriority(this.priority);

        if (this.persistent) {
            properties.setDeliveryMode(MessageProperties.DEFAULT_DELIVERY_MODE);
        }

        if (this.ttl > 0) {
            properties.setExpiration(String.valueOf(this.ttl));
        }

        // 延迟消息（需要安装延迟插件）
        if (this.delayMillis > 0) {
            properties.setHeader("x-delay", this.delayMillis);
        }

        // 业务头
        if (this.tenantId != null) {
            properties.setHeader("tenantId", this.tenantId);
        }
        properties.setHeader("messageType", this.messageType);
        properties.setHeader("sendTime", System.currentTimeMillis());

        if (this.bizId != null) {
            properties.setHeader("bizId", this.bizId);
        }

        if (this.tag != null) {
            properties.setHeader("tag", this.tag);
        }

        // 自定义头
        if (this.headers != null) {
            this.headers.forEach(properties::setHeader);
        }

        return properties;
    }
}
