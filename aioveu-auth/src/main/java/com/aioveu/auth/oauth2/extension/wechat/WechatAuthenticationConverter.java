package com.aioveu.auth.oauth2.extension.wechat;

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
 * @Description: TODO 微信小程序认证参数解析器
 *                          解析请求参数中的微信小程序 Code，并构建相应的身份验证(Authentication)对象
 *                          实现Spring Security的AuthenticationConverter接口，用于自定义认证流程
 *                              核心功能详细说明：
 *                               1. 认证类型识别
 *                                  专有grant_type：只处理 grant_type=wechat_mini_app的请求
 *                                  责任链模式：如果不是目标类型返回null，由后续转换器处理
 *                               2. 参数验证逻辑
 *                                  必需参数：code参数必须存在且非空
 *                                  可选参数：scope参数可选，但有严格的格式验证
 *                                  错误处理：使用标准OAuth2错误码和错误文档链接
 *                               3. 参数处理策略
 *                                  客户端身份：从SecurityContext获取已认证的客户端信息
 *                                  Scope处理：将字符串格式的scope转换为Set集合
 *                                  附加参数：收集除标准参数外的所有参数，用于后续认证流程
 *                               4. OAuth2标准兼容
 *                                  错误码规范：使用 OAuth2ErrorCodes.INVALID_REQUEST等标准错误码
 *                                  参数命名：使用 OAuth2ParameterNames常量确保参数名一致性
 *                                  错误文档：提供详细的API文档链接便于调试
 *                               5. 微信小程序集成
 *                                  Code参数：专门处理微信小程序的临时登录凭证code
 *                                  API参考：错误信息链接到微信官方开发文档
 *                                  扩展性：通过附加参数支持微信小程序的其他认证需求
 *                                  这个转换器是OAuth2认证流程的z关键组件，负责将HTTP请求参数转换为Spring Security可识别的认证令牌，
 *                                  为后续的认证逻辑提供标准化的输入数据。
 * org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter
 * @Author: 雒世松
 * @Date: 2025/6/5 17:49
 * @param
 * @return:
 **/

@Slf4j
public class WechatAuthenticationConverter implements AuthenticationConverter {

    /**
     *    todo      微信小程序登录API错误文档地址
     *          用于在错误响应中提供详细的错误信息参考链接
     */
    public static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html";


    /**
     *   TODO           将HTTP请求转换为微信小程序认证令牌(Authentication)
     *              主要处理grant_type=wechat_mini_app的OAuth2认证请求
     *
     * @param request HTTP请求对象
     * @return 微信小程序认证令牌(WechatAuthenticationToken)，如果请求不是微信小程序认证类型则返回null
    //     * @throws 如果参数验证失败，会通过OAuth2EndpointUtils.throwError抛出OAuth2错误
     */
    @Override
    public Authentication convert(HttpServletRequest request) {
        // 授权类型 (必需)
        log.info("=====微信小程序认证参数解析器=====");
        log.info("1. 验证授权类型 - 只处理微信小程序认证类型，grant：授予");
        log.info("1. 从请求参数中获取grant_type参数");
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);

        log.info("如果grant_type不是微信小程序认证类型，返回null（由其他AuthenticationConverter处理）");
        if (!WechatAuthenticationToken.WECHAT_MINI_APP.getValue().equals(grantType)) {
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

        // 微信小程序 Code (必需)
        log.info("5. 验证必需的微信小程序Code参数");
        log.info("微信小程序前端通过wx.login()获取的临时登录凭证code");
        String code = parameters.getFirst(OAuth2ParameterNames.CODE);

        log.info("Code参数是必需的，不能为空");
        if (StrUtil.isBlank(code)) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,   // 错误码：无效请求
                    OAuth2ParameterNames.CODE,       // 错误参数名：code
                    ACCESS_TOKEN_REQUEST_ERROR_URI);   // 微信小程序API文档地址
        }
        // 附加参数(微信小程序 Code)
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

        log.info("7. 构建并返回微信小程序认证令牌");
        return new WechatAuthenticationToken(
                clientPrincipal,   // 客户端认证信息
                requestedScopes,   // 请求的权限范围（可能为null）
                additionalParameters   // 其他附加参数（包含微信code等）
        );
    }


}
