package com.aioveu.common.annotation;

import java.lang.annotation.*;

/**
 * 标记该接口为【公共接口】
 *
 * ✅ 无需登录
 * ✅ 依赖 clientId 解析租户
 * ✅ 不经过 JWT 认证
 *
 * 示例：
 * <pre>
 * {@code
 * @PublicApi
 * @GetMapping("/config")
 * public Config config() { ... }
 * }
 * </pre>
 *
 * ⚠️ 注意：
 * - 仅用于 Controller 方法
 * - 不应用于内部接口
 * - 与 @PreAuthorize / @Secured 互斥
 *
 * @author aioveu
 * @since 2026-07-10
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PublicApi {

    /**
     * 是否允许匿名访问（无 clientId）
     * 默认 false：公共接口仍需 clientId
     */
    boolean allowAnonymous() default false;

    /**
     * 接口描述（用于文档 / 审计）
     */
    String description() default "";
}
