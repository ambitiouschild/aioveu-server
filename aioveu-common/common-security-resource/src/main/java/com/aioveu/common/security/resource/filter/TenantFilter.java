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
@RequiredArgsConstructor
//因为 OncePerRequestFilter本身是一个抽象类，你需要用 extends而不是 implements。
public class TenantFilter extends OncePerRequestFilter{


    /**
     * 租户过滤器（资源服务器专用）
     *
     * ✅ 职责：
     * 1. 从 Spring Security Context 中读取 JwtAuthenticationToken
     * 2. 从 JWT claim 中获取 tenant_id
     * 3. 设置到 TenantContextHolder
     *
     * ❌ 不解析 Header / 参数
     * ❌ 不使用 SecurityUtils（Filter 是边界，不是消费者）
     */
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


        String authHeader = request.getHeader("Authorization");
        log.error("🔴 Authorization header = {}", authHeader);

        //✅ 正确做法：TenantFilter自己读 JwtAuthenticationToken
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        // ✅ 1️有认证信息：直接放行，留给 JWT 过滤器
        if (authentication == null) {
            log.debug("【TenantFilter】No Authentication, skip. uri={}", uri);
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ 2️不是 JWT：直接放行
        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            log.debug("【TenantFilter】Not JwtAuthenticationToken, skip. uri={}", uri);
            filterChain.doFilter(request, response);
            return;
        }else {

            // ✅ 3️JWT 已解析：取 tenantId

            Jwt jwt = jwtAuth.getToken();
            Object tenantIdObj = jwt.getClaim(JwtClaimConstants.Tenant.ID);

            log.info("【TenantFilter】✅ JWT authenticated, uri={}, tenantId={}",
                    uri, tenantIdObj);

            if (!(tenantIdObj instanceof Number)) {
                // ❌ 这是 JWT 内容非法，不是认证问题
                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "Missing or invalid tenant_id in JWT"
                );
                return;
            }

            long tenantId = ((Number) tenantIdObj).longValue();
            TenantContextHolder.setTenantId(tenantId);
            log.debug("【TenantFilter】TenantContextHolder set tenantId={}, uri={}",
                    tenantId, uri);

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
