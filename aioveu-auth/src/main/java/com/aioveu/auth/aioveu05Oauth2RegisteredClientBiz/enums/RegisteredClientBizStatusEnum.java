package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.enums;

import com.aioveu.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @Description: TODO 客户端状态枚举
 * @Author: 雒世松
 * @Date: 2026/6/11 18:13
 * @param
 * @return:
 **/


public enum RegisteredClientBizStatusEnum implements IEnum<Integer> {

    DISABLED(0, "禁用"),
    NORMAL(1, "正常"),
    UNKNOWN(2, "未知状态"),

    ;

    RegisteredClientBizStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }


    @Getter
    private Integer value;

    @Getter
    private String label;
}
