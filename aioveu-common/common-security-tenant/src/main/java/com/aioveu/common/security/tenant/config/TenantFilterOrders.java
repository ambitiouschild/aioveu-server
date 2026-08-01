package com.aioveu.common.security.tenant.config;


/**
 * 租户过滤器执行顺序定义
 *
 * <p>规则：
 * <ul>
 *   <li>数值越小，优先级越高</li>
 *   <li>租户解析必须早于 Security / MVC / MyBatis</li>
 *   <li>避免与 Spring Security 原生过滤器冲突</li>
 * </ul>
 *
 * @author aioveu
 * @since 2026-07-29
 */
public interface TenantFilterOrders {

    /**
     * 公共租户解析过滤器（ClientId / 平台引导）
     * 早于：
     * - SecurityContextPersistenceFilter (-110)
     * - UsernamePasswordAuthenticationFilter (-50)
     * - MyBatis / MP 租户插件
     */
    int PUBLIC_TENANT_FILTER = -210;  // ClientId 解析

    /**
     * 租户上下文清理过滤器
     * 必须在：
     * - MVC 执行完
     * - MyBatis 执行完
     * - 事务提交后
     * 执行
     *     * 晚于：
     *      * - DispatcherServlet
     *      * - HandlerInterceptor
     *      * - MyBatis Executor
     *      * - TransactionInterceptor
     */
    int TENANT_CLEAR_FILTER = Integer.MAX_VALUE - 100;  // 清理上下文
}
