package com.aioveu.order.model.aioveu05OrderPay.form;

import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.common.enums.pay.PaymentMethodEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 订单支付表单对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:12
 * @param
 * @return:
 **/

@Data
@Schema(description ="订单支付表单对象")
public class OrderPaymentForm {

    @Schema(description="订单编号")
    private String orderSn;

    @Schema(description="小程序 AppId")
    private String appId;


    @Schema(description="支付方式")
    private PaymentChannelEnum paymentChannel;


    @Schema(description="支付方式")
    private PaymentMethodEnum paymentMethod;

    @Schema(description="支付金额")
    private Long paymentAmount;

}
