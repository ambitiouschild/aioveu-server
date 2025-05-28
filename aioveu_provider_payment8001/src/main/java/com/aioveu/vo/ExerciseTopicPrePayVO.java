package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/7 0007 10:46
 */
@Data
public class ExerciseTopicPrePayVO {

    private BigDecimal price;

    private List<ExerciseTopicItemVO> exerciseList;
}