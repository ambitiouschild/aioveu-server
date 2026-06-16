package com.aioveu.pay.model.aioveu01PayOrder.form;


import com.aioveu.common.enums.pay.PaymentBizTypeEnum;
import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.common.enums.pay.PaymentMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName: PayOrderCreateForm
 * @Description TODO  OMS 用的：PayOrderCreateForm（放在 pay-api）
 *                                      这是 OMS 唯一允许接触的类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/16 0:22
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "支付订单创建表单")
public class PayOrderCreateForm implements Serializable {

    @Schema(description = "业务订单号")
    @NotBlank(message = "【PayOrderCreateForm】业务订单号不能为空")
    private String orderNo;

    @Schema(description = "业务类型")
    @NotNull(message = "【PayOrderCreateForm】业务类型不能为空")
    private PaymentBizTypeEnum bizType;

    @Schema(description = "用户ID")
    @NotNull(message = "【PayOrderCreateForm】用户ID不能为空")
    private Long userId;

    @Schema(description = "支付金额")
    @NotNull(message = "【PayOrderCreateForm】支付金额不能为空")
    private BigDecimal paymentAmount;

    @Schema(description = "支付渠道")
    @NotNull(message = "【PayOrderCreateForm】支付渠道不能为空")
    private PaymentChannelEnum paymentChannel;

    @Schema(description = "支付方式")
    @NotNull(message = "【PayOrderCreateForm】支付方式不能为空")
    private PaymentMethodEnum paymentMethod;

    @Schema(description = "订单标题")
    @Size(max = 200)
    private String subject;

    @Schema(description = "客户端IP")
    private String clientIp;
}
