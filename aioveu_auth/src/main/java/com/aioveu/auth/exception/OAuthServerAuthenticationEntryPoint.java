package com.aioveu.auth.exception;

import com.aioveu.auth.common.model.ResultCode;
import com.aioveu.auth.common.model.ResultMsg;
import com.aioveu.auth.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 雒世松
 * 用于处理客户端想认证出错，包括客户端id、密码错误
 */
@Component
@Slf4j
public class OAuthServerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 认证失败处理器会调用这个方法返回提示信息
     * TODO 实际开发中可以自己定义，此处直接返回JSON数据：客户端认证失败错误提示
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ResponseUtils.result(response,new ResultMsg(ResultCode.CLIENT_AUTHENTICATION_FAILED.getCode(),ResultCode.CLIENT_AUTHENTICATION_FAILED.getMsg(),null));
    }
}