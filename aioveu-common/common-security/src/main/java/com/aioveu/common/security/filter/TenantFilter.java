package com.aioveu.common.security.filter;

import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.tenant.TenantContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
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
@Component
//因为 OncePerRequestFilter本身是一个抽象类，你需要用 extends而不是 implements。
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // 直接从SecurityUtils获取当前用户的租户ID
            Long tenantId = SecurityUtils.getTenantId();

            if (tenantId != null) {
                // 设置到租户上下文
                TenantContextHolder.setTenantId(tenantId);
                System.out.println("过滤器设置租户ID: " + tenantId);
            } else {
                // 如果没有租户ID，可以设置默认值或记录日志
                // TenantContextHolder.setTenantId(1L); // 默认租户
                System.err.println("警告：SecurityUtils.getTenantId()返回null，请求路径: " +
                        ((HttpServletRequest) request).getRequestURI());
            }

            filterChain.doFilter(request, response);
        } finally {
            // 清理租户上下文
            TenantContextHolder.clear();
        }
    }
}
