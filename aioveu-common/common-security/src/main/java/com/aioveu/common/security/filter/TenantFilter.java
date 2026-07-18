package com.aioveu.common.security.filter;

import com.aioveu.common.security.model.SecurityFilterOrders;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.tenant.TenantContextHolder;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
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
 *                      **TenantFilter 只做一件事：
 *                      从 SecurityUtils 取 tenantId，设置到 TenantContextHolder**
 *                      TenantFilter永远不认识 clientId
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/13 21:33
 * @Version 1.0
 **/

/**
 * 租户过滤器（资源服务器专用）
 *
 * ✅ 职责单一：
 * 1. 从 Spring Security Context 中获取 tenantId（来自 JWT）
 * 2. 设置到 TenantContextHolder
 * 3. 请求结束后清理
 *
 * ❌ 不兜底、不解析 Header、不解析参数
 * ❌ 不信任前端直接传来的 tenantId
 */

/**
 * 租户过滤器
 *
 * ✅ 职责：
 * 1. 从 SecurityUtils 获取 JWT 中的 tenantId
 * 2. 设置到 TenantContextHolder
 * 3. 供【业务代码 / 日志 / 审计 / 非 MP SQL】使用
 *
 * ❌ 注意：
 * MyBatis-Plus 的 TenantLineHandler **不会**使用 TenantContextHolder
 * 它只使用 SecurityUtils 中的 JWT 信息
 *
 * 因此：本 Filter 对 MP 插件“无感知、无影响”
 */

@Slf4j
@Component
@RequiredArgsConstructor
//因为 OncePerRequestFilter本身是一个抽象类，你需要用 extends而不是 implements。
public class TenantFilter extends OncePerRequestFilter implements Ordered {




    @Override
    public int getOrder() {
        return SecurityFilterOrders.TENANT_FILTER;
    }

    /*
    * 方案3：调试SecurityUtils.getTenantId()
    * */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        log.info("=== 【TenantFilter】TenantFilter里永远只认 SecurityUtils ===");

        try {
            // 直接从SecurityUtils获取当前用户的租户ID
            // 调试1：直接调用   // 1. 从JWT解析租户ID
            Long tenantId = SecurityUtils.getTenantId();
            log.info("【TenantFilter】SecurityUtils.getTenantId() 结果: " + tenantId);


            if (tenantId != null) {
                // 设置到租户上下文
                TenantContextHolder.setTenantId(tenantId);
                log.info("【TenantFilter】TenantContextHolder = 复印件,设置租户ID到设置到租户上下文: " + tenantId);
            } else {
                // ✅ 没有 tenantId 是异常情况，但不是 Filter 的责任
                log.info("【TenantFilter】⚠️ 没有租户ID，跳过设置");
            }
            filterChain.doFilter(request, response);
        }
        finally {
            // 清理租户上下文
//            TenantContextHolder.clear();
            log.info("【TenantFilter】⚠️ TenantFilter（只 set，不清空）");
        }
    }

    /*
    * 原因 1：安全红线
        资源服务器永远不应该相信前端传来的 tenantId
    * */
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

    /**
     * ✅ 只对 HTTP 请求生效
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return false;
    }

}
