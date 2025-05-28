package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description 计费充值选项
 * @author: 雒世松
 * @date: Created in 2025/4/23 11:41
 */
@TableName("sport_charging_charge_option")
@Data
public class ChargingChargeOption extends IdNameEntity{

    private String chargingCode;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 充值价格
     */
    private BigDecimal price;

    private Integer count;

    /**
     * 赠送次数
     */
    private Integer giftCount;

}
