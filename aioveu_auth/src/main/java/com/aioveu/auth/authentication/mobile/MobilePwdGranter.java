package com.aioveu.auth.authentication.mobile;

import com.aioveu.auth.authentication.SecurityConstants;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义的授权模式，模式名称为：mobile_pwd
 */
public class MobilePwdGranter extends OAuth2TokenGranter  {
    private static final String GRANT_TYPE = "mobile_pwd";

    private final AuthenticationManager authenticationManager;

    public MobilePwdGranter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(RegisteredClient registeredClient,
                                                           OAuth2AuthorizationContext context){
        // 1. 参数验证
        OAuth2AuthorizationGrantAuthorizationToken authorizationGrantToken =
                (OAuth2AuthorizationGrantAuthorizationToken) context.getPrincipal();
        Map<String, Object> parameters = authorizationGrantToken.getAdditionalParameters();

        String mobile = (String) parameters.get(SecurityConstants.MOBILE_PARAMETER);
        String password = (String) parameters.get("password");

        // 2. 执行认证
        Authentication authentication = authenticationManager.authenticate(
                new MobilePasswordAuthenticationToken(mobile, password)
        );

        // 3. 生成访问令牌
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generateTokenValue(),
                Instant.now(),
                Instant.now().plusSeconds(registeredClient.getTokenSettings().getAccessTokenTimeToLive())
        );

        // 4. 构建令牌响应
        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient,
                authentication,
                accessToken,
                Collections.emptyMap()
        );
    }

    @Override
    public boolean supports(String grantType) {
        return GRANT_TYPE.equals(grantType);
    }
}