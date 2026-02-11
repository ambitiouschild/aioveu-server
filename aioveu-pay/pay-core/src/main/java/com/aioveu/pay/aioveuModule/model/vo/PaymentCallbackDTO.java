package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName: PaymentCallbackDTO
 * @Description TODO 支付回调请求参数 -支付回调是第三方支付平台发起给我们的请求
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:48
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "支付回调请求参数")
public class PaymentCallbackDTO implements Serializable {

   @Schema(description = "支付单号", required = true, example = "P202502110001")
   private String paymentNo;

   @Schema(description = "商户订单号", example = "202502110001")
   private String orderNo;

   @Schema(description = "第三方支付单号", example = "4200002009202402111234567890")
   private String thirdPaymentNo;

   @Schema(description = "交易状态", example = "SUCCESS")
   private String tradeStatus;  // SUCCESS/FAILED/CLOSED

   @Schema(description = "支付金额(元)", example = "100.00")
   private BigDecimal amount;

   @Schema(description = "支付时间", example = "2024-02-11 18:30:00")
   private LocalDateTime paymentTime;

   @Schema(description = "签名", example = "C380BEC2BFD727A4B6845133519F3AD6")
   private String sign;

   @Schema(description = "签名类型", example = "MD5")
   private String signType;

   @Schema(description = "渠道返回的原始数据", example = "{\"trade_no\":\"202502110001\"}")
   private String rawData;

   @Schema(description = "错误码", example = "ORDER_NOT_EXIST")
   private String errorCode;

   @Schema(description = "错误信息", example = "订单不存在")
   private String errorMessage;

   @Schema(description = "支付渠道", example = "wechat")
   private String channel;

   @Schema(description = "是否成功", example = "true")
   private Boolean success;
}
