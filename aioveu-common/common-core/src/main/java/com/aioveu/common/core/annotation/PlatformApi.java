package com.aioveu.common.core.annotation;


import java.lang.annotation.*;


/**
 * @ClassName: PlatformApi
 * @Description TODO  标记该接口为【平台级接口】
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/08/03 12:08
 * @Version 1.0
 **/

/**
 * 标记该接口为【平台级接口】
 * 特点：
 * - 不依赖租户上下文
 * - 不走 MP 多租户
 * - 通常用于登录、注册、公共查询
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PlatformApi {

    /**
     * 接口描述（用于文档 / 审计）
     */
    String description() default "";
}
