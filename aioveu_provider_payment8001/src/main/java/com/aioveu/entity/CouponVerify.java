package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_coupon_verify")
@Data
public class CouponVerify extends IdEntity {

    private String userId;

    private Long userCouponId;

    private Long storeId;

    private String remark;

}
