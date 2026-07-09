package com.aioveu.gateway.filter;


import com.aioveu.common.constant.JwtClaimConstants;
import com.aioveu.gateway.service.ClientWhitelistWithRedisService;
import com.aioveu.gateway.service.TenantQueryService;
import com.aioveu.gateway.util.ClaimUtils;
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
// ✅ 正确：来自 oauth2-core
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor; // 这个接口才有 getClaimAsLong

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
/*
直接 @Component即可，不需要任何额外配置
Spring Cloud Gateway 不是 Servlet
没有 HttpSecurity
没有 BearerTokenAuthenticationFilter
👉 Gateway 只认 GlobalFilter/ GatewayFilter
* */

/**
 * ============================================================
 *  ClientIdGatewayFilter（平台级 · 最终版）
 * ============================================================
 *
 *  【职责定位】
 *  ------------------------------------------------------------------
 *  在 Gateway 层统一收敛：
 *
 *  ✅ X-Client-Id（租户身份）
 *  ✅ X-Tenant-Id（租户隔离）
 *
 *  不做：
 *  ❌ 业务权限校验
 *  ❌ 用户角色校验
 *
 *  【架构原则】
 *  ------------------------------------------------------------------
 *  - WebFlux Gateway 只做 Header / 路由 / 校验
 *  - 所有阻塞 IO（Feign / JDBC）必须放在独立线程池或由 WebClient 调用
 *  - 所有 chain.filter 必须统一出口
 *
 *  【租户模型（SaaS 电商）】
 *  ------------------------------------------------------------------
 *  1. 每个租户 = 一个 client_id
 *  2. 微信登录 = 只有 user_id，没有 tenant_id
 *  3. 登录后业务请求 = 必须有 tenant_id
 *
 *  【请求类型判定】
 *  ------------------------------------------------------------------
 *  ┌──────────────┬──────────────────────────────┐
 *  │ 是否有 Token │ 处理逻辑                    │
 *  ├──────────────┼──────────────────────────────┤
 *  │ 否           │ 公共接口：clientId → tenantId │
 *  │ 是（JWT）    │ 优先 JWT tenantId            │
 *  │              │ 无 tenantId → clientId 兜底  │
 *  └──────────────┴──────────────────────────────┘
 *
 *  【核心出口】
 *  ------------------------------------------------------------------
 *  所有请求最终只通过 doFilter() 转发
 *
 *  @author aioveu
 *  @since 2026-07
 */
@Component
@Slf4j
public class ClientIdGatewayFilter implements GlobalFilter, Ordered {

    /** 前端 / 内部约定的租户标识 Header */

    private static final String HEADER_CLIENT_ID = "X-Client-Id";
    private static final String HEADER_TENANT_ID = "X-Tenant-Id";
    private static final Long NO_TENANT = -1L;

    private final ReactiveJwtDecoder jwtDecoder;
    private final ClientWhitelistWithRedisService clientWhitelistWithRedisService;

    //是 Gateway Filter 在启动阶段依赖了 Feign，触发了 Spring 的循环依赖保护
    /** 通过 WebClient 调用租户服务（非阻塞） */
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
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        log.info("【ClientIdGatewayFilter】进入 filter, path={}", exchange.getRequest().getURI().getPath());

        // ✅ 关键：先保存引用
//        GatewayFilterChain finalChain = chain;
        String path = exchange.getRequest().getURI().getPath();
        log.info("【ClientIdGatewayFilter】解析 clientId（优先级从高到低）path:{}", path);
        // ✅ 1. 敏感接口：必须用 JWT

        // ✅ JWT 接口：tenantId 来自 JWT，不再校验 clientId 白名单,✅ 只要带 Token，一律走 JWT
        if (hasAuthorizationHeader(exchange)) {
            return handleJwtRequest(exchange, chain);
        }

