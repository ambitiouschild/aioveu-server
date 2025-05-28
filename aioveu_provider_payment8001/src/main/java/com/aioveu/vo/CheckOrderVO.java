package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:05
 */
@Data
public class CheckOrderVO {

    private String orderId;

    private String name;

    private BigDecimal cost;

}
