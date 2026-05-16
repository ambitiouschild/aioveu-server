package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RocketMQ;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: SendContext
 * @Description TODO SendContext是一个自定义的类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/16 15:33
 * @Version 1.0
 **/
@Data
public class SendContext {

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息体
     */
    private Object messageBody;

    /**
     * Exchange
     */
    private String exchange;

    /**
     * Routing key
     */
    private String routingKey;

    /**
     * 重试次数
     */
    private Integer retryCount = 0;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount = 3;

    /**
     * 下次重试时间
     */
    private Long nextRetryTime;

    /**
     * 消息属性
     */
    private Map<String, Object> properties = new HashMap<>();

    /**
     * 创建时间
     */
    private Long createTime = System.currentTimeMillis();

    /**
     * 上次重试时间
     */
    private Long lastRetryTime;
}
