package com.aioveu.common.security.filter;

import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.tenant.TenantContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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


    /*
    * 方案3：调试SecurityUtils.getTenantId()
    * */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("=== 调试SecurityUtils.getTenantId() ===");

        try {
            // 直接从SecurityUtils获取当前用户的租户ID
            // 调试1：直接调用
            Long tenantId = SecurityUtils.getTenantId();
            System.out.println("SecurityUtils.getTenantId() 结果: " + tenantId);

            // 调试2：查看Spring Security上下文
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Spring Security认证: " + authentication);
            if (authentication != null) {
                System.out.println("Principal: " + authentication.getPrincipal());
                System.out.println("是否认证: " + authentication.isAuthenticated());
            }

            // 调试3：查看请求Header
            System.out.println("Authorization Header: " + request.getHeader("Authorization"));
            System.out.println("租户过滤器执行，URI: " + request.getRequestURI());
            System.out.println("请求Header: " + request.getHeaderNames());

            if (tenantId != null) {
                // 设置到租户上下文
                TenantContextHolder.setTenantId(tenantId);
                System.out.println("过滤器✅ 从SecurityUtils设置租户ID: " + tenantId);
            } else {
                // 没有租户ID，不设置
                System.out.println("⚠️ 没有租户ID，跳过设置");
            }

            filterChain.doFilter(request, response);
        } finally {
            // 清理租户上下文
            TenantContextHolder.clear();
        }
    }


}
