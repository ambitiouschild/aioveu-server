package com.aioveu.common.core.enums.pay;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Description: TODO CallbackTriggerSourceEnum  回调触发来源
 * @Author: 雒世松
 * @Date: 2026/7/26 21:59
 * @param
 * @return:
 **/


public enum CallbackTriggerSourceEnum {

    WECHAT_CALLBACK(1, "WECHAT_CALLBACK", "微信回调"),
    POLLING(2, "POLLING", "前端轮询"),
    MANUAL(3, "MANUAL", "人工发送"),
    JOB_RETRY(4, "JOB_RETRY", "Job定时重试"),
    UNKNOWN(5, "UNKNOWN", "未知渠道");

    ;

    CallbackTriggerSourceEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }


    @Getter
    private int code;


    @EnumValue                      // 关键！
    @Getter
    private String value;

    @Getter
    private String label;


    public static CallbackTriggerSourceEnum fromCode(Integer code) {
        for (CallbackTriggerSourceEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的支付渠道编码：" + code);
    }


    public static CallbackTriggerSourceEnum fromValue(String value) {
        if (value == null || value.isEmpty()) return null;
        for (CallbackTriggerSourceEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return UNKNOWN;
    }

}
