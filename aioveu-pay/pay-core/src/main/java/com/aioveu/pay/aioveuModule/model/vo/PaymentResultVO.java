package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
public class PaymentResultVO {

    private String refundNo;
    private String thirdRefundNo;
    private BigDecimal refundAmount;
    private Integer refundStatus;
    private Date refundTime;
    private String errorMessage;
}
