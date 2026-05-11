package com.aioveu.pay.aioveu10MqSendRecord.model.entity;


import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: MqSendRecord
 * @Description TODO MQ消息发送记录实体对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:11
 * @Version 1.0
 * @Param
 * @return
 **/
@Getter
@Setter
@TableName("mq_send_record")
public class MqSendRecord extends BaseEntityWithTenantId {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID，0表示平台默认
     */
    private Long tenantId;
    /**
     * 消息ID
     */
    private String messageId;
    /**
     * 业务ID(支付单号)
     */
    private String bizId;
    /**
     * 业务类型:payment_success
     */
    private String bizType;
    /**
     * Topic
     */
    private String topic;
    /**
     * Tag
     */
    private String tag;
    /**
     * 分片Key
     */
    private String shardingKey;
    /**
     * 消息体(JSON格式)
     */
    private String messageBody;
    /**
     * 发送状态:0-未发送,1-发送中,2-发送成功,3-发送失败,4-已取消,5-已超时,6-进入死信队列
     */
    private Integer sendStatus;
    /**
     * 重试次数
     */
    private Integer retryCount;
    /**
     * 最大重试次数
     */
    private Integer maxRetry;
    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryTime;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
