package com.aioveu.auth.oauth2.extension.customRefreshToken;

import cn.hutool.core.util.StrUtil;
import com.aioveu.auth.oauth2.extension.wechat.WechatAuthenticationToken;
import com.aioveu.auth.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: CustomRefreshTokenAuthenticationConverter
 * @Description TODO 创建自定义的刷新令牌转换器和提供者
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/6 16:52
 * @Version 1.0
 **/

@Slf4j
public class CustomRefreshTokenAuthenticationConverter implements AuthenticationConverter {

    /**
     *    todo      微信小程序登录API错误文档地址
     *          用于在错误响应中提供详细的错误信息参考链接
     */
    public static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html";


    /**
     *   TODO           将HTTP请求转换为刷新令牌转换器令牌(Authentication)
     *              主要处理grant_type=refresh_token的OAuth2认证请求
     *
     */
    @Override
    public Authentication convert(HttpServletRequest request) {
        // 授权类型 (必需)
        log.info("=====刷新令牌认证参数解析器=====");
        log.info("1. 验证授权类型 - 只处理微刷新令牌认证类型，grant：授予");
        log.info("1. 从请求参数中获取grant_type参数");
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        log.info("如果grant_type不是刷新令牌认证类型，返回null（由其他AuthenticationConverter处理）");
        if (!CustomRefreshTokenAuthenticationToken.REFRESH_TOKEN.getValue().equals(grantType)) {
            log.info("grant_type 不是 refresh_token: {}", grantType);
            return null;
        }

        // 客户端信息
        log.info("2. 获取客户端认证信息");
        log.info("从Spring Security上下文中获取当前已认证的客户端信息");
        log.info("这通常是在之前的过滤器链中通过客户端认证（如client_id和client_secret）设置的");
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // 参数提取验证
        log.info("3. 提取和解析请求参数");
        log.info("将请求参数转换为MultiValueMap，便于处理同一个参数名对应多个值的情况");
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);


        // 令牌申请访问范围验证 (可选)
        log.info("4. 处理scope参数（可选参数验证）");
        log.info("获取scope参数，scope表示请求的权限范围");
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);

        log.info("验证scope参数格式：如果有值，必须且只能出现一次");
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {

            log.info("scope参数格式错误时抛出OAuth2标准错误");
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,   // 错误码：无效请求
                    OAuth2ParameterNames.SCOPE,      // 错误参数名
                    ACCESS_TOKEN_REQUEST_ERROR_URI);  // 错误文档地址
        }

        log.info("将scope字符串转换为Set集合，便于后续处理");
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {

            log.info("将空格分隔的scope字符串拆分为Set集合");
            log.info("例如：\"read write\" -> [\"read\", \"write\"]");
            requestedScopes = new HashSet<>(Arrays.asList(
                    StringUtils.delimitedListToStringArray(scope, " ")));
        }

        // 刷新令牌"(必需)
        log.info("5. 验证必需刷新令牌");
        String refreshToken = parameters.getFirst(OAuth2ParameterNames.REFRESH_TOKEN);

        log.info("refreshToken参数是必需的，不能为空");
        if (StrUtil.isBlank(refreshToken)) {

            log.info("缺少 refresh_token 参数");

            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,   // 错误码：无效请求
                    OAuth2ParameterNames.REFRESH_TOKEN,       // 错误参数名：code
                    ACCESS_TOKEN_REQUEST_ERROR_URI);   // 微信小程序API文档地址
        }


        log.info("6. 收集附加参数");
        log.info("提取除grant_type和scope之外的所有其他参数，作为附加参数传递给认证令牌");
        Map<String, Object> additionalParameters = parameters
                .entrySet()
                .stream()
                // 过滤掉grant_type和scope参数，这两个参数已经单独处理
                .filter(e ->
                        !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE)
                                && !e.getKey().equals(OAuth2ParameterNames.SCOPE)
                )
                // 转换为Map，每个参数名对应第一个参数值（通常每个参数名只有一个值）
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().get(0)));

        log.info("转换刷新令牌请求, 客户端: {}, refresh_token: {}..., scope: {}",
                clientPrincipal != null ? ((OAuth2ClientAuthenticationToken) clientPrincipal).getRegisteredClient().getClientId() : "unknown",
                refreshToken.substring(0, Math.min(10, refreshToken.length())),
                scope);

        log.info("7. 构建并返回微信小程序认证令牌");
        return new CustomRefreshTokenAuthenticationToken(
                refreshToken,
                clientPrincipal,   // 客户端认证信息
                requestedScopes,   // 请求的权限范围（可能为null）
                additionalParameters   // 其他附加参数（包含微信code等）
        );
    }

}
