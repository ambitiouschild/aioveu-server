package com.aioveu.auth.oauth2.oidc;

import cn.hutool.core.lang.Assert;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;

/**
 * @Description: TODO 自定义 OidcToken
 * @Author: 雒世松
 * @Date: 2025/6/5 17:51
 * @param
 * @return:
 **/

public class CustomOidcToken extends OidcUserInfoAuthenticationToken {

    private final Authentication principal;

    private final CustomOidcUserInfo userInfo;

    public CustomOidcToken(Authentication principal) {
        super(principal);
        Assert.notNull(principal, "principal cannot be null");
        this.principal = principal;
        this.userInfo = null;
        this.setAuthenticated(false);
    }

    public CustomOidcToken(Authentication principal, CustomOidcUserInfo userInfo) {
        super(principal, userInfo);
        Assert.notNull(principal, "principal cannot be null");
        Assert.notNull(userInfo, "userInfo cannot be null");
        this.principal = principal;
        this.userInfo = userInfo;
        this.setAuthenticated(true);
    }


    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

}
