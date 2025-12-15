package com.aioveu.common.sms.service;

import com.aioveu.common.sms.enmus.SmsTypeEnum;

import java.util.Map;

/**
 * @Description: TODO 短信服务接口层
 * @Author: 雒世松
 * @Date: 2025/6/5 16:18
 * @param
 * @return:
 **/

public interface SmsService {

    /**
     * 发送短信验证码
     *
     * @param mobile   手机号 13061656199
     * @param templateCode  短信模板 SMS_194640010
     * @param templateParam 模板参数 "[{"code":"123456"}]"
     *
     * @return  boolean 是否发送成功
     */
    boolean sendSmsOld(String mobile, String templateCode, String templateParam);

    /**
     * 发送短信
     *
     * @param mobile         手机号 13388886666
     * @param smsType        短信模板 SMS_194640010，模板内容：您的验证码为：${code}，请在5分钟内使用
     * @param templateParams 模板参数 [{"code":"123456"}] ，用于替换短信模板中的变量
     * @return boolean 是否发送成功
     */
    boolean sendSms(String mobile, SmsTypeEnum smsType, Map<String, String> templateParams);


}
