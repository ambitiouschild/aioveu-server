package com.aioveu.pay.aioveu10MqSendRecord.model.entity;


import com.aioveu.common.base.BaseEntityWithTenantId;
import com.aioveu.common.enums.pay.PaymentSceneEnum;
import com.aioveu.common.rabbitmq.enums.SendStatusEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import kotlin.jvm.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @ClassName: MqSendRecord
 * @Description TODO MQ消息发送记录实体对象
 *                      新增extraInfo字段，但查询时忽略
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
@JsonInclude(JsonInclude.Include.NON_NULL) // JSON序列化时忽略null字段
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
     * 支付场景：ORDER-商品订单 MEMBERSHIP-会员开通 RECHARGE-账户充值 ACTIVITY-活动订单
     */
    private Integer paymentScene;

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


    /** 确认时间戳（收到Broker确认的时间） */
    private LocalDateTime confirmTime;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;

//补充必要字段
    /**
     * 交换机名称
     */
    private String exchange;

    /**
     * 路由键
     */
    private String routingKey;


    /** 消息类型 */
    private String messageType;


    /** RabbitMQ关联ID（CorrelationData ID） */
    private String correlationId;



    /**
     * 发送总耗时（毫秒）
     * 从调用 send() 到收到 confirm 的总时间
     */
    private Long costTime;


    /** 确认时间戳（收到Broker确认的时间） */
    private LocalDateTime brokerAckTime;

    /**
     * 网络传输耗时（毫秒）
     * 从发送到Broker接收的时间
     */
    private Long networkCostTime;


    /**
     * Broker处理耗时（毫秒）
     * Broker收到消息到返回确认的时间
     */
    private Long brokerProcessCostTime;



    // ========== 新增字段 ==========

    /**
     * 扩展信息（临时字段，不映射到数据库）
     * @Transient: JPA忽略此字段，不持久化到数据库
     * @JsonIgnore: JSON序列化时忽略
     */
    @Transient
    @JsonIgnore
    @TableField(exist = false)
    private Map<String, Object> extraInfo;

    /**
     * 扩展信息（存储到数据库，但查询时忽略）
     * 使用@Column(insertable = false, updatable = false)表示不参与插入和更新
     * 但可以读取
     */
    @JsonIgnore
    @TableField(exist = false)
    private String extraInfoJson;

}
