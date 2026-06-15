package com.aioveu.common.rabbitmq.producer.model.payment;


import com.aioveu.common.enums.pay.PaymentChannelEnum;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PaymentFailedMessage
 * @Description TODO 支付失败消息DTO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 18:31
 * @Version 1.0
 **/
@Data
@Builder
public class PaymentFailedMessage implements Serializable {

    /** ============ 幂等 & 路由 ============ */

    /**
     * MQ 消息唯一ID（必须与发送记录一致）
     */
    private String messageId;

    /**
     * 租户ID（多租户必填）
     */
    private Long tenantId;

    /** ============ 业务信息 ============ */

    /**
     * 支付单号（out_trade_no）
     */
    private String paymentNo;

    /**
     * 订单号（oms_order.order_sn）
     */
    private String orderNo;

    /**
     * 支付金额（分）
     */
    private BigDecimal amount;

    /**
     * 支付渠道
     */
    private PaymentChannelEnum channel;

    /**
     * 业务类型（PAY / REFUND / CLOSE）
     */
    private String bizType;


    /** ============ 失败信息 ============ */

    /**
     * 支付状态（PAY_FAILED / CLOSED）
     */
    private Integer paymentStatus;

    /**
     * 错误码（微信 / 支付宝）
     */
    private String errorCode;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 是否可重试（true = 可补偿）
     */
    private Boolean retryable;

    /**
     * 失败时间
     */
    private LocalDateTime failTime;
}
