package com.aioveu.oms.aioveu08MqConsumeRecord.model.entity;


import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: MqConsumeRecord
 * @Description TODO MQ消息消费记录实体对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:26
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("mq_consume_record")
public class MqConsumeRecord extends BaseEntityWithTenantId {


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
     * Topic
     */
    private String topic;
    /**
     * Tag
     */
    private String tag;
    /**
     * 消费者组
     */
    private String consumerGroup;
    /**
     * 业务ID(订单号)
     */
    private String bizId;
    /**
     * 消费状态:0-未消费,1-消费中,2-消费成功,3-消费失败,4-进入死信
     */
    private Integer consumeStatus;
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
     * 消费时间
     */
    private LocalDateTime consumeTime;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
