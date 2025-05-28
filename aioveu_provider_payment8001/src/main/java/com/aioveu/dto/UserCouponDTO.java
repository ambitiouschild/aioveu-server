package com.aioveu.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 用户优惠券对象
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class UserCouponDTO {

    private String userId;

    private Integer status;

    private String name;

    private String rule;

    private String time;

    private Date createDate;

    private Date updateDate;

    private String updateDateStr;

    private BigDecimal activePrice;

    private Long id;

    private Long userCouponId;
    /**
     * 优惠券价值金额
     */
    private BigDecimal amount;

}
