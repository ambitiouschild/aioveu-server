package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author xlfan10
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class OrderCouponVO {

    private String orderId;

    private String orderName;

    private String userId;

    private Date createDate;

    private String phone;

    private String childName;

    private String couponName;

    private String couponCount;

}
