package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: PaymentResultVO
 * @Description TODO 支付结果对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:13
 * @Version 1.0
 **/

@Getter
@Setter
@Builder //需要为 VO 类添加 Lombok 的构建器模式支持
@Schema( description = "支付结果对象")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResultVO {

    /** 支付单号 */
    private String paymentNo;

    /** 订单号 */
    private String orderNo;

    /** 支付渠道 */
    private String channel;

    /** 支付金额（元） */
    private BigDecimal amount;

    /** 是否成功 */
    private Boolean success;

    /** 支付状态：SUCCESS, FAILED, PROCESSING */
    private String status;

    /** 交易状态 */
    private String tradeStatus;

    /** 消息 */
    private String message;

    /** 错误码 */
    private String errorCode;

    /** 错误信息 */
    private String errorMessage;

    /** 交易号（渠道返回） */
    private String tradeNo;

    /** 商户订单号 */
    private String outTradeNo;

    /** 微信预支付ID */
    private String prepayId;

    /** 微信/支付宝交易号 */
    private String transactionId;

    /** 支付时间戳 */
    private Long payTime;

    /** 完成时间戳 */
    private Long finishTime;

    /** 用户ID */
    private Long userId;
}
