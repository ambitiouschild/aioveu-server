package com.aioveu.common.security.filter;


import com.aioveu.common.annotation.PublicApi;
import com.aioveu.common.security.service.PublicTenantResolver;
import com.aioveu.common.tenant.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;

/**
 * @ClassName: PublicTenantFilter
 * @Description TODO  Filter 里只允许“计算”，不允许“通信” （适配 CompletableFuture）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 10:20
 * @Version 1.0
 **/
/**
 * 公共接口租户解析过滤器
 *
 * <p><b>核心职责：</b>
 * 仅对标注了 {@link PublicApi} 的接口，根据 {@code X-Client-Id} 解析并设置租户上下文。
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
public class PublicTenantFilter extends OncePerRequestFilter {


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

    public PublicTenantFilter(PublicTenantResolver publicTenantResolver) {
        this.publicTenantResolver = publicTenantResolver;
    }


    /**
     * 判断是否跳过当前过滤器
     *
     * <p><b>核心逻辑：</b>
     * <ol>
     *   <li>从 Spring MVC 上下文中获取当前 HandlerMethod</li>
     *   <li>若不是 MVC 请求（如 actuator、静态资源），直接跳过</li>
     *   <li>检查方法上是否存在 {@link PublicApi} 注解</li>
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
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        HandlerMethod handlerMethod = getHandlerMethod(request);

        // 非 MVC 请求（如 /actuator/health、静态资源）
        if (handlerMethod == null) {
            return true; // 非 MVC 请求（如 actuator、static）
        }
        // 仅对标注 @PublicApi 的方法生效
        return handlerMethod.getMethodAnnotation(PublicApi.class) == null;
    }



    /**
     * 过滤器核心逻辑
     *
     * <p><b>执行流程：</b>
     * <ol>
     *   <li>调用 {@link PublicTenantResolver#resolve(HttpServletRequest)} 获取 tenantId</li>
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

        publicTenantResolver.resolve(request)
                // 解析成功后设置租户上下文
                .thenApply(tenantId -> {
                    TenantContextHolder.setTenantId(tenantId);
                    log.debug("PublicTenantFilter set tenantId={}", tenantId);
                    return tenantId;
                })
                // 租户上下文就绪后继续执行过滤器链
                .thenRun(() -> {
                    try {
                        filterChain.doFilter(request, response);
                    } catch (Exception e) {
                        // 包装异常，保证 CompletableFuture 链路不中断
                        throw new RuntimeException(e);
                    }
                })
                // 解析失败或执行异常时的兜底处理
                .exceptionally(ex -> {
                    log.warn("【PublicTenantFilter】解析失败", ex);
                    response.setStatus(HttpStatus.BAD_REQUEST.value());
                    return null;
                })
                // 必须 join，否则 Filter 会提前返回
                .join(); // ✅ 等待完成，保证顺序
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
