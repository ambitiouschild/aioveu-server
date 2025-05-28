package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_balance_change")
@Data
public class UserBalanceChange extends IdNameEntity {

    private String userId;

    private String description;

    private String orderId;

    /**
     * 0 增加 1 减少
     */
    private Integer changeType;

    /**
     * 账户类型 默认0 普通账户 1推广账户
     */
    private Integer accountType;

    private BigDecimal amount;

    private Long userVipCardId;

    private BigDecimal balance;

}
