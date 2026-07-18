package com.aioveu.gateway.filter;


import com.aioveu.common.constant.JwtClaimConstants;
import com.aioveu.gateway.service.ClientWhitelistWithRedisService;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;


import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName: ClientIdGatewayFilter
 * @Description TODO Gateway 永远不应该“调用”微服务,👉 它只负责“把请求转交给微服务”,👉 谁“调用”，谁就拥有了业务复杂度
 *                              你现在这套系统，已经经历了三个阶段：
 *                              1️能跑（Gateway + WebClient）
 *                              2️踩坑（Observation / 空 body / 前端失败）
 *                              3️觉醒（Gateway 只转发，不调用）✅ ← 你现在在这里
 *
 * 👉 能走到第 3 步的人，不多。
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/9 23:09
 * @Version 1.0
 **/
/*
直接 @Component即可，不需要任何额外配置
Spring Cloud Gateway 不是 Servlet
没有 HttpSecurity
没有 BearerTokenAuthenticationFilter
👉 Gateway 只认 GlobalFilter/ GatewayFilter
* */
@Component
@Slf4j
public class ClientIdGatewayFilter implements GlobalFilter, Ordered {

    /** 前端 / 内部约定的租户标识 Header */

    private static final String HEADER_CLIENT_ID = "X-Client-Id";
    private static final String HEADER_TEENANT_ID = "X-Tenant-Id";
    private static final String HEADER_CLIENT_VERIFIED = "X-Client-Verified";

    private final ReactiveJwtDecoder jwtDecoder;
    private final ClientWhitelistWithRedisService clientWhitelistWithRedisService;

    private static final Set<String> BYPASS_PATHS = Set.of(
            "/oauth2/jwks",
            "/oauth2/token",
            "/oauth2/authorize"
    );



    //构造函数注入
     // ✅ 关键：@Lazy 方案 A（强烈推荐）：把构造函数注入改成 @Lazy
    public ClientIdGatewayFilter(@Lazy @Qualifier("gatewayJwtDecoder") ReactiveJwtDecoder jwtDecoder,
                                 ClientWhitelistWithRedisService clientWhitelistWithRedisService
    ) {
        this.jwtDecoder = jwtDecoder;
        this.clientWhitelistWithRedisService = clientWhitelistWithRedisService;
    }

    @Override
    public int getOrder() {
        return -100; // 越早越好
    }


    /*
      方案 ①（✅ 推荐）：真正用 JWT 校验
    * 方案 ②（✅ 网关常用）：不校验 JWT，只解析
    * */
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
    /**
     * ============================================================
     *  全局入口
     *  ============================================================
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();
        log.info("【ClientIdGatewayFilter】进入 filter, path={}", path);

        String auth = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        // 1️有 Token：走 JWT → tenantId
        //这一行能直接治好你前面的 JWKS 401
        if (BYPASS_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        /*
        在网关里，X-Tenant-Id 的真正作用是：
        “给 JWT 里的 tenantId 做一个‘不可变的镜像’供校验使用”
        * */

        /*
        * /oauth2/jwks
            ✅ 没有 Authorization Header
            ✅ 不是 JWT 请求
            ✅ 不属于任何 client
        *
        * */


        /*
        *  ✅ 网关：解析 + 传 Header（用于路由/限流/日志）
            ✅ 资源服务器：自己解析 JWT 拿 tenantId（业务权威来源）
            ✅ 你现在的“资源服务器兜底解析”是 100% 正确的
            ❌ 不要让资源服务器“只依赖网关传的 Header”
        *
        *
        * */
        if (StringUtils.isNotBlank(auth) && auth.startsWith("Bearer ")) {
            return resolveTenantFromJwt(auth.substring(7))
                    .flatMap(tenantId ->
                            chain.filter(
                                    exchange.mutate()
                                            .request(mutateTenantHeader(exchange, tenantId))
                                            .build()
                            )
                    )
                    .switchIfEmpty(unauthorized(exchange, "JWT 中缺失 tenantId"));
        }


        // 2️无 Token：走 clientId 白名单
        return resolveClientId(exchange)
                .flatMap(clientId -> {
                    if (!clientWhitelistWithRedisService.isValid(clientId)) {
                        return unauthorized(exchange, "非法 clientId: " + clientId);
                    }

                    log.info("【ClientIdGatewayFilter】匿名请求，clientId 校验通过: {}", clientId);


                    ServerHttpRequest finalRequest = exchange.getRequest().mutate()
                            .header(HEADER_CLIENT_ID, clientId)
                            .header(HEADER_CLIENT_VERIFIED, "true")
                            .build();

                    log.error("【ClientIdGatewayFilter】FINAL HEADERS: {}", finalRequest.getHeaders());

                    return chain.filter(
                            exchange.mutate()
                                    .request(mutateClientVerifiedHeader(exchange, clientId))
                                    .build()
                    );

                })
                .switchIfEmpty(unauthorized(exchange, "缺失 X-Client-Id"));
    }

    /**
     * ✅ 解析 clientId（JWT > Header）
     * JWT 是权威来源，Header 是传输载体
     */
//    private Mono<String> resolveTenantFromJwt(String token) {
//        return jwtDecoder.decode(token)
//                .map(jwt -> jwt.getClaimAsString(JwtClaimConstants.Tenant.ID))
//                .filter(StringUtils::isNotBlank);
//    }
    private Mono<Long> resolveTenantFromJwt(String token) {
        return jwtDecoder.decode(token)
                .map(jwt -> {
                    Object value = jwt.getClaims().get(JwtClaimConstants.Tenant.ID);
                    if (value instanceof Long l) return l;
                    if (value instanceof Integer i) return i.longValue();
                    return null;
                })
                .filter(Objects::nonNull);
    }

    private Mono<String> resolveClientId(ServerWebExchange exchange) {
        String clientId = exchange.getRequest()
                .getHeaders()
                .getFirst(HEADER_CLIENT_ID);

        if (StringUtils.isNotBlank(clientId)) {
            return Mono.just(clientId.split(",")[0]);
        }
        return Mono.empty();
    }

    /*
    Header 注入（只注入，不覆盖）
    * */
    private ServerHttpRequest mutateTenantHeader(ServerWebExchange exchange, Long tenantId) {
        return exchange.getRequest().mutate()
                .header(HEADER_TEENANT_ID, tenantId.toString())
                .build();
    }

        /*
    Header 注入（只注入，不覆盖）
    * */
    private ServerHttpRequest mutateClientVerifiedHeader(ServerWebExchange exchange, String clientId) {
        return exchange.getRequest().mutate()
                .header(HEADER_CLIENT_ID, clientId)
                .header(HEADER_CLIENT_VERIFIED, "true")
                .build();
    }

    /**
     * ✅ 统一返回 401（WebFlux 写法）
     *    因为前端要的不是“401 状态码”，而是“一段能解析的 JSON”
     *    ❌ setComplete()= “我结束了，啥也不发”
     */
//    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
//        log.error("【ClientIdGatewayFilter】UNAUTHORIZED: {}", msg);
//        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//        return exchange.getResponse().setComplete();
//    }

    /**
     * ✅ 统一返回 401（WebFlux 写法）
     *  writeWith(JSON)= “我结束了，但我把话说明白了”
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        log.error("【ClientIdGatewayFilter】UNAUTHORIZED: {}", msg);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");

        String body = """
            {
              "code": 401,
              "msg": "%s",
              "data": null
            }
            """.formatted(msg);

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(body.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}


