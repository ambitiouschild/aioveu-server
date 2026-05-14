package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
 */
@Data
@Builder
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
    private String tenantId;

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
}
