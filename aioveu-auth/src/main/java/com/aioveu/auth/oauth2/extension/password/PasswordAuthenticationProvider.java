package com.aioveu.auth.oauth2.extension.password;


import cn.hutool.core.lang.Assert;
import com.aioveu.auth.util.OAuth2AuthenticationProviderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
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
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: TODO 密码模式身份验证提供者 - OAuth2密码模式认证核心逻辑实现  Provider职责单一：只负责业务认证逻辑
 *                      处理基于用户名和密码的身份验证
 *                       * 核心功能：处理基于用户名和密码的OAuth2密码模式认证流程
 *                       * 职责：验证用户凭证、生成访问令牌、刷新令牌、ID令牌，并持久化授权记录
 *                       * 认证流程：
 *                       * 1. 验证客户端支持密码模式
 *                       * 2. 提取用户名密码进行身份验证
 *                       * 3. 验证请求的权限范围(scope)
 *                       * 4. 生成访问令牌、刷新令牌和ID令牌
 *                       * 5. 持久化授权记录到数据库
 * @Author: 雒世松
 * @Date: 2025/6/5 17:47
 * @param
 * @return:
 **/

@Slf4j
public class PasswordAuthenticationProvider implements AuthenticationProvider {


    // OAuth2错误响应URI（遵循RFC 6749标准）
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    // OpenID Connect ID令牌的令牌类型定义
    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

    // Spring Security认证管理器，用于执行实际的用户名密码验证
    private final AuthenticationManager authenticationManager;

    // OAuth2授权服务，用于持久化授权记录到数据库
    private final OAuth2AuthorizationService authorizationService;

    // OAuth2令牌生成器，用于生成访问令牌、刷新令牌等
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    /**
     * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProviderNew} using the provided parameters.
     * 构造函数：依赖注入所需的组件
     * @param authenticationManager the authentication manager  认证管理器，用于用户名密码验证
     * @param authorizationService  the authorization service  授权服务，用于持久化授权记录
     * @param tokenGenerator        the token generator   令牌生成器，用于生成各种OAuth2令牌
     * @since 0.2.3
     */
    public PasswordAuthenticationProvider(AuthenticationManager authenticationManager,
                                          OAuth2AuthorizationService authorizationService,
                                          OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator
    ) {

        // 参数非空校验，确保依赖组件正确注入
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }


