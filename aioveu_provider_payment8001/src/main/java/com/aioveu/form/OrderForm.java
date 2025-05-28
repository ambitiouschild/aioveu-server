package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class OrderForm {

    @NotNull(message = "分类id不能为空")
    private Long categoryId;

    private String productId;

    @NotEmpty(message = "报名人不能为空")
    private String phoneList;

    @NotEmpty(message = "userId不能为空")
    private String userId;

    private Long userCouponId;

    private String shareUserId;

    private Integer number;

    private String address;

    private String wxPayAppId;

    private String remark;

    private String coachUserId;

}
