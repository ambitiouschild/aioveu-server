package com.aioveu.common.security.resource.handler;

import cn.hutool.core.util.StrUtil;

import com.aioveu.common.security.core.annotation.DataPermission;
import com.aioveu.common.security.core.enums.DataScopeEnum;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.aioveu.common.core.base.IBaseEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import com.aioveu.common.security.resource.helper.JwtSecurityUtils;
import java.lang.reflect.Method;

/**
 * @Description: TODO 数据权限控制器
 *                          这个 MyDataPermissionHandler只能放 common-security-resource，而且必须放。
 *                          它和 PermissionService是同一个阵营的，都是 “资源服务器运行时拦截器”。
 *                          MyDataPermissionHandler= MyBatis‑Plus 数据权限拦截器
 *                          ❌ 绝不能放 common-security-core
 *                          ❌ 更不能放 auth / common-mybatis（裸）
 *                          为什么它只能放 resource？（6 个铁证）
 *                          ✅ 铁证 1：它用了 JwtSecurityUtils
 *                          ✅ 铁证 2：它依赖 SecurityContext
 *                          ✅ 铁证 3：它依赖 MyBatis‑Plus（基础设施）
 *                          ✅ 铁证 4：它解析 Mapper 方法注解
 *                          ✅ 铁证 5：它拼接 SQL（极度危险）
 *                          ✅ 铁证 6：它和 TenantLineHandler是“兄弟组件”
 * @Author: 雒世松
 * @Date: 2025/6/5 15:52
 * @param
 * @return:
 **/

@Slf4j
public class MyDataPermissionHandler implements DataPermissionHandler {

    @Override
    @SneakyThrows
    public Expression getSqlSegment(Expression where, String mappedStatementId) {

        Class<?> clazz = Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf('.')));
        String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(StringPool.DOT) + 1);
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            DataPermission annotation = method.getAnnotation(DataPermission.class);
            // 没有注解，不进行数据权限过滤
            if (annotation == null) {
                return where;
            }
            // 超级管理员不受数据权限控制
            if (JwtSecurityUtils.isRoot()) {
                return where;
            }
            if (annotation != null
                    && (method.getName().equals(methodName) || (method.getName() + "_COUNT").equals(methodName))) {
                return dataScopeFilter(annotation.deptAlias(), annotation.deptIdColumnName(), annotation.userAlias(), annotation.userIdColumnName(), where);
            }
        }
        return where;
    }

    /**
     * 构建过滤条件
     *
     * @param where 当前查询条件
     * @return 构建后查询条件
     */
    @SneakyThrows
    public static Expression dataScopeFilter(String deptAlias, String deptIdColumnName, String userAlias, String userIdColumnName, Expression where) {


        String deptColumnName = StrUtil.isNotBlank(deptAlias) ? (deptAlias + StringPool.DOT + deptIdColumnName) : deptIdColumnName;
        String userColumnName = StrUtil.isNotBlank(userAlias) ? (userAlias + StringPool.DOT + userIdColumnName) : userIdColumnName;

        // 获取当前用户的数据权限
        Integer dataScope = JwtSecurityUtils.getDataScope();

        DataScopeEnum dataScopeEnum = IBaseEnum.getEnumByValue(dataScope, DataScopeEnum.class);

        Long deptId, userId;
        String appendSqlStr;
        switch (dataScopeEnum) {
            case ALL -> {
                return where;
            }
            case DEPT -> {
                deptId = JwtSecurityUtils.getDeptId();
                appendSqlStr = deptColumnName + StringPool.EQUALS + deptId;
            }
            case SELF -> {
                userId = JwtSecurityUtils.getUserId();
                appendSqlStr = userColumnName + StringPool.EQUALS + userId;
            }
            // 默认部门及子部门数据权限
            default -> {
                deptId = JwtSecurityUtils.getDeptId();
                appendSqlStr = deptColumnName + " IN ( SELECT id FROM sys_dept WHERE id = " + deptId + " or find_in_set( " + deptId + " , tree_path ) )";
            }
        }

        if (StrUtil.isBlank(appendSqlStr)) {
            return where;
        }

        Expression appendExpression = CCJSqlParserUtil.parseCondExpression(appendSqlStr);

        if (where == null) {
            return appendExpression;
        }

        return new AndExpression(where, appendExpression);
    }


}

