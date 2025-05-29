package com.aioveu.auth.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileNumberValidator {

    // 正则表达式用于匹配中国大陆的手机号码
    private static final String MOBILE_NUMBER_REGEX = "^1[3-9]\\d{9}$";

    /**
     * 是否是手机号码校验
     * @param mobileNumber
     * @return
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(MOBILE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }
}