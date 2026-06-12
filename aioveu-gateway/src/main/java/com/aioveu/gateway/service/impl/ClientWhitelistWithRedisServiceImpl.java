package com.aioveu.gateway.service.impl;
import com.aioveu.gateway.service.ClientWhitelistWithRedisService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ClientWhitelistServiceImpl
 * @Description TODO 网关clientId客户端检测服务
 *                       修正后的「生产级版本」（推荐你直接用）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 20:32
 * @Version 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientWhitelistWithRedisServiceImpl implements ClientWhitelistWithRedisService {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, Object> redisTemplate;
    //Redis 缓存（防止高频打库） 进阶优化（强烈推荐）
    private static final String CACHE_KEY = "CLIENT_WHITELIST";
    private static final long CACHE_TTL = 30; // 分钟
    /**
     * 校验 clientId 是否合法 （✅ 完全本地化，无 Feign）
     */
    @Override
    public boolean isValid(String clientId) {

        if (clientId == null || clientId.isBlank()) {
            log.warn("【Gateway】clientId 为空");
            return false;
        }


        // 1️.Redis 校验
        Boolean cached = redisTemplate.opsForSet()
                .isMember(CACHE_KEY, clientId);

        // ✅ 只有 true 才放行
        if (Boolean.TRUE.equals(cached)) {
            log.info("【Gateway】Redis 校验通过，clientId={}", clientId);
            return true;
        }


        // ❌ Redis 没有，直接拦截
        log.warn("【Gateway】Redis 未命中，拦截 clientId={}", clientId);
        return false;
    }



}