    /**
     * TODO 核心认证方法：处理密码模式认证请求
     *          这是Spring Security AuthenticationProvider接口的核心方法，包含完整的认证流程
     *
     * @param authentication 认证请求对象，包含客户端和用户认证信息
     * @return Authentication 认证结果，包含生成的令牌信息
     * @throws AuthenticationException 认证过程中发生的任何异常
     *
     * TODO 方法执行流程：
     *          1. 客户端验证 → 2. 用户认证 → 3. 权限范围验证 → 4. 令牌生成 → 5. 持久化记录
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 步骤1: 类型转换和客户端认证验证
        // 将通用的Authentication对象转换为密码模式特定的令牌类型
        log.info("=====密码模式身份验证提供者=====");
        log.info("步骤1: 类型转换和客户端认证验证");
        log.info("将通用的Authentication对象转换为密码模式特定的令牌类型");
        PasswordAuthenticationToken passwordAuthenticationToken = (PasswordAuthenticationToken) authentication;

        // 获取已认证的客户端主体，如果客户端未认证则抛出异常
        log.info("获取已认证的客户端主体，如果客户端未认证则抛出异常");
        OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
                .getAuthenticatedClientElseThrowInvalidClient(passwordAuthenticationToken);


        // 从客户端主体获取注册的客户端信息
        log.info("从客户端主体获取注册的客户端信息");
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();


        // 步骤2: 验证客户端是否支持密码授权模式
        // 检查客户端的授权类型列表是否包含PASSWORD模式
        // 验证客户端是否支持授权类型(grant_type=password)
        log.info("步骤2: 验证客户端是否支持密码授权模式");
        log.info("检查客户端的授权类型列表是否包含PASSWORD模式");
        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.PASSWORD)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        // 步骤3: 提取认证参数并进行用户名密码认证
        // 从认证令牌中获取附加参数（包含用户名和密码）
        log.info("步骤3: 提取认证参数并进行用户名密码认证");
        log.info("从认证令牌中获取附加参数（包含用户名和密码）");
        Map<String, Object> additionalParameters = passwordAuthenticationToken.getAdditionalParameters();


        // 生成用户名密码身份验证令牌
        // 从参数中提取用户名和密码
        log.info("从参数中提取用户名和密码");
        String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
        String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);

        // 创建Spring Security标准的用户名密码认证令牌
        log.info("创建Spring Security标准的用户名密码认证令牌");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // 用户名密码身份验证，成功后返回带有权限的认证信息

        // 使用AuthenticationManager执行实际的用户名密码认证
        // 这里会委托给SysUserDetailsService.loadUserByUsername进行用户验证
        log.info("使用AuthenticationManager执行实际的用户名密码认证");
        log.info("=====调用用户服务=====");
        log.info("这里会委托给SysUserDetailsService.loadUserByUsername进行用户验证");
        Authentication usernamePasswordAuthentication;
        try {
            usernamePasswordAuthentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            // 需要将其他类型的异常转换为 OAuth2AuthenticationException
            // 才能被自定义异常捕获处理，逻辑源码 OAuth2TokenEndpointFilter#doFilterInternal
            // 这样可以被OAuth2TokenEndpointFilter统一处理并返回标准错误响应
            throw new OAuth2AuthenticationException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }

        // 验证申请访问范围(Scope)
        // 步骤4: 验证和确定权限范围(Scope)
        // 获取客户端允许的权限范围和用户请求的权限范围
        log.info("步骤4: 验证和确定权限范围(Scope)");
        log.info("获取客户端允许的权限范围和用户请求的权限范围");
        Set<String> authorizedScopes = registeredClient.getScopes();
        Set<String> requestedScopes = passwordAuthenticationToken.getScopes();

        // 如果用户请求了特定的权限范围，进行验证
        if (!CollectionUtils.isEmpty(requestedScopes)) {

            // 检查请求的权限范围是否都在客户端允许的范围内
            Set<String> unauthorizedScopes = requestedScopes.stream()
                    .filter(requestedScope -> !registeredClient.getScopes().contains(requestedScope))
                    .collect(Collectors.toSet());

            // 如果存在未授权的范围，抛出异常
            if (!CollectionUtils.isEmpty(unauthorizedScopes)) {
                throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
            }

            // 使用用户请求的范围作为最终授权范围
            log.info("使用用户请求的范围作为最终授权范围");
            authorizedScopes = new LinkedHashSet<>(requestedScopes);
        }

        // 访问令牌(Access Token) 构造器
        // 步骤5: 构建令牌生成上下文 - 访问令牌
        // 创建令牌生成器的构建器，设置各种生成参数
        log.info("步骤5: 构建令牌生成上下文 - 访问令牌");
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)     // 注册的客户端信息
                .principal(usernamePasswordAuthentication) // 身份验证成功的认证信息(用户名、权限等信息)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())  // 授权服务器上下文
                .authorizedScopes(authorizedScopes)    // 授权的权限范围
                .authorizationGrantType(AuthorizationGrantType.PASSWORD) // 授权类型为密码模式
                .authorizationGrant(passwordAuthenticationToken)   // 授权授予对象
                ;

        // 生成访问令牌(Access Token)
        log.info("生成访问令牌(Access Token)");
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType((OAuth2TokenType.ACCESS_TOKEN)).build();

        // 检查令牌生成是否成功
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        // 构建标准的OAuth2访问令牌对象
        log.info("构建标准的OAuth2访问令牌对象");
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());


        // 步骤6: 构建授权记录并准备持久化
        // 创建授权记录构建器，设置授权相关信息
        log.info("步骤6: 构建授权记录并准备持久化");
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())    // 主体名称（用户名）
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)    // 授权类型
                .authorizedScopes(authorizedScopes)                         // 授权范围
                .attribute(Principal.class.getName(), usernamePasswordAuthentication); // attribute 字段  // 用户认证信息属性

        // 如果生成的访问令牌包含声明信息，特殊处理
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // 生成刷新令牌(Refresh Token)
        // 步骤7: 生成刷新令牌(Refresh Token) - 如果客户端支持

        log.info("步骤7: 生成刷新令牌(Refresh Token) - 如果客户端支持");
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                // 不向公共客户端颁发刷新令牌（无客户端认证的客户端）
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            // 构建刷新令牌生成上下文
            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);

            // 验证生成的刷新令牌类型是否正确
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            log.info("将刷新令牌添加到授权记录");
            authorizationBuilder.refreshToken(refreshToken);   // 将刷新令牌添加到授权记录
        }

        // ----- ID token -----
        // 步骤8: 生成OpenID Connect ID令牌 - 如果请求了openid scope
        log.info("步骤8: 生成OpenID Connect ID令牌 - 如果请求了openid scope");
        OidcIdToken idToken;
        if (requestedScopes.contains(OidcScopes.OPENID)) {
            // @formatter:off
            // 构建ID令牌生成上下文
            tokenContext = tokenContextBuilder
                    .tokenType(ID_TOKEN_TOKEN_TYPE)
                    .authorization(authorizationBuilder.build())	// ID token customizer may need access to the access token and/or refresh token
                    .build();
            // @formatter:on
            OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);

            // 验证生成的ID令牌类型是否正确（必须是JWT）
            if (!(generatedIdToken instanceof Jwt)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the ID token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            if (log.isTraceEnabled()) {
                log.trace("Generated id token");
            }

            // 构建标准的OIDC ID令牌对象
            idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                    generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
            authorizationBuilder.token(idToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
        } else {
            idToken = null;
        }

        // 步骤9: 持久化授权记录到数据库
        log.info("步骤9: 持久化授权记录到数据库");
        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        // 步骤10: 准备响应参数（包含ID令牌等）
        log.info("步骤10: 准备响应参数（包含ID令牌等）");
        additionalParameters = (idToken != null)
                ? Collections.singletonMap(OidcParameterNames.ID_TOKEN, idToken.getTokenValue())
                : Collections.emptyMap();

        // 返回最终的认证结果，包含所有生成的令牌
        log.info("返回最终的认证结果，包含所有生成的令牌");
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal,
                accessToken, refreshToken, additionalParameters);
    }

    /**
     * 支持判断方法：确定当前Provider是否支持处理指定的认证类型
     * 判断传入的 authentication 类型是否与当前认证提供者(AuthenticationProvider)相匹配--模板方法
     * <p>
     * ProviderManager#authenticate 遍历 providers 找到支持对应认证请求的 provider-迭代器模式
     *  设计模式：模板方法模式 - Spring Security使用迭代器模式遍历Provider
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
