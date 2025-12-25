package com.aioveu.common.security.exception;

/**
 * @ClassName: CaptchaValidationException
 * @Description TODO  验证码校验异常
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/20 17:13
 * @Version 1.0
 **/
public class CaptchaValidationException extends RuntimeException {
    public CaptchaValidationException(String message) {
        super(message);
    }
}
