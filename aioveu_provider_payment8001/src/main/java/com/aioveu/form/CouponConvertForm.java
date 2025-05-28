package com.aioveu.form;

import com.aioveu.vo.TemplateRule;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description 优惠券转换
 * @author: 雒世松
 * @date: 2025/4/14 23:12
 */
@Data
public class CouponConvertForm {

    @NotEmpty(message = "用户id不能为空")
    private String userId;

    @NotNull(message = "待转换的优惠券id不能为空")
    private Long couponTemplateId;

    @NotNull(message = "待转换的优惠券数量不能为空")
    private Integer sourceCount;

    @NotNull(message = "目标优惠券id不能为空")
    private Long targetTemplateId;

    @NotNull(message = "转换后的优惠券数量不能为空")
    private Integer targetCount;

    private TemplateRule.Expiration expiration;

    private BigDecimal couponAmount;

}
