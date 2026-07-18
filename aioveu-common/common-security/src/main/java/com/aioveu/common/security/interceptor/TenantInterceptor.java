package com.aioveu.common.security.interceptor;


import com.aioveu.common.tenant.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * @ClassName: TenantInterceptor
 * @Description TODO TenantInterceptor 拦截器TenantInterceptor（专责清空租户上下文）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/18 15:25
 * @Version 1.0
 **/

@Component
@Slf4j
public class TenantInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        TenantContextHolder.clear();
        log.info("【TenantInterceptor】TenantInterceptor专责清空租户上下文");
        log.debug("【TenantInterceptor】TenantInterceptor cleared tenant context");
    }
}