        // ✅ 2. 公共接口：clientId → tenantId ✅ 没 Token，才是公共接口
        return handlePublicClient(exchange, chain);

    }


    // ===============================
    // JWT 接口处理
    // ===============================

    /**
     * 处理携带 JWT 的请求
     *
     * 优先级：
     * 1. JWT 中已有 tenant_id → 直接使用
     * 2. JWT 无 tenant_id（登录态）→ 走 clientId 兜底逻辑
     */
    private Mono<Void> handleJwtRequest(ServerWebExchange exchange, GatewayFilterChain chain) {

        return resolveTenantIdFromJwt(exchange)
                .flatMap(tenantId -> {

                    ServerHttpRequest.Builder builder = exchange.getRequest().mutate();

                    // ✅ 1. 有 tenantId（业务态）
                    if (!NO_TENANT.equals(tenantId)) {
                        // ✅ 有 tenantId，正常往下走
                        log.info("【ClientIdGatewayFilter】JWT tenantId={}", tenantId);
                        builder.header("X-Tenant-Id", String.valueOf(tenantId));
                        return doFilter(exchange, chain, builder);

                    }


                        log.info("【ClientIdGatewayFilter】JWT 无 tenant_id（登录态）→ 走公共接口逻辑");

                        //------------------------------------------------------------------
                        // ✅ 2. JWT 无 tenant_id（登录态）→ 走公共逻辑
                        return resolveClientId(exchange)
                                .flatMap(clientId ->
                                        handleClientIdToTenant(exchange, chain, clientId, builder)
                                );

                        //------------------------------------------------------------------


                })
                .onErrorResume(e -> {
                    log.warn("【ClientIdGatewayFilter】JWT 解析失败，直接放行", e);
                    return unauthorized(exchange, "JWT 解析失败");
                })
                .switchIfEmpty(unauthorized(exchange, "JWT 无 tenantId"));
    }







    /**
     * 解析 clientId（优先级从高到低） （✅ 带校验）
     *     * 解析 clientId（优先级从高到低）
     *      * 返回 Mono<String> 以支持 WebFlux 非阻塞模型
     */
    // ===============================
    // 公共接口处理
    // ===============================

    /**
     * 处理无 Token 的公共接口
     *
     * 规则：
     * - clientId → 查 tenantId
     * - 查不到 → 放行（由下游决定）
     */
    private Mono<Void> handlePublicClient(ServerWebExchange exchange,GatewayFilterChain chain) {


        ServerHttpRequest.Builder builder = exchange.getRequest().mutate();
        // ✅ 防御：理论上不可能进来，但防止以后被人改 filter 顺序
        if (hasAuthorizationHeader(exchange)) {
            return unauthorized(exchange, "公共接口不允许携带 Authorization");
        }

        // 1. 解析 clientId (返回 Mono<String>)
        // 统一解析 clientId（不再区分敏感 / 公共）
        return resolveClientId(exchange)
                // 2. 如果解析失败或为空，给一个默认值
                // .defaultIfEmpty("system_default")
                // 3. 使用 flatMap 确保非阻塞
                .flatMap(clientId -> {
                    if ("system_default".equals(clientId)) {
                        return unauthorized(exchange, "非法 clientId: system_default");
                    }
                    return handleClientIdToTenant(exchange, chain, clientId, exchange.getRequest().mutate());
                });
    }







    // ===============================
    // 公共逻辑：clientId → tenantId
    // ===============================

    /**
     * ✅ 公共逻辑：clientId → tenantId → 转发
     *
     * 使用场景：
     * - 公共接口
     * - JWT 登录态兜底
     *
     * 职责：
     * - 校验 clientId 白名单
     * - 调用租户服务
     * - 拼 Header
     *
     * ❌ 不负责 chain.filter（统一出口）
     */
    private Mono<Void> handleClientIdToTenant(
            ServerWebExchange exchange,
            GatewayFilterChain chain,
            String clientId,
            ServerHttpRequest.Builder builder) {

        // ✅✅✅ 检测就放在这里 ✅✅✅
        if (!clientWhitelistWithRedisService.isValid(clientId)) {
            return forbidden(exchange, "非法 clientId: " + clientId);
        }
        log.debug("【ClientIdGatewayFilter】注入 X-Client-Id = {}", clientId);


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
                .doOnSubscribe(s -> log.info("【ClientIdGatewayFilter】调用租户服务, clientId={}", clientId))
                // 1. 调试用：看看 WebClient 到底返回了什么（如果有值的话）
                .doOnNext(tenantId -> log.info("【ClientIdGatewayFilter】租户服务返回 tenantId={}", tenantId))

                // 2. 过滤掉 null 和 tenantId 为空的情况
                .filter(Objects::nonNull)
                // 4. 处理有值的情况
                .flatMap(tenantId -> {

                    log.info("【ClientIdGatewayFilter】通过 clientId:{} 获取 tenantId:{}",
                            clientId, tenantId);
                    builder.header(HEADER_CLIENT_ID, clientId)
                            .header(HEADER_TENANT_ID, String.valueOf(tenantId));
                    return doFilter(exchange, chain, builder);

//                    return chain.filter(
//                            exchange.mutate()
//                                    .request(buildRequestWithTenantId(exchange, clientId, tenantId))
//                                    .build()
//                    );
                })
                .switchIfEmpty(unauthorized(exchange, "未查到租户, clientId=" + clientId));
    }

    // ===============================
    // 统一出口（非常重要）
    // ===============================

    /**
     * ✅ 所有 chain.filter 的唯一出口
     *
     * 好处：
     * - 不分散
     * - 易监控
     * - 易扩展（日志 / Trace / Metrics）
     */
    private Mono<Void> doFilter(
            ServerWebExchange exchange,
            GatewayFilterChain chain,
            ServerHttpRequest.Builder builder) {

        log.info("【ClientIdGatewayFilter】即将转发请求到 对应微服务");
        // ⚠️ 这里如果返回 null，WebFlux 会直接结束，不报错
        return chain.filter(exchange.mutate().request(builder.build()).build())
                .doOnSubscribe(s -> log.info("【ClientIdGatewayFilter】已转发请求"))
                .doOnError(e -> log.error("【ClientIdGatewayFilter】转发失败", e));
    }


    /* ===================== 终止请求工具方法 ===================== */

    /** ✅ 终止请求，不再 chain.filter */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String msg) {
        log.error("【ClientIdGatewayFilter】UNAUTHORIZED: {}", msg);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    private Mono<Void> forbidden(ServerWebExchange exchange, String msg) {
        log.error("【ClientIdGatewayFilter】FORBIDDEN: {}", msg);
        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        return exchange.getResponse().setComplete();
    }

    // ===============================
    // 工具 & 解析方法
    // ===============================

    /** 解析 clientId（Header / 域名 / 兜底） */
    /**
     * 解析 clientId（优先级从高到低） （✅ 带校验）
     *     * 解析 clientId（优先级从高到低）
     *      * 返回 Mono<String> 以支持 WebFlux 非阻塞模型
     */
    private Mono<String> resolveClientId(ServerWebExchange exchange) {


        // 2前端带了就用（小程序 / H5） 公共接口：信任前端 Header
        String clientIdFromHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HEADER_CLIENT_ID);

        log.info("【ClientIdGatewayFilter】前端带了就用（小程序 / H5） 公共接口：信任前端 Header:{}", clientIdFromHeader);

        if (StringUtils.isNotBlank(clientIdFromHeader)) {

            // ✅ 最小修改点：只取第一个 clientId，防拼接
            String cleanClientId = clientIdFromHeader.split(",")[0];
            log.info("【ClientIdGatewayFilter】公共接口，清洗 clientId = {} -> {}",
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





    private boolean isSensitivePath(String path) {
        return path.contains("/order/submit")
                || path.contains("/pay")
                || path.contains("/trade");
    }


    /**
     * 使用 ReactiveJwtDecoder 非阻塞解析 JWT中的 ClientId
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
        //client_id 永远是 String
        return jwtDecoder.decode(token)
                .map(jwt ->
                        jwt.getClaimAsString(JwtClaimConstants.Client.ID)

                )
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

    /**
     * 解析 JWT 中的 tenant_id
     *
     * 返回值语义：
     * - 有 tenant_id → 正常 Long
     * - 无 tenant_id → NO_TENANT（标记值）
     * - 无 Token / 解析失败 → Mono.empty()
     */
    private Mono<Long> resolveTenantIdFromJwt(ServerWebExchange exchange) {

        String auth = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(auth) || !auth.startsWith("Bearer ")) {
            return Mono.empty();
        }

        String token = auth.substring(7);

        //jwtDecoder.decode(token)  类型  Mono<Jwt>
        //.map(jwt -> Long / NO_TENANT)  Mono<Long>
        return jwtDecoder.decode(token)
                .map(jwt -> {
                        // ✅ 不抛异常、不返回 null
                        Long tenantId = ClaimUtils.getClaimAsLong(jwt, JwtClaimConstants.Tenant.ID);
                        if (tenantId == null) {
                            log.info("【ClientIdGatewayFilter】JWT 中无 tenant_id（登录态）,NO_TENANT");
                            return NO_TENANT;   // ✅ 关键
                        }
                        return tenantId;
                })
                .onErrorResume(e -> {
                    log.warn("JWT 解析 tenant_id 失败", e);
                    return Mono.<Long>empty();
                });
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


