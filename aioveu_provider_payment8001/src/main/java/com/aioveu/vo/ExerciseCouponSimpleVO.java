package com.aioveu.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/24 0024 23:44
 */
@Data
public class ExerciseCouponSimpleVO {

    private Long id;

    /**
     * 优惠券数量
     */
    private Integer couponNumber;

    private String name;

    /**
     * 优惠券的过期时间
     * 这里的优先级高于优惠券模板的
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date couponExpireTime;

    /**
     * 优惠券过期日期(优惠券的过期时间 二选一)
     * 这里的优先级高于优惠券模板的
     */
    private Integer couponExpireDay;

    /**
     * 卡券模版id
     */
    private String couponTemplateId;
}
