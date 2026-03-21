package com.aioveu.common.security.filter;

import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.tenant.TenantContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;


/**
 * @ClassName: TenantFilter
 * @Description TODO  租户过滤器 - 用于从Token解析租户ID并设置到上下文
 *                      简化版本（如果SecurityUtils已实现）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/13 21:33
 * @Version 1.0
 **/
@Slf4j
@Component
//因为 OncePerRequestFilter本身是一个抽象类，你需要用 extends而不是 implements。
public class TenantFilter extends OncePerRequestFilter {


    /*
    * 方案3：调试SecurityUtils.getTenantId()
    * */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("=== 【租户过滤器工作】 ===");

        try {
            // 直接从SecurityUtils获取当前用户的租户ID
            // 调试1：直接调用   // 1. 从JWT解析租户ID
            Long tenantId = SecurityUtils.getTenantId();
            log.info("【租户过滤器工作】SecurityUtils.getTenantId() 结果: " + tenantId);

            // 调试2：查看Spring Security上下文
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info("【租户过滤器工作】Spring Security认证: " + authentication);
            if (authentication != null) {
                log.info("【租户过滤器工作】Principal: " + authentication.getPrincipal());
                log.info("【租户过滤器工作】是否认证: " + authentication.isAuthenticated());
            }

            // 调试3：查看请求Header
            log.info("【租户过滤器工作】Authorization Header: " + request.getHeader("Authorization"));
            log.info("【租户过滤器工作】租户过滤器执行，URI: " + request.getRequestURI());
            log.info("【租户过滤器工作】请求Header: " + request.getHeaderNames());

            // 尝试从多种来源获取租户ID
            Long requestTenantId = getTenantIdFromRequest(request);

            if (tenantId != null) {
                // 设置到租户上下文
                TenantContextHolder.setTenantId(tenantId);
                log.info("【租户过滤器工作】过滤器✅ 从SecurityUtils设置租户ID: " + tenantId);
            } else {
                log.info("【租户过滤器工作】⚠️ 没有租户ID，跳过设置");
            }

            if (requestTenantId != null) {
                // 设置到租户上下文
                TenantContextHolder.setTenantId(requestTenantId);
                log.info("【租户过滤器工作】过滤器✅ 从request请求Header设置租户ID: " + requestTenantId);
            }

            filterChain.doFilter(request, response);
        } finally {
            // 清理租户上下文
//            TenantContextHolder.clear();
            // 清理租户上下文
            log.info("【租户过滤器工作】⚠️ 过滤后不清理租户上下文");
        }
    }

    private Long getTenantIdFromRequest(HttpServletRequest request) {
        // 1. 从Header获取
        String tenantIdHeader = request.getHeader("X-Tenant-Id");
        if (StringUtils.hasText(tenantIdHeader)) {
            return Long.parseLong(tenantIdHeader);
        }

        // 2. 从参数获取
        String tenantIdParam = request.getParameter("tenantId");
        if (StringUtils.hasText(tenantIdParam)) {
            return Long.parseLong(tenantIdParam);
        }

        // 3. 从Basic认证解析（如果是mall-app:123456格式）
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Basic ")) {
            // 可以解析Basic认证，看是否包含租户信息
            // 这取决于您的认证服务设计
        }

        return null;
    }

}
