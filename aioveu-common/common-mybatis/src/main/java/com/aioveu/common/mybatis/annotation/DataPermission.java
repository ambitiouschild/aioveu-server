package com.aioveu.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @Description: TODO MP数据权限注解 https://gitee.com/baomidou/mybatis-plus/issues/I37I90
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
     * 数据权限 {@link com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor}
     */
    String deptAlias() default "";

    String deptIdColumnName() default "dept_id";
    String userAlias() default "";

    String userIdColumnName() default "create_by";

}

