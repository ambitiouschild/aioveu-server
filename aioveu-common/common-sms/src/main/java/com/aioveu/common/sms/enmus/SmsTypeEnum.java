package com.aioveu.common.sms.enmus;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @ClassName: SmsTypeEnum
 * @Description TODO  短信类型枚举  value 值对应 application-*.yml 中的 sms.templates.* 配置
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 14:48
 * @Version 1.0
 **/
@Getter
public enum SmsTypeEnum implements IBaseEnum<String> {

    /**
     * 注册短信验证码
     */
    REGISTER("register", "注册短信验证码"),

    /**
     * 登录短信验证码
     */
    LOGIN("login", "登录短信验证码"),

    /**
     * 修改手机号短信验证码
     */
    CHANGE_MOBILE("change-mobile", "修改手机号短信验证码");

    private final String value;
    private final String label;

    SmsTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
