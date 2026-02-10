package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: PaymentRequestDTO
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:44
 * @Version 1.0
 **/

@Getter
@Setter
@Builder //需要为 VO 类添加 Lombok 的构建器模式支持
@Schema( description = "创建支付订单DTO对象")
public class PaymentRequestDTO implements Serializable {


    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @NotBlank(message = "业务类型不能为空")
    private String bizType;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    private BigDecimal amount;

    @NotBlank(message = "支付渠道不能为空")
    private String channel;

    private String paymentMethod;

    @NotBlank(message = "订单标题不能为空")
    private String subject;

    private String body;

    private String clientIp;

    private String deviceInfo;

    private String notifyUrl;

    private String returnUrl;

    private Integer expireMinutes = 30; // 默认30分钟过期


    private String openid;
    /**
     * 渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联
     */
    private String channelCode;
    /**
     * 渠道名称
     */
    private String channelName;



}
