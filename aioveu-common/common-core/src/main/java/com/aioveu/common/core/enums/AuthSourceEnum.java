package com.aioveu.common.core.enums;

import lombok.Getter;

/**
 * @Description: TODO AuthSourceEnum
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/


public enum AuthSourceEnum {

    SYSTEM(1, "SYSTEM", "SYSTEM"),
    LSS(2, "LSS", "LSS"),
    TENANT(3, "TENANT", "TENANT"),
    MINI_APP(4, "MINI_APP", "MINI_APP"),
    UNKNOWN(8, "UNKNOWN", "未知");

    ;

    AuthSourceEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }


    @Getter
    private int code;

    @Getter
    private String value;

    @Getter
    private String label;

    // ---------- 反向查找 ----------
    public static AuthSourceEnum fromCode(Integer code) {

        if (code == null) return null;  // ✅ 改这里，别抛异常

        for (AuthSourceEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的支付渠道编码：" + code);
    }

    public static AuthSourceEnum fromValue(String value) {
        if (value == null || value.isEmpty()) return null;
        for (AuthSourceEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return UNKNOWN;
    }
}
