package com.aioveu.common.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Description: TODO 自定义 token 无效异常
 * @Author: 雒世松
 * @Date: 2025/6/5 16:11
 * @param
 * @return:
 **/

@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        mapper.writeValue(response.getOutputStream(), Result.failed(ResultCode.TOKEN_INVALID));
    }
}
