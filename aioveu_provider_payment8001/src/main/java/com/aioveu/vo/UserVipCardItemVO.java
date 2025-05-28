package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/28 0028 21:39
 */
@Data
public class UserVipCardItemVO extends IdNameVO {

    private String vipCode;

    private String storeName;

    private Long storeId;

    private BigDecimal price;

    private Double discount;

    private Date validTime;

    private BigDecimal balance;



}
