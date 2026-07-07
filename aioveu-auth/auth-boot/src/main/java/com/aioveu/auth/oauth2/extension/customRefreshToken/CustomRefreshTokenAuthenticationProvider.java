package com.aioveu.auth.oauth2.extension.customRefreshToken;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.lang.Assert;
import com.aioveu.auth.config.WxMiniAppConfig;
import com.aioveu.auth.model.MemberDetails;
import com.aioveu.auth.oauth2.extension.wechat.WechatAuthenticationToken;
import com.aioveu.auth.service.MemberDetailsService;
import com.aioveu.auth.util.OAuth2AuthenticationProviderUtils;
import com.aioveu.common.constant.JwtClaimConstants;
import com.aioveu.common.constant.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
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
import java.util.HashMap;
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
/**
 * 自定义刷新令牌授权 Provider（无手写构造函数版）
 */
@Slf4j
@RequiredArgsConstructor   // Lombok注解，自动注入final字段的依赖
public class CustomRefreshTokenAuthenticationProvider implements AuthenticationProvider {

    /**
     * OAuth2错误文档URI（RFC 6749标准文档）
     */
    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    // ==================== 依赖注入 ====================
    private final OAuth2AuthorizationService authorizationService;   // OAuth2授权服务，用于保存授权信息
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;  // OAuth2令牌生成器
    private final MemberDetailsService memberDetailsService;  // 会员详情服务，用于加载用户信息
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 是否重用刷新令牌（配置驱动，默认 false）
     * reuseRefreshTokens的正确使用位置只有一个：
     * 👉 决定是否把「旧的 RefreshToken」保存到新的 OAuth2Authorization里
     * ✅ false= 每次刷新都生成新 refresh_token（推荐，安全） false = 每次刷新都生成新的 refresh_token（✅ 推荐）
     * ❌ true= 复用旧的 refresh_token（不安全，不推荐）
     */
    @Value("${custom.oauth2.reuse-refresh-tokens:false}")
    private final boolean reuseRefreshTokens;


    /**
     *  TODO 构造函数，依赖注入必要的服务组件
     *
     * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProviderNew} using the provided parameters.
     *
     * @param authorizationService the authorization service  授权服务，不能为null
     * @param tokenGenerator       the token generator  令牌生成器，不能为null
     */
//    public CustomRefreshTokenAuthenticationProvider(
//            OAuth2AuthorizationService authorizationService,
//            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
//            boolean reuseRefreshTokens
//
//    ) {
//
//        log.info("【CustomRefreshTokenAuthenticationProvider】使用断言验证参数非空，确保自定义刷新令牌授权认证 Provider正确初始化");
//        Assert.notNull(authorizationService, "authorizationService cannot be null");
//        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
//        this.authorizationService = authorizationService;
//        this.tokenGenerator = tokenGenerator;
//        this.reuseRefreshTokens = reuseRefreshTokens;
//    }


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

        CustomRefreshTokenAuthenticationToken  refreshTokenAuthentication =
                (CustomRefreshTokenAuthenticationToken) authentication;
        log.info("1. 类型转换和客户端认证验证");


        OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
                .getAuthenticatedClientElseThrowInvalidClient(refreshTokenAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        log.info("验证客户端身份，如果客户端未认证则抛出异常");

        if (!registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);


        }
        // 验证客户端是否支持授权类型(grant_type=wechat_mini_app)
        log.info("2. 验证客户端是否支持刷新令牌授权类型");
        log.info("客户端验证：确保只有注册的客户端可以使用刷新令牌认证流程");


        Map<String, Object> additionalParameters = refreshTokenAuthentication.getAdditionalParameters();
        //如果 additionalParameters.get("code")返回的不是字符串，这个强制转换就会出问题。
        String refreshTokenValue  = (String) additionalParameters.get(OAuth2ParameterNames.REFRESH_TOKEN);
        log.info("前端请求中的附加参数获取,前端请求中的获取并验证刷新令牌refreshTokenValue:{}",refreshTokenValue);
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
        //authorization.getAttribute()返回的是 String（principalName），不是 Authentication。
//        Authentication userAuthentication = authorization.getAttribute(Principal.class.getName());
        String openId = authorization.getPrincipalName();

        //刷新令牌阶段，tenantId 怎么拿？✅（重点）

        //✅ 方案 1（最推荐）：从 Authorization 里拿 .attribute("tenant_id", tenantId) // ✅
        //✅ 方案 2：从 JWT 里拿（资源服务器用） claims.put("tenant_id", tenantId);
        //✅ 方案 3：从 clientId 再查一次（不推荐）

        Long tenantId = authorization.getAttribute(JwtClaimConstants.Tenant.ID);
        log.info("获取tenantId: {}", tenantId);
        // 7. 重新加载用户
        MemberDetails memberDetails =
                memberDetailsService.loadMemberByOpenIdAndTenantId(openId,tenantId);

        // 8. 构建新的 Authentication
        UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(
                        memberDetails,
                        null,
                        memberDetails.getAuthorities()
                );

