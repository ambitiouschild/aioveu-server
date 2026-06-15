package com.aioveu.oms.aioveu11MqConsumer.model.vo;


import com.aioveu.common.enums.pay.PaymentChannelEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: OrderPaySuccessDTO
 * @Description TODO OrderPaySuccessDTO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 22:00
 * @Version 1.0
 **/
@Data
public class OrderPaySuccessDTO {

    private String messageId;        // 消息ID
    private String paymentNo;        // 支付单号
    private String orderNo;          // 订单号
    private String transactionId;    // 交易流水号
    private BigDecimal amount;       // 支付金额
    private PaymentChannelEnum channel;          // 支付渠道：wechat/alipay
    private Long memberId;           // 会员ID
    private LocalDateTime paymentTime; // 支付时间

    /**
     * Topic
     */
    private String topic;
    /**
     * Tag
     */
    private String tag;
}
