package com.aioveu.common.web.interceptor;


import com.aioveu.common.core.annotation.PlatformApi;
import com.aioveu.common.core.tenant.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @ClassName: PlatformApiInterceptor
 * @Description TODO Web 拦截器（关键）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 22:41
 * @Version 1.0
 **/

@Slf4j
public class PlatformApiInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        if (handler instanceof HandlerMethod method) {

            if (method.hasMethodAnnotation(PlatformApi.class)) {
                log.info("[PlatformApi] 平台接口标记: {}",
                        method.getMethod().getName());

                // ✅ 标记当前请求为“平台接口”
                TenantContextHolder.markPlatform();
            }
        }
        return true;
    }
}
