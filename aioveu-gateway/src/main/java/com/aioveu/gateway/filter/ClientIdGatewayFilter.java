package com.aioveu.gateway.filter;


import com.alibaba.nacos.common.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName: ClientIdGatewayFilter
 * @Description TODO 在 Gateway 层收敛 X-Client-Id，是你这套架构从“能用”走向“平台级”的关键一步
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 23:09
 * @Version 1.0
 **/
@Component
@Slf4j
public class ClientIdGatewayFilter implements GlobalFilter, Ordered {

    private static final String HEADER_CLIENT_ID = "X-Client-Id";


    /*
    * ✅ WebFlux
        ✅ 非阻塞
        ✅ 生产可用
        *
        *
        * ✅ Header 一定存在
        ✅ 不怕前端漏传
        * 这是“中台”和“普通微服务”的分水岭
    *
    * */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String clientId = resolveClientId(exchange);

        ServerHttpRequest newRequest = exchange.getRequest()
                .mutate()
                .header(HEADER_CLIENT_ID, clientId)
                .build();

        log.debug("Gateway 注入 X-Client-Id = {}", clientId);

        return chain.filter(
                exchange.mutate().request(newRequest).build()
        );
    }

    /**
     * 解析 clientId（优先级从高到低） （✅ 带校验）
     */
    private String resolveClientId(ServerWebExchange exchange) {


        String path = exchange.getRequest().getURI().getPath();

        // ✅ 1. 敏感接口：必须用 JWT
        if (isSensitivePath(path)) {
            String clientId = resolveClientIdFromJwt(exchange);
            if (StringUtils.isNotBlank(clientId)) {
                log.info("敏感接口，Gateway 使用 JWT clientId = {}", clientId);
                return clientId;
            }
            throw new RuntimeException("非法请求：敏感接口缺少 clientId");
        }


        // 2前端带了就用（小程序 / H5） 公共接口：信任前端 Header
        String clientId = exchange.getRequest()
                .getHeaders()
                .getFirst(HEADER_CLIENT_ID);

        if (StringUtils.hasText(clientId)) {
            log.debug("公共接口，Gateway 透传 clientId = {}", clientId);
            return clientId;
        }

        // ✅ 3. 兜底（域名 / 默认）根据域名 / 路径判断（SaaS 常用）
        String host = exchange.getRequest().getURI().getHost();
        if (host != null) {
            if (host.contains("miniapp")) {
                return "default_miniapp";
            }
            if (host.contains("h5")) {
                return "default_h5";
            }
        }

        // 3️兜底（防止空指针）
        return "system_default";
    }

    @Override
    public int getOrder() {
        return -100; // 越早越好
    }


    private boolean isSensitivePath(String path) {
        return path.contains("/order/submit")
                || path.contains("/pay")
                || path.contains("/trade");
    }

    private String resolveClientIdFromJwt(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(authHeader)
                || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7);

        try {
            // ✅ 推荐用 JJWT / NimbusJWT
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("clientId", String.class);

        } catch (Exception e) {
            log.warn("JWT 解析失败", e);
            return null;
        }
    }

}


