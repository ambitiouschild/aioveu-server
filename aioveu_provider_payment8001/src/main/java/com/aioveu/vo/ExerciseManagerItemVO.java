package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/24 19:29
 */
@Data
public class ExerciseManagerItemVO {

    private Long id;

    private Date createDate;

    private String categoryName;

    private Long categoryId;

    private String topicName;

    private String name;

    private BigDecimal originalPrice;

    private BigDecimal price;

    private Date startTime;

    private Date endTime;

    private Integer status;

    private Integer limitNumber;

    private String storeName;

}
