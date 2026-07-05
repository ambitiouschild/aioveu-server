package com.aioveu.auth.oauth2.extension.smscode;

import cn.hutool.core.util.StrUtil;
import com.aioveu.auth.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: TODO 短信验证码认证参数转换器
 *                  核心功能：将HTTP请求参数转换为Spring Security认证对象(Authentication)
 *                  处理流程：
 *                  1. 验证请求是否为短信验证码认证类型(grant_type=sms_code)
 *                  2. 提取和验证必填参数（手机号、验证码）
 *                  3. 提取可选参数（scope、其他附加参数）
 *                  4. 构建SmsCodeAuthenticationToken认证令牌
 *                  实现原理：
 *                  - 实现AuthenticationConverter接口，这是Spring Security的认证转换器标准接口
 *                  - 在OAuth2令牌端点过滤器中被调用
 *                  - 负责从HTTP请求中提取认证参数并创建相应的认证令牌
 *                  参数要求：
 *                  必需参数：
 *                    - grant_type: 必须为 "sms_code"
 *                    - mobile: 手机号码
 *                    - code: 短信验证码
 *                  可选参数：
 *                    - scope: 访问范围（如"openid profile"）
 *                  异常处理：
 *                  - 参数缺失或格式错误时抛出OAuth2AuthenticationException
 *                  - 错误信息符合OAuth2规范
 * 解析请求参数中的手机号和验证码，并转换成相应的身份验证(Authentication)对象
 * org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter
 * @Author: 雒世松
 * @Date: 2025/6/5 17:48
 * @param
 * @return:
 **/

@Slf4j
public class SmsCodeAuthenticationConverter implements AuthenticationConverter {

    /**
     * TODO
     *      核心转换方法：将HTTP请求转换为认证对象
     *      Spring Security调用时机：
     *      1. 客户端向/oauth2/token端点发起POST请求
     *      2. Spring Security的OAuth2TokenEndpointFilter拦截请求
     *      3. 遍历所有AuthenticationConverter尝试转换
     *      4. 如果本转换器匹配grant_type=sms_code，则进行处理
     *      方法执行流程：
     *      1. 验证授权类型 → 2. 获取客户端凭证 → 3. 提取请求参数 → 4. 验证必填参数 → 5. 构建认证令牌
     *
     * @param request HTTP请求对象，包含客户端提交的认证参数
     * @return Authentication 认证令牌对象，包含客户端信息和用户凭证
     *         返回null表示不处理此请求（由其他转换器处理）
     *         返回Authentication对象表示已成功创建认证令牌
     *
     */
    @Override
    public Authentication convert(HttpServletRequest request) {


        System.out.println("=== SmsCodeAuthenticationConverter 被调用 ===");
        System.out.println("grant_type: " + request.getParameter("grant_type"));
        System.out.println("所有参数: " + OAuth2EndpointUtils.getParameters(request));

        log.info("==================== 短信验证码认证开始 ====================");
        // 授权类型 (必需)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        log.info("【短信验证码认证】从请求参数中获取grant_type参数：{}", grantType);

        if (!SmsCodeAuthenticationToken.SMS_CODE.getValue().equals(grantType)) {
            log.info("【短信验证码认证】检查grant_type是否为短信验证码认证类型");
            log.info("【短信验证码认证】如果不是，返回null让其他认证转换器处理（如密码模式、微信模式等）");
            return null;  // 不是短信验证码认证，交由其他转换器处理
        }

        // 客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        log.info("【短信验证码认证】从SecurityContext中获取已认证的客户端信息");
        log.info("【短信验证码认证】注意：此时客户端已通过ClientAuthenticationFilter认证（client_id + client_secret）");
        log.info("【短信验证码认证】客户端认证发生在认证转换器之前");

        // 参数提取验证
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
        log.info("【短信验证码认证】将HTTP请求的所有参数提取为MultiValueMap（支持同名参数多个值）:{}",parameters);

        // 令牌申请访问范围验证 (可选)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        log.info("【短信验证码认证】scope参数格式：空格分隔的字符串，如 \"openid profile email\":{}",scope);

        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {

            log.info("【短信验证码认证】验证scope参数格式：如果有值，必须且只能有一个scope参数");
            log.info("【短信验证码认证】 scope参数格式错误，抛出OAuth2标准错误");
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,   // 错误码：无效请求
                    OAuth2ParameterNames.SCOPE,         // 错误参数名
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);   // 错误文档URI
        }
        Set<String> requestedScopes = null;
        log.info("【短信验证码认证】 将scope字符串转换为Set集合，便于后续处理");

        if (StringUtils.hasText(scope)) {


            requestedScopes = new HashSet<>(Arrays.asList
                    (StringUtils.delimitedListToStringArray(scope, " ")));
            log.info("【短信验证码认证】 使用空格分割scope字符串，并转换为Set去重");
        }

        // 手机号(必需)
        String mobile = parameters.getFirst(SmsCodeParameterNames.MOBILE);
        log.info("【短信验证码认证】 从参数中获取手机号，参数名定义在SmsCodeParameterNames.MOBILE常量中:{}",mobile);

        if (StrUtil.isBlank(mobile)) {
            log.info("【短信验证码认证】 验证手机号不能为空");
            log.info("【短信验证码认证】 手机号参数缺失，抛出OAuth2标准错误");
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,   // 错误码：无效请求
                    SmsCodeParameterNames.MOBILE,     // 错误参数名：mobile
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);  // 错误文档URI
        }

        // 验证码(必需)  转换器期望的参数名  code
        String code = parameters.getFirst(SmsCodeParameterNames.CODE);
        log.info("【短信验证码认证】 从参数中获取验证码，参数名定义在SmsCodeParameterNames.CODE常量中:{}",code);

        if (StrUtil.isBlank(code)) {

            log.info("【短信验证码认证】 验证验证码不能为空");
            log.info("【短信验证码认证】 验证码号参数缺失，抛出OAuth2标准错误");

            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,  // 错误码：无效请求
                    SmsCodeParameterNames.CODE,         // 错误参数名：code
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);  // 错误文档URI
        }


        // 附加参数(手机号码、验证码)
        log.info("【短信验证码认证】 收集除grant_type和scope之外的所有其他参数，作为附加参数传递给认证令牌");
        log.info("【短信验证码认证】 这些附加参数可能包含自定义的认证扩展信息");
        Map<String, Object> additionalParameters = parameters
                .entrySet()
                .stream()
                .filter(e ->  // 过滤掉grant_type和scope参数
                        !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE)
                                && !e.getKey().equals(OAuth2ParameterNames.SCOPE)
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,  // 参数名作为Key
                        e -> e.getValue().get(0)));  // 取参数值的第一个（同名参数只取第一个）

        log.info("【短信验证码认证】 创建SmsCodeAuthenticationToken对象，包含所有认证信息");
        return new SmsCodeAuthenticationToken(
                clientPrincipal,    // 客户端认证信息（已通过client_id/client_secret认证）
                requestedScopes,    // 请求的访问范围（可能为null）
                additionalParameters  // 其他附加参数（包含mobile和code）
        );
    }


}
