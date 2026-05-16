package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RocketMQ;


/**
 * @ClassName: RocketMQSendResult
 * @Description TODO 创建 RocketMQSendResult 包装类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/16 17:54
 * @Version 1.0
 **/

/*
        TODO
            推荐方案1：创建包装类，不继承 RocketMQ 的原生类，因为：
            1.松耦合：不依赖 RocketMQ 的具体实现
            2.灵活性：可以添加自定义字段
            3.易于序列化：Lombok 的注解便于 JSON 序列化
            4.兼容性好：即使 RocketMQ 升级，你的代码也不受影响
            方案2（继承方式）的缺点是：
            继承会暴露 RocketMQ 的所有细节
            可能破坏封装性
            升级 RocketMQ 时可能需要修改代码
            选择方案1，创建独立的 RocketMQSendResult包装类。

* */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;

/**
 * RocketMQ 发送结果包装类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RocketMQSendResult {

    // 基础信息
    private boolean success;
    private String messageId;
    private String topic;
    private String sendStatus;  // 发送状态字符串
    private String errorMsg;

    // RocketMQ 特有信息
    private String msgId;
    private Integer queueId;
    private Long queueOffset;
    private String transactionId;
    private String regionId;

    // 时间信息
    private Long sendTime = System.currentTimeMillis();
    private Long costTime = 0L;

    // 原始结果（用于调试）
    private SendResult originalResult;

    /**
     * 从 RocketMQ 的 SendResult 转换
     */
    public static RocketMQSendResult from(SendResult sendResult, String messageId) {
        if (sendResult == null) {
            return failure(messageId, "发送结果为null");
        }

        boolean success = sendResult.getSendStatus() == SendStatus.SEND_OK;

        return RocketMQSendResult.builder()
                .success(success)
                .messageId(messageId)
                .topic(sendResult.getMessageQueue().getTopic())
                .sendStatus(sendResult.getSendStatus().name())  // 转换为 String
                .msgId(sendResult.getMsgId())
                .queueId(sendResult.getMessageQueue().getQueueId())
                .queueOffset(sendResult.getQueueOffset())
                .transactionId(sendResult.getTransactionId())
                .regionId(sendResult.getRegionId())
                .errorMsg(success ? null : "发送失败: " + sendResult.getSendStatus())
                .originalResult(sendResult)
                .build();
    }

    /**
     * 成功结果
     */
    public static RocketMQSendResult success(String messageId, SendResult sendResult) {
        return from(sendResult, messageId);
    }

    /**
     * 失败结果
     */
    public static RocketMQSendResult failure(String messageId, String errorMsg) {
        return RocketMQSendResult.builder()
                .success(false)
                .messageId(messageId)
                .errorMsg(errorMsg)
                .sendTime(System.currentTimeMillis())
                .build();
    }

    /**
     * 失败结果（带异常）
     */
    public static RocketMQSendResult failure(String messageId, Exception e) {
        return failure(messageId, e.getMessage());
    }

    /**
     * 是否成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置耗时
     */
    public RocketMQSendResult withCostTime(long costTime) {
        this.costTime = costTime;
        return this;
    }

}
