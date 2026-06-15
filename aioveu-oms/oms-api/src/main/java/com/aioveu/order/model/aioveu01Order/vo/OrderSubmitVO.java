package com.aioveu.order.model.aioveu01Order.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName: OrderSubmitVO
 * @Description TODO 新建一个「订单提交返回 VO」
 *                      ✅ 这是订单 → 支付 的“唯一真理契约”
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/16 2:40
 * @Version 1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSubmitVO implements Serializable {

    /** 业务订单号（展示用） */
    private String orderSn;

    /** 支付订单号（干活用） */
    private String paymentNo;

    /** 支付金额（分） */
    private Long paymentAmount;
}
