package com.aioveu.common.security.resource.exception;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aioveu.common.core.result.Result;
import com.aioveu.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: TODO 自定义 token 无效异常
 * @Author: 雒世松
 * @Date: 2025/6/5 16:11
 * @param
 * @return:
 **/

/**
 * 资源服务器 401 统一返回
 *
 * ✅ 特点
 * ✅ 返回 JSON（网关 / 前端友好）
 * ✅ 不跳转页面
 * ✅ 不依赖 Spring 默认 WWW-Authenticate
 * ✅ 和 MyAccessDeniedHandler风格一致
 *
 */
@Slf4j
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
    throws IOException {

        log.warn("【资源服务器认证失败】{} | {}", request.getRequestURI(), authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 401);
        result.put("msg", "未登录或令牌已失效");
        result.put("error", authException.getMessage());
        result.put("path", request.getRequestURI());
        result.put("timestamp", System.currentTimeMillis());

        response.getWriter().write(JSONUtil.toJsonStr(result));
    }
}
