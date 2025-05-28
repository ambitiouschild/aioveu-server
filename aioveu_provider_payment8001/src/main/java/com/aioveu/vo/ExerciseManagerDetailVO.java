package com.aioveu.vo;

import com.aioveu.entity.Exercise;
import com.aioveu.entity.JoinExerciseRule;
import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/1 0001 23:00
 */
@Data
public class ExerciseManagerDetailVO extends Exercise {

    private String storeName;

    private Long companyId;

    /**
     * 优惠券列表
     */
    private List<ExerciseCouponSimpleVO> exerciseCouponList;


    /**
     * 产品分类=拼单
     * 拼单规则
     */
    private List<JoinExerciseRule> joinExerciseRules;
}
