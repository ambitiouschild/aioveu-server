package com.aioveu.common.enums.oms;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @Description: TODO 快递公司编码 DeliveryCompanyCodeEnum
 * @Author: 雒世松
 * @Date: 2026/7/27 10:32
 * @param
 * @return:
 **/

/**
 * 快递公司编码
 * DeliveryCompanyCodeEnum
 *
 * @author aioveu
 */
public enum DeliveryCompanyCodeEnum {

    SF(1, "SF", "顺丰速运"),
    ZTO(2, "ZTO", "中通快递"),
    YTO(3, "YTO", "圆通速递"),
    YD(4, "YD", "韵达快递"),
    STO(5, "STO", "申通快递"),
    JD(6, "JD", "京东物流"),
    EMS(7, "EMS", "邮政EMS"),
    JTSD(8, "JTSD", "极兔速递"),
    DBLK(9, "DBLK", "德邦快递"),
    UNKNOWN(10, "UNKNOWN", "未知快递");

    ;

    DeliveryCompanyCodeEnum(int code, String value, String label) {
        this.code = code;
        this.value = value;
        this.label = label;
    }


    @Getter
    private int code;

    @EnumValue
    @Getter
    private String value;

    @Getter
    private String label;

    // ---------- 反向查找 ----------
    public static DeliveryCompanyCodeEnum fromCode(Integer code) {

        if (code == null) return null;  // ✅ 改这里，别抛异常

        for (DeliveryCompanyCodeEnum e : values()) {
            if (e.code == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("不支持的快递公司编码DeliveryCompanyCodeEnum：" + code);
    }

    public static DeliveryCompanyCodeEnum fromValue(String value) {
        if (value == null || value.isEmpty()) return null;
        for (DeliveryCompanyCodeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return UNKNOWN;
    }
}
