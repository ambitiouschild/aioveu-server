package com.aioveu.common.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: RepeatSubmit
 * @Description TODO  防止重复提交注解  该注解用于方法上，防止在指定时间内的重复提交。 默认时间为5秒。
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/16 14:04
 * @Version 1.0
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RepeatSubmit {

    /**
     * 锁过期时间（秒）
     * <p>
     * 默认5秒内不允许重复提交
     */
    int expire() default 5;
}
