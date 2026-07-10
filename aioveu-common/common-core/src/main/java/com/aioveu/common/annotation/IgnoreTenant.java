package com.aioveu.common.annotation;


import java.lang.annotation.*;

/**
 * 忽略租户隔离 （用于特殊场景）
 * 仅用于平台级接口
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreTenant {
}
