package com.aioveu.gateway.config;


import com.aioveu.gateway.config.property.GatewayProperties;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.text.ParseException;

/**
 * @ClassName: JwtConfig
 * @Description TODO  只声明 ReactiveJwtDecoderBean（不绑定 Security）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/16 21:15
 * @Version 1.0
 **/

/**
 * ✅ Gateway 专用 JWT 解析配置
 * ❌ 不使用 ReactiveJwtDecoder（避免触发 Security）
 */
@Configuration
@EnableConfigurationProperties(GatewayProperties.class)
public class JwtConfig {


    private final GatewayProperties gatewayProperties;

    public JwtConfig(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }


    /**
     * ✅ 纯工具 Bean
     * ✅ 不实现 Security 接口
     * ✅ 不触发 ResourceServer 自动配置
     */
    @Bean("gatewayJwtDecoder")
    public GatewayJwtParser gatewayJwtParser() {
        return new GatewayJwtParser();
    }

    /**
     * ✅ Gateway 专用 JWT 解析器
     * 只做一件事：解析 claim
     */
    public static class GatewayJwtParser {

        /**
         * 解析 JWT，提取 tenantId
         */
        public Mono<Long> parseTenantId(String token) {
            if (token == null || token.isEmpty()) {
                return Mono.empty();
            }

            try {
                SignedJWT jwt = (SignedJWT) JWTParser.parse(token);
                Object tenantId = jwt.getJWTClaimsSet()
                        .getClaim("tenant_id");

                if (tenantId instanceof Long l) {
                    return Mono.just(l);
                }
                if (tenantId instanceof Integer i) {
                    return Mono.just(i.longValue());
                }
                return Mono.empty();
            } catch (ParseException e) {
                return Mono.empty();
            }
        }
    }
}
