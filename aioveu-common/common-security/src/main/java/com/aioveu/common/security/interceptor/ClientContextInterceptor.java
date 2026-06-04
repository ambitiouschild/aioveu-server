package com.aioveu.common.security.interceptor;


/**
 * @ClassName: ClientContextInterceptor
 * @Description TODO Web 拦截器（订单 / 网关 / 公共接口）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 22:41
 * @Version 1.0
 **/

import com.aioveu.common.tenant.ClientContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Web MVC 拦截器  自动清理 ThreadLocal（防内存泄漏）
 */
public class ClientContextInterceptor implements HandlerInterceptor {


    private static final String HEADER_CLIENT_ID = "X-Client-Id";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String clientId = request.getHeader(HEADER_CLIENT_ID);
        ClientContextHolder.setClientId(clientId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        ClientContextHolder.clear();
    }


}
