package com.aioveu.auth.aioveu02Oauth2Authorization.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: Oauth2Authorization
 * @Description TODO OAuth2授权信息，存储所有的授权记录、令牌和状态信息实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 13:54
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("oauth2_authorization")
public class Oauth2Authorization extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 关联的注册客户端ID，指向oauth2_registered_client表
     */
    private String registeredClientId;
    /**
     * 用户主体名称，通常是用户名或用户标识
     */
    private String principalName;
    /**
     * 授权类型，如authorization_code、password、client_credentials、refresh_token等
     */
    private String authorizationGrantType;
    /**
     * 授权的范围列表，以空格分隔的scope字符串
     */
    private String authorizedScopes;
    /**
     * 扩展属性，存储认证过程中的额外信息，序列化为JSON格式
     */
    private byte[] attributes;
    /**
     * OAuth2授权码流程中的state参数，用于防止CSRF攻击
     */
    private String state;
    /**
     * 授权码的值，加密存储
     */
    private byte[] authorizationCodeValue;
    /**
     * 授权码颁发时间
     */
    private LocalDateTime authorizationCodeIssuedAt;
    /**
     * 授权码过期时间（通常很短，如5分钟）
     */
    private LocalDateTime authorizationCodeExpiresAt;
    /**
     * 授权码的元数据
     */
    private byte[] authorizationCodeMetadata;
    /**
     * 访问令牌的值，JWT或opaque token格式
     */
    private byte[] accessTokenValue;
    /**
     * 访问令牌颁发时间
     */
    private LocalDateTime accessTokenIssuedAt;
    /**
     * 访问令牌过期时间
     */
    private LocalDateTime accessTokenExpiresAt;
    /**
     * 访问令牌的元数据
     */
    private byte[] accessTokenMetadata;
    /**
     * 访问令牌类型，通常是Bearer
     */
    private String accessTokenType;
    /**
     * 访问令牌关联的scope
     */
    private String accessTokenScopes;
    /**
     * OpenID Connect ID令牌的值
     */
    private byte[] oidcIdTokenValue;
    /**
     * ID令牌颁发时间
     */
    private LocalDateTime oidcIdTokenIssuedAt;
    /**
     * ID令牌过期时间
     */
    private LocalDateTime oidcIdTokenExpiresAt;
    /**
     * ID令牌的元数据
     */
    private byte[] oidcIdTokenMetadata;
    /**
     * 刷新令牌的值
     */
    private byte[] refreshTokenValue;
    /**
     * 刷新令牌颁发时间
     */
    private LocalDateTime refreshTokenIssuedAt;
    /**
     * 刷新令牌过期时间（通常较长，如30天）
     */
    private LocalDateTime refreshTokenExpiresAt;
    /**
     * 刷新令牌的元数据
     */
    private byte[] refreshTokenMetadata;
    /**
     * 设备授权流程中的用户码
     */
    private byte[] userCodeValue;
    /**
     * 用户码颁发时间
     */
    private LocalDateTime userCodeIssuedAt;
    /**
     * 用户码过期时间
     */
    private LocalDateTime userCodeExpiresAt;
    /**
     * 用户码的元数据
     */
    private byte[] userCodeMetadata;
    /**
     * 设备授权流程中的设备码
     */
    private byte[] deviceCodeValue;
    /**
     * 设备码颁发时间
     */
    private LocalDateTime deviceCodeIssuedAt;
    /**
     * 设备码过期时间
     */
    private LocalDateTime deviceCodeExpiresAt;
    /**
     * 设备码的元数据
     */
    private byte[] deviceCodeMetadata;
}
