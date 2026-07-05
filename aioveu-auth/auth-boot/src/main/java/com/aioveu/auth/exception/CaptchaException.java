package com.aioveu.auth.exception;

/**
 * @ClassName: CaptchaException
 * @Description TODO  自定义验证码异常类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/9 19:58
 * @Version 1.0
 **/
public class CaptchaException extends RuntimeException{

    private final String errorCode;

    public CaptchaException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
