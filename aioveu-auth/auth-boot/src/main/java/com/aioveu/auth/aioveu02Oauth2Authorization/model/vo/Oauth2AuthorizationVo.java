package com.aioveu.auth.aioveu02Oauth2Authorization.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: Oauth2AuthorizationVo
 * @Description TODO OAuth2授权信息，存储所有的授权记录、令牌和状态信息视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 13:57
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "OAuth2授权信息，存储所有的授权记录、令牌和状态信息视图对象")
public class Oauth2AuthorizationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "授权记录的唯一标识符，通常是UUID")
    private String id;
    @Schema(description = "关联的注册客户端ID，指向oauth2_registered_client表")
    private String registeredClientId;
    @Schema(description = "用户主体名称，通常是用户名或用户标识")
    private String principalName;
    @Schema(description = "授权类型，如authorization_code、password、client_credentials、refresh_token等")
    private String authorizationGrantType;
    @Schema(description = "授权的范围列表，以空格分隔的scope字符串")
    private String authorizedScopes;
    @Schema(description = "扩展属性，存储认证过程中的额外信息，序列化为JSON格式")
    private byte[] attributes;
    @Schema(description = "OAuth2授权码流程中的state参数，用于防止CSRF攻击")
    private String state;
    @Schema(description = "授权码的值，加密存储")
    private byte[] authorizationCodeValue;
    @Schema(description = "授权码颁发时间")
    private LocalDateTime authorizationCodeIssuedAt;
    @Schema(description = "授权码过期时间（通常很短，如5分钟）")
    private LocalDateTime authorizationCodeExpiresAt;
    @Schema(description = "授权码的元数据")
    private byte[] authorizationCodeMetadata;
    @Schema(description = "访问令牌的值，JWT或opaque token格式")
    private byte[] accessTokenValue;
    @Schema(description = "访问令牌颁发时间")
    private LocalDateTime accessTokenIssuedAt;
    @Schema(description = "访问令牌过期时间")
    private LocalDateTime accessTokenExpiresAt;
    @Schema(description = "访问令牌的元数据")
    private byte[] accessTokenMetadata;
    @Schema(description = "访问令牌类型，通常是Bearer")
    private String accessTokenType;
    @Schema(description = "访问令牌关联的scope")
    private String accessTokenScopes;
    @Schema(description = "OpenID Connect ID令牌的值")
    private byte[] oidcIdTokenValue;
    @Schema(description = "ID令牌颁发时间")
    private LocalDateTime oidcIdTokenIssuedAt;
    @Schema(description = "ID令牌过期时间")
    private LocalDateTime oidcIdTokenExpiresAt;
    @Schema(description = "ID令牌的元数据")
    private byte[] oidcIdTokenMetadata;
    @Schema(description = "刷新令牌的值")
    private byte[] refreshTokenValue;
    @Schema(description = "刷新令牌颁发时间")
    private LocalDateTime refreshTokenIssuedAt;
    @Schema(description = "刷新令牌过期时间（通常较长，如30天）")
    private LocalDateTime refreshTokenExpiresAt;
    @Schema(description = "刷新令牌的元数据")
    private byte[] refreshTokenMetadata;
    @Schema(description = "设备授权流程中的用户码")
    private byte[] userCodeValue;
    @Schema(description = "用户码颁发时间")
    private LocalDateTime userCodeIssuedAt;
    @Schema(description = "用户码过期时间")
    private LocalDateTime userCodeExpiresAt;
    @Schema(description = "用户码的元数据")
    private byte[] userCodeMetadata;
    @Schema(description = "设备授权流程中的设备码")
    private byte[] deviceCodeValue;
    @Schema(description = "设备码颁发时间")
    private LocalDateTime deviceCodeIssuedAt;
    @Schema(description = "设备码过期时间")
    private LocalDateTime deviceCodeExpiresAt;
    @Schema(description = "设备码的元数据")
    private byte[] deviceCodeMetadata;
}
