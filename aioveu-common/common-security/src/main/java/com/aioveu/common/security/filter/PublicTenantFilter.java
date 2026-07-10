package com.aioveu.common.security.filter;


import com.aioveu.common.security.service.PublicTenantResolver;
import com.aioveu.common.tenant.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.javadoc.PublicApi;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;

/**
 * @ClassName: PublicTenantFilter
 * @Description TODO  Filter 里只允许“计算”，不允许“通信” （适配 CompletableFuture）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 10:20
 * @Version 1.0
 **/
@Slf4j
@Component
public class PublicTenantFilter extends OncePerRequestFilter {

    private final PublicTenantResolver publicTenantResolver;

    public PublicTenantFilter(PublicTenantResolver publicTenantResolver) {
        this.publicTenantResolver = publicTenantResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        HandlerMethod handlerMethod = getHandlerMethod(request);
        if (handlerMethod == null) {
            return true; // 非 MVC 请求，直接跳过
        }

        return handlerMethod.getMethodAnnotation(PublicApi.class) == null;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        publicTenantResolver.resolve(request)
                .thenApply(tenantId -> {
                    TenantContextHolder.setTenantId(tenantId);
                    log.debug("PublicTenantFilter set tenantId={}", tenantId);
                    return tenantId;
                })
                .thenRun(() -> {
                    try {
                        filterChain.doFilter(request, response);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .exceptionally(ex -> {
                    log.warn("【PublicTenantFilter】解析失败", ex);
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return null;
                })
                .join(); // ✅ 等待完成，保证顺序
    }
}
