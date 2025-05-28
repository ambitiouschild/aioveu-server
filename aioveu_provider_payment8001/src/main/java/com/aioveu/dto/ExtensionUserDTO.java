package com.aioveu.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ExtensionUserDTO {

    @NotBlank(message = "name不能为空")
    private String name;

    private String id;

    @NotBlank(message = "手机号码不能为空")
    private String phone;

    private String head;

    private Integer gender;

    private String extensionId;

    /**
     * 验证码
     */
    private String captcha;

    private String confirmPassword;

    private String password;

    private String userName;

    private Integer status;
}
