
package com.aioveu.auth.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.aioveu.auth.model.SysUserDetails;
import com.aioveu.auth.oauth2.extension.captcha.CaptchaAuthenticationConverter;
import com.aioveu.auth.oauth2.extension.captcha.CaptchaAuthenticationProvider;
import com.aioveu.auth.oauth2.extension.password.PasswordAuthenticationConverter;
import com.aioveu.auth.oauth2.extension.password.PasswordAuthenticationProvider;
import com.aioveu.auth.oauth2.extension.smscode.SmsCodeAuthenticationConverter;
import com.aioveu.auth.oauth2.extension.smscode.SmsCodeAuthenticationProvider;
import com.aioveu.auth.oauth2.extension.smscode.SmsCodeAuthenticationToken;
import com.aioveu.auth.oauth2.extension.wechat.WechatAuthenticationConverter;
import com.aioveu.auth.oauth2.extension.wechat.WechatAuthenticationProvider;
import com.aioveu.auth.oauth2.extension.wechat.WechatAuthenticationToken;
import com.aioveu.auth.oauth2.handler.MyAuthenticationFailureHandler;
import com.aioveu.auth.oauth2.handler.MyAuthenticationSuccessHandler;
import com.aioveu.auth.oauth2.jackson.SysUserMixin;
import com.aioveu.auth.oauth2.oidc.CustomOidcAuthenticationConverter;
import com.aioveu.auth.oauth2.oidc.CustomOidcAuthenticationProvider;
import com.aioveu.auth.oauth2.oidc.CustomOidcUserInfoService;
import com.aioveu.auth.service.MemberDetailsService;
import com.aioveu.common.constant.RedisConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * @Description: TODO 授权服务器配置
 *                   登录请求的完整流程
 *                          前端登录请求 (HTTP请求)
 *                              ↓
 *                          [网关层] Gateway全局过滤器（如CORS、日志等）
 *                              ↓
 *                          [网关层] TokenValidationGlobalFilter (你自定义的JWT黑名单检查) ← 对登录请求可能不生效
 *                              ↓
 *                          路由到认证服务的 /oauth2/token 端点  认证服务（auth）的 /oauth2/token 端点
 *                              ↓
 *                          [认证服务] ClientAuthenticationFilter (Spring Security OAuth2内置) （客户端认证）← 客户端认证在这里完成  认证内容 client_id + client_secret  验证哪个应用在请求令牌
 *                              ↓
 *                          [认证服务] OAuth2TokenEndpointFilter (Spring Security OAuth2内置)  （令牌端点处理）
 *                              ↓
 *                          [认证服务] 认证过滤器处理（如PasswordAuthenticationConverter） 前端提交的参数 密码模式参数解析器 - OAuth2密码模式认证转换器  (你的自定义转换器) ← 此时客户端已认证   验证用户身份是否合法
 *                              ↓
 *                          [认证服务] 认证提供者调用（如PasswordAuthenticationProvider）  密码模式身份验证提供者 (你的自定义提供者) ← 实际认证逻辑    验证用户身份是否合法
 *                              ↓
 *                          [认证服务] 认证服务调用用户服务（通过UserFeignClient）
 *                              ↓
 *                          [用户服务]（system）返回用户数据
 *                              ↓
 *                          [认证服务] 认证服务完成认证并生成令牌
 *                              ↓
 *                          返回JWT令牌给前端
 *
 *
 *
 * <a href="https://github.com/spring-projects/spring-authorization-server/blob/49b199c5b41b5f9279d9758fc2f5d24ed1fe4afa/samples/demo-authorizationserver/src/main/java/sample/config/AuthorizationServerConfig.java#L112">AuthorizationServerConfig</a>
 * @Author: 雒世松
 * @Date: 2025/6/5 17:40
 * @param
 * @return:
 **/

