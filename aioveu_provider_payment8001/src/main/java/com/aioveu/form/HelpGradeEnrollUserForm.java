package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/6 0006 22:57
 */
@Data
public class HelpGradeEnrollUserForm extends GradeEnrollUserForm {

    @NotEmpty(message = "验证码不能为空!")
    private String code;

}
