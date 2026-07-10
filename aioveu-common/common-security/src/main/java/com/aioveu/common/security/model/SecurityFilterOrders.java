package com.aioveu.common.security.model;


import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @ClassName: SecurityFilterOrders
 * @Description TODO 定义一个“Filter Order 常量表”
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 16:21
 * @Version 1.0
 **/

public final class SecurityFilterOrders {

    public static final int PUBLIC_TENANT_FILTER = Ordered.HIGHEST_PRECEDENCE + 10;
    public static final int JWT_VERSION_FILTER    = Ordered.HIGHEST_PRECEDENCE + 20;
    public static final int JWT_BLACKLIST_FILTER = Ordered.HIGHEST_PRECEDENCE + 30;
    public static final int TENANT_FILTER         = Ordered.HIGHEST_PRECEDENCE + 40;

    private SecurityFilterOrders() {}
}
