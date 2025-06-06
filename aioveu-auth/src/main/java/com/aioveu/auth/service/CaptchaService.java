package com.aioveu.auth.service;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.CodeGenerator;
import com.aioveu.auth.config.CaptchaProperties;
import com.aioveu.auth.enums.CaptchaTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;

/**
 * @Description: TODO 图形验证码生成器
 * @Author: 雒世松
 * @Date: 2025/6/5 17:52
 * @param
 * @return:
 **/

@Component
@RequiredArgsConstructor
public class CaptchaService {

    private final CodeGenerator codeGenerator;
    private final Font captchaFont;
    private final CaptchaProperties captchaProperties;


    /**
     * 生成图形验证码
     */
    public AbstractCaptcha generate() {

        String captchaType = captchaProperties.getType();
        int width = captchaProperties.getWidth();
        int height = captchaProperties.getHeight();
        int interfereCount = captchaProperties.getInterfereCount();
        int codeLength = captchaProperties.getCode().getLength();

        AbstractCaptcha captcha;
        if (CaptchaTypeEnum.CIRCLE.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createCircleCaptcha(width, height, codeLength, interfereCount);
        } else if (CaptchaTypeEnum.GIF.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createGifCaptcha(width, height, codeLength);
        } else if (CaptchaTypeEnum.LINE.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createLineCaptcha(width, height, codeLength, interfereCount);
        } else if (CaptchaTypeEnum.SHEAR.name().equalsIgnoreCase(captchaType)) {
            captcha = CaptchaUtil.createShearCaptcha(width, height, codeLength, interfereCount);
        } else {
            throw new IllegalArgumentException("Invalid captcha type: " + captchaType);
        }
        captcha.setGenerator(codeGenerator);
        captcha.setTextAlpha(captchaProperties.getTextAlpha());
        captcha.setFont(captchaFont);

       return captcha;
    }

}
