package com.aioveu.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: CaptchaInfo
 * @Description TODO  验证码信息
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 14:27
 * @Version 1.0
 **/

@Schema(description = "验证码信息")
@Data
@Builder
public class CaptchaInfo {

    @Schema(description = "验证码缓存 Key")
    private String captchaKey;

    @Schema(description = "验证码图片Base64字符串")
    private String captchaBase64;

}
