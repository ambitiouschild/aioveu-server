package com.aioveu.common.annotation;

/*
 * @ClassName: ${NAME}
 * @Description TODO 数据权限注解
 * @Param
 * @return
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date  2026/2/21 20:17
 * @Version 1.0
 **/

import java.lang.annotation.*;

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
