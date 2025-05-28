package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description 店铺用户余额
 * @author: 雒世松
 * @date: 2025/1/17 0017 21:23
 */
@Data
public class UserStoreBalanceVO {

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 推广余额
     */
    private BigDecimal extensionBalance;

}
