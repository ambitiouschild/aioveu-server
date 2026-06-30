package com.aioveu.gateway.config;


import com.aioveu.gateway.config.property.GatewayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

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

        String jwksUri = gatewayProperties.getEndpoint() + "/oauth2/jwks";

        NimbusReactiveJwtDecoder decoder =
                NimbusReactiveJwtDecoder.withJwkSetUri(jwksUri).build();

        // ✅ 超时保护
        decoder.setJwtValidator(JwtValidators.createDefault());
//        decoder.setJwtDecoderClockSkew(Duration.ofSeconds(30));

        return decoder;
    }
}
