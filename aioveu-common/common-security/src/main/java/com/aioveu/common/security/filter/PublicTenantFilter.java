package com.aioveu.common.security.filter;


import cn.hutool.core.collection.CollectionUtil;
import com.aioveu.common.security.config.property.SecurityProperties;
import com.aioveu.common.security.model.SecurityFilterOrders;
import com.aioveu.common.security.service.PublicTenantResolver;
import com.aioveu.common.tenant.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

/**
 * @ClassName: PublicTenantFilter
 * @Description TODO  Filter 里只允许“计算”，不允许“通信”
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 10:20
 * @Version 1.0
 **/
/**
 * 公共接口租户解析过滤器
 *
 * <p><b>核心职责：</b>
 *
 * <p><b>设计原则：</b>
 * <ul>
 *   <li>Filter 中只允许“内存计算”，不允许直接发起远程调用</li>
 *   <li>远程调用（Feign）被 {@link PublicTenantResolver} 中的 Caffeine Cache 封装</li>
 *   <li>Filter 不关心 tenantId 的来源细节，只关心结果</li>
 *   <li>解析失败直接拒绝请求，不兜底、不默认租户</li>
 * </ul>
 *
 * <p><b>执行顺序：</b>
 * <ol>
 *   <li>{@link #shouldNotFilter}：判断是否需要对当前请求生效</li>
 *   <li>{@link #doFilterInternal}：解析租户并设置上下文</li>
 *   <li>{@link TenantContextHolder}：线程级租户上下文，供下游业务使用</li>
 * </ol>
 *
 * <p><b>重要约束：</b>
 * <ul>
 *   <li>本 Filter 仅作用于 Servlet MVC 请求</li>
 *   <li>非 MVC 请求（如 actuator、static）直接跳过</li>
 *   <li>必须与 {@link PublicTenantResolver} 配合使用</li>
 * </ul>
 *
 * @author aioveu
 * @author 雒世松
 * @since 2026-07-10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PublicTenantFilter extends OncePerRequestFilter implements Ordered {


    private static final String HEADER_CLIENT_ID = "X-Client-Id";
    private static final String HEADER_CLIENT_VERIFIED = "X-Client-Verified";
    private final SecurityProperties securityProperties;

    /**
     * 公共租户解析器
     *
     * <p>职责：
     * <ul>
     *   <li>从请求头 {@code X-Client-Id} 中提取 clientId</li>
     *   <li>通过本地 Caffeine Cache 获取 tenantId</li>
     *   <li>Cache Miss 时异步调用 Feign 查询</li>
     *   <li>不阻塞 Filter 线程</li>
     * </ul>
     */
    private final PublicTenantResolver publicTenantResolver;

