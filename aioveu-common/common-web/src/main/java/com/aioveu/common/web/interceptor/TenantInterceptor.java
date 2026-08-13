package com.aioveu.common.web.interceptor;


import com.aioveu.common.core.tenant.TenantContextHolder;
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

@Slf4j
public class TenantInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        /*
        *       这解决了你刚才那个 致命问题：
                ❌ 之前：Interceptor 在 Controller 之前把 TenantContextHolder清了
                ✅ 现在：Controller / Service / MP 都能读到 tenantId
        *
        * */
        // ✅ 什么都不清
        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {

        /*
        *       这是 唯一正确的清理时机：
                    Controller 已执行 ✅
                    Service 已执行 ✅
                    MP SQL 已执行 ✅
                    不会再被当前请求使用 ✅
        *
        * */
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            log.debug("【TenantInterceptor】clear tenantId={}", tenantId);
        }
        TenantContextHolder.clear();
        log.info("【TenantInterceptor】TenantInterceptor专责清空租户上下文");
        log.debug("【TenantInterceptor】TenantInterceptor cleared tenant context");
    }
}
