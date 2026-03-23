package com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: Oauth2RegisteredClientForm
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:00
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "OAuth2注册客户端，存储所有已注册的客户端应用信息表单对象")
public class Oauth2RegisteredClientForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "客户端记录的唯一标识符，通常是UUID")
    @Size(max=100, message="客户端记录的唯一标识符，通常是UUID长度不能超过100个字符")
    private String id;

    @Schema(description = "客户端标识符，OAuth2请求中使用的client_id")
    @NotBlank(message = "客户端标识符，OAuth2请求中使用的client_id不能为空")
    @Size(max=100, message="客户端标识符，OAuth2请求中使用的client_id长度不能超过100个字符")
    private String clientId;

    @Schema(description = "客户端ID的创建时间")
    @NotNull(message = "客户端ID的创建时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clientIdIssuedAt;

    @Schema(description = "客户端密钥，已加密存储。公共客户端可为NULL")
    @Size(max=200, message="客户端密钥，已加密存储。公共客户端可为NULL长度不能超过200个字符")
    private String clientSecret;

    @Schema(description = "客户端密钥的过期时间，NULL表示永不过期")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clientSecretExpiresAt;

    @Schema(description = "客户端显示名称，用于用户界面显示")
    @NotBlank(message = "客户端显示名称，用于用户界面显示不能为空")
    @Size(max=200, message="客户端显示名称，用于用户界面显示长度不能超过200个字符")
    private String clientName;

    @Schema(description = "支持的客户端认证方法，JSON格式数组")
    @Size(max=1000, message="支持的客户端认证方法，JSON格式数组。长度不能超过1000个字符")
    private String clientAuthenticationMethods;

    @Schema(description = "支持的授权类型，JSON格式数组。")
    @Size(max=1000, message="支持的授权类型，JSON格式数组。长度不能超过1000个字符")
    private String authorizationGrantTypes;

    @Schema(description = "允许的重定向URI列表，JSON格式数组。必须与授权请求中的redirect_uri完全匹配")
    @Size(max=1000, message="允许的重定向URI列表，JSON格式数组。必须与授权请求中的redirect_uri完全匹配长度不能超过1000个字符")
    private String redirectUris;

    @Schema(description = "登出后的重定向URI列表，JSON格式数组")
    @Size(max=1000, message="登出后的重定向URI列表，JSON格式数组长度不能超过1000个字符")
    private String postLogoutRedirectUris;

    @Schema(description = "客户端可请求的范围列表，JSON格式数组。")
    @Size(max=1000, message="客户端可请求的范围列表，JSON格式数组。长度不能超过1000个字符")
    private String scopes;

    @Schema(description = "客户端设置，JSON格式。包含requireAuthorizationConsent、requireProofKey等配置")
    @Size(max=2000, message="客户端设置，JSON格式。包含requireAuthorizationConsent、requireProofKey等配置长度不能超过2000个字符")
    private String clientSettings;

    @Schema(description = "令牌设置，JSON格式。包含accessTokenTimeToLive、refreshTokenTimeToLive等配置")
    @Size(max=2000, message="令牌设置，JSON格式。包含accessTokenTimeToLive、refreshTokenTimeToLive等配置长度不能超过2000个字符")
    private String tokenSettings;

    //修改你的 DTO 类（添加更多配置项）
    @Data
    public static class ClientSettingsDto {
        @Schema(description = "是否需要授权同意页", example = "false")
        private Boolean requireAuthorizationConsent = false;

        @Schema(description = "是否需要PKCE", example = "false")
        private Boolean requireProofKey = false;

        // 可以添加更多配置
        @Schema(description = "客户端密钥JWT的签名算法", example = "RS256")
        private String jwkSetUrl;

        @Schema(description = "客户端认证方法签名算法", example = "RS256")
        private String tokenEndpointAuthenticationSigningAlgorithm;
    }

    @Data
    public static class TokenSettingsDto {
        @Schema(description = "访问令牌有效期(秒)", example = "86400")
        private Long accessTokenTimeToLiveSeconds = 86400L; // 1天

        @Schema(description = "刷新令牌有效期(秒)", example = "2592000")
        private Long refreshTokenTimeToLiveSeconds = 2592000L; // 30天

        @Schema(description = "是否重用刷新令牌", example = "true")
        private Boolean reuseRefreshTokens = true;

        @Schema(description = "访问令牌格式", example = "SELF_CONTAINED")
        private String accessTokenFormat = "SELF_CONTAINED";

        @Schema(description = "ID Token签名算法", example = "RS256")
        private String idTokenSignatureAlgorithm = "RS256";
    }
}
