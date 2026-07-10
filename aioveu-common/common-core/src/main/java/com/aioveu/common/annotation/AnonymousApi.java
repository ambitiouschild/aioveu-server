package com.aioveu.common.annotation;


import java.lang.annotation.*;

/**
 * 完全匿名接口 （可选，但很香）
 * 无 clientId / 无 tenantId
 *
 * 示例：健康检测、首页静态数据
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PublicApi(allowAnonymous = true)
public @interface AnonymousApi {

    String description() default "";
}
