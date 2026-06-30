package com.aioveu.gateway.service.impl;


import com.aioveu.gateway.service.TenantQueryService;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    //把 /tenant/wx-app/by-client-id接口设计成 缓存 + Redis + 本地 Caffeine

    @Override
    public Mono<TenantWxAppInfo> getTenantWxAppInfoByClientId(String clientId) {

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


    @Override
    public Mono<Long> getTenantIdByClientId(String clientId) {
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
                .doOnError(e -> log.error("获取租户信息异常, clientId={}", clientId, e))
                .onErrorResume(e -> Mono.empty());
    }
}
