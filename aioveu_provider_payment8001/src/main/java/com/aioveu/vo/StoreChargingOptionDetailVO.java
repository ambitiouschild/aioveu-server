package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/5/7 9:55
 */
@Data
public class StoreChargingOptionDetailVO {

    /**
     * 最低起充金额
     */
    private BigDecimal minAmount;

    /**
     * 单次价格
     */
    private BigDecimal price;

    /**
     * 商户增值服务次数
     */
    private int total;

    private Long chargingOptionId;

    private Long storeChargingOptionId;

}
