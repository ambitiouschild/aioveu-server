package com.aioveu.tenant.aioveu02User.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName: PasswordVerifyForm
 * @Description TODO 密码校验表单
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:36
 * @Version 1.0
 **/
@Schema(description = "密码校验表单")
@Data
public class PasswordVerifyForm {

    @Schema(description = "当前密码")
    @NotBlank(message = "当前密码不能为空")
    private String password;
}
