package com.aioveu.tenant.aioveu02User.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @ClassName: MobileUpdateForm
 * @Description TODO 修改手机表单
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:33
 * @Version 1.0
 **/
@Schema(description = "修改手机表单")
@Data
public class MobileUpdateForm {

    @Schema(description = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1(3\\d|4[5-9]|5[0-35-9]|6[2567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$", message = "手机号码格式不正确")
    private String mobile;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;

    @Schema(description = "当前密码")
    @NotBlank(message = "当前密码不能为空")
    private String password;
}
