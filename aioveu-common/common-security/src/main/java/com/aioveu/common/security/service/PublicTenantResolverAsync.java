package com.aioveu.common.security.service;


import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: PublicTenantResolver
 * @Description TODO  配套的 PublicTenantResolver（异步版）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 10:22
 * @Version 1.0
 **/
@Slf4j
//@Component
@RequiredArgsConstructor
public class PublicTenantResolverAsync {



    private final TenantQueryService tenantQueryService;

    /**
     * ✅ 缓存
     * - 最大 10,000 个 clientId
     * - 写入后 5 分钟过期
     * - 后台异步刷新（不阻塞读）
     */
    private final LoadingCache<String, CompletableFuture<Long>> cache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .refreshAfterWrite(Duration.ofMinutes(5))
                    .build(this::loadTenantIdAsync);

    /**
     * ❌ 不允许业务直接调用
     * ✅ 只允许 Caffeine 调用
     */
    private CompletableFuture<Long> loadTenantIdAsync(String clientId) {
        log.info("【PublicTenantResolver】异步刷新 tenantId, clientId={}", clientId);

        return CompletableFuture.supplyAsync(() ->
                tenantQueryService.getTenantIdByClientId(clientId)
        );
    }


    /**
     * ✅ Filter 唯一入口
     * - 永远不阻塞
     * - 返回的是 Future
     */
    public CompletableFuture<Long> resolve(HttpServletRequest request) {
        String clientId = request.getHeader("X-Client-Id");
        if (clientId == null) {
            return CompletableFuture.failedFuture(
                    new BadRequestException("Missing X-Client-Id")
            );
        }

        return cache.get(clientId);
    }




}
