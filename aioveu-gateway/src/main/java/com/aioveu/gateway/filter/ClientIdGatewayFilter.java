package com.aioveu.gateway.filter;


import com.aioveu.gateway.service.ClientWhitelistWithRedisService;
import com.aioveu.gateway.service.TenantQueryService;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @ClassName: ClientIdGatewayFilter
 * @Description TODO 在 Gateway 层收敛 X-Client-Id，是你这套架构从“能用”走向“平台级”的关键一步
 *                       在 Gateway 层收敛 X-Client-Id (WebFlux 非阻塞版)
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 23:09
 * @Version 1.0
 **/
@Component
@Slf4j
public class ClientIdGatewayFilter implements GlobalFilter, Ordered {

    private static final String HEADER_CLIENT_ID = "X-Client-Id";


    private final ReactiveJwtDecoder jwtDecoder;
    private final ClientWhitelistWithRedisService clientWhitelistWithRedisService;

    //是 Gateway Filter 在启动阶段依赖了 Feign，触发了 Spring 的循环依赖保护
    @Lazy
    private final TenantQueryService tenantQueryService;


    //构造函数注入
     // ✅ 关键：@Lazy 方案 A（强烈推荐）：把构造函数注入改成 @Lazy
    public ClientIdGatewayFilter(@Lazy @Qualifier("gatewayJwtDecoder") ReactiveJwtDecoder jwtDecoder,
                                 ClientWhitelistWithRedisService clientWhitelistWithRedisService,
                                 TenantQueryService tenantQueryService   // ✅ 补上
    ) {
        this.jwtDecoder = jwtDecoder;
        this.clientWhitelistWithRedisService = clientWhitelistWithRedisService;
        this.tenantQueryService = tenantQueryService;
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
    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        log.info("【ClientIdGatewayFilter】进入 filter, path={}", exchange.getRequest().getURI().getPath());

        // ✅ 关键：先保存引用
        GatewayFilterChain finalChain = chain;

        // 1. 解析 clientId (返回 Mono<String>)
        // 统一解析 clientId（不再区分敏感 / 公共）
        return resolveClientId(exchange)
                // 2. 如果解析失败或为空，给一个默认值
//                .defaultIfEmpty("system_default")
                // 3. 使用 flatMap 确保非阻塞
                .flatMap(clientId -> {


                    // ✅✅✅ 检测就放在这里 ✅✅✅
                    if (!clientWhitelistWithRedisService.isValid(clientId)) {
                        log.error("【ClientIdGatewayFilter】非法 clientId: {}", clientId);
                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                        return exchange.getResponse().setComplete();
                    }
                    log.debug("【ClientIdGatewayFilter】注入 X-Client-Id = {}", clientId);

                    // ✅ 是否是 JWT 接口
                    boolean hasJwt = hasAuthorizationHeader(exchange);


                    if (hasJwt) {
                        // JWT 接口：交给 JWT decoder，后面 TenantFilter 从 SecurityUtils 取
                        return chain.filter(
                                exchange.mutate()
                                        .request(buildRequestWithClientId(exchange, clientId))
                                        .build()
                        );
                    }


                    // ✅ 公共接口：直接查 tenantId
                    //方案一（✅ 强烈推荐）：用 Mono.fromCallable
                    //这是 WebFlux + Feign 在 Gateway 里的标准写法
                    //把 Feign 扔到 独立线程池,不阻塞 WebFlux event loopmGateway 不再吞异常

                    /*
                    *   ✅ 正确原则（你这个架构必须遵守）
                        WebFlux Gateway 里：
                        ✅ 只做 Header / 路由 / 校验
                        ❌ 不直接在 Filter 里调 Feign
                    * */
                    return  tenantQueryService.getTenantIdByClientId(clientId)
                            // 1. 调试用：看看 WebClient 到底返回了什么（如果有值的话）
                            .doOnNext(tenantId -> log.info("【ClientIdGatewayFilter】WebClient 返回 tenantId={}", tenantId))
                            // 2. 过滤掉 null 和 tenantId 为空的情况
                            .filter(tenantId -> tenantId != null)
                            .switchIfEmpty(Mono.defer(() -> {
                                log.error("【ClientIdGatewayFilter】WebClient 返回 empty 或未匹配到租户, clientId={}", clientId);
                                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                return Mono.empty(); // ✅ 关键
                            }))
                            // 4. 处理有值的情况
                            .flatMap(tenantId -> {

                                log.info("【ClientIdGatewayFilter】通过 clientId={} 获取 tenantId={}",
                                        clientId, tenantId);

                                return chain.filter(
                                        exchange.mutate()
                                                .request(buildRequestWithTenantId(exchange, clientId, tenantId))
                                                .build()
                                );
                            });

                }).switchIfEmpty(Mono.defer(() -> {
                    String path = exchange.getRequest().getURI().getPath();
                    if (isSensitivePath(path)) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    }

                    ServerHttpRequest newReq = exchange.getRequest()
                            .mutate()
                            .header(HEADER_CLIENT_ID, "system_default")
                            .build();

                    return finalChain.filter(exchange.mutate().request(newReq).build());
                }));
    }

    /**
     * 解析 clientId（优先级从高到低） （✅ 带校验）
     *     * 解析 clientId（优先级从高到低）
     *      * 返回 Mono<String> 以支持 WebFlux 非阻塞模型
     */
    private Mono<String> resolveClientId(ServerWebExchange exchange) {


        String path = exchange.getRequest().getURI().getPath();

        log.info("【ClientIdGatewayFilter】解析 clientId（优先级从高到低）path:{}", path);


        // ✅ 1. 敏感接口：必须用 JWT
        if (isSensitivePath(path)) {
            return resolveClientIdFromJwt(exchange);
        }

        // 2前端带了就用（小程序 / H5） 公共接口：信任前端 Header
        String clientIdFromHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HEADER_CLIENT_ID);

        log.info("【ClientIdGatewayFilter】前端带了就用（小程序 / H5） 公共接口：信任前端 Header:{}", clientIdFromHeader);

        if (StringUtils.isNotBlank(clientIdFromHeader)) {

            // ✅ 最小修改点：只取第一个 clientId，防拼接
            String cleanClientId = clientIdFromHeader.split(",")[0];


            log.debug("【ClientIdGatewayFilter】公共接口，清洗 clientId = {} -> {}",
                    clientIdFromHeader, cleanClientId);

            return Mono.just(cleanClientId);
        }

        // ✅ 3. 兜底（域名 / 默认）根据域名 / 路径判断（SaaS 常用）
        String host = exchange.getRequest().getURI().getHost();
        if (host != null) {
            if (host.contains("miniapp")) {
                log.debug("【ClientIdGatewayFilter】 miniapp兜底（域名 / 默认）根据域名 / 路径判断（SaaS 常用） clientIdFromHeader： {}", clientIdFromHeader);
                return Mono.just("default_miniapp");

            }
            if (host.contains("h5")) {
                log.debug("【ClientIdGatewayFilter】 h5兜底（域名 / 默认）根据域名 / 路径判断（SaaS 常用） clientIdFromHeader： {}", clientIdFromHeader);
                return Mono.just("default_miniapp");
            }
        }

        // 4. 最终兜底
        return Mono.just("system_default");
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


    /**
     * 使用 ReactiveJwtDecoder 非阻塞解析 JWT
     */
    private  Mono<String> resolveClientIdFromJwt(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(authHeader)
                || !authHeader.startsWith("Bearer ")) {
            return Mono.empty(); // ✅ 不是 null
        }

        String token = authHeader.substring(7);

        // 关键点：decode 返回 Mono<Jwt>，完全非阻塞
        return jwtDecoder.decode(token)
                .map(jwt -> jwt.getClaimAsString("client_id"))
                .onErrorResume(e -> {
                    log.warn("JWT 解析失败", e);
                    return Mono.empty(); // ✅ 不炸
                });


        /*
        ✅ 不依赖 Spring Security
        ✅ 最干净、最推荐
        * */
//        return Mono.fromCallable(() -> {
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(getSecretKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//            return claims.get("client_id", String.class);
//        }).onErrorResume(e -> {
//            log.warn("JWT 解析失败", e);
//            return Mono.empty();
//        });


    }


    private boolean hasAuthorizationHeader(ServerWebExchange exchange) {
        String auth = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        return StringUtils.isNotBlank(auth) && auth.startsWith("Bearer ");
    }

    private ServerHttpRequest buildRequestWithTenantId(
            ServerWebExchange exchange, String clientId, Long tenantId) {

        return exchange.getRequest()
                .mutate()
                .header("X-Client-Id", clientId)
                .header("X-Tenant-Id", String.valueOf(tenantId))
                .build();
    }


    private ServerHttpRequest buildRequestWithClientId(
            ServerWebExchange exchange, String clientId) {

        return exchange.getRequest()
                .mutate()
                .header("X-Client-Id", clientId)
                .build();
    }



}


