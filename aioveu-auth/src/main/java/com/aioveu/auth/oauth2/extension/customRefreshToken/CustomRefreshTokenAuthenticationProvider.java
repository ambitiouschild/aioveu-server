package com.aioveu.auth.oauth2.extension.customRefreshToken;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.lang.Assert;
import com.aioveu.auth.config.WxMiniAppConfig;
import com.aioveu.auth.model.MemberDetails;
import com.aioveu.auth.oauth2.extension.wechat.WechatAuthenticationToken;
import com.aioveu.auth.service.MemberDetailsService;
import com.aioveu.auth.util.OAuth2AuthenticationProviderUtils;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
 * @Description: TODO 自定义刷新令牌授权认证 Provider
 *                           支持刷新令牌的认证和新的访问令牌生成
 *                            主要功能：
 *                              1. 验证刷新令牌的有效性
 *                              2. 验证客户端身份
 *                              3. 生成新的访问令牌
 *                              4. 可选生成新的刷新令牌
 *                              5. 更新授权信息
 *                            自定义特性：
 *                              1. 添加刷新令牌使用日志
 *                              2. 支持刷新令牌的重新使用策略
 *                              3. 支持自定义令牌生成
 * @Author: 雒世松
 * @Date: 2026/4/6 17:49
 * @param
 * @return:
 **/

@Slf4j
public class CustomRefreshTokenAuthenticationProvider implements AuthenticationProvider {

    /**
     * OAuth2错误文档URI（RFC 6749标准文档）
     */
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    // 依赖注入的组件
    private final OAuth2AuthorizationService authorizationService;   // OAuth2授权服务，用于保存授权信息
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;  // OAuth2令牌生成器

    private final boolean reuseRefreshTokens; // 是否重用刷新令牌


    @Autowired
    private WxMiniAppConfig wxMiniAppConfig;

    // 微信小程序服务，用于调用微信API

