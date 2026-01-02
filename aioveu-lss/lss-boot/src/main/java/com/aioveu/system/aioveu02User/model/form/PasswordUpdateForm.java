package com.aioveu.system.aioveu02User.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: PasswordUpdateForm
 * @Description TODO  修改密码表单
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/30 22:54
 * @Version 1.0
 **/

@Schema(description = "修改密码表单")
@Data
public class PasswordUpdateForm {

    @Schema(description = "原密码")
    private String oldPassword;

    @Schema(description = "新密码")
    private String newPassword;

    @Schema(description = "确认密码")
    private String confirmPassword;
}