        // ✅ token_version 校验（强烈推荐）
        Long memberId = memberDetails.getId();
        if (memberId != null) {
            String versionKey = RedisConstants.Auth.USER_TOKEN_VERSION + memberId;

            // ✅ 只读取，不 increment ✅ 这是唯一正确的刷新令牌校验方式
            Long tokenVersion = (Long) redisTemplate.opsForValue().get(versionKey);
            if (tokenVersion == null) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error(OAuth2ErrorCodes.INVALID_GRANT, "令牌已失效", ERROR_URI)
                );
            }

            Map<String, Object> details = new HashMap<>();
            details.put(JwtClaimConstants.Token.VERSION, tokenVersion);
            newAuthentication.setDetails(details);
        }

        // 7. 记录刷新令牌使用日志
        log.info("刷新令牌验证通过, 用户openId: {}, 客户端clientId: {}",
                openId, registeredClient.getClientId());

        // 访问令牌(Access Token) 构造器
        log.info("6. 构建令牌上下文，准备生成新的访问令牌");
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)   // 注册的客户端信息
                .principal(newAuthentication)  // 用户主体信息// ✅ 使用 Authentication 对象，newAuthentication不是 Principal
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())  // 授权服务器上下文
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)   // 授权类型：使用标准的刷新令牌授权类型
                .authorizationGrant(refreshTokenAuthentication); // 授权请求信息


        OAuth2TokenContext accessTokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(accessTokenContext);
        // 生成访问令牌(Access Token)
        log.info("7. 生成访问令牌(Access Token)generatedAccessToken:{}",generatedAccessToken);

        if (generatedAccessToken == null) {
            log.info("令牌生成失败处理");
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.生成Access Token失败", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }


        OAuth2AccessToken newAccessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,  // Bearer令牌类型
                generatedAccessToken.getTokenValue(),   // 令牌值
                generatedAccessToken.getIssuedAt(),  // 颁发时间
                generatedAccessToken.getExpiresAt(),   // 过期时间
                accessTokenContext.getAuthorizedScopes());  // 授权范围

        log.info("8. 构建新的标准的OAuth2访问令牌:{}, 过期时间: {}", newAccessToken , newAccessToken.getExpiresAt());


        // 保存 Authorization 时也用新的 Authentication
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(openId)  // ✅ 从原有授权信息中获取主体名称
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)  // 授权类型 ✅ 使用标准的刷新令牌授权类型
                .attribute(Principal.class.getName(), openId);  // 主体属性 // ✅attribute 里只存 principalName（String）

        log.info("9. 更新授权信息:{}", authorizationBuilder);

        log.info("10. 处理令牌声明（如果令牌支持声明）");
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(newAccessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(newAccessToken);
        }


        // ✅ Refresh Token（reuseRefreshTokens 真正生效点）
        OAuth2RefreshToken newRefreshToken = null;
        boolean supportRefreshToken =
                registeredClient.getAuthorizationGrantTypes()
                        .contains(AuthorizationGrantType.REFRESH_TOKEN)
                        &&       !clientPrincipal.getClientAuthenticationMethod()
                        .equals(ClientAuthenticationMethod.NONE);
                // Do not issue refresh token to public client
                // 不为公共客户端生成刷新令牌（公共客户端使用NONE认证方式）

        if (supportRefreshToken) {

            if(!reuseRefreshTokens){
                //✅ false= 每次刷新都生成新 refresh_token（推荐，安全） false = 每次刷新都生成新的 refresh_token（✅ 推荐）
                OAuth2TokenContext refreshTokenContext =
                        tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                OAuth2Token generatedRefreshToken =
                        this.tokenGenerator.generate(refreshTokenContext);

                // 生成刷新令牌(Refresh token)
                log.info("11. 生成新的刷新令牌（如果不重用）(Refresh Token) - 条件性生成");


                if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                            "The token generator failed to generate the refresh token.生成Refresh Token失败", ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }
                log.info("验证生成的刷新令牌类型");

                newRefreshToken  = (OAuth2RefreshToken) generatedRefreshToken;
                authorizationBuilder.refreshToken(newRefreshToken);
                log.info("生成新的刷新令牌:{}",newRefreshToken);

            }else{
                //true= 复用旧的 refresh_token（不安全，不推荐）
                newRefreshToken = refreshToken.getToken();
                authorizationBuilder.refreshToken(newRefreshToken);
                log.info("重用原有的刷新令牌");
            }



        }


        OAuth2Authorization updatedAuthorization = authorizationBuilder.build();
        this.authorizationService.save(updatedAuthorization);
        log.info("12. 保存授权信息到数据库:{}", updatedAuthorization);
        log.info("===== 自定义刷新令牌认证完成 =====");

        log.info("13. 返回最终的访问令牌认证结果");
        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient,   // 注册的客户端
                clientPrincipal,   // 客户端认证信息
                newAccessToken,   // 访问令牌  // ✅ 新的 Access Token（一定新）
                newRefreshToken,   // 刷新令牌（可能为null）  // ⚠️ 可能是新、可能是旧、可能是 null
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
