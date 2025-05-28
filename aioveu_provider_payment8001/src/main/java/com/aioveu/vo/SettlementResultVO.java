package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/6/21 0021 21:36
 */
@Data
public class SettlementResultVO {

    /**
     * 结果结算金额
     */
    private Double cost;

    /**
     * 优惠金额
     */
    private Double couponCost;

    /**
     * 总金额
     */
    private BigDecimal totalCost;
}
