package com.aioveu.common.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
/**
 * @Description: TODO 自定义无权限异常
 * @Author: 雒世松
 * @Date: 2025/6/5 16:11
 * @param
 * @return:
 **/

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), Result.failed(ResultCode.ACCESS_UNAUTHORIZED));
    }
}
