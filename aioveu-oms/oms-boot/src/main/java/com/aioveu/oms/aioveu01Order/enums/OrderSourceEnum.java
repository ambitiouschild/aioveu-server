package com.aioveu.oms.aioveu01Order.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @Description: TODO 订单来源枚举
 * @Author: 雒世松
 * @Date: 2025/6/5 18:07
 * @param
 * @return:
 **/


public enum OrderSourceEnum implements IBaseEnum<Integer> {

    APP(1, "APP"), // APP订单
    WEB(2, "WEB"), // 网页
    ;

    OrderSourceEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


    @Getter
    private Integer value;

    @Getter
    private String label;
}
