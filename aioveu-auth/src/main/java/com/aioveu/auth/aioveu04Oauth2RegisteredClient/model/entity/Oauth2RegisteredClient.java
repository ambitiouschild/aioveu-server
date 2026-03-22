package com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: Oauth2RegisteredClient
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:59
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("oauth2_registered_client")
public class Oauth2RegisteredClient extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 客户端标识符，OAuth2请求中使用的client_id
     */
    private String clientId;
    /**
     * 客户端ID的创建时间
     */
    private LocalDateTime clientIdIssuedAt;
    /**
     * 客户端密钥，已加密存储。公共客户端可为NULL
     */
    private String clientSecret;
    /**
     * 客户端密钥的过期时间，NULL表示永不过期
     */
    private LocalDateTime clientSecretExpiresAt;
    /**
     * 客户端显示名称，用于用户界面显示
     */
    private String clientName;
    /**
     * 支持的客户端认证方法，JSON格式数组。如["client_secret_basic","client_secret_post"]
     */
    private String clientAuthenticationMethods;
    /**
     * 支持的授权类型，JSON格式数组。如["authorization_code","refresh_token","password","client_credentials"]
     */
    private String authorizationGrantTypes;
    /**
     * 允许的重定向URI列表，JSON格式数组。必须与授权请求中的redirect_uri完全匹配
     */
    private String redirectUris;
    /**
     * 登出后的重定向URI列表，JSON格式数组
     */
    private String postLogoutRedirectUris;
    /**
     * 客户端可请求的范围列表，JSON格式数组。如["openid","profile","email"]
     */
    private String scopes;
    /**
     * 客户端设置，JSON格式。包含requireAuthorizationConsent、requireProofKey等配置
     */
    private String clientSettings;
    /**
     * 令牌设置，JSON格式。包含accessTokenTimeToLive、refreshTokenTimeToLive等配置
     */
    private String tokenSettings;
}
