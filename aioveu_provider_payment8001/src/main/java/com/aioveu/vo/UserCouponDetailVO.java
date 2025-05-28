package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xlfan10
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class UserCouponDetailVO {

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

    /**
     * 核销码
     */
    private String code;

    private String codeNumber;

}
