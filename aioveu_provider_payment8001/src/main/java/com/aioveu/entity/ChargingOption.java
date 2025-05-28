package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description 计费选项
 * @author: 雒世松
 * @date: Created in 2025/4/23 11:41
 */
@TableName("sport_charging_option")
@Data
public class ChargingOption extends IdNameEntity{

    private String chargingCode;

    /**
     * 最低起充金额
     */
    private BigDecimal minAmount;

    /**
     * 单次价格
     */
    private BigDecimal price;

    /**
     * 首次初始化赠送次数
     */
    private Integer giftCount;

}
