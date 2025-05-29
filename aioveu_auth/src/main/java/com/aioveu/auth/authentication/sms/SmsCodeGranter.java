package com.aioveu.auth.authentication.sms;

import com.aioveu.auth.authentication.SecurityConstants;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description 自定义的授权模式，模式名称为：sms_code
 * @author: 雒世松
 * @date: 2024/10/28 23:07
 */
public class SmsCodeGranter extends AbstractTokenGranter  {

    private static final String GRANT_TYPE = "sms_code";

    private final AuthenticationManager authenticationManager;

    public SmsCodeGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices,
                             ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get(SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE);
        String code = parameters.get(SecurityConstants.DEFAULT_PARAMETER_NAME_CODE);
        Authentication userAuth = new SmsCodeAuthenticationToken(mobile, code);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        //调用AuthenticationManager进行认证，内部会根据SmsCodeAuthenticationToken找到对应的Provider进行认证
        userAuth = authenticationManager.authenticate(userAuth);
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate mobile: " + mobile);
        }
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }
}
