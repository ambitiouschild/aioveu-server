package com.aioveu.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description 数据处理工具类
 * @author: 雒世松
 * @date: 2025/2/24 11:11
 */
public class SportDataUtils {

    /**
     * 隐藏手机号中间四位
     * @param phone
     * @return
     */
    public static String phoneHide(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    public static void main(String[] args) {
        BigDecimal actual = new BigDecimal("1.02");
        BigDecimal bigDecimal = actual.multiply(new BigDecimal("0.9903")).setScale(2, RoundingMode.HALF_UP);
        System.out.println(bigDecimal.toString());
    }
}
