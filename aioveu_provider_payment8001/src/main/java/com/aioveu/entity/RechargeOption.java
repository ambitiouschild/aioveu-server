package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description 充值选项
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_recharge_option")
@Data
public class RechargeOption extends IdNameEntity{

    private BigDecimal money;

    private BigDecimal give;

}
