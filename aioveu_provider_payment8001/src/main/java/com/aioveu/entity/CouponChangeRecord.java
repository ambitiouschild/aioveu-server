package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_coupon_change_record")
@Data
public class CouponChangeRecord extends IdEntity {

    private String userId;

    private Long userCouponId;

    private Long gradeId;

    private String remark;

}
