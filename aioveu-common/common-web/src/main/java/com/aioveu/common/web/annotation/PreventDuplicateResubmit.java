package com.aioveu.common.web.annotation;


import java.lang.annotation.*;

/**
 * @Description: TODO 防重提交注解
 * @Author: 雒世松
 * @Date: 2025/6/5 16:20
 * @param
 * @return:
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface PreventDuplicateResubmit {

    /**
     * 防重提交锁过期时间(秒)
     * <p>
     * 默认5秒内不允许重复提交
     */
    int expire() default 5;

}
