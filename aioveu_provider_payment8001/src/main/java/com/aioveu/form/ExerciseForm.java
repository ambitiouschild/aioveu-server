package com.aioveu.form;

import com.aioveu.entity.Exercise;
import com.aioveu.entity.ExerciseCoupon;
import com.aioveu.entity.JoinExerciseRule;
import lombok.Data;

import java.util.List;

/**
 * @description 灵活课包
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class ExerciseForm extends Exercise {

    /**
     * 优惠券列表
     */
    private List<ExerciseCoupon> exerciseCouponList;

    /**
     * 产品分类=拼单
     * 拼单规则
     */
    private List<JoinExerciseRule> joinExerciseRules;
}
