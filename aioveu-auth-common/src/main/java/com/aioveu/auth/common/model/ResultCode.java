package com.aioveu.auth.common.model;

/**
 * @author 雒世松
 * 响应码、提示信息
 */
public enum ResultCode {

    CLIENT_AUTHENTICATION_FAILED(1001,"客户端认证失败"),

    USERNAME_OR_PASSWORD_ERROR(1002,"用户名或密码错误"),

    UNSUPPORTED_GRANT_TYPE(1003, "不支持的认证模式"),

    NO_PERMISSION(1005,"无访问权限"),
    UNAUTHORIZED(401, "系统错误"),
    SERVICE_NOT_FOUND(504, "系统错误"),

    INVALID_TOKEN(1004,"未登录或登录失效, 请重新登录");



    private final int code;

    private final String msg;

    ResultCode(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
