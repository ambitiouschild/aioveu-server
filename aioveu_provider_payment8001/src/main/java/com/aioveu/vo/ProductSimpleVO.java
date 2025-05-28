package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/8/29 0029 17:01
 */
@Data
public class ProductSimpleVO {

    private Long id;

    private Long categoryId;

    private String categoryUrl;

    private String name;

    private String image;

    private BigDecimal price;

    private Long storeId;

    private Date createDate;

    private Integer enrollNumber;
}
