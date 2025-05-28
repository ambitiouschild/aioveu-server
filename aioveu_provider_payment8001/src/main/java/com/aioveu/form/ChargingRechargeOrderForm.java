package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class ChargingRechargeOrderForm {

    @NotNull(message = "增值服务id不能为空")
    private Long chargingOptionId;

    @NotNull(message = "店铺充值选项id不能为空")
    private Long storeChargingOptionId;

    @NotNull(message = "充值金额不能为空")
    private BigDecimal amount;

    /**
     * 充值选项id 自定义输入金额没有值
     */
    private Long chargingChargeOptionId;

    @NotEmpty(message = "用户信息错误")
    private String openId;

}
