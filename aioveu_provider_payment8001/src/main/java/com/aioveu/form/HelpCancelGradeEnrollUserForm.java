package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/6 0006 22:57
 */
@Data
public class HelpCancelGradeEnrollUserForm  {

    @NotEmpty(message = "验证码不能为空!")
    private String code;

    @NotEmpty(message = "手机号码不能为空!")
    private String phone;

    @NotNull(message = "约课id不能为空")
    private Long gradeEnrollUserId;

    private Long fixedId;

    private int type;

}
