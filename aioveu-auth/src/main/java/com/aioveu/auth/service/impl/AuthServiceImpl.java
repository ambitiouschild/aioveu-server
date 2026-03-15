package com.aioveu.auth.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.auth.TokenManager.service.AuthTokenManagerService;
import com.aioveu.auth.config.CaptchaProperties;
import com.aioveu.auth.model.CaptchaResult;
import com.aioveu.auth.model.SysUserDetails;
import com.aioveu.auth.oauth2.extension.password.PasswordAuthenticationConverter;
import com.aioveu.auth.oauth2.extension.password.PasswordAuthenticationProvider;
import com.aioveu.auth.service.SysUserDetailsService;
import com.aioveu.common.TokenManager.service.TokenManagerService;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.auth.model.AuthenticationToken;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import com.aioveu.common.sms.property.AliyunSmsProperties;
import com.aioveu.common.sms.service.SmsService;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.UserAuthInfoWithTenantId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Service;
import com.aioveu.auth.service.CaptchaService;
import com.aioveu.auth.service.AuthService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.aioveu.auth.util.SecurityUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * @Description: TODO 认证服务   - 处理用户认证相关的业务逻辑，包括验证码生成、短信发送等
 * @Author: 雒世松
 * @Date: 2025/6/5 17:52
 * @param
 * @return:
 **/

@Slf4j
@Service   // 标识为Spring服务层组件，由Spring容器管理，处理业务逻辑
@RequiredArgsConstructor   // Lombok注解，自动生成包含所有final字段的构造函数，实现依赖注入
public class AuthServiceImpl implements AuthService {


    // 验证码配置属性，从配置文件中读取验证码相关设置（如过期时间、宽度、高度等）
    private final CaptchaProperties captchaProperties;

    // 验证码服务，负责生成具体的验证码图片（图形验证码、算术验证码等）
    private final CaptchaService captchaService;

    // 阿里云短信配置属性，包含accessKey、secretKey、模板代码等配置信息
    private final AliyunSmsProperties aliyunSmsProperties;

    // 短信发送服务，封装了短信发送的具体实现
    private final SmsService smsService;

    // Redis模板，用于操作Redis数据库，存储验证码和短信验证码等临时数据
    private final StringRedisTemplate redisTemplate;

    private final TenantFeignClient tenantFeignClient;

    private final AuthTokenManagerService authTokenManagerService;// 令牌生成器

//    private final PasswordAuthenticationConverter passwordAuthenticationConverter;

    // 直接注入登录用的 tokenGenerator
//    private final PasswordAuthenticationProvider passwordAuthenticationProvider;

    // Spring Security认证管理器，用于执行实际的用户名密码验证
    private final AuthenticationManager authenticationManager;
    // OAuth2授权服务，用于持久化授权记录到数据库
    private final OAuth2AuthorizationService authorizationService;
    // OAuth2令牌生成器，用于生成访问令牌、刷新令牌等
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    // 添加 UserDetailsService 依赖
    private final SysUserDetailsService sysUserDetailsService;

    /**
     * 生成图形验证码并缓存到Redis
     * 流程：生成验证码图片 → 生成唯一ID → 缓存验证码文本 → 返回给前端
     *
     * @return CaptchaResult 包含验证码ID和Base64编码的图片数据
     *
     * 使用场景：用户登录、注册、敏感操作时的安全验证
     * 安全机制：验证码文本存储在Redis，有效期内只能使用一次
     */
    @Override
    public CaptchaResult getCaptcha() {

        // 调用验证码服务生成验证码图片（包含图片和验证码文本）
        AbstractCaptcha captcha = captchaService.generate();

        // 验证码文本缓存至Redis，用于登录校验
        // 生成唯一的验证码ID，用于后续验证时从Redis中查找对应的验证码文本
        String captchaId = IdUtil.fastSimpleUUID();

        // ✅ 修改：使用 StrUtil.format 构建Key，与验证时保持一致
        String redisKey = StrUtil.format(RedisConstants.Captcha.IMAGE_CODE, captchaId);

        // 将验证码文本存储到Redis，设置过期时间（防止验证码被长期滥用）
        redisTemplate.opsForValue().set(
//                RedisConstants.CAPTCHA_CODE_PREFIX + captchaId,  // Redis键：captcha:code:{captchaId}
                redisKey,  // 使用统一的Key格式
                captcha.getCode(),   // 验证码实际文本（如"AB12"）
                captchaProperties.getExpireSeconds(),   // 从配置读取过期时间（通常60-300秒）
                TimeUnit.SECONDS  // 时间单位：秒
        );

        // 添加调试日志
        log.info("验证码生成 - captchaId: {}, redisKey: {}, code: {}, expire: {}秒",
                captchaId, redisKey, captcha.getCode(), captchaProperties.getExpireSeconds());

        // 构建返回结果对象，包含验证码ID和Base64编码的图片数据
        CaptchaResult captchaResult = CaptchaResult.builder()
                .captchaId(captchaId)  // 前端需要保存此ID，验证时随验证码文本一起提交
                .captchaBase64(captcha.getImageBase64Data())    // Base64图片数据，可直接在img标签中显示
                .build();

        return captchaResult;
    }

