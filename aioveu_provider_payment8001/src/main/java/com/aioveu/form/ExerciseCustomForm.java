package com.aioveu.form;

import com.aioveu.entity.ExerciseCoupon;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description 灵活课包
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class ExerciseCustomForm {

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    /**
     * 活动开始报名时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotNull(message = "原价不能为空")
    private BigDecimal originalPrice;

    private Integer limitNumber;

    /**
     * 单个用户的限制人数 0 不限制
     */
    private Integer singleNumber;

    /**
     * 优惠券领取类型 默认0 1支付成功领取 2核销领取
     */
    private Integer couponReceiveType;

    /**
     * 优惠券列表
     */
    private List<ExerciseCoupon> exerciseCouponList;
}
