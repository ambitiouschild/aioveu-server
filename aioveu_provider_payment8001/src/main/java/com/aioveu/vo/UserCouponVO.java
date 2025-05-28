package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xlfan10
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class UserCouponVO {

    private Long id;

    private Long templateId;

    private BigDecimal activePrice;

    private String name;

    private String category;

    /**
     * 有效期 空值就永久
     */
    private String time;

    /**
     * 适用产品
     */
    private String productName;

    private String remark;

    private Integer status;

    private BigDecimal amount;

    /**
     * 会员价可用
     * 为true，正常使用
     * 为false，选择会员卡，使用会员价的时候，不可使用该卡券叠加优惠
     */
    private Integer vipPriceCanUse;

}
