package com.aioveu.common.security.core.annotation;

import java.lang.annotation.*;

/**
 * @Description: TODO MP数据权限注解 https://gitee.com/baomidou/mybatis-plus/issues/I37I90
 *                      DataPermission注解也挪到 security-core的决定是完全一致的，这两个是“绑定兄弟”。
 *
 * @Author: 雒世松
 * @Date: 2025/6/5 15:47
 * @param
 * @return:
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DataPermission {

    /**
     * 数据权限
     */
    String deptAlias() default "";

    String deptIdColumnName() default "dept_id";
    String userAlias() default "";

    String userIdColumnName() default "create_by";

}

