package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/3 0003 23:52
 */
@Data
public class OrderNumberVO {

    /**
     * 待支付
     */
    private Integer unPay;

    /**
     * 待使用
     */
    private Integer unUse;

    /**
     * 使用中
     */
    private Integer using;

    /**
     * 已完成
     */
    private Integer finish;

}
