package com.aioveu.auth.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.auth.config.CaptchaProperties;
import com.aioveu.auth.enums.CaptchaTypeEnum;
import com.aioveu.auth.model.CaptchaInfo;
import com.aioveu.auth.model.CaptchaResult;
import com.aioveu.auth.model.WxMiniAppCodeLoginDTO;
import com.aioveu.auth.model.WxMiniAppPhoneLoginDTO;
import com.aioveu.auth.service.AuthService;
import com.aioveu.auth.service.CaptchaService;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.ResultCode;
import com.aioveu.common.security.extension.sms.SmsAuthenticationToken;
import com.aioveu.common.security.extension.wx.WxMiniAppCodeAuthenticationToken;
import com.aioveu.common.security.extension.wx.WxMiniAppPhoneAuthenticationToken;
import com.aioveu.common.security.model.AuthenticationToken;
import com.aioveu.common.security.token.TokenManager;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.sms.enmus.SmsTypeEnum;
import com.aioveu.common.sms.property.AliyunSmsProperties;
import com.aioveu.common.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.aioveu.common.constant.SecurityConstants;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO 认证服务  认证服务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 17:52
 * @param
 * @return:
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;


    private final CaptchaProperties captchaProperties;
    private final CodeGenerator codeGenerator;
    private final Font captchaFont;
    private final CaptchaService captchaService;

    private final AliyunSmsProperties aliyunSmsProperties;
    private final SmsService smsService;

    private final StringRedisTemplate redisTemplate;


    /**
     * 用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 访问令牌
     */
    @Override
    public AuthenticationToken login(String username, String password) {
        // 1. 创建用于密码认证的令牌（未认证）
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username.trim(), password);

        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        AuthenticationToken authenticationTokenResponse =
                tokenManager.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authenticationTokenResponse;
    }

    /**
     * 微信一键授权登录
     *
     * @param code 微信登录code
     * @return 访问令牌
     */
    @Override
    public AuthenticationToken loginByWechat(String code) {
        // 1. 创建用户微信认证的令牌（未认证）
        WxMiniAppCodeAuthenticationToken authenticationToken = new WxMiniAppCodeAuthenticationToken(code);

        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        AuthenticationToken token = tokenManager.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return token;
    }

    /**
     * 发送登录短信验证码
     *
     * @param mobile 手机号
     */
    @Override
    public void sendSmsLoginCode(String mobile) {

        // 随机生成4位验证码
        // String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        // TODO 为了方便测试，验证码固定为 1234，实际开发中在配置了厂商短信服务后，可以使用上面的随机验证码
        String code = "1234";

        // 发送短信验证码
        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("code", code);
        try {
            smsService.sendSms(mobile, SmsTypeEnum.LOGIN, templateParams);
        } catch (Exception e) {
            log.error("发送短信验证码失败", e);
        }
        // 缓存验证码至Redis，用于登录校验
        redisTemplate.opsForValue().set(StrUtil.format(RedisConstants.Captcha.SMS_LOGIN_CODE, mobile), code, 5, TimeUnit.MINUTES);
    }

    /**
     * 发送登录短信验证码
     *
     * @param mobile 手机号
     * @return true|false 是否发送成功
     */
    public boolean sendLoginSmsCode(String mobile) {
        // 获取短信模板代码
        String templateCode = aliyunSmsProperties.getTemplateCodes().get("login");

        // 生成随机4位数验证码
        String code = RandomUtil.randomNumbers(4);

        // 短信模板: 您的验证码：${code}，该验证码5分钟内有效，请勿泄漏于他人。
        // 其中 ${code} 是模板参数，使用时需要替换为实际值。
        String templateParams = JSONUtil.toJsonStr(Collections.singletonMap("code", code));

        boolean result = smsService.sendSmsOld(mobile, templateCode, templateParams);
        if (result) {
            // 将验证码存入redis，有效期5分钟
            redisTemplate.opsForValue().set(RedisConstants.REGISTER_SMS_CODE_PREFIX + mobile, code, 5, TimeUnit.MINUTES);

            // TODO 考虑记录每次发送短信的详情，如发送时间、手机号和短信内容等，以便后续审核或分析短信发送效果。
        }
        return result;
    }

    /**
     * 短信验证码登录
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return 访问令牌
     */
    @Override
    public AuthenticationToken loginBySms(String mobile, String code) {
        // 1. 创建用户短信验证码认证的令牌（未认证）
        SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken(mobile, code);

        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(smsAuthenticationToken);

        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        AuthenticationToken authenticationToken = tokenManager.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authenticationToken;
    }


    /**
     * 注销登录
     */
    @Override
    public void logout() {
        String token = SecurityUtils.getTokenFromRequest();
        if (StrUtil.isNotBlank(token) && token.startsWith(SecurityConstants.BEARER_TOKEN_PREFIX )) {
            token = token.substring(SecurityConstants.BEARER_TOKEN_PREFIX .length());
            // 将JWT令牌加入黑名单
            tokenManager.invalidateToken(token);
            // 清除Security上下文
            SecurityContextHolder.clearContext();
        }
    }


    /**
     * 获取验证码
     *
     * @return 验证码
     */
    @Override
    public CaptchaInfo getCaptcha() {

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

        String captchaCode = captcha.getCode();
        String imageBase64Data = captcha.getImageBase64Data();

        // 验证码文本缓存至Redis，用于登录校验
        String captchaKey = IdUtil.fastSimpleUUID();
        redisTemplate.opsForValue().set(
                StrUtil.format(RedisConstants.Captcha.IMAGE_CODE, captchaKey),
                captchaCode,
                captchaProperties.getExpireSeconds(),
                TimeUnit.SECONDS
        );

        return CaptchaInfo.builder()
                .captchaKey(captchaKey)
                .captchaBase64(imageBase64Data)
                .build();
    }

    /**
     * 获取图形验证码
     *
     * @return Result<CaptchaResult>
     */
    public CaptchaResult getCaptcha2() {

        AbstractCaptcha captcha = captchaService.generate();

        // 验证码文本缓存至Redis，用于登录校验
        String captchaId = IdUtil.fastSimpleUUID();
        redisTemplate.opsForValue().set(
                RedisConstants.CAPTCHA_CODE_PREFIX + captchaId,
                captcha.getCode(),
                captchaProperties.getExpireSeconds(),
                TimeUnit.SECONDS
        );

        CaptchaResult captchaResult = CaptchaResult.builder()
                .captchaId(captchaId)
                .captchaBase64(captcha.getImageBase64Data())
                .build();

        return captchaResult;
    }

    /**
     * 刷新token
     *
     * @param refreshToken 刷新令牌
     * @return 新的访问令牌
     */
    @Override
    public AuthenticationToken refreshToken(String refreshToken) {
        // 验证刷新令牌
        boolean isValidate = tokenManager.validateRefreshToken(refreshToken);

        if (!isValidate) {
            throw new BusinessException(ResultCode.REFRESH_TOKEN_INVALID);
        }
        // 刷新令牌有效，生成新的访问令牌
        return tokenManager.refreshToken(refreshToken);
    }

    /**
     * 微信小程序Code登录
     *
     * @param loginDTO 登录参数
     * @return 访问令牌
     */
    @Override
    public AuthenticationToken loginByWxMiniAppCode(WxMiniAppCodeLoginDTO loginDTO) {
        // 1. 创建微信小程序认证令牌（未认证）
        WxMiniAppCodeAuthenticationToken authenticationToken = new WxMiniAppCodeAuthenticationToken(loginDTO.getCode());

        // 2. 执行认证（认证中）
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 认证成功后生成 JWT 令牌，并存入 Security 上下文，供登录日志 AOP 使用（已认证）
        AuthenticationToken token = tokenManager.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return token;
    }

    /**
     * 微信小程序手机号登录
     *
     * @param loginDTO 登录参数
     * @return 访问令牌
     */
    @Override
    public AuthenticationToken loginByWxMiniAppPhone(WxMiniAppPhoneLoginDTO loginDTO) {
        // 创建微信小程序手机号认证Token
        WxMiniAppPhoneAuthenticationToken authenticationToken = new WxMiniAppPhoneAuthenticationToken(
                loginDTO.getCode(),
                loginDTO.getEncryptedData(),
                loginDTO.getIv()
        );

        // 执行认证
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 认证成功后生成JWT令牌，并存入Security上下文
        AuthenticationToken token = tokenManager.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return token;
    }





}
