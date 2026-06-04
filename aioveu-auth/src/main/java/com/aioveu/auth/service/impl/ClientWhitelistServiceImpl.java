package com.aioveu.auth.service.impl;


import com.aioveu.auth.aioveu04Oauth2RegisteredClient.mapper.Oauth2RegisteredClientMapper;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.entity.Oauth2RegisteredClient;
import com.aioveu.auth.service.ClientWhitelistService;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ClientWhitelistServiceImpl
 * @Description TODO 白名单客户端服务
 *                       修正后的「生产级版本」（推荐你直接用）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 20:32
 * @Version 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientWhitelistServiceImpl implements ClientWhitelistService {


    private final Oauth2RegisteredClientMapper oauth2RegisteredClientMapper;


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RedisTemplate<String, Object> redisTemplate;
    //Redis 缓存（防止高频打库） 进阶优化（强烈推荐）
    private static final String CACHE_KEY = "CLIENT_WHITELIST";
    private static final long CACHE_TTL = 30; // 分钟
    private final TenantFeignClient tenantFeignClient;
    /**
     * 校验 clientId 是否合法
     */
    @Override
    public boolean isValid(String clientId) {

        // 1️.Redis 校验
        Boolean cached = redisTemplate.opsForSet()
                .isMember(CACHE_KEY, clientId);

        if (Boolean.TRUE.equals(cached)) {
            return true;
        }

        // 2️.DB 校验
        boolean valid = checkFromDb(clientId);

        // 3️合法则写缓存（带 TTL）
        if (valid) {
            redisTemplate.opsForSet()
                    .add(CACHE_KEY, clientId);
            redisTemplate.expire(
                    CACHE_KEY,
                    CACHE_TTL,
                    TimeUnit.MINUTES
            );
        }

        return valid;
    }

    /*
    * DB 校验
    * */
    private boolean checkFromDb(String clientId) {

        // 1️.基础校验
        if (StringUtils.isBlank(clientId)) {
            log.warn("clientId 为空");
            return false;
        }

        // 2️.防注入 / 非法字符
        if (!clientId.matches("^[A-Za-z0-9_-]{3,64}$")) {
            log.warn("clientId 格式非法: {}", clientId);
            return false;
        }

        // 3️.查数据库（✅ 唯一信任源）
        Oauth2RegisteredClient client = oauth2RegisteredClientMapper.selectOne(
                new LambdaQueryWrapper<Oauth2RegisteredClient>()
                        .eq(Oauth2RegisteredClient::getClientId, clientId)
        );

        if (client == null) {
            log.warn("未知 clientId: {}", clientId);
            return false;
        }


        /*
        *   {
              "settings": {
                "requireProofKey": false,
                "requireAuthorizationConsent": false
              },
              "enabled": true
            }
        *
        * */

        // 4️.是否被禁用 启用校验（非常重要）
        // ✅ 从 clientSettings 判断是否启用
        String clientSettings = client.getClientSettings();
        if (StringUtils.isBlank(clientSettings)) {
            log.warn("clientSettings 为空，clientId={}", clientId);
            return false;
        }

        try {
        // 1. 通过clientId获取tenantId
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("【ClientWhitelistService】通过clientId获取tenantWxAppInfo:{}",tenantWxAppInfo);

        Long tenantId = tenantWxAppInfo.getTenantId();

        // 2. 业务校验
        if (tenantWxAppInfo == null || tenantWxAppInfo.getIsDeleted() == 1 || tenantWxAppInfo.getEnabled() == 0) {
            log.warn("客户端被业务禁用或已删除, clientId={}", clientId);
            return false;
        }
        } catch (Exception e) {
            log.error("解析 clientSettings 失败", e);
            return false;
        }

        // 3. 继续走 Spring Security 流程...
        return true;

    }


}
