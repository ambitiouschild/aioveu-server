package com.aioveu.auth.config;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import com.aioveu.auth.enums.CaptchaCodeTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * @Description: TODO 验证码自动装配配置
 * @Author: 雒世松
 * @Date: 2025/6/5 17:42
 * @param
 * @return:
 **/

@Configuration
@RequiredArgsConstructor
public class CaptchaConfig {

    private final CaptchaProperties captchaProperties;

    /**
     * 验证码文字生成器
     *
     * @return CodeGenerator
     */
    @Bean
    public CodeGenerator codeGenerator() {
        String codeType = captchaProperties.getCode().getType();
        int codeLength = captchaProperties.getCode().getLength();
        if (CaptchaCodeTypeEnum.MATH.name().equalsIgnoreCase(codeType)) {
            // 数学公式验证码
            return new MathGenerator(codeLength);
        } else if (CaptchaCodeTypeEnum.RANDOM.name().equalsIgnoreCase(codeType)) {
            // 随机字符验证码
            return new RandomGenerator(codeLength);
        } else {
            throw new IllegalArgumentException("Invalid captcha generator type: " + codeType);
        }
    }

    /**
     * 验证码字体
     */
    @Bean
    public Font captchaFont() {
        String fontName = captchaProperties.getFont().getName();
        int fontSize = captchaProperties.getFont().getSize();
        int fontWight = captchaProperties.getFont().getWeight();
        return new Font(fontName, fontWight, fontSize);
    }

}
