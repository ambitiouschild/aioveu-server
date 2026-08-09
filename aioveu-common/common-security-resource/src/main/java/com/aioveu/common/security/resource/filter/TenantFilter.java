package com.aioveu.common.security.resource.filter;

import com.aioveu.common.core.constant.JwtClaimConstants;
import com.aioveu.common.core.tenant.TenantContextHolder;
import com.aioveu.common.security.core.config.property.SecurityFilterOrders;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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


        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();

        log.info("【TenantFilter】🌐 Incoming request | {} {} | query={}",
                method, uri, query != null ? query : "<none>");

        //✅ 正确做法：TenantFilter自己读 JwtAuthenticationToken
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuth) {

            Jwt jwt = jwtAuth.getToken();
            Object tenantIdObj = jwt.getClaim(JwtClaimConstants.Tenant.ID);

            if (tenantIdObj instanceof Number n) {
                long tenantId = n.longValue();
                TenantContextHolder.setTenantId(tenantId);
                log.info("【TenantFilter】从 JWT 设置租户ID: {}", tenantId);
                log.info("【TenantFilter】TenantContextHolder = 复印件,设置租户ID到设置到租户上下文: " + tenantId);
            } else {
                throw new IllegalStateException(
                        "JWT 缺失 tenant_id，URI=" + request.getRequestURI()
                );
            }
        } else {
            // 匿名请求不应进入 /me
            throw new IllegalStateException(
                    "非 JWT 认证请求，URI=" + request.getRequestURI()
            );
        }


        try {

            filterChain.doFilter(request, response);
        }
        finally {
            // 清理租户上下文
            // TenantContextHolder.clear();
            log.info("【TenantFilter】tenantId 已设置，由 TenantInterceptor 负责清理");
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
     * ✅ 只对 HTTP 请求生效  TenantFilter 永远不加白名单
     */
    /**
     * 租户过滤器（资源服务器专用）
     * ✅ 只认 JWT
     * ❌ 不认 clientId
     */
    /*
    *
    *
    *       ✅ 这个 attribute：
                不是 URL
                不是配置
                不是业务语义
                是 Filter 之间的技术契约
                👉 这是主流架构里最常见的“内部信号”机制
    *
    * */
    /**
     * 该接口仅用于 OAuth2 Password 模式认证链路
     *
     * ✅ 由授权服务在认证过程中调用
     * ✅ 此时尚无用户 JWT
     * ✅ 不应进入 TenantFilter 的 JWT 校验逻辑
     *
     * 不属于业务接口，不属于资源服务器对外 API
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String uri = request.getRequestURI();

        // 1️认证前接口
        if (uri.startsWith("/aioveu/api/v8/admin/tenant/users/tenants/")) {
            return true; // ✅ 我不执行
        }

        /*
         * 1️认证服务内部调用
         * - OAuth2 Password 模式
         * - 无 JWT、无用户态
         * - 仅用于加载用户凭证
         */
        boolean authInternalCall =
                uri.startsWith("/aioveu/api/v8/admin/tenant/users/")
                        && uri.contains("/UserAuthCredentials");

        /*
         * 2️公共 clientId 请求
         * - 无 JWT
         * - 由 ClientIdTenantResolutionFilter 托管
         */
        boolean publicClientRequest =
                request.getAttribute("__PUBLIC_CLIENT_REQUEST__") != null;

        // ✅ 两类请求均不应进入 JWT 租户校验
        return authInternalCall || publicClientRequest;
    }

}
