package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/3 0003 23:52
 */
@Data
public class CategoryBaseVO extends IdNameVO {

    private String cover;

    private String code;

    private BigDecimal price;
}
