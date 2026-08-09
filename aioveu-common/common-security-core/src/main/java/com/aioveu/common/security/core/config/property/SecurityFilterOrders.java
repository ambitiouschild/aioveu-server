package com.aioveu.common.security.core.config.property;


/**
 * @ClassName: SecurityFilterOrders
 * @Description TODO 定义一个“Filter Order 常量表” 安全 & 租户过滤器执行顺序（全局唯一）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 16:21
 * @Version 1.0
 **/
/**
 * 安全 & 租户过滤器执行顺序（全局唯一）
 *
 * <p>规则：
 * <ul>
 *   <li>数值越小，越早执行</li>
 *   <li>所有安全相关 Filter 必须在此定义</li>
 *   <li>禁止在各自 Filter 中硬编码 order</li>
 * </ul>
 */

public interface SecurityFilterOrders {


    /* ==================== 租户解析（最早） ==================== */
    /* ==================== 容器级（非 Security） ==================== */
    /**
     * ✅ 公共租户解析（不依赖 JWT）
     * 用于 /public/** 等接口
     * ClientId 解析（无 JWT 公共接口）
     * 不进入 Spring Security 链
     */
    int PUBLIC_TENANT_FILTER = -210;


    /* ==================== Security 链（JWT 前） ==================== */

    /**
     * ✅ 认证期内部调用跳过 JWT
     *
     * ⚠️ 注意：
     * - 该值仅作为语义锚点
     * - 实际顺序由 addFilterBefore(..., BearerTokenAuthenticationFilter.class) 决定
     * - Spring 不会自动使用该 order
     */
    int JWT_AUTH_SKIPPING = -205;

    /**
     * JWT 解析完成（Spring Security OAuth2 Resource Server）
     * 由 Spring 自动插入，此处仅为参考基准
     * ✅ 它是 BearerTokenAuthenticationFilter的实际 order
     * ❌ 仅供阅读参考
     * 实际为 BearerTokenAuthenticationFilter 的 order
     * 不由我们控制
     */
    int JWT_AUTH_FILTER = -200;  //✅（Spring 实际 JWT Filter）


    /* ==================== JWT 之后 ==================== */
    /**
     * ✅ JWT 已解析，可安全读取 JwtAuthenticationToken
     * ✅ JWT 解码已经完成
     * 可以安全访问 JwtAuthenticationToken JWT 版本校验
     */
    int JWT_VERSION_FILTER = -190;

    /**
     * ✅ JWT 已验证，可校验黑名单
     * ✅ 校验 JWT 是否被吊销
     * 必须在 JWT 解码后
     */
    int JWT_BLACKLIST_FILTER = -180;

    /**
     * ✅ 从 JWT 解析租户 从 JWT 设置租户上下文（必须晚于 JWT）
     * 必须在 JWT + 黑名单之后
     */
    int TENANT_FILTER = -170;

    /* ==================== Security 授权 ==================== */

    int EXCEPTION_TRANSLATION = -30;
    int AUTHORIZATION = -20;

    /* ==================== 清理（最晚） ==================== */

    int TENANT_CLEAR_FILTER = Integer.MAX_VALUE - 100;

}
