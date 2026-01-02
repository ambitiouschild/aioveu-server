package com.aioveu.system.aioveu02User.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName: EmailUpdateForm
 * @Description TODO  修改邮箱表单
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/30 22:55
 * @Version 1.0
 **/

@Schema(description = "修改邮箱表单")
@Data
public class EmailUpdateForm {

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
