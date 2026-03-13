package com.aioveu.common.mybatis.handler;

import com.aioveu.common.mybatis.config.property.TenantProperties;
import com.aioveu.common.tenant.TenantContextHolder;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.stereotype.Component;
//import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class MyTenantLineHandler implements TenantLineHandler {

    private final TenantProperties tenantProperties;



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

        log.info("=== 【MyTenantLineHandler】 被创建 ===");

        log.info("getTenantId() 被调用");

        // 获取当前租户ID
        Long tenantId = TenantContextHolder.getTenantId();
        log.info("从 TenantContextHolder 获取租户ID: {}", tenantId);


        // 如果租户ID为null，不添加租户条件
        if (tenantId == null) {
            log.info("【MyTenantLineHandler】租户ID为null，不添加租户过滤条件");
            log.info("【MyTenantLineHandler】问题根源：TenantLineInnerInterceptor在 getTenantId()返回 null时，还是添加了 AND tenant_id = null。");
//            return new LongValue(-999L);
            return null;
        }

//        if (tenantId == null) {
//            throw new IllegalStateException(
//                    "TenantId is required but was null. Ensure TenantContextHolder is set (e.g., via token) before DB access.");
//        }

        // 正常租户ID，添加过滤条件
        log.info("【MyTenantLineHandler】添加租户过滤条件: tenant_id = " + tenantId);

        return new LongValue(tenantId);
    }

    /**
     * 获取租户字段名
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getColumn();
    }

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

        log.info("检查表是否忽略: {}", tableName);

        // 1. 获取当前租户ID
        Long tenantId = TenantContextHolder.getTenantId();
        log.info("当前租户ID: {}", tenantId);

        // 2. 关键逻辑：如果租户ID为null或0，忽略表
        // 1. 租户ID为null时，只忽略sys_user表
        if (tenantId == null) {

            log.info("【MyTenantLineHandler】当前是登录查询用户租户列表场景");

            // 这个场景下，sys_user 和 sys_tenant 表都需要特殊处理
            log.info("【MyTenantLineHandler】这个场景下，sys_user 和 sys_tenant 表都需要特殊处理");

            if ("sys_user".equalsIgnoreCase(tableName)) {
                log.info("✅ 租户ID为null,✅ 查询用户租户场景，忽略 sys_user 表");
                return true;
            }
            if ("sys_tenant".equalsIgnoreCase(tableName)) {
                log.info("✅ 租户ID为null,✅ 查询用户租户场景，忽略 sys_tenant 表");
                return true;
            }
        }else{

            if ("sys_user".equalsIgnoreCase(tableName)) {
                log.info("✅ 租户ID不为null,✅ 查询用户租户场景，不忽略 sys_user 表");
                return false;
            }

            return true;
        }

        Set<String> systemTables = Set.of(
                "tables",
                "columns",
                "all_tables",
                "all_tab_comments",
                "all_objects",
                "all_tab_columns",
                "all_col_comments",
                "all_cons_columns",
                "all_constraints"
        );
        if (systemTables.contains(tableName.toLowerCase())) {
            return true;
        }

        // 如果设置了忽略租户标志，则本次查询全部表都跳过租户过滤
        if (TenantContextHolder.isIgnoreTenant()) {
            return true;
        }

        List<String> ignoreTables = tenantProperties.getIgnoreTables();
        if (ignoreTables == null || ignoreTables.isEmpty()) {
            return false;
        }

        // 忽略表名匹配（不区分大小写）
        return ignoreTables.stream()
                .anyMatch(ignoreTable -> ignoreTable.equalsIgnoreCase(tableName));
    }
}
