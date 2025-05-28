package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 班级用户签到课评
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_sign_evaluate")
@Data
public class GradeSignEvaluate extends IdEntity {

    private Long gradeEnrollUserId;

    private Long gradeId;

    private String userId;

    private String evaluate;

    /**
     * 签到者用户id
     */
    private String signUserId;

    /**
     * 课评人用户id
     */
    private String evaluateUserId;

    // status 1 已签到 2 已课评 0 删除

}
