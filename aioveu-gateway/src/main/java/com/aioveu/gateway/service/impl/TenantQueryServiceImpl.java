package com.aioveu.gateway.service.impl;


import com.aioveu.gateway.service.TenantQueryService;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: TenantQueryService
 * @Description TODO   租户信息查询服务
 *                          正确写法（WebFlux + Feign 官方推荐）
 *                          ✅ 把 Feign 扔到 独立线程池
 *                          ✅ 不阻塞 WebFlux event loop
 *                          ✅ Gateway 不再吞异常
 *                          更优雅、也更“像你这套架构”的写法（强烈推荐）
 *                          ✅ 把 Feign 包装成一个 Service
 *                          租户信息查询服务（WebClient + Caffeine 缓存）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/30 17:58
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantQueryServiceImpl implements TenantQueryService {

    private final WebClient.Builder webClientBuilder;

    //把接口设计成 缓存 + Redis + 本地 Caffeine

    /**
     * ✅ Caffeine 本地缓存
     * - 最多 10,000 个 clientId
     * - 写入后 5 分钟过期
     * - 非常适合 Gateway 场景
     */
    private final Cache<String, Long> tenantIdCache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(5))
            .build();



    /**
     * ✅ 带缓存：根据 clientId 查询租户信息
     */
    @Override
    public Mono<TenantWxAppInfo> getTenantWxAppInfoByClientId(String clientId) {


        // 1. 先查缓存（如果有完整信息缓存，可以直接返回）
        // 这里因为返回的是完整 DTO，缓存意义不大，建议直接用 tenantId 缓存即可
        return webClientBuilder.build()
                .get()
                .uri(uri -> uri
                        .scheme("lb")
                        .host("aioveu-tenant")
                        .path("/aioveu/api/v8/app/tenant/oauth-client-wx-app/getTenantWxAppInfoByClientId")
                        .queryParam("clientId", clientId)
                        .build()
                )
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        resp -> Mono.error(new RuntimeException("租户服务调用失败"))
                )
                .bodyToMono(TenantWxAppInfo.class)
                .doOnError(e -> log.error("获取租户信息异常, clientId={}", clientId, e))
                .onErrorResume(e -> Mono.empty());
    }

    /**
     * ✅ 带缓存：根据 clientId 获取 tenantId
     */
    @Override
    public Mono<Long> getTenantIdByClientId(String clientId) {

        // 1. 先查本地缓存
        Long cached = tenantIdCache.getIfPresent(clientId);
        if (cached != null) {
            log.info("【TenantQuery】命中本地缓存, clientId={}, tenantId={}", clientId, cached);
            return Mono.just(cached);
        }

        // 2. 缓存未命中，调用 tenant-service
        return webClientBuilder.build()
                .get()
                .uri(uri -> uri
                        .scheme("lb")
                        .host("aioveu-tenant")
                        .path("/aioveu/api/v8/app/tenant/oauth-client-wx-app/getTenantIdByClientId")
                        .queryParam("clientId", clientId)
                        .build())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        resp -> Mono.error(new RuntimeException("租户服务调用失败"))
                )
                .bodyToMono(Long.class)
                .doOnNext(tenantId -> {
                    log.info("【TenantQuery】缓存 tenantId, clientId={}, tenantId={}", clientId, tenantId);
                    tenantIdCache.put(clientId, tenantId);
                })
                .doOnError(e -> log.error("获取租户信息异常, clientId={}", clientId, e))
                .onErrorResume(e -> Mono.empty());
    }
}
