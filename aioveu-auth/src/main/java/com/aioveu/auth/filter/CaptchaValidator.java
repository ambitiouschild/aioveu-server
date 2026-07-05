package com.aioveu.auth.filter;


import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * @ClassName: CaptchaValidator
 * @Description TODO  抽一个「纯验证码校验器」（✅ 以后只改这里）
 *                      纯校验工具（可复用）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/5 13:17
 * @Version 1.0
 **/
@Component   // ✅ 关键
@Slf4j
@RequiredArgsConstructor
public class CaptchaValidator {

    private final StringRedisTemplate redisTemplate;
    private final CodeGenerator codeGenerator;

    /**
     * 校验图形验证码
     *
     * @param captchaKey   验证码Key
     * @param captchaCode  用户输入的验证码
     */
    public void validate(String captchaKey, String captchaCode) {

        if (StrUtil.isBlank(captchaKey) || StrUtil.isBlank(captchaCode)) {
            log.warn("验证码参数为空");
            throw new BadCredentialsException(ResultCode.USER_VERIFICATION_CODE_REQUIRED.getMsg());
        }

        String redisKey = StrUtil.format(RedisConstants.Captcha.IMAGE_CODE, captchaKey);
        String cacheCode = redisTemplate.opsForValue().get(redisKey);

        if (cacheCode == null) {
            log.warn("验证码不存在或已过期, key={}", captchaKey);
            throw new BadCredentialsException(ResultCode.USER_VERIFICATION_CODE_EXPIRED.getMsg());
        }

        if (!codeGenerator.verify(cacheCode, captchaCode)) {
            log.warn("验证码错误, input={}, expect={}", captchaCode, cacheCode);
            throw new BadCredentialsException(ResultCode.USER_VERIFICATION_CODE_ERROR.getMsg());
        }

        // ✅ 校验一次即删除（防重放）
        redisTemplate.delete(redisKey);
        log.info("验证码校验通过并已删除, key={}", captchaKey);
    }
}
