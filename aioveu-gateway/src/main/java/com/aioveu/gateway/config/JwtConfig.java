package com.aioveu.gateway.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import java.time.Duration;

/**
 * @ClassName: JwtConfig
 * @Description TODO  只声明 ReactiveJwtDecoderBean（不绑定 Security）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/16 21:15
 * @Version 1.0
 **/
@Configuration
@EnableConfigurationProperties(GatewayProperties.class)
public class JwtConfig {


    private final GatewayProperties gatewayProperties;

    public JwtConfig(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }


    @Bean("gatewayJwtDecoder")
    public ReactiveJwtDecoder reactiveJwtDecoder() {

        String jwksUri = gatewayProperties.getEndpoint() + "/aioveu-auth/oauth2/jwks";

        NimbusReactiveJwtDecoder decoder =
                NimbusReactiveJwtDecoder.withJwkSetUri(jwksUri).build();

        // ✅ 超时保护
        decoder.setJwtValidator(JwtValidators.createDefault());
//        decoder.setJwtDecoderClockSkew(Duration.ofSeconds(30));

        return decoder;
    }
}
