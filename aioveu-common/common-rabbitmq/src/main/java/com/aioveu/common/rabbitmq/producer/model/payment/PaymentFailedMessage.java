package com.aioveu.common.rabbitmq.producer.model.payment;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
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

    private String paymentNo;
    private String orderNo;
    private String errorCode;
    private String errorMsg;
    private String channel;
    private LocalDateTime failTime;
}
