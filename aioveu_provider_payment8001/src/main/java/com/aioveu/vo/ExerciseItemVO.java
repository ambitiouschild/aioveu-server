package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/7 0007 10:46
 */
@Data
public class ExerciseItemVO {

    private Long id;

    private String image;

    private String name;

    private String address;

    /**
     * 距离
     */
    private Double distance;

    private String date;

    /**
     * 限制人数
     */
    private Integer limitNumber;

    /**
     * 报名人数
     */
    private Integer enrollNumber;

    private BigDecimal price;
    private Long storeProductCategoryId;
    private String storeProductCategoryName;
}
