package com.aioveu.auth.aioveu04Oauth2RegisteredClient.utils;


import com.aioveu.auth.oauth2.extension.smscode.SmsCodeAuthenticationToken;
import com.aioveu.auth.oauth2.extension.wechat.WechatAuthenticationToken;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

/**
 * @ClassName: RegisteredClientInitializer
 * @Description TODO 「客户端初始化器」（✅ 正确位置）  它和“租户创建时初始化 client”是互斥设计
 *                      这 100% 是“手动初始化”，而且是硬编码的、一次性的、不感知租户的那种手动初始化。
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/5 13:45
 * @Version 1.0
 **/
@Component
@RequiredArgsConstructor
@Slf4j
public class RegisteredClientInitializer {

    private final JdbcRegisteredClientRepository registeredClientRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        initMallAppClient();
        initMallAdminClient();
    }

    /**
     * 初始化商城APP客户端
     * 用于移动端APP的OAuth2客户端注册，支持微信小程序和短信登录
     */
    private void initMallAppClient() {

        String clientId = "mall-app";
        String clientSecret = "123456";
        String clientName = "商城APP客户端";

        // 如果使用明文，在客户端认证的时候会自动升级加密方式，直接使用 bcrypt 加密避免不必要的麻烦
        // 加密客户端密钥
        String encodeSecret = passwordEncoder.encode(clientSecret);

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

    /**
     * 初始化商城管理后台客户端
     * 用于管理后台系统的OAuth2客户端注册
     */
    private void initMallAdminClient() {

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
        String encodeSecret = passwordEncoder.encode(clientSecret);

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
}
