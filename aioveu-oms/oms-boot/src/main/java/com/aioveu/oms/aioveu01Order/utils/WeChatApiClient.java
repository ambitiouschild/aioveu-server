package com.aioveu.oms.aioveu01Order.utils;


import com.aioveu.common.result.Result;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: WeChatApiClient
 * @Description TODO 核心工具类 (Token & HTTP)
 *                      这部分负责获取 access_token和发送 POST 请求
 *                      微信接口客户端（生产级）
 *                      支持多租户 + Redis Token缓存 + 防并发
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 17:11
 * @Version 1.0
 **/
/**
 * 微信接口客户端（生产级）
 * 支持多租户 + Redis Token缓存 + 防并发
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatApiClient {

//    @Value("${wechat.miniapp.appid}")
//    private String wxAppid;
//
//    @Value("${wechat.miniapp.secret}")
//    private String appSecret;

    private final TenantFeignClient tenantFeignClient;
    private final RedisTemplate<String, String> redisTemplate;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /** Token 缓存前缀 */
    private static final String REDIS_TOKEN_KEY_PREFIX = "WX:ACCESS_TOKEN:";
    /** Token 过期时间（秒） */
    private static final long TOKEN_EXPIRE_SECONDS = 7000L;

    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String UPLOAD_SHIPPING_URL = "https://api.weixin.qq.com/wxa/sec/order/upload_shipping_info?access_token=%s";
    private static final String NOTIFY_RECEIVE_URL = "https://api.weixin.qq.com/wxa/sec/order/notify_confirm_receive?access_token=%s";
    private static final String GET_ORDER_URL = "https://api.weixin.qq.com/wxa/sec/order/get_order?access_token=%s";



    /* ========================= Token ========================= */
    /**
     * 获取 AccessToken (生产环境请务必加入 Redis 缓存) （Redis + 防并发）
     */
    public String getAccessToken(String clientId) {


        String redisKey = REDIS_TOKEN_KEY_PREFIX + clientId;

        // 1. Redis 命中
        String token = redisTemplate.opsForValue().get(redisKey);
        if (token != null) {
            return token;
        }


        // 2. 防并发锁（防止多个线程同时调微信）
        String lockKey = redisKey + ":LOCK";
        Boolean locked = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(locked)) {
            try {

                //真正获取微信配置的地方
                token = doFetchAccessToken(clientId);
                redisTemplate.opsForValue()
                        .set(redisKey, token, TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
                return token;
            } finally {
                redisTemplate.delete(lockKey);
            }
        } else {
            // 未抢到锁，稍等重试
            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {}
            return getAccessToken(clientId);
        }


    }


    /**
     * 真实调用微信接口
     */
    private String doFetchAccessToken(String clientId) {


        log.info("【微信发货】开始查询clientId: {}", clientId);
        // 这里需要你实现数据库查询
        Result<TenantWxAppInfo> result =
                tenantFeignClient.getTenantWxAppInfoByClientId(clientId);
        TenantWxAppInfo tenantWxAppInfo = result.getData();
        log.info("【微信发货】查询到的tenantWxAppInfo: {}", tenantWxAppInfo);

        if (tenantWxAppInfo == null || tenantWxAppInfo.getWxAppid() == null) {
            throw new RuntimeException("【微信发货】租户微信配置不存在，clientId=" + clientId);
        }

        String wxAppid = tenantWxAppInfo.getWxAppid();
        Long tenantId = tenantWxAppInfo.getTenantId();
        String appSecret = tenantWxAppInfo.getAppSecret();
        log.info("【微信发货】查询到租户信息 - wxAppid: {}, tenantId: {}, appSecret: {}", wxAppid, tenantId, appSecret);

        String url = String.format(
                TOKEN_URL,
                wxAppid,
                appSecret
        );

        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode node = objectMapper.readTree(response);
            if (node.has("errcode") && node.get("errcode").asInt() != 0) {
                throw new RuntimeException("【微信发货】微信获取token失败：" + response);
            }
            return node.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("【微信发货】解析微信token失败：" + response, e);
        }
    }


    /* ========================= 发货 ========================= */
    // 封装接口地址
    public JsonNode uploadShippingInfo(String clientId, Object body) {
        String token = getAccessToken(clientId);
        return postRequest(String.format(UPLOAD_SHIPPING_URL, token), body);
    }

    public JsonNode notifyConfirmReceive(String clientId, Object body) {
        String token = getAccessToken(clientId);
        return postRequest(String.format(NOTIFY_RECEIVE_URL, token), body);
    }

    public JsonNode getOrderStatus(String clientId, Object body) {
        String token = getAccessToken(clientId);
        return postRequest(String.format(GET_ORDER_URL, token), body);
    }

    /* ========================= HTTP ========================= */


    /**
     * 通用 POST 请求方法
     */
    public JsonNode postRequest(String url, Object body) {
        String response = restTemplate.postForObject(url, body, String.class);
        try {
            JsonNode node = objectMapper.readTree(response);
            if (node.has("errcode") && node.get("errcode").asInt() != 0) {
                throw new RuntimeException("微信接口调用失败：" + response);
            }
            return node;
        } catch (Exception e) {
            throw new RuntimeException("微信接口调用失败: " + response);
        }
    }


    /*
    * 在 WeChatApiClient里封装 JSON 构建方法
    * 方案一（✅ 强烈推荐｜最优雅）
    * */
    public ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }

}
