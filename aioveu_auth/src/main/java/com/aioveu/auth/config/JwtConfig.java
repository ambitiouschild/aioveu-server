package com.aioveu.auth.config;

import com.aioveu.auth.common.model.SecurityUser;
import com.aioveu.auth.common.model.TokenConstant;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.stream.Collectors;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/5/29 13:59
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@Configuration
@EnableWebSecurity
public class JwtConfig {

    // ==================== JWT 编解码器配置 ====================
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(
                Keys.hmacShaKeyFor(TokenConstant.SIGN_KEY.getBytes())
        ).build();
    }

    // ==================== 令牌增强配置 ====================
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            SecurityUser user = extractUserDetails(jwt);
            return user.getAuthorities().stream()
                    .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
                    .collect(Collectors.toList());
        });
        return converter;
    }

    private SecurityUser extractUserDetails(Jwt jwt) {
        return SecurityUser.builder()
                .id(jwt.getClaim(TokenConstant.USER_ID))
                .name(jwt.getClaim(TokenConstant.NICK_NAME))
                .head(jwt.getClaim(TokenConstant.AVATAR))
                .phone(jwt.getClaim(TokenConstant.MOBILE))
                .email(jwt.getClaim(TokenConstant.EMAIL))
                .gender(jwt.getClaim(TokenConstant.GENDER))
                .build();
    }

    // ==================== JWT 声明自定义 ====================
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (context.getPrincipal() instanceof SecurityUser user) {
                context.getClaims()
                        .claim(TokenConstant.USER_ID, user.getId())
                        .claim(TokenConstant.NICK_NAME, user.getName())
                        .claim(TokenConstant.AVATAR, user.getHead())
                        .claim(TokenConstant.MOBILE, user.getPhone())
                        .claim(TokenConstant.EMAIL, user.getEmail())
                        .claim(TokenConstant.GENDER, user.getGender());
            }
        };
    }
}
