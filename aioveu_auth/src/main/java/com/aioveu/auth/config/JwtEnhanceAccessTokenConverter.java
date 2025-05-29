package com.aioveu.auth.config;

import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * @author 雒世松
 * 令牌转换器，将身份认证信息转换后存储在令牌内
 */
public class JwtEnhanceAccessTokenConverter extends DefaultAccessTokenConverter {
    public JwtEnhanceAccessTokenConverter(){
        super.setUserTokenConverter(new com.high.sport.auth.config.JwtEnhanceUserAuthenticationConverter());
    }
}
