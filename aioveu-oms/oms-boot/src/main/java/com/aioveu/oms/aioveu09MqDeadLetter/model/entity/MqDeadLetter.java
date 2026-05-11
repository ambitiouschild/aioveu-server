package com.aioveu.oms.aioveu09MqDeadLetter.model.entity;


import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: MqDeadLetter
 * @Description TODO MQ死信队列实体对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:47
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("mq_dead_letter")
public class MqDeadLetter extends BaseEntityWithTenantId {

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
     * 业务ID
     */
    private String bizId;
    /**
     * 消息体
     */
    private String messageBody;
    /**
     * 消费次数
     */
    private Integer consumeTimes;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 死信原因
     */
    private String deadReason;
    /**
     * 处理状态:0-未处理,1-已处理
     */
    private Integer handleStatus;
    /**
     * 处理时间
     */
    private LocalDateTime handleTime;
    /**
     * 处理结果
     */
    private String handleResult;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
