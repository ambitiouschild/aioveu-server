package com.aioveu.auth.exception;

import com.aioveu.auth.common.model.ResultCode;
import com.aioveu.auth.common.model.ResultMsg;
import com.aioveu.auth.utils.ResponseUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 雒世松
 *
 * 当认证后的用户访问受保护的资源时，权限不够，则会进入这个处理器
 */
@Component
public class RequestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ResponseUtils.result(response,new ResultMsg(ResultCode.NO_PERMISSION.getCode(),ResultCode.NO_PERMISSION.getMsg(),null));
    }
}