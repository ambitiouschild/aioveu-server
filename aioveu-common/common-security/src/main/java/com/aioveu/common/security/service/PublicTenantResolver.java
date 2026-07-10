package com.aioveu.common.security.service;


import com.aioveu.tenant.api.TenantFeignClient;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: PublicTenantResolver
 * @Description TODO  配套的 PublicTenantResolver（同步版）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 10:22
 * @Version 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class PublicTenantResolver {



    private final TenantQueryService tenantQueryService;

    /**
     * ✅ 同步 LoadingCache
     * Cache Miss 时同步查 Feign
     * refreshAfterWrite 保证后台异步刷新
     */
    private final LoadingCache<String, Long> cache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .expireAfterWrite(Duration.ofMinutes(5))
                    .build(this::loadTenantId);

    /**
     * ❌ 不允许业务直接调用
     * ✅ 只允许 Caffeine 调用
     */
    private Long loadTenantId(String clientId) {

        Long loadTenantId =  tenantQueryService.getTenantIdByClientId(clientId);
        log.info("【PublicTenantResolver】clientId:{},加载 tenantId", clientId,loadTenantId);
        return loadTenantId;
    }


    /**
     * ✅ Filter 唯一入口
     */
    public Long resolve(String clientId) {

        if (clientId == null) {
            throw new IllegalArgumentException("Missing X-Client-Id");
        }
        return cache.get(clientId);
    }




}
