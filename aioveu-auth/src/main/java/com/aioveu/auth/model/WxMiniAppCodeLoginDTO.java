package com.aioveu.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @ClassName: WxMiniAppCodeLoginDTO
 * @Description TODO  微信小程序Code登录请求参数
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 14:30
 * @Version 1.0
 **/

@Schema(description = "微信小程序Code登录请求参数")
@Data
public class WxMiniAppCodeLoginDTO {

    @Schema(description = "微信小程序登录时获取的code", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "code不能为空")
    private String code;
}
