package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 基础订单信息
 * @author: 雒世松
 * @date: 2025/03/26 0014 22:29
 */
@Data
public class BasicOrderVO {

    private String id;

    private String name;

    private Date createDate;

    private Integer status;

    private String payType;

    private Integer count;

    /**
     * 订单总金额 = 优惠金额 + 消费金额 + 实际消费金额
     */
    private BigDecimal amount;

    /**
     * 订单优惠金额
     */
    private BigDecimal couponAmount;

    /**
     * 消费金额 就是本次消费的金额 包含虚拟卡付款的金额
     */
    private BigDecimal consumeAmount;

    /**
     * 实际消费金额 是实际线上支付的金额，不包含储值卡等虚拟卡的付款金额
     */
    private BigDecimal actualAmount;

    /**
     * 退款金额 发生退款时候的实际退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 可退款金额 还可以退款的金额 为0表示不能退款了
     */
    private BigDecimal canRefundAmount;

    private Date startTime;

    private Date endTime;


}
