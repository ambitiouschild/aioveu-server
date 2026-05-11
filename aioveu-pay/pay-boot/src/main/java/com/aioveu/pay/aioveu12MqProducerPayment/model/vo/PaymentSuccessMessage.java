package com.aioveu.pay.aioveu12MqProducerPayment.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PaymentMessage
 * @Description TODO 支付成功消息DTO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 18:30
 * @Version 1.0
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessMessage implements Serializable {

    // 消息头
    private String messageId;
    private LocalDateTime createTime = LocalDateTime.now();
    private Integer version = 1;

    // 支付信息
    private String paymentNo;
    private String orderNo;
    private String transactionId;
    private BigDecimal amount;
    private String channel;  // wechat/alipay

    // 业务信息
    private Long memberId;
    private String memberName;
    private LocalDateTime paymentTime;


    // 回调信息
    private String bankType;
    private String timeEnd;

    // 重试信息
    @Builder.Default
    private Integer retryCount = 0;


    // 扩展字段
    private Map<String, Object> extParams = new HashMap<>();
}
