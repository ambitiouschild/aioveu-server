package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/17 0017 21:23
 */
@Data
public class UserRealTimeVO {

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 余额
     */
    private BigDecimal balance;

    private BigDecimal totalBalance;

    private BigDecimal totalIncome;

    private Integer couponCount;

    /**
     * 订单数量
     */
    private Integer orderNumber;
}
