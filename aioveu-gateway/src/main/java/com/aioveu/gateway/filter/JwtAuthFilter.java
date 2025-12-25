package com.aioveu.gateway.filter;

import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.List;

/**
 * @ClassName: JwtAuthFilter
 * @Description TODO  创建 JWT 验证过滤器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/16 18:06
 * @Version 1.0
 **/

@Component
public class JwtAuthFilter  extends AbstractGatewayFilterFactory<JwtAuthFilter.Config>{

    private final String jwtSecret = "your-jwt-secret-key-for-gateway";

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getURI().getPath();

            // 检查是否是公开路径
            if (isPublicPath(path)) {
                return chain.filter(exchange);
            }

            // 获取Token
            String token = getTokenFromRequest(request);

            if (token == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 验证Token
            if (!validateToken(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 从Token中获取用户信息
            String username = getUsernameFromToken(token);
            String userId = getUserIdFromToken(token);

            // 添加用户信息到Header
            ServerHttpRequest newRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .header("X-Username", username)
                    .build();

            return chain.filter(exchange.mutate().request(newRequest).build());
        };
    }

    private boolean isPublicPath(String path) {
        List<String> publicPaths = List.of(
                "/api/v1/auth/login",
                "/api/v1/auth/logout",
                "/api/v1/auth/refresh",
                "/api/v1/auth/captcha",
                "/api/v1/auth/sms/",
                "/api/v1/auth/wx/",
                "/api/v1/auth/register",
                "/api/v1/system/users/register",
                "/api/v1/system/users/public/",
                "/actuator/",
                "/swagger-ui/",
                "/v3/api-docs/",
                "/webjars/",
                "/gateway/test/"
        );

        return publicPaths.stream().anyMatch(path::startsWith);
    }

    private String getTokenFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return "unknown";
        }
    }

    private String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("userId", String.class);
        } catch (Exception e) {
            return "0";
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public static class Config {
        // 配置参数
    }


}
