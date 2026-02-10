package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName: PaymentParamsVO
 * @Description TODO 获取支付参数
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:47
 * @Version 1.0
 **/

@Getter
@Setter
@Builder //需要为 VO 类添加 Lombok 的构建器模式支持
@Schema( description = "获取支付参数对象")
public class PaymentParamsVO implements Serializable {

    private String paymentNo;          // 支付单号
    private String paymentParams;      // 支付参数（JSON字符串）
    private String thirdPaymentNo;     // 第三方支付单号
    private String qrCodeUrl;          // 二维码URL（仅Native支付使用）
    private String h5Url;              // H5支付URL
}
