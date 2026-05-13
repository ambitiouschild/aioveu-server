package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;




import com.alibaba.fastjson.JSON;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
public class RabbitMQMessageSendRequest {

    // ========== 必填字段 ==========
    /** 主题/交换机 */
    @NotBlank(message = "主题不能为空")
    private String topic;

    /** 消息体（JSON/XML/String） */
    @NotNull(message = "消息体不能为空")
    private Object body;

    // ========== 业务标识 ==========
    /** 业务ID（订单号、用户ID等） */
    private String bizId;

    /** 业务类型（ORDER-订单, USER-用户, NOTIFY-通知等） */
    private String bizType;

    /** 消息标签（用于消费过滤） */
    private String tag;

    /** 消息键（用于顺序消息/分区路由） */
    private String messageKey;

    // ========== 消息控制 ==========
    /** 发送超时时间（毫秒） */
    private Long timeout = 3000L;

    /** 延迟发送时间（毫秒，0表示立即） */
    private Long delayTime = 0L;

    /** 消息优先级（0-9，数字越大优先级越高） */
    private Integer priority = 5;

    /** 过期时间（毫秒，0表示永不过期） */
    private Long ttl = 0L;

    /** 是否异步发送 */
    private Boolean async = false;

    // ========== 事务/顺序消息 ==========
    /** 是否为事务消息 */
    private Boolean transactional = false;

    /** 是否为顺序消息 */
    private Boolean ordered = false;

    /** 分区键（用于顺序消息） */
    private String shardingKey;

    // ========== 重试策略 ==========
    /** 重试次数（0表示不重试） */
    private Integer retryTimes = 3;

    /** 重试间隔（毫秒） */
    private Long retryInterval = 1000L;

    /** 是否启用退避策略 */
    private Boolean backoffEnabled = true;

    // ========== 扩展属性 ==========
    /** 消息属性（扩展字段） */
    private Map<String, String> properties = new HashMap<>();

    /** 消息头（RocketMQ Headers/Kafka Headers） */
    private Map<String, String> headers = new HashMap<>();

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
    public RabbitMQMessageSendRequest() {
    }

    public RabbitMQMessageSendRequest(String topic, Object body) {
        this.topic = topic;
        this.body = body;
    }

    public RabbitMQMessageSendRequest(String topic, Object body, String bizId, String bizType) {
        this.topic = topic;
        this.body = body;
        this.bizId = bizId;
        this.bizType = bizType;
    }

    // ========== 工具方法 ==========
    /**
     * 添加属性
     */
    public RabbitMQMessageSendRequest addProperty(String key, String value) {
        if (this.properties == null) {
            this.properties = new HashMap<>();
        }
        this.properties.put(key, value);
        return this;
    }

    /**
     * 添加消息头
     */
    public RabbitMQMessageSendRequest addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
        return this;
    }

    /**
     * 设置业务标识
     */
    public RabbitMQMessageSendRequest withBizInfo(String bizId, String bizType) {
        this.bizId = bizId;
        this.bizType = bizType;
        return this;
    }

    /**
     * 启用异步发送
     */
    public RabbitMQMessageSendRequest async() {
        this.async = true;
        return this;
    }

    /**
     * 启用延迟发送
     */
    public RabbitMQMessageSendRequest delay(long delayMillis) {
        this.delayTime = delayMillis;
        return this;
    }

    /**
     * 获取消息体字符串
     */
    public String getBodyAsString() {
        if (body == null) {
            return null;
        }
        if (body instanceof String) {
            return (String) body;
        }
        // 默认使用JSON序列化
        return JSON.toJSONString(body);
    }

    /**
     * 获取消息大小
     */
    public int getBodySize() {
        String bodyStr = getBodyAsString();
        return bodyStr != null ? bodyStr.getBytes().length : 0;
    }
}
