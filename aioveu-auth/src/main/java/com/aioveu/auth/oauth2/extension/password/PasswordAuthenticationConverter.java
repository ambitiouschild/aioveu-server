package com.aioveu.auth.oauth2.extension.password;

import cn.hutool.core.util.StrUtil;
import com.aioveu.auth.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
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
 * @Description: TODO 密码模式参数解析器   - OAuth2密码模式认证转换器  Converter职责单一：只负责参数提取和验证
 *                  核心功能：将HTTP请求参数转换为Spring Security认证对象(Authentication)
 *                  参考实现：基于Spring Authorization Server的OAuth2AuthorizationCodeAuthenticationConverter设计模式
 *                  工作流程：
 *                      1. 检查授权类型是否为"password"
 *                      2. 验证必需参数（用户名、密码）是否存在
 *                      3. 解析可选参数（scope等）
 *                      4. 构建PasswordAuthenticationToken对象供后续认证使用
 *
 * org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter
 * @Author: 雒世松
 * @Date: 2025/6/5 17:47
 * @param
 * @return:
 **/

@Slf4j
public class PasswordAuthenticationConverter implements AuthenticationConverter {




    /**
     * TODO   将HTTP请求转换为认证对象的核心方法
     *        此方法在Spring Security过滤器链中被调用，用于处理密码模式认证请求
     *
     * @param request HTTP请求对象，包含认证所需的所有参数
     * @return Authentication 认证对象，如果请求不是密码模式则返回null
     *    TODO
     *              处理流程：
     *              1. 验证授权类型 → 2. 提取参数 → 3. 验证必需参数 → 4. 构建认证令牌
     *
     * 请求示例：
     * POST /oauth2/token?grant_type=password&username=admin&password=123456&scope=read write
     * Content-Type: application/x-www-form-urlencoded
     * Authorization: Basic {client_credentials}
     */
    @Override
    public Authentication convert(HttpServletRequest request) {

        // 授权类型 (必需)
        // 步骤1: 验证授权类型 - 只处理密码模式(grant_type=password)的请求
        log.info("=====密码模式参数解析器=====");
        log.info("步骤1: 验证授权类型 - 只处理密码模式(grant_type=password)的请求");
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        if (!AuthorizationGrantType.PASSWORD.getValue().equals(grantType)) {

            // 如果不是密码模式，返回null让其他认证转换器处理
            log.info("如果不是密码模式，返回null让其他认证转换器处理");
            return null;
        }

        // 客户端信息
        // 步骤2: 获取客户端认证信息
        // 客户端认证已在之前的过滤器中完成，这里从安全上下文中获取认证信息
        log.info("步骤2: 获取客户端认证信息");
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // 参数提取验证
        // 步骤3: 提取并解析--前端提交的登录--  所有请求参数
        log.info("步骤3: 提取并解析--前端提交的登录--所有请求参数");
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // 令牌申请访问范围验证 (可选)
        // 步骤4: 处理scope参数（可选参数）
        // scope表示请求的权限范围，如"read write"或"openid profile"
        log.info("步骤4: 处理scope参数（可选参数）");
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            // scope参数格式错误：必须为单个字符串，不能有多个同名参数
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.SCOPE,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // 解析scope字符串为Set集合，便于后续处理
        log.info("解析scope字符串为Set集合，便于后续处理");
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {

            // 将空格分隔的scope字符串转换为Set，如"read write" -> ["read", "write"]
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        // 用户名验证(必需)
        // 步骤5: 验证用户名参数（必需参数）
        log.info("步骤5: 验证用户名参数（必需参数）");
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (StrUtil.isBlank(username)) {
            // 用户名不能为空，否则抛出OAuth2错误
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.USERNAME,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI
            );
        }

        // 密码验证(必需)
        // 步骤6: 验证密码参数（必需参数）
        log.info("步骤6: 验证密码参数（必需参数）");
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (StrUtil.isBlank(password)) {
            // 密码不能为空，否则抛出OAuth2错误
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.PASSWORD,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI
            );
        }

        // 附加参数(保存用户名/密码传递给 PasswordAuthenticationProvider 用于身份认证)
        // 步骤7: 提取附加参数（排除已处理的grant_type和scope）
        // 这些参数将传递给认证提供者进行进一步处理
        log.info("步骤7: 提取附加参数（排除已处理的grant_type和scope）");
        Map<String, Object> additionalParameters = parameters
                .entrySet()
                .stream()
                .filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) && // 排除grant_type
                        !e.getKey().equals(OAuth2ParameterNames.SCOPE)  // 排除scope
                ).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));// 转换为单值Map


        // 步骤8: 构建密码模式认证令牌
        // 此令牌将被传递给PasswordAuthenticationProvider进行实际的身份验证
        log.info("步骤8: 构建密码模式认证令牌");
        log.info("此令牌将被传递给PasswordAuthenticationProvider进行实际的身份验证");
        return new PasswordAuthenticationToken(
                clientPrincipal,  // 客户端认证信息（已在前置过滤器验证）
                requestedScopes,  // 请求的权限范围（可能为null）
                additionalParameters  // 其他参数（包含username和password）
        );
    }


}
