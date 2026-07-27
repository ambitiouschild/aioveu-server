package com.aioveu.common.enums.oms;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Description: TODO 物流类型枚举 LogisticsTypeEnum
 * @Author: 雒世松
 * @Date: 2026/7/27 09:52
 * @param
 * @return:
 **/

/**
 * 物流类型枚举
 * 对应微信发货接口 logistics_type 字段
 *
 * @author aioveu
 */
public enum LogisticsTypeEnum {

    PHYSICAL(1, "PHYSICAL", "物流配送"),
    VIRTUAL(2, "VIRTUAL", "无需物流（虚拟商品）"),
    SELF_PICKUP(3, "SELF_PICKUP", "到店自提"),
    LOCAL_DELIVERY(4, "LOCAL_DELIVERY", "同城配送"),
    UNKNOWN(8, "UNKNOWN", "未知");

    ;

    LogisticsTypeEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }

    @EnumValue          // ← 关键！告诉 MP 用这个值存/取 DB
    @Getter
    private int code;

    @Getter
    private String value;

    @Getter
    private String label;

    // ---------- 反向查找 ----------
    public static LogisticsTypeEnum fromCode(Integer code) {

        if (code == null) return null;  // ✅ 改这里，别抛异常

        for (LogisticsTypeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的物流类型枚举编码LogisticsTypeEnum：" + code);
    }

    public static LogisticsTypeEnum fromValue(String value) {
        if (value == null || value.isEmpty()) return null;
        for (LogisticsTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return UNKNOWN;
    }
}
