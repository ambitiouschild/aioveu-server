package com.aioveu.common.sms.service;

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
    boolean sendSms(String mobile, String templateCode, String templateParam);


}
