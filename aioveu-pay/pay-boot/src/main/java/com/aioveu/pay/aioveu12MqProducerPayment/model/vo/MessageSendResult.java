package com.aioveu.pay.aioveu12MqProducerPayment.model.vo;

import lombok.Data;
/**
 * @ClassName: MessageSendResult
 * @Description TODO 发送结果
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 17:48
 * @Version 1.0
 **/



/**
 * 发送结果
 */
@Data
public class MessageSendResult {

    /** 消息ID */
    private String messageId;

    /** 发送状态（SUCCESS/FAILURE） */
    private SendStatus sendStatus;

    /** 队列ID（RocketMQ）或分区（Kafka） */
    private Integer queueId;

    /** 偏移量 */
    private Long offset;

    /** 事务ID（事务消息） */
    private String transactionId;

    /** 错误信息 */
    private String errorMsg;

    /** 错误码 */
    private String errorCode;

    /** 发送耗时（毫秒） */
    private long costTime;

    /** 时间戳 */
    private long timestamp = System.currentTimeMillis();

    public boolean isSuccess() {
        return SendStatus.SUCCESS == sendStatus;
    }

    public enum SendStatus {
        SUCCESS, FAILURE, UNKNOWN
    }
}
