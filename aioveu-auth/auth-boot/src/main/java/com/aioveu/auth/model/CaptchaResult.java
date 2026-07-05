package com.aioveu.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * @Description: TODO 验证码响应对象
 * @Author: 雒世松
 * @Date: 2025/6/5 17:44
 * @param
 * @return:
 **/

@Schema(description = "验证码信息")
@Builder
@Data
public class CaptchaResult {

    /**
     * 验证码唯一标识(用于从Redis获取验证码Code)
     */
    @Schema(description = "验证码唯一标识(用于从Redis获取验证码Code)")
    private String captchaId;

    /**
     * 验证码图片Base64字符串
     */
    @Schema(description = "验证码图片Base64字符串")
    private String captchaBase64;
}
