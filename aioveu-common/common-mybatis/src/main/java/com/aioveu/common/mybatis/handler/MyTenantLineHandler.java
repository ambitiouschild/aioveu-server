package com.aioveu.common.mybatis.handler;


import com.aioveu.common.core.tenant.JobContextHolder;
import com.aioveu.common.core.tenant.TenantContextHolder;
import com.aioveu.common.mybatis.config.property.TenantMybatisProperties;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @ClassName: MyTenantLineHandler
 * @Description TODO  MyBatis-Plus 多租户处理器
 *                      * <p>
 *                      * 实现 TenantLineHandler 接口，自动为 SQL 添加租户过滤条件
 *                      * </p>
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/13 20:31
 * @Version 1.0
 **/

/**
 * MyBatis-Plus 多租户处理器（基础设施层）
 * <p>
 * 原则：
 * 1. 只消费 TenantContextHolder，不解析 JWT
 * 2. 不依赖 security-resource
 * 3. 特殊表忽略规则通过配置驱动
 */

@Slf4j
// 去掉 @Component / @PostConstruct
@RequiredArgsConstructor
public class MyTenantLineHandler implements TenantLineHandler {

    /** 平台 / Job 使用的占位租户ID（不会真正拼 SQL） */
    private static final long PLATFORM_TENANT_ID = 0L;

    private final TenantMybatisProperties tenantMybatisProperties;

    @PostConstruct
    public void init() {
        log.info("=== MyTenantLineHandler 初始化检查 ===");
        log.info("tenantProperties: {}", tenantMybatisProperties);
    }

    /**
     * 获取租户ID表达式
     * <p>
     * 从 TenantContextHolder 获取当前租户ID
     * 如果未设置或忽略租户，抛出异常
     * </p>
     *
     * @return 租户ID表达式
     */
    @Override
    public Expression getTenantId() {

        log.debug("=== 【MyTenantLineHandler】getTenantId() 被调用 ===");
        log.info("=== 【MyTenantLineHandler】JWT = 原件,MP = 只认原件, 只消费 TenantContextHolder，不解析 JWT ===");
        // ✅ 1. 登录期接口：直接返回 null（由 ignoreTable 控制）

        Long tenantId = TenantContextHolder.getTenantId();
        log.info("【MyTenantLineHandler】如果TenantContextHolder有租户id就赋值到租户上下文工具类: {}", tenantId);
        log.info("【MyTenantLineHandler】过滤器 → 解析Token → 设置租户上下文 → 后续所有组件都从上下文获取");

        if (tenantId != null) {
            return new LongValue(tenantId);
        }

        // ✅ 非 Web 场景（Job / Platform）：返回占位值
        if (JobContextHolder.isJob() || TenantContextHolder.isPlatform()) {
            log.debug("Job / Platform 场景，返回占位租户ID");
            return new LongValue(PLATFORM_TENANT_ID);
        }

        // ✅ Web 场景但未设置租户：严格报错
        throw new IllegalStateException(
                "租户上下文未初始化，Web 请求必须通过 Token 设置租户"
        );
    }

    /**
     * 获取租户字段名
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return tenantMybatisProperties.getColumn();
    }


    private static final Set<String> DEFAULT_IGNORE_TABLES = Set.of(
            "sys_tenant",
            "sys_menu"
    );

    /**
     * 判断表是否忽略多租户过滤
     * <p>
     * 系统表、租户表等不需要租户隔离的表应返回 true
     * </p>
     *
     * @param tableName 表名
     * @return true-忽略，false-不忽略
     */
    @Override
    public boolean ignoreTable(String tableName) {
        if (tableName == null) {
            return false;
        }

        // ✅ Job / Platform：全部忽略租户条件
        if (JobContextHolder.isJob() || TenantContextHolder.isPlatform()) {
            return true;
        }

        // 如果设置了忽略租户标志，则本次查询全部表都跳过租户过滤
        if (TenantContextHolder.isIgnoreTenant()) {
            log.debug("【MyTenantLineHandler】全局忽略租户过滤");
            return true;
        }


        Set<String> ignoreTables = tenantMybatisProperties.getIgnoreTables();
        Set<String> allIgnoreTables = new HashSet<>(DEFAULT_IGNORE_TABLES);
        if (ignoreTables != null) {
            allIgnoreTables.addAll(ignoreTables);
        }


        // 忽略表名匹配（不区分大小写）
        return allIgnoreTables.stream()
                .anyMatch(ignoreTable -> ignoreTable.equalsIgnoreCase(tableName));
    }
}
