package com.aioveu.auth.oauth2.extension.customRefreshToken;

import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @Description: TODO 刷新令牌授权模式身份验证令牌
 * OAuth2AuthorizationGrantAuthenticationToken
 * @Author: 雒世松
 * @Date: 2025/6/5 17:50
 * @param
 * @return: 
 **/

public class CustomRefreshTokenAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {


    private static final long serialVersionUID = 1L;

    /**
     * 令牌申请访问范围
     */
    private final Set<String> scopes;

    /**
     * 授权类型：刷新令牌
     */
    public static final AuthorizationGrantType REFRESH_TOKEN = new AuthorizationGrantType("refresh_token");

    private static final AuthorizationGrantType REFRESH_TOKEN2 = AuthorizationGrantType.REFRESH_TOKEN;

    private final String refreshToken;

    private OAuth2AccessToken accessToken;
    private OAuth2RefreshToken newRefreshToken;


    protected CustomRefreshTokenAuthenticationToken(
            String refreshToken,
            Authentication clientPrincipal,
            Set<String> scopes,
            @Nullable Map<String, Object> additionalParameters
    ) {
        super(CustomRefreshTokenAuthenticationToken.REFRESH_TOKEN, clientPrincipal, additionalParameters);
        Assert.hasText(refreshToken, "刷新令牌不能为空,refreshToken cannot be empty");
        this.refreshToken = refreshToken;
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    }


    public CustomRefreshTokenAuthenticationToken(org.springframework.security.oauth2.server.authorization.client.RegisteredClient registeredClient,
                                                 Authentication clientPrincipal,
                                                 OAuth2AccessToken accessToken,
                                                 OAuth2RefreshToken newRefreshToken,
                                                 Map<String, Object> additionalParameters) {
        super(REFRESH_TOKEN, clientPrincipal, additionalParameters);
        this.refreshToken = null;
        this.scopes = null;
        this.accessToken = accessToken;
        this.newRefreshToken = newRefreshToken;
    }

    /**
     * 用户凭证(微信小程序 Code)
     */
    @Override
    public Object getCredentials() {
        return this.getAdditionalParameters().get(OAuth2ParameterNames.CODE);
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public Set<String> getScopes() {
        return this.scopes;
    }

    public OAuth2AccessToken getAccessToken() {
        return this.accessToken;
    }

    public OAuth2RefreshToken getNewRefreshToken() {
        return this.newRefreshToken;
    }

    private static Set<String> getScopes(Map<String, Object> additionalParameters) {
        if (additionalParameters.containsKey("scope")) {
            String scope = (String) additionalParameters.get("scope");
            return new HashSet<>(Arrays.asList(scope.split(" ")));
        }
        return Collections.emptySet();
    }

}
