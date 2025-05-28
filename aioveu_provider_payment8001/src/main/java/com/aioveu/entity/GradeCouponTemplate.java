package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_coupon_template")
@Data
public class GradeCouponTemplate extends IdEntity {

    private Long gradeId;

    private Long couponTemplateId;

    /**
     * 消耗课时
     */
    private Integer classHour;

}
