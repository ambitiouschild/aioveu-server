package com.aioveu.pay.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

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
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO implements Serializable {



    private String userAgent;
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    /**
     * 业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值
     */
    @NotBlank(message = "业务类型不能为空")
    private String bizType;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    private BigDecimal amount;

    /*    支付渠道 (Pay Channel)
    定义：指第三方支付平台
    作用：解决"用谁的钱"的问题
    举例：微信支付、支付宝、银联、Apple Pay、PayPal
    比喻：银行/支付机构，就像不同的银行*/
    @Schema(description = "支付渠道")
    @NotBlank(message = "支付渠道不能为空")
    private String channel;

    /*    支付类型/方式 (Pay Type)
    定义：指具体的支付交互方式
    作用：解决"怎么付"的问题
    举例：APP支付、小程序支付、扫码支付、H5支付
    比喻：银行的支付方式，就像银行的ATM、网银、手机银行*/
    @Schema(description = "支付类型/方式")
    private String payType;

    @NotBlank(message = "订单标题不能为空")
    private String subject;

    private String body;

    private String clientIp;

    private String deviceInfo;

    private String notifyUrl;

    private String returnUrl;

    private Integer expireMinutes = 30; // 默认30分钟过期


    /**
     * openId
     */
    private String openId;
    /**
     * 渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联
     */
    private String channelCode;
    /**
     * 渠道名称
     */
    private String channelName;


    Map<String, Object> extraParams;



}
