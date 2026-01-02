package com.aioveu.system.aioveu02User.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName: MobileUpdateForm
 * @Description TODO  修改手机表单
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/30 22:54
 * @Version 1.0
 **/

@Schema(description = "修改手机表单")
@Data
public class MobileUpdateForm {

    @Schema(description = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    @Schema(description = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String code;
}
