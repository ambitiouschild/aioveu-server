package com.aioveu.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.crypto.SecretKey;
import java.util.List;

/**
 * @ClassName: JwtAuthFilter
 * @Description TODO ✅ 网关层：仅校验 JWT 合法性（不解析业务字段、不校验黑名单）
 *                      JWT 合法性在「网关」校验，黑名单用户状权限在「资源服务器」校验
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/16 18:06
 * @Version 1.0
 **/

@Slf4j
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final ReactiveJwtDecoder jwtDecoder;

    public JwtAuthFilter(ReactiveJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }


    @Override
    public int getOrder() {
        return -110; // ✅ 比 tenant filter 更早
    }

    /*
    * （✅ 推荐）：在 apply()里用 OrderedGatewayFilter
    * 这是 Spring Cloud Gateway 官方推荐写法。
    * */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 1. 白名单放行
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        // 获取Token
        String token = getTokenFromRequest(request);


        // 2️✅ 没有 Token：不校验，不拦截，交给下一个 Filter
        if (token == null) {
            log.debug("JWT 缺失，跳过校验，交由 ClientIdGatewayFilter 处理");
            return chain.filter(exchange);
        }

        // 2. ✅ 只校验 JWT 是否合法，不做任何业务解析
        return jwtDecoder.decode(token)
                .flatMap(jwt -> {

                    // ✅ 合法，直接放行
                    return chain.filter(exchange);

                })
                .onErrorResume(e -> {
                    log.warn("JWT 校验失败，path={}", path, e);
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }

    /** ✅ 白名单路径 */
    private boolean isPublicPath(String path) {
        List<String> publicPaths = List.of(
                "/aioveu/api/v8/admin/auth"
        );

        return publicPaths.stream().anyMatch(path::startsWith);
    }


    /** ✅ 从 Header 取 Token */
    private String getTokenFromRequest(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    public static class Config {
        // 配置参数
    }



}
