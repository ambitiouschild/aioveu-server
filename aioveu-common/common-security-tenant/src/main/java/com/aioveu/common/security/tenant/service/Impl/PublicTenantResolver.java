package com.aioveu.common.security.tenant.service.Impl;


import com.aioveu.common.security.tenant.config.CommonTenantFeignAutoConfiguration;
import com.aioveu.common.security.tenant.service.TenantLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @ClassName: PublicTenantResolver
 * @Description TODO  配套的 PublicTenantResolver（同步版）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 10:22
 * @Version 1.0
 **/
@Slf4j
@Configuration
@AutoConfigureAfter(CommonTenantFeignAutoConfiguration.class)   //✅ 在自动装配类里注册
@RequiredArgsConstructor
public class PublicTenantResolver {



    //✅ 谁有 TenantLoader，谁才有 PublicTenantResolver  ✅ 彻底消灭 NPE
    @Bean
    @ConditionalOnMissingBean
    public PublicTenantResolver publicTenantResolver(
            @Autowired(required = false) TenantLoader tenantLoader
    ) {
        return new PublicTenantResolver(tenantLoader);
    }

    /**
     * ✅ 加载函数：由 aioveu-tenant 注入
     */
    /**
     * ✅ 可选：只有 aioveu-tenant 会提供
     */
    @Nullable
    private final TenantLoader tenantLoader;

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
//    private Long loadTenantId(String clientId) {
//
//        Long loadTenantId =  tenantQueryService.getTenantIdByClientId(clientId);
//        log.info("【PublicTenantResolver】clientId:{},加载 tenantId", clientId,loadTenantId);
//        return loadTenantId;
//    }

    //✅ 改进（防御式，日志友好）
    private Long loadTenantId(String clientId) {
        if (tenantLoader == null) {
            log.error("No TenantLoader available for clientId={}", clientId);
            throw new IllegalStateException(
                    "TenantLoader not configured. Check security.tenant.enabled and tenant-api dependency."
            );
        }
        log.info("Cache Miss, clientId={}", clientId);
        return tenantLoader.load(clientId);
    }

    /**
     * ✅ Filter 唯一入口  ✅ 或者：不要让 Caffeine 缓存异常
     */
    public Long resolve(String clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Missing X-Client-Id");
        }
        try {
            return cache.get(clientId);
        } catch (RuntimeException e) {
            log.error("Failed to resolve tenantId for clientId={}", clientId, e);
            throw e;
        }
    }




}
