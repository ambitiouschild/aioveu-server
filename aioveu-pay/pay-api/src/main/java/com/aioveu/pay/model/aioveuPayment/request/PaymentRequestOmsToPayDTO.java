package com.aioveu.pay.model.aioveuPayment.request;

import com.aioveu.common.enums.pay.PaymentBizTypeEnum;
import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.common.enums.pay.PaymentMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName: PaymentRequestOmsToPayDTO
 * @Description TODO  PaymentRequestOmsToPayDTO （OMS → Pay）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:44
 * @Version 1.0
 **/

@Getter
@Setter
@Builder //需要为 VO 类添加 Lombok 的构建器模式支持
@Schema( description = "创建（OMS → Pay）支付订单DTO对象")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestOmsToPayDTO implements Serializable {



    private String userAgent;


    @NotBlank(message = "【PaymentRequestOmsToPayDTO】业务订单号不能为空")
    @Schema(description = "OMS 业务订单号")
    private String orderSn;

    /**
     * 业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值
     */
    @NotNull(message = "【PaymentRequestOmsToPayDTO】业务类型不能为空")
    private PaymentBizTypeEnum bizType;

//    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "【PaymentRequestOmsToPayDTO】支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    private Long paymentAmount;

    /*    支付渠道 (Pay Channel)
    定义：指第三方支付平台
    作用：解决"用谁的钱"的问题
    举例：微信支付、支付宝、银联、Apple Pay、PayPal
    比喻：银行/支付机构，就像不同的银行*/
    @Schema(description = "支付渠道")
    @NotNull(message = "【PaymentRequestOmsToPayDTO】支付渠道不能为空")
    private PaymentChannelEnum paymentChannel;

    /*    支付类型/方式 (Pay Type)
    定义：指具体的支付交互方式
    作用：解决"怎么付"的问题
    举例：APP支付、小程序支付、扫码支付、H5支付
    比喻：银行的支付方式，就像银行的ATM、网银、手机银行*/
    @Schema(description = "支付类型/方式")
    @NotNull(message = "【PaymentRequestOmsToPayDTO】支付类型/方式不能为空")
    private PaymentMethodEnum paymentMethod;

    private String openId;

}
