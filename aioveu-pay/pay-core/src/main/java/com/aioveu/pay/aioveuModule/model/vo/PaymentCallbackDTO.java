package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @ClassName: PaymentCallbackDTO
 * @Description TODO 支付回调
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:48
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "支付订单视图对象")
public class PaymentCallbackDTO implements Serializable {
}