    /**
     * 发送登录短信验证码
     * 流程：生成随机验证码 → 调用短信服务发送 → 缓存验证码到Redis → 返回发送结果
     * @param mobile 手机号 需要验证格式：11位数字）
     * @return true|false 是否发送成功
     *
     *      * 安全考虑：
     *      * 1. 需要防止短信轰炸（应在调用前检查发送频率）
     *      * 2. 验证码有效期较短（通常5分钟）
     *      * 3. 验证码使用后立即失效
     *      *
     *      * 业务场景：手机号登录、手机号绑定、重要操作验证
     */
    @Override
    public boolean sendLoginSmsCode(String mobile) {
        // 获取短信模板代码  // 从配置中获取登录相关的短信模板代码（不同业务场景使用不同模板）
        String templateCode = aliyunSmsProperties.getTemplateCodes().get("login");

        // 生成随机4位数验证码 // 生成4位随机数字验证码（如："1234"）
        String code = RandomUtil.randomNumbers(4);

        // 短信模板: 您的验证码：${code}，该验证码5分钟内有效，请勿泄漏于他人。
        // 其中 ${code} 是模板参数，使用时需要替换为实际值。
        // 示例模板内容："您的验证码：${code}，该验证码5分钟内有效，请勿泄漏于他人。"
        String templateParams = JSONUtil.toJsonStr(Collections.singletonMap("code", code));


        // 调用短信服务发送验证码短信
        boolean result = smsService.sendSms(mobile, templateCode, templateParams);

        if (result) {
            // 将验证码存入redis，有效期5分钟
            // 将验证码存储到Redis，key格式：register:sms:code:{手机号}
            redisTemplate.opsForValue().set(
                    RedisConstants.REGISTER_SMS_CODE_PREFIX + mobile,  // Redis键前缀+手机号
                    code,   // 4位数字验证码
                    5,    // 有效期5分钟（防止验证码被长期使用）
                    TimeUnit.MINUTES);   // 时间单位：分钟

            // TODO 考虑记录每次发送短信的详情，如发送时间、手机号和短信内容等，以便后续审核或分析短信发送效果。
            // TODO 待完善功能：记录短信发送日志，用于后续审计和分析
            // 可记录：发送时间、手机号、IP地址、发送结果、短信内容等
            // 便于监控短信发送情况、防止滥用、分析发送效果
        }
        return result;
    }

    /**
     * 注销登录
     */
    @Override
    public void logout() {
        String token = SecurityUtils.getToken(); //获取token
        if (StrUtil.isNotBlank(token)) {
            authTokenManagerService.invalidateToken(token);
            // 清除Security上下文
            SecurityContextHolder.clearContext();
        }
    }



