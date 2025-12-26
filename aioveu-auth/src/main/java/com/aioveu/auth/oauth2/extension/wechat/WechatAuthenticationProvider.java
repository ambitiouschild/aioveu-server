package com.aioveu.auth.oauth2.extension.wechat;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.lang.Assert;
import com.aioveu.auth.service.MemberDetailsService;
import com.aioveu.auth.util.OAuth2AuthenticationProviderUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.util.Map;

/**
 * @Description: TODO 微信授权认证 Provider
 *                      实现Spring Security的AuthenticationProvider接口，处理微信小程序的OAuth2认证流程
 *                      负责验证微信code、获取用户信息、生成访问令牌和刷新令牌
 *                      核心功能详细说明：
 *                          1. 认证流程概述
 *                              多步骤验证：客户端验证 → 微信API调用 → 用户信息加载 → 令牌生成
 *                              异常处理：每个步骤都有完善的错误处理和日志记录
 *                              标准兼容：严格遵循OAuth2.0和Spring Security规范
 *                          2. 微信集成流程
 *                              Code验证：通过微信code2Session接口验证临时登录凭证
 *                              OpenID获取：从微信响应中提取用户唯一标识openid
 *                              错误处理：捕获微信API异常并转换为认证异常
 *                          3. 令牌生成策略
 *                              访问令牌：Bearer类型的JWT令牌，包含用户身份和权限信息
 *                              刷新令牌：条件性生成，不提供给公共客户端以增强安全性
 *                              声明支持：支持JWT声明，便于资源服务器验证
 *                          4. 安全考虑
 *                              客户端验证：确保只有注册的客户端可以使用微信认证流程
 *                              公共客户端限制：不为公共客户端生成刷新令牌
 *                              令牌存储：授权信息持久化到数据库，支持令牌撤销和验证
 *                          5. 扩展性设计
 *                              依赖注入：通过构造函数注入，支持不同的实现替换
 *                              参数验证：使用断言确保组件正确初始化
 *                              类型安全：支持方法确保只处理对应的认证令牌类型
 *                              这个Provider是OAuth2授权服务器的核心组件，将微信小程序的登录流程无缝集成到标准的OAuth2认证框架中，
 *                              为前端应用提供了安全、标准的认证解决方案
 * @Author: 雒世松
 * @Date: 2025/6/5 17:49
 * @param
 * @return:
 **/

@Slf4j
public class WechatAuthenticationProvider implements AuthenticationProvider {

    /**
     * OAuth2错误文档URI（RFC 6749标准文档）
     */
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    // 依赖注入的组件
    private final OAuth2AuthorizationService authorizationService;   // OAuth2授权服务，用于保存授权信息
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;  // OAuth2令牌生成器

    private final MemberDetailsService memberDetailsService;  // 会员详情服务，用于加载用户信息

    private final WxMaService wxMaService;   // 微信小程序服务，用于调用微信API


