package com.aioveu.common.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.aioveu.common.base.IBaseEnum;
import com.aioveu.common.mybatis.annotation.DataPermission;
import com.aioveu.common.mybatis.enums.DataScopeEnum;
import com.aioveu.common.security.util.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import java.lang.reflect.Method;

/**
 * @Description: TODO 数据权限控制器
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

        Class<?> clazz = Class.forName(mappedStatementId.substring(0, mappedStatementId.lastIndexOf(StringPool.DOT)));
        String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(StringPool.DOT) + 1);
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            DataPermission annotation = method.getAnnotation(DataPermission.class);
            // 没有注解，不进行数据权限过滤
            if (annotation == null) {
                return where;
            }
            // 超级管理员不受数据权限控制
            if (SecurityUtils.isRoot()) {
                return where;
            }
            if (ObjectUtils.isNotEmpty(annotation)
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
        Integer dataScope = SecurityUtils.getDataScope();

        DataScopeEnum dataScopeEnum = IBaseEnum.getEnumByValue(dataScope, DataScopeEnum.class);

        Long deptId, userId;
        String appendSqlStr;
        switch (dataScopeEnum) {
            case ALL -> {
                return where;
            }
            case DEPT -> {
                deptId = SecurityUtils.getDeptId();
                appendSqlStr = deptColumnName + StringPool.EQUALS + deptId;
            }
            case SELF -> {
                userId = SecurityUtils.getUserId();
                appendSqlStr = userColumnName + StringPool.EQUALS + userId;
            }
            // 默认部门及子部门数据权限
            default -> {
                deptId = SecurityUtils.getDeptId();
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