    /*
     * 切换租户
     * */
    @Override
    public Result<AuthenticationToken> switchTenantWithJwt(Long tenantId) {


        try {
            // 1. 权限校验
            if (!tenantFeignClient.hasTenantSwitchPermission()) {
                return Result.failed("无权限切换租户");
            }

            // 1. 获取当前认证信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof SysUserDetails details)) {
                return Result.failed(ResultCode.ACCESS_TOKEN_INVALID);
            }

            // 2. 校验用户是否能访问该租户
            boolean canAccess = tenantFeignClient.canAccessTenant(details.getUserId(), tenantId);
            if (!canAccess) {
                return Result.failed("无权限访问该租户");
            }

            // 获取用户在新租户下的权限信息（可选，如果需要更新权限） 创建包含新租户ID的用户详情
            UserAuthInfoWithTenantId userAuthInfoWithTenantId = tenantFeignClient.getUserAuthInfoWithTenantId
                    (details.getUsername(), tenantId);

            SysUserDetails newDetails = new SysUserDetails(userAuthInfoWithTenantId);
            newDetails.setUserId(details.getUserId());
            newDetails.setUsername(details.getUsername());
            newDetails.setDeptId(details.getDeptId());
            newDetails.setDataScopes(details.getDataScopes());
            newDetails.setTenantId(tenantId);
            newDetails.setCanSwitchTenant(details.getCanSwitchTenant());

            Authentication newAuth = new UsernamePasswordAuthenticationToken(newDetails, authentication.getCredentials(), authentication.getAuthorities());
            AuthenticationToken token = authTokenManagerService.generateToken(newAuth);

            log.info("用户 {} 切换到租户 {} 成功", details.getUsername(), tenantId);

            return Result.success(token);

        } catch (Exception e) {
            log.error("切换租户失败", e);
            return Result.failed("切换失败: " + e.getMessage());
        }

    }

    /**
     * 切换租户 - 复用登录的 Token 生成器
     */
    @Override
    public Result<Authentication> switchTenant(Long tenantId) {


        try {
            // 1. 权限校验
            if (!tenantFeignClient.hasTenantSwitchPermission()) {
                return Result.failed("无权限切换租户");
            }

            // 1. 获取当前认证信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof SysUserDetails details)) {
                return Result.failed(ResultCode.ACCESS_TOKEN_INVALID);
            }

            // 2. 校验用户是否能访问该租户
            boolean canAccess = tenantFeignClient.canAccessTenant(details.getUserId(), tenantId);
            if (!canAccess) {
                return Result.failed("无权限访问该租户");
            }

            // 获取用户在新租户下的权限信息（可选，如果需要更新权限） 创建包含新租户ID的用户详情
            UserAuthInfoWithTenantId userAuthInfoWithTenantId = tenantFeignClient.getUserAuthInfoWithTenantId
                    (details.getUsername(), tenantId);


            SysUserDetails newDetails = new SysUserDetails(userAuthInfoWithTenantId);
            newDetails.setUserId(details.getUserId());
            newDetails.setUsername(details.getUsername());
            newDetails.setDeptId(details.getDeptId());
            newDetails.setDataScopes(details.getDataScopes());
            newDetails.setTenantId(tenantId);
            newDetails.setCanSwitchTenant(details.getCanSwitchTenant());


            // 6. 模拟登录请求
            HttpServletRequest request = buildLoginRequest(newDetails);

            // 1. 手动创建 PasswordAuthenticationConverter
            PasswordAuthenticationConverter passwordConverter = new PasswordAuthenticationConverter();
            // 7. 通过 Converter 创建认证令牌
            Authentication passwordAuth  = passwordConverter.convert(request);

            if (passwordAuth == null) {
                throw new RuntimeException("构建认证请求失败");
            }

            PasswordAuthenticationProvider passwordProvider = new PasswordAuthenticationProvider(
                    authenticationManager,
                    authorizationService,
                    tokenGenerator,
                    sysUserDetailsService
            );

            // 8. 调用认证提供者（复用登录流程）
            Authentication tokenResult = passwordProvider.authenticate(passwordAuth);

            log.info("用户 {} 切换到租户 {} 成功", details.getUsername(), tenantId);

            return Result.success(tokenResult);

        } catch (Exception e) {
            log.error("切换租户失败", e);
            return Result.failed("切换失败: " + e.getMessage());
        }
    }

    /**
     * 构建模拟的登录 HTTP 请求
     */
    private HttpServletRequest buildLoginRequest(SysUserDetails userDetails) {
        // 获取当前请求上下文
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new RuntimeException("无法获取请求上下文");
        }

        HttpServletRequest originalRequest = attributes.getRequest();

        // 创建包装请求，修改参数
        return new HttpServletRequestWrapper(originalRequest) {
            private final Map<String, String[]> modifiedParams = new HashMap<>();

            {
                // 复制原参数
                Map<String, String[]> originalParams = originalRequest.getParameterMap();
                modifiedParams.putAll(originalParams);

                // 修改登录参数
                modifiedParams.put("grant_type", new String[]{"password"});
                modifiedParams.put("username", new String[]{userDetails.getUsername()});
                modifiedParams.put("password", new String[]{"[TENANT_SWITCH]"}); // 特殊标记
                modifiedParams.put("tenant_id", new String[]{String.valueOf(userDetails.getTenantId())});

                // 添加客户端认证（从配置读取）
//                modifiedParams.put("client_id", new String[]{"your-client-id"});
//                modifiedParams.put("client_secret", new String[]{"your-client-secret"});
            }

            @Override
            public String getParameter(String name) {
                String[] values = modifiedParams.get(name);
                return values != null && values.length > 0 ? values[0] : null;
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return modifiedParams;
            }

            @Override
            public String[] getParameterValues(String name) {
                return modifiedParams.get(name);
            }
        };
    }






}