    public CustomRefreshTokenAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this(authorizationService, tokenGenerator, false);
    }

    /**
     *  TODO 构造函数，依赖注入必要的服务组件
     *
     * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProviderNew} using the provided parameters.
     *
     * @param authorizationService the authorization service  授权服务，不能为null
     * @param tokenGenerator       the token generator  令牌生成器，不能为null
     */
    public CustomRefreshTokenAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
            boolean reuseRefreshTokens

    ) {

        log.info("【CustomRefreshTokenAuthenticationProvider】使用断言验证参数非空，确保自定义刷新令牌授权认证 Provider正确初始化");
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.reuseRefreshTokens = reuseRefreshTokens;
    }


    /**
     *       TODO       执行自定义刷新令牌认证流程
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

        log.info("=====【CustomRefreshTokenAuthenticationProvider】自定义刷新令牌认证=====");
        log.info("1. 类型转换和客户端认证验证");
        CustomRefreshTokenAuthenticationToken  refreshTokenAuthentication = (CustomRefreshTokenAuthenticationToken) authentication;

        log.info("验证客户端身份，如果客户端未认证则抛出异常");
        OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
                .getAuthenticatedClientElseThrowInvalidClient(refreshTokenAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // 验证客户端是否支持授权类型(grant_type=wechat_mini_app)
        log.info("2. 验证客户端是否支持刷新令牌授权类型");
        log.info("客户端验证：确保只有注册的客户端可以使用刷新令牌认证流程");
        if (!registeredClient.getAuthorizationGrantTypes().contains(refreshTokenAuthentication.REFRESH_TOKEN)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        // 微信 code 获取 openid
        log.info("前端请求中的附加参数获取");
        Map<String, Object> additionalParameters = refreshTokenAuthentication.getAdditionalParameters();

        log.info("前端请求中的获取并验证刷新令牌");

        //如果 additionalParameters.get("code")返回的不是字符串，这个强制转换就会出问题。
        String refreshTokenValue  = (String) additionalParameters.get(OAuth2ParameterNames.REFRESH_TOKEN);

        //------------------------------------------------
        // 在获取 code 之后，获取 clientId
        // 5. ★ 关键修改：获取 clientId
        String clientId = registeredClient.getClientId();  // OAuth2 客户端ID

        log.info("处理刷新令牌请求, 客户端: {}, 刷新令牌: {}...",
                clientId,
                refreshTokenValue.substring(0, Math.min(10, refreshTokenValue.length())));

        OAuth2Authorization authorization = this.authorizationService.findByToken(
                refreshTokenValue, OAuth2TokenType.REFRESH_TOKEN);


        if (authorization == null) {
            log.info("刷新令牌不存在或已失效");
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        // 4. 验证刷新令牌是否过期
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getRefreshToken();
        if (refreshToken == null || !refreshToken.isActive()) {
            log.info("刷新令牌已过期");
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        // 5. 验证刷新令牌是否被撤销
        if (authorization.getAccessToken().isInvalidated()) {
            log.info("刷新令牌已被撤销");
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        // 6. 获取用户认证对象（Authentication），不是 Principal
        Authentication userAuthentication = authorization.getAttribute(Principal.class.getName());
        if (userAuthentication == null) {
            log.info("授权信息中缺少用户主体");
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_GRANT);
        }

        // 7. 记录刷新令牌使用日志
        log.info("刷新令牌验证通过, 用户: {}, 客户端: {}",
                authorization.getPrincipalName(), registeredClient.getClientId());

        // 访问令牌(Access Token) 构造器
        log.info("6. 构建令牌上下文，准备生成新的访问令牌");
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)   // 注册的客户端信息
                .principal(userAuthentication)  // 用户主体信息// ✅ 使用 Authentication 对象，不是 Principal
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())  // 授权服务器上下文
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)   // 授权类型：使用标准的刷新令牌授权类型
                .authorizationGrant(refreshTokenAuthentication); // 授权请求信息

        // 生成访问令牌(Access Token)
        log.info("7. 生成访问令牌(Access Token)");
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);


        if (generatedAccessToken == null) {
            log.info("令牌生成失败处理");
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }


        OAuth2AccessToken newAccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,  // Bearer令牌类型
                generatedAccessToken.getTokenValue(),   // 令牌值
                generatedAccessToken.getIssuedAt(),  // 颁发时间
                generatedAccessToken.getExpiresAt(),   // 过期时间
                tokenContext.getAuthorizedScopes());  // 授权范围

        log.info("8. 构建新的标准的OAuth2访问令牌:{}, 过期时间: {}", newAccessToken , newAccessToken.getExpiresAt());



        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(authorization.getPrincipalName())  // ✅ 从原有授权信息中获取主体名称
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)  // 授权类型 ✅ 使用标准的刷新令牌授权类型
                .attribute(Principal.class.getName(), userAuthentication);  // 主体属性   // ✅ 这里可以用 Principal

        log.info("9. 更新授权信息:{}", authorizationBuilder);

        log.info("10. 处理令牌声明（如果令牌支持声明）");
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(newAccessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(newAccessToken);
        }


        OAuth2RefreshToken newRefreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                // 不为公共客户端生成刷新令牌（公共客户端使用NONE认证方式）
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);

            // 生成刷新令牌(Refresh token)
            log.info("11. 生成新的刷新令牌（如果不重用）(Refresh Token) - 条件性生成");

            log.info("验证生成的刷新令牌类型");
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            newRefreshToken  = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(newRefreshToken);
            log.info("生成新的刷新令牌");
        }else {
            log.info("重用原有的刷新令牌");
        }


        OAuth2Authorization updatedAuthorization = authorizationBuilder.build();
        this.authorizationService.save(updatedAuthorization);
        log.info("12. 保存授权信息到数据库:{}", updatedAuthorization);


        log.info("===== 自定义刷新令牌认证完成 =====");


        log.info("13. 返回最终的访问令牌认证结果");
        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient,   // 注册的客户端
                clientPrincipal,   // 客户端认证信息
                newAccessToken,   // 访问令牌
                newRefreshToken,   // 刷新令牌（可能为null）
                additionalParameters);  // 附加参数
    }


    private OAuth2ClientAuthenticationToken getAuthenticatedClient(
            CustomRefreshTokenAuthenticationToken authentication) {

        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(
                authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }


    /**
     * TODO  判断该Provider是否支持指定的认证令牌类型
     *
     * @param authentication 认证类型的Class对象
     * @return 如果支持WechatAuthenticationToken类型返回true，否则返回false
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return CustomRefreshTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
