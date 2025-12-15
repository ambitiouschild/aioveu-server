package com.aioveu.common.annotation;

import com.aioveu.common.enums.LogModuleEnum;

import java.lang.annotation.*;

/**
 * @ClassName: annotation
 * @Description TODO  日志注解
 * @Author 可我不敌可爱
 * @Date 2025/12/12 21:52
 * @Version 1.0
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Log {

    /*
     * @Author 可我不敌可爱
     * @Description //TODO  日志描述
     * @Date  2025/12/12 21:53
     * @Param []
     * @return java.lang.String
     **/
    String value() default "";

    /**
     * 日志模块
     *
     * @return 日志模块
     */

    LogModuleEnum module();

    /**
     * 是否记录请求参数
     *
     * @return 是否记录请求参数
     */
    boolean params() default true;

    /**
     * 是否记录响应结果
     * <br/>
     * 响应结果默认不记录，避免日志过大
     * @return 是否记录响应结果
     */
    boolean result() default false;
}
