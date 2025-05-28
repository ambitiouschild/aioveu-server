package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: fanxiaole
 * @date: 2025/3/8 17:25
 */
@Data
public class ExerciseCountPayVO {

    private String id;

    private BigDecimal hourPrice;

    private String venueName;

    private String fieldName;

}
