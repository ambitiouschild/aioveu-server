package com.aioveu.oms.aioveu10MqConsumeIdempotent.model.entity;


import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: MqConsumeIdempotent
 * @Description TODO MQ消费幂等性实体对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 0:30
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("mq_consume_idempotent")
public class MqConsumeIdempotent extends BaseEntityWithTenantId {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID，0表示平台默认
     */
    private Long tenantId;
    /**
     * 业务唯一键
     */
    private String bizUniqueKey;
    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 消息ID
     */
    private String messageId;
    /**
     * 状态:1-已处理
     */
    private Integer status;
    /**
     * 消费时间
     */
    private LocalDateTime consumeTime;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
