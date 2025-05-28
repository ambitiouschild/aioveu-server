package com.aioveu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author： yao
 * @Date： 2025/1/3 11:34
 * @Describe：
 */
public class StringValidatorUtils {
    /**
     * 校验姓名合法性
     * 只允许输入中、英文
     * 不允许输入符号或特殊符号
     * @param name
     * @return
     */
    public static boolean validateName(String name){
        String validateStr = "^([\u4e00-\u9fa5]+|[a-zA-Z]+( [a-zA-Z]+){0,2}){2,30}$";
        return name.matches(validateStr);
    }
    /**
     * 是否是手机号码校验
     * @param mobileNumber
     * @return
     */
    public static boolean isValidMobileNumber(String mobileNumber) {
        if (mobileNumber == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("^1[3-9]\\d{9}$");
        Matcher matcher = pattern.matcher(mobileNumber);
        return matcher.matches();
    }
}
