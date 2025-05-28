package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:14
 */
@Data
public class AnalysisCouponVO {

    /**
     * 金额
     */
    private BigDecimal amount;

    private String username;

    private String phone;

    private Date createDate;

    private String couponName;

}
