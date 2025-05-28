package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_exercise_coupon")
@Data
public class ExerciseCoupon extends IdEntity {

    private Long exerciseId;

    private Long couponTemplateId;

    private Long categoryId;

    /**
     * 优惠券数量
     */
    private Integer couponNumber;

    /**
     * 优惠券的过期时间
     * 这里的优先级高于优惠券模板的
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date couponExpireTime;

    /**
     * 优惠券过期日期(优惠券的过期时间 二选一)
     * 这里的优先级高于优惠券模板的
     */
    private Integer couponExpireDay;

}
