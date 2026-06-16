package com.aioveu.pay.model.aioveuPayment;

import com.aioveu.common.enums.pay.PaymentCallbackStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
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

   @Schema(description = "内部支付单号", required = true)
   private String paymentNo;

   @Schema(description = "业务订单号")
   private String orderNo;

   @Schema(description = "支付渠道")
   private String channel; // WECHAT / ALIPAY

   @Schema(description = "第三方交易号")
   private String thirdTransactionId;

   @Schema(description = "支付金额（元）")
   private BigDecimal paidAmount;

   @Schema(description = "支付状态")  //正确做法（✅ 支付系统标准）
   private PaymentCallbackStatusEnum status; // SUCCESS / FAILED

   @Schema(description = "支付完成时间")
   private LocalDateTime paidTime;

   @Schema(description = "渠道原始数据（JSON）")
   private String rawData;

   @Schema(description = "错误码")
   private String errorCode;

   @Schema(description = "错误信息")
   private String errorMessage;

}
