package com.aioveu.sms.aioveu02Coupon.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @Description: TODO 优惠券适用类型枚举
 * @Author: 雒世松
 * @Date: 2025/6/5 18:45
 * @param
 * @return:
 **/

@Getter
public enum CouponApplicationScopeEnum implements IBaseEnum<Integer> {

    ALL(0, "全场通用"),
    /**
     * 指定商品分类
     */
    SPU_CATEGORY(1, "指定商品分类"),
    SPU(2, "指定商品"),
    ;

    @Getter
    @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    private Integer value;

    @Getter
    @JsonValue //  表示对枚举序列化时返回此字段
    private String label;

    CouponApplicationScopeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