    /**
     *  TODO 构造函数，依赖注入必要的服务组件
     *
     * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProviderNew} using the provided parameters.
     *
     * @param authorizationService the authorization service  授权服务，不能为null
     * @param tokenGenerator       the token generator  令牌生成器，不能为null
     * @param memberDetailsService 会员详情服务，不能为null
     * @param wxMaService 微信小程序服务，不能为null
     */
    public WechatAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
            MemberDetailsService memberDetailsService,
            WxMaService wxMaService

    ) {

        log.info("使用断言验证参数非空，确保组件正确初始化");
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(memberDetailsService, "userDetailsService cannot be null");
        Assert.notNull(wxMaService, "wxMaService cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.memberDetailsService = memberDetailsService;
        this.wxMaService = wxMaService;
    }


    /**
     *       TODO       执行微信小程序认证流程
     *              1. 验证客户端身份和授权类型
     *              2. 通过微信code获取openid
     *              3. 根据openid加载用户信息
     *              4. 生成访问令牌和刷新令牌
     *              5. 保存授权信息并返回认证结果
     *
     * @param authentication 认证令牌，包含客户端信息和请求参数
     * @return 包含访问令牌的认证结果
     * @throws AuthenticationException 认证过程中出现的各种异常
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        log.info("=====微信授权认证 Provider=====");
        log.info("1. 类型转换和客户端认证验证");
        WechatAuthenticationToken wechatAuthenticationToken = (WechatAuthenticationToken) authentication;

        log.info("验证客户端身份，如果客户端未认证则抛出异常");
        OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
                .getAuthenticatedClientElseThrowInvalidClient(wechatAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // 验证客户端是否支持授权类型(grant_type=wechat_mini_app)
        log.info("2. 验证客户端是否支持微信小程序授权类型");
        log.info("客户端验证：确保只有注册的客户端可以使用微信认证流程");
        if (!registeredClient.getAuthorizationGrantTypes().contains(WechatAuthenticationToken.WECHAT_MINI_APP)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        // 微信 code 获取 openid
        log.info("前端请求中的微信小程序code验证和openid获取");
        Map<String, Object> additionalParameters = wechatAuthenticationToken.getAdditionalParameters();

        log.info("前端请求中的微信小程序code");
        String code = (String) additionalParameters.get(OAuth2ParameterNames.CODE);

        log.info("Code验证：通过微信code2Session接口验证临时登录凭证");
        WxMaJscode2SessionResult sessionInfo;
        try {

            log.info("调用微信API，通过code获取session信息（包含openid和session_key）");
            sessionInfo = wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            log.info("微信API调用失败，记录日志并抛出认证异常");
            log.error("微信小程序登录失败", e);
            throw new OAuth2AuthenticationException(e.getMessage());
        }

        log.info("OpenID获取：从微信响应中提取用户唯一标识openid");
        String openid = sessionInfo.getOpenid();

        // 根据 openid 获取会员信息
        log.info("4. 根据openid加载用户信息");
        UserDetails userDetails = memberDetailsService.loadUserByOpenid(openid);

        log.info("5. 构建用户名密码认证令牌（用于后续的令牌生成）");
        Authentication usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword());  // 密码用于认证验证

        // 访问令牌(Access Token) 构造器
        log.info("6. 构建令牌上下文，准备生成访问令牌");
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)   // 注册的客户端信息
                .principal(usernamePasswordAuthentication)  // 用户主体信息
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())  // 授权服务器上下文
                .authorizationGrantType(WechatAuthenticationToken.WECHAT_MINI_APP)   // 授权类型：微信小程序
                .authorizationGrant(wechatAuthenticationToken);  // 授权请求信息

        // 生成访问令牌(Access Token)
        log.info("7. 生成访问令牌(Access Token)");
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);

        log.info("令牌生成失败处理");
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        log.info("8. 构建标准的OAuth2访问令牌");
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,  // Bearer令牌类型
                generatedAccessToken.getTokenValue(),   // 令牌值
                generatedAccessToken.getIssuedAt(),  // 颁发时间
                generatedAccessToken.getExpiresAt(),   // 过期时间
                tokenContext.getAuthorizedScopes());  // 授权范围

        log.info("9. 构建授权信息");
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(userDetails.getUsername())  // 主体名称
                .authorizationGrantType(WechatAuthenticationToken.WECHAT_MINI_APP)  // 授权类型
                .attribute(Principal.class.getName(), usernamePasswordAuthentication);  // 主体属性

        log.info("10. 处理令牌声明（如果令牌支持声明）");
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // 生成刷新令牌(Refresh token)
        log.info("11. 生成刷新令牌(Refresh Token) - 条件性生成");
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                // 不为公共客户端生成刷新令牌（公共客户端使用NONE认证方式）
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);

            log.info("验证生成的刷新令牌类型");
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        log.info("12. 保存授权信息到数据库");
        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        log.info("13. 返回最终的访问令牌认证结果");
        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient,   // 注册的客户端
                clientPrincipal,   // 客户端认证信息
                accessToken,   // 访问令牌
                refreshToken,   // 刷新令牌（可能为null）
                additionalParameters);  // 附加参数
    }


    /**
     * TODO  判断该Provider是否支持指定的认证令牌类型
     *
     * @param authentication 认证类型的Class对象
     * @return 如果支持WechatAuthenticationToken类型返回true，否则返回false
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
