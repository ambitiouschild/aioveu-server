package com.aioveu.rocketmq.producer.model.vo;


/**
 * @ClassName: MqBatchSendResult
 * @Description TODO  Apache RocketMQ 风格
 *                      适用于 RocketMQ 的批量发送，包含消息ID、队列分配和错误消息详情。
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 16:03
 * @Version 1.0
 **/

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * RocketMQ批量发送结果
 */
@Data
public class RocketBatchSendResult {


    /** 发送状态（SEND_OK, FLUSH_DISK_TIMEOUT, etc） */
    private SendStatus sendStatus;

    /** 消息ID列表（与发送的消息一一对应） */
    private List<String> msgIds = new ArrayList<>();

    /** 主题 */
    private String topic;

    /** 队列ID列表 */
    private List<Integer> queueIds = new ArrayList<>();

    /** 队列偏移量列表（消息在队列中的位置） */
    private List<Long> queueOffsets = new ArrayList<>();

    /** 事务ID（如果是事务消息） */
    private String transactionId;

    /** 区域ID（跨地域部署时使用） */
    private String regionId;

    /** 错误消息（如果有） */
    private String errorMsg;

    /** 是否全部成功 */
    public boolean isSuccess() {
        return SendStatus.SEND_OK == sendStatus;
    }

    public enum SendStatus {
        SEND_OK,           // 发送成功
        FLUSH_DISK_TIMEOUT, // 刷盘超时
        FLUSH_SLAVE_TIMEOUT, // 同步到从节点超时
        SLAVE_NOT_AVAILABLE // 从节点不可用
    }
}