/* * 主要功能：
 * 1. 配置多种认证方式（密码、验证码、微信、短信）
 * 2. 管理JWT令牌的生成和验证
 * 3. 注册OAuth2客户端应用
 * 4. 配置授权端点安全
 *
 * 认证流程架构
 * HTTP请求 → 认证转换器 → 认证提供者 → 令牌生成器 → 响应处理器
 *
 * */

@Configuration   // 标记为配置类，包含Spring Bean定义
@RequiredArgsConstructor   // Lombok注解，自动注入final字段的依赖
@Slf4j  // SLF4J日志注解
public class AuthorizationServerConfig {


    // 微信小程序服务，用于微信登录认证
    private final WxMaService wxMaService;

    // 会员详情服务，用于加载用户信息
    private final MemberDetailsService memberDetailsService;

    // 自定义OIDC用户信息服务，用于OpenID Connect用户信息端点
    private final CustomOidcUserInfoService customOidcUserInfoService;

    // JWT令牌定制器，用于在JWT令牌中添加自定义声明
    private final OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;


    // Redis模板，用于存储JWK密钥对等数据
    private final StringRedisTemplate redisTemplate;

    // 验证码生成器，用于图形验证码生成
    private final CodeGenerator codeGenerator;


    /**
     * 授权服务器端点配置
     *
     *      * 这是授权服务器的核心配置，定义令牌端点、认证流程等
     *      *
     *      * @Order(Ordered.HIGHEST_PRECEDENCE) 确保此过滤器链最先执行，优先级最高
     *      *
     *      * 流程：请求 → 认证转换器 → 认证提供者 → 令牌生成 → 响应处理
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)  // 最高优先级，确保授权服务器端点先于资源服务器处理
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,    // Spring Security认证管理器
            OAuth2AuthorizationService authorizationService,   // OAuth2授权服务
            OAuth2TokenGenerator<?> tokenGenerator            // 令牌生成器

    ) throws Exception {

        // 应用OAuth2授权服务器的默认安全配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);


        log.info("认证服务（auth）中的登录入口");

        //TODO  认证服务（auth）中的登录入口
        // 自定义授权服务器配置
        // 在 AuthorizationServerConfig 中配置的令牌端点
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // 自定义授权模式转换器(Converter)
                // 配置令牌端点（/oauth2/token）
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        // 配置认证请求转换器（将HTTP请求转换为认证令牌）   Converter职责单一：只负责参数提取和验证
                        .accessTokenRequestConverters(
                                authenticationConverters -> // <1>
                                        // 自定义授权模式转换器(Converter)
                                        // 添加四种自定义认证模式的请求转换器
                                        authenticationConverters.addAll(

                                                List.of(
                                                        new PasswordAuthenticationConverter(),  // 密码模式转换器  // 密码模式登录入口
                                                        new CaptchaAuthenticationConverter(),   // 图形验证码模式转换器  // 验证码登录入口
                                                        new WechatAuthenticationConverter(),   // 微信登录模式转换器   // 微信登录入口
                                                        new SmsCodeAuthenticationConverter()    // 短信验证码模式转换器   // 短信登录入口
                                                )
                                        )
                        )
                        // 配置认证提供者（执行实际的认证逻辑）  Provider职责单一：只负责业务认证逻辑
                        .authenticationProviders(
                                authenticationProviders -> // <2>
                                        // 自定义授权模式提供者(Provider)
                                        // 添加四种自定义认证模式的提供者
                                        authenticationProviders.addAll(
                                                List.of(
                                                        // 密码模式：使用用户名密码认证
                                                        new PasswordAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator),
                                                        // 验证码模式：验证图形验证码+密码
                                                        new CaptchaAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator, redisTemplate, codeGenerator),
                                                        // 微信模式：使用微信code认证
                                                        // 微信认证提供者使用WxMaService进行微信登录验证
                                                        new WechatAuthenticationProvider(authorizationService, tokenGenerator, memberDetailsService, wxMaService),
                                                        // 短信模式：使用手机号+短信验证码认证
                                                        new SmsCodeAuthenticationProvider(authorizationService, tokenGenerator, memberDetailsService, redisTemplate)
                                                )
                                        )
                        )
                        // 自定义认证成功响应处理器
                        .accessTokenResponseHandler(new MyAuthenticationSuccessHandler()) // 自定义成功响应
                        // 自定义认证失败响应处理器
                        .errorResponseHandler(new MyAuthenticationFailureHandler()) // 自定义失败响应
                )
                // Enable OpenID Connect 1.0 自定义
                .oidc(oidcCustomizer ->
                        oidcCustomizer.userInfoEndpoint(userInfoEndpointCustomizer ->
                                {
                                    // 自定义用户信息请求转换器
                                    userInfoEndpointCustomizer.userInfoRequestConverter(new CustomOidcAuthenticationConverter(customOidcUserInfoService));
                                    // 自定义用户信息认证提供者
                                    userInfoEndpointCustomizer.authenticationProvider(new CustomOidcAuthenticationProvider(authorizationService));
                                }
                        )
                );
        // 配置异常处理
        http
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                // 对于HTML请求，重定向到登录页面
                                new LoginUrlAuthenticationEntryPoint("/login"),  // 👈 这里就是重定向的配置
                                //这个配置的意思是当请求的Accept头包含text/html（即浏览器请求）,并且认证失败时,会重定向到/login页面
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )

                )
                // 配置OAuth2资源服务器（JWT验证）
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));  // 使用默认JWT配置
        return http.build();
    }


    /**
     * JWK（JSON Web Key）源配置
     * 用于生成和存储JWT签名所需的RSA密钥对
     *
     * 实现机制：优先从Redis获取现有密钥，不存在时生成新密钥并存储
     *
     * TODO 在你的认证服务中，已经有一个JWKSource<SecurityContext> jwkSource()Bean，
     *     它从Redis中获取JWKSet。同时，Spring Authorization Server会自动暴露/oauth2/jwks端点，
     *     该端点会调用jwkSource来获取当前的JWK Set。
     *
     * TODO    但是，如果由于某些原因，你希望从Redis中直接获取公钥（即资源服务器直接读Redis，而不通过认证服务的端点），
     *     那么你需要自定义一个JwtDecoder，直接从Redis中获取JWK Set。
     *     这种方式不太标准，因为资源服务器和认证服务共享了Redis，增加了耦合。通常建议通过端点暴露公钥。
     *
     * TODO  你的配置是标准的OAuth2授权服务器和资源服务器之间的公钥分发方式。
     *      认证服务负责生成和存储密钥对，并通过标准端点暴露公钥；其他微服务通过配置该端点来获取公钥，用于JWT验证。
     *       这种设计符合OAuth2和OIDC的标准，并且Spring Security OAuth2提供了自动配置，使得实现起来非常方便。
     *       JWT验证架构解析这意味着：
     *          1.认证服务将JWK（包含公钥）存储在Redis中
     *          2.认证服务通过/oauth2/jwks端点暴露公钥   Spring Authorization Server自动提供的端点
     *          3.其他微服务通过配置的jwk-set-uri获取公钥来验证JWT  // Spring Boot自动根据jwk-set-uri创建JwtDecoder\
     *          // 不需要手动配置JwtDecoder Bean
     *
     * @return JWKSource JWT密钥源
     */
    @Bean // <5>
    @SneakyThrows   // Lombok注解，自动处理受检异常
    public JWKSource<SecurityContext> jwkSource() {

        // 尝试从Redis中获取JWKSet(JWT密钥对，包含非对称加密的公钥和私钥)
        String jwkSetStr = redisTemplate.opsForValue().get(RedisConstants.JWK_SET_KEY);
        if (StrUtil.isNotBlank(jwkSetStr)) {
            // 如果存在，解析JWKSet并返回
            // 如果Redis中存在JWKSet，直接解析并使用
            JWKSet jwkSet = JWKSet.parse(jwkSetStr);
            log.info("从Redis加载现有JWKSet");

            return new ImmutableJWKSet<>(jwkSet);
        } else {
            // 如果Redis中不存在JWKSet，生成新的JWKSet
            log.info("生成新的RSA密钥对并存储到Redis");

            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            // 构建RSAKey
            // 构建RSA密钥对象
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)   // 设置私钥（用于签名）
                    .keyID(UUID.randomUUID().toString())  // 设置密钥ID（唯一标识）
                    .build();

            // 构建JWKSet
            // 创建JWKSet（包含一个或多个JWK）
            JWKSet jwkSet = new JWKSet(rsaKey);

            // 将JWKSet存储在Redis中
            // 将JWKSet持久化到Redis，避免服务器重启后密钥丢失
            redisTemplate.opsForValue().set(RedisConstants.JWK_SET_KEY, jwkSet.toString(Boolean.FALSE));
            return new ImmutableJWKSet<>(jwkSet);
        }

    }

    /**
     * 生成RSA密钥对（2048位）
     * 用于JWT令牌的签名和验证
     *
     * @return KeyPair RSA密钥对（公钥+私钥）
     */
    private static KeyPair generateRsaKey() { // <6>
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);  // 使用2048位密钥长度，平衡安全性和性能
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }


    /**
     * JWT解码器
     * 用于资源服务器验证JWT令牌的签名和有效性
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 授权服务器设置
     * 配置授权服务器的基本参数，如签发者地址等
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();   // 使用默认配置
    }

    /**
     * 密码编码器
     * 使用Spring Security的委托密码编码器，支持多种编码格式
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    /**
     * 注册客户端仓库
     * 管理OAuth2客户端应用注册信息（基于数据库存储）
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {

        // 创建基于JDBC的客户端仓库
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

        // 初始化 OAuth2 客户端
        // 初始化系统所需的客户端应用
        initMallAppClient(registeredClientRepository);   // 商城APP客户端
        initMallAdminClient(registeredClientRepository);  // 管理后台客户端

        return registeredClientRepository;
    }


    /**
     * OAuth2授权服务
     * 管理OAuth2授权的存储和检索（基于数据库）
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
                                                           RegisteredClientRepository registeredClientRepository) {

        // 创建基于JDBC的OAuth2授权服务。这个服务使用JdbcTemplate和客户端仓库来存储和检索OAuth2授权数据。
        // 创建基于JDBC的OAuth2授权服务
        JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);

        // 创建并配置用于处理数据库中OAuth2授权数据的行映射器。
        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
        rowMapper.setLobHandler(new DefaultLobHandler());  // 设置大对象处理器

        // 配置Jackson ObjectMapper用于序列化/反序列化
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);   // 注册Spring Security模块
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());  // 注册OAuth2授权服务器模块
        // You will need to write the Mixin for your class so Jackson can marshall it.

        // 添加自定义Mixin，用于序列化/反序列化特定的类。
        // Mixin类需要自行实现，以便Jackson可以处理这些类的序列化。
        // 注册自定义Mixin，解决特定类的序列化问题
        objectMapper.addMixIn(SysUserDetails.class, SysUserMixin.class);  // 用户详情Mixin
        objectMapper.addMixIn(Long.class, Object.class);  // Long类型Mixin

        // 将配置好的ObjectMapper设置到行映射器中。
        rowMapper.setObjectMapper(objectMapper);

        // 将自定义的行映射器设置到授权服务中。
        service.setAuthorizationRowMapper(rowMapper);

        return service;
    }


    /**
     * OAuth2授权同意服务
     * 管理用户对客户端的授权同意记录
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
                                                                         RegisteredClientRepository registeredClientRepository) {
        // Will be used by the ConsentController
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }



    /**
     * OAuth2令牌生成器
     * 组合JWT生成器、访问令牌生成器和刷新令牌生成器
     */
    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {

        // JWT生成器（使用NimbusJwtEncoder）
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer);   // 设置JWT定制器


        // 访问令牌生成器
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();

        // 刷新令牌生成器
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();

        // 委托令牌生成器（按顺序尝试不同的生成器）
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }


    /**
     * Spring Security认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 初始化商城管理后台客户端
     * 用于管理后台系统的OAuth2客户端注册
     */
    private void initMallAdminClient(JdbcRegisteredClientRepository registeredClientRepository) {

        String clientId = "aioveu-admin";
        String clientSecret = "123456";
        String clientName = "aioveu管理前端";

        /*
          如果使用明文，客户端认证时会自动升级加密方式，换句话说直接修改客户端密码，所以直接使用 bcrypt 加密避免不必要的麻烦
          官方ISSUE： https://github.com/spring-projects/spring-authorization-server/issues/1099
         */
        /*
         * 密码加密说明：
         * 如果使用明文，客户端认证时会自动升级加密方式，换句话说直接修改客户端密码
         * 所以直接使用bcrypt加密避免不必要的麻烦
         * 官方ISSUE：https://github.com/spring-projects/spring-authorization-server/issues/1099
         */

        // 检查客户端是否已存在，避免重复创建
        String encodeSecret = passwordEncoder().encode(clientSecret);

        RegisteredClient registeredMallAdminClient = registeredClientRepository.findByClientId(clientId);
        String id = registeredMallAdminClient != null ? registeredMallAdminClient.getId() : UUID.randomUUID().toString();


        // 构建客户端注册信息
        RegisteredClient mallAppClient = RegisteredClient.withId(id)
                .clientId(clientId)   // 客户端ID
                .clientSecret(encodeSecret)   // 客户端密钥（已加密）
                .clientName(clientName)    // 客户端名称
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)    // 客户端认证方式
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)  // 授权码模式
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)   // 刷新令牌模式
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)   // 客户端凭证模式
                .authorizationGrantType(AuthorizationGrantType.PASSWORD) // 密码模式
                .redirectUri("http://127.0.0.1:8080/authorized")   // 重定向URI
                .postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")   // 登出重定向URI
                .scope(OidcScopes.OPENID)   // OpenID Connect范围
                .scope(OidcScopes.PROFILE)   // 用户档案范围
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build()) // 令牌设置（1天有效期）
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())  // 客户端设置（需要授权同意）
                .build();
        registeredClientRepository.save(mallAppClient);
    }

    /**
     * 初始化商城APP客户端
     * 用于移动端APP的OAuth2客户端注册，支持微信小程序和短信登录
     */
    private void initMallAppClient(JdbcRegisteredClientRepository registeredClientRepository) {

        String clientId = "mall-app";
        String clientSecret = "123456";
        String clientName = "商城APP客户端";

        // 如果使用明文，在客户端认证的时候会自动升级加密方式，直接使用 bcrypt 加密避免不必要的麻烦
        // 加密客户端密钥
        String encodeSecret = passwordEncoder().encode(clientSecret);

        // 检查客户端是否已存在
        RegisteredClient registeredMallAppClient = registeredClientRepository.findByClientId(clientId);
        String id = registeredMallAppClient != null ? registeredMallAppClient.getId() : UUID.randomUUID().toString();


        // 构建APP客户端注册信息
        RegisteredClient mallAppClient = RegisteredClient.withId(id)
                .clientId(clientId)
                .clientSecret(encodeSecret)
                .clientName(clientName)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(WechatAuthenticationToken.WECHAT_MINI_APP)  // 微信小程序模式（自定义授权类型）
                .authorizationGrantType(SmsCodeAuthenticationToken.SMS_CODE) // 短信验证码模式（自定义授权类型）
                .redirectUri("http://127.0.0.1:8080/authorized")
                .postLogoutRedirectUri("http://127.0.0.1:8080/logged-out")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1)).build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        registeredClientRepository.save(mallAppClient);
    }

}
