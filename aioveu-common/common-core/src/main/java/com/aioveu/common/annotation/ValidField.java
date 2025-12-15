package com.aioveu.common.annotation;

import java.lang.annotation.*;

import com.aioveu.common.validator.FieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * @ClassName: ValidField
 * @Description TODO  用于验证字段值是否合法的注解
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 17:09
 * @Version 1.0
 **/

@Documented
@Constraint(validatedBy = FieldValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidField {


    /**
     * 验证失败时的错误信息。
     */
    String message() default "非法字段";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 允许的合法值列表。
     */
    String[] allowedValues();

}
