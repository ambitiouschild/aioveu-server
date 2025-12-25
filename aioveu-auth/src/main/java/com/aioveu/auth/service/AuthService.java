package com.aioveu.auth.service;

import com.aioveu.auth.model.*;

/**
 * @ClassName: AuthService
 * @Description TODO  认证服务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 14:23
 * @Version 1.0
 **/
public interface AuthService {


    /**
     * 获取验证码
     *
     * @return 验证码
     */
    CaptchaResult getCaptcha();


    boolean sendLoginSmsCode(String mobile);


}
