package com.aioveu.common.security.resource.config.property;


import org.springframework.core.Ordered;

/**
 * @ClassName: SecurityFilterOrders
 * @Description TODO 定义一个“Filter Order 常量表”
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 16:21
 * @Version 1.0
 **/

public final class SecurityFilterOrders {

    /**
     * ✅ 公共租户解析（不依赖 JWT）
     * 用于 /public/** 等接口
     */
    public static final int PUBLIC_TENANT_FILTER = Ordered.HIGHEST_PRECEDENCE + 10;

    /**
     * ✅ JWT 解码已经完成
     * 可以安全访问 JwtAuthenticationToken
     */
    public static final int JWT_VERSION_FILTER = 100;

    /**
     * ✅ 校验 JWT 是否被吊销
     * 必须在 JWT 解码后
     */
    public static final int JWT_BLACKLIST_FILTER = 200;

    /**
     * ✅ 从 JWT 解析租户
     * 必须在 JWT + 黑名单之后
     */
    public static final int TENANT_FILTER = 300;

    private SecurityFilterOrders() {}
}
