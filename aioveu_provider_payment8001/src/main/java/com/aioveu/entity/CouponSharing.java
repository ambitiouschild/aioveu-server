package com.aioveu.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sport_coupon_sharing")
public class CouponSharing extends IdEntity{

    /**
     *分享人id
     */
    private String sharingUserId;

    /**
     * 优惠券码
     */
    private String couponCode;

    private Long templateId;

    /**
     * 领取人id
     */
    private String receiveUserId;

}
