package com.aioveu.common.enums.oms;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @Description: TODO 订单来源枚举
 * @Author: 雒世松
 * @Date: 2025/6/5 18:07
 * @param
 * @return:
 **/


public enum OrderSourceEnum {

    WX(1, "WX"), // APP订单
    APP(2, "APP"),
    WEB(3, "WEB"), // 网页

    ;

    OrderSourceEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


    //100% 稳妥的「生产级最终版」（强烈推荐）
    @EnumValue
    @Getter
    private Integer value;

    @Getter
    private String label;
}