//    public PublicTenantFilter(PublicTenantResolver publicTenantResolver) {
//        this.publicTenantResolver = publicTenantResolver;
//    }
    @Override
    public int getOrder() {
        return SecurityFilterOrders.PUBLIC_TENANT_FILTER;
    }

    /**
     * 判断是否跳过当前过滤器
     *
     * <p><b>核心逻辑：</b>
     * <ol>
     *   <li>从 Spring MVC 上下文中获取当前 HandlerMethod</li>
     *   <li>若不是 MVC 请求（如 actuator、静态资源），直接跳过</li>
     *   <li>存在 → 执行 Filter；不存在 → 跳过</li>
     * </ol>
     *
     * <p><b>为什么不用 URL 前缀？</b>
     * <ul>
     *   <li>URL 是路由，不是语义</li>
     *   <li>URL 变动会导致安全边界失效</li>
     *   <li>注解是显式契约，可读、可维护、可扫描</li>
     * </ul>
     *
     * @param request 当前 HTTP 请求
     * @return true = 跳过过滤器；false = 执行过滤器
     * @PublicApi从来就不是给 Filter 用的
     *
     * 它是给人、给文档、给 AOP、给扫描工具用的
     * 专门处理“白名单请求”的租户解析
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        log.error("【PublicTenantFilter】shouldNotFilter called, URI={}", request.getRequestURI());
        HandlerMethod handlerMethod = getHandlerMethod(request);

        log.error("【PublicTenantFilter】handlerMethod={}", handlerMethod);



        /*
        * shouldNotFilter()的语义是：
                ✅ true  = 不执行这个 Filter
        * */
        String uri = request.getRequestURI();
        List<String> whitelist = securityProperties.getWhitelistPaths();
        if (CollectionUtil.isEmpty(whitelist)) {
            return true;
        }


        /*
        * shouldNotFilter()的语义是：
                ✅ false = 执行这个 Filter
                * // ✅ 在白名单 → 执行 Filter → false
                *  // ✅ 不在白名单 → 跳过 Filter → true
        * */
        boolean isWhitelist = whitelist.stream()
                .anyMatch(uri::startsWith);

        log.info("【PublicTenantFilter】URI={}, isWhitelist={}", uri, isWhitelist);
        return !isWhitelist;
    }



    /**
     * 过滤器核心逻辑
     *
     * <p><b>执行流程：</b>
     * <ol>
     *   <li>将 tenantId 设置到 {@link TenantContextHolder}</li>
     *   <li>继续执行过滤器链</li>
     *   <li>无论成功与否，最终清理租户上下文</li>
     * </ol>
     *
     * <p><b>线程模型说明：</b>
     * <ul>
     *   <li>resolve() 返回 {@link }，不阻塞 Tomcat 线程</li>
     *   <li>thenApply / thenRun 在回调线程中执行</li>
     *   <li>join() 用于保证 Filter 顺序（必须等待完成）</li>
     * </ul>
     *
     * <p><b>异常处理策略：</b>
     * <ul>
     *   <li>解析失败 → 返回 HTTP 400</li>
     *   <li>不缓存异常、不兜底租户</li>
     *   <li>日志中记录完整异常栈，便于排查</li>
     * </ul>
     *
     * @param request     当前 HTTP 请求
     * @param response    当前 HTTP 响应
     * @param filterChain 过滤器链
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String clientId;
        String verified = request.getHeader(HEADER_CLIENT_VERIFIED);
        log.info("【PublicTenantFilter】请求提取解析verified: {}", verified);

// ✅ Gateway 转发请求
        if ("true".equals(verified)) {
            clientId = request.getHeader(HEADER_CLIENT_ID);
            log.info("【PublicTenantFilter】Gateway request, clientId:{}", clientId);
            if (clientId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing X-Client-Id");
                return;
            }
        }
// ✅ Feign 内部调用（重点！） //Feign 没盖章 → 必须走 Param / 内部信任
        else {
            // ❗❗❗ 这里！必须从 param 拿
            clientId = request.getParameter("clientId");
            log.info("【PublicTenantFilter】Internal Feign request, clientId:{}", clientId);
            if (clientId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing clientId param");
                return;
            }
        }



        Long tenantId;
        try {
            // ✅ 同步解析（Cache 内部异步刷新，Filter 不关心）
            tenantId = publicTenantResolver.resolve(clientId);
            log.info("【PublicTenantFilter】同步解析tenantId: {}", tenantId);
        } catch (IllegalArgumentException e) {
            log.warn("【PublicTenantFilter】参数非法: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        } catch (Exception e) {
            log.error("【PublicTenantFilter】解析 tenantId 失败", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "租户解析失败");
            return;
        }

        TenantContextHolder.setTenantId(tenantId);
        log.info("【PublicTenantFilter】设置到上下文 tenantId：{}", tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            // ✅ 必须清理，防止线程复用
            TenantContextHolder.clear();
        }
    }


    /**
     * 从 Spring MVC 上下文中获取当前请求的 HandlerMethod
     *
     * <p><b>说明：</b>
     * <ul>
     *   <li>该方法仅在 DispatcherServlet 处理完成后有效</li>
     *   <li>非 MVC 请求（如 actuator、静态资源）返回 null</li>
     *   <li>返回值可用于判断方法注解、类注解等元数据</li>
     * </ul>
     *
     * @param request 当前 HTTP 请求
     * @return HandlerMethod 实例，或 null
     */
    private HandlerMethod getHandlerMethod(HttpServletRequest request) {
        Object handler = request.getAttribute(
                HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE
        );
        return handler instanceof HandlerMethod hm ? hm : null;
    }
}
