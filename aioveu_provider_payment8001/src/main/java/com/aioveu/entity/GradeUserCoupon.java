package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 用户约课使用优惠券记录表
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_grade_user_coupon")
@Data
public class GradeUserCoupon extends IdEntity {

    private String userId;

    private Long gradeId;

    private Long userCouponId;

    // status 2 删除

    //用户约课报名主键id
    private Long gradeEnrollUserId;

}
