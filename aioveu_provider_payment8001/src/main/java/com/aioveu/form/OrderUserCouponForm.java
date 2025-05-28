package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/6/21 0021 21:24
 */
@Data
public class OrderUserCouponForm {

    @NotEmpty(message = "userId不能为空")
    private String userId;

    @NotNull(message = "优惠券模板id不能为空")
    private Long couponTemplateId;

    @NotEmpty(message = "订单id不能为空")
    private String orderId;

    private BigDecimal singleCouponAmount;

    @Min(message = "不能小于1", value = 1)
    @Max(message = "不能大于100", value = 100)
    private Integer orderLimit;

    /**
     * 优惠券的过期时间
     * 这里的优先级高于优惠券模板的
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date couponExpireTime;

    /**
     * 优惠券过期日期(优惠券的过期时间 二选一)
     * 这里的优先级高于优惠券模板的
     */
    private Integer couponExpireDay;
}
