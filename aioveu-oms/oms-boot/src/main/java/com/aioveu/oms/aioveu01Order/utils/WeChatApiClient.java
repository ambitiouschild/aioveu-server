package com.aioveu.oms.aioveu01Order.utils;


import com.aioveu.common.core.enums.oms.DeliveryCompanyCodeEnum;
import com.aioveu.common.core.enums.oms.LogisticsTypeEnum;
import com.aioveu.common.core.result.Result;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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
    public JsonNode uploadShippingInfo(String clientId, ObjectNode body) {
        String token = getAccessToken(clientId);
        return postRequest(String.format(UPLOAD_SHIPPING_URL, token), body);
    }

    public JsonNode notifyConfirmReceive(String clientId, ObjectNode body) {
        String token = getAccessToken(clientId);
        return postRequest(String.format(NOTIFY_RECEIVE_URL, token), body);
    }

    public JsonNode getOrderStatus(String clientId, ObjectNode body) {
        String token = getAccessToken(clientId);
        return postRequest(String.format(GET_ORDER_URL, token), body);
    }

    /* ========================= HTTP ========================= */


    /**
     * 通用 POST 请求方法
     */
    public JsonNode postRequest(String url, ObjectNode body) {

        log.info("【微信发货】POST 请求 url={}, body={}", url, body);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ObjectNode> request = new HttpEntity<>(body, headers);  //天然是 HttpEntity<ObjectNode>，编译器不会再报任何类型错误


        try {

            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    request,  // 现在是 HttpEntity<ObjectNode>，跟泛型对上了
                    JsonNode.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                // 非 2xx 但 exchange 没抛异常（说明没触发 DefaultResponseErrorHandler）
                //RestTemplate 默认把 4xx 当成异常抛出，response.getBody()拿不到微信的错误详情
                //把 412 的 response body 打出来
                String errMsg = response.getBody() != null ? response.getBody().toString() : "empty";
                log.error("【微信发货】HTTP {} 调用失败, url={}, body={}",
                        response.getStatusCode(), url, errMsg);
                throw new RuntimeException("微信接口返回 HTTP " + response.getStatusCode() + ", body=" + errMsg);
            }

        } catch (HttpClientErrorException e) {

            // ✅ 这里一定能拿到 body
            String responseBody = e.getResponseBodyAsString();
            log.error("【微信发货】HTTP {} 调用失败, url={}, responseBody={}",
                    e.getStatusCode(), url, responseBody);
            throw new RuntimeException("微信接口返回 HTTP " + e.getStatusCode()
                    + "，响应体: " + responseBody, e);
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



    /*
    * 解析响应
    * */
    private JsonNode parseResponse(String response, String url){
        try {
            JsonNode node = objectMapper.readTree(response);
            if (node.has("errcode") && node.get("errcode").asInt() != 0) {
                int errcode = node.get("errcode").asInt();
                String errmsg = node.has("errmsg") ? node.get("errmsg").asText() : "";
                log.error("【微信发货】微信业务错误, url={}, errcode={}, errmsg={}", url, errcode, errmsg);
                throw new RuntimeException(
                        String.format("微信接口业务错误: errcode=%d, errmsg=%s", errcode, errmsg));
            }
            return node;
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("解析微信响应JSON失败: " + response, e);
        }
    }



    /*
    * 在 WeChatApiClient里封装 Builder 方法
    * */


    /**
     * 构建微信发货请求体（主流设计）
     */
    public ObjectNode buildShippingRequestBody(
            String transactionId,   // ← 改用微信支付单号   （更可靠）
            LogisticsTypeEnum logisticsType,
            List<ShippingItem> shippingItems,
            List<ItemDesc> itemDescs,
            String payerOpenid) {


        // ========================
        // Step 4: 组装微信发货请求体
        // ========================
        log.info("【微信发货】开始组装微信发货请求参数，transactionId={}", transactionId);
        ObjectNode body = createObjectNode();

        // 1. order_key
        ObjectNode orderKey = createObjectNode();
        orderKey.put("order_number_type", 1); // 1=微信支付单号（transaction_id）  2  商户订单号（out_trade_no）


        orderKey.put("transaction_id", transactionId);
        body.set("order_key", orderKey);  // order_key 订单标识（用微信支付单号）

        // 2. logistics_type
        body.put("logistics_type", logisticsType.getCode()); // 1: 实体物流   // 2=无需物流（虚拟商品）

        // 3. delivery_mode（统一发货）
        body.put("delivery_mode", 1); // 1: 统一发货

        // 4. shipping_list  物流信息
        ArrayNode shippingList = createArrayNode();
        if (logisticsType == LogisticsTypeEnum.PHYSICAL) {
            for (ShippingItem item : shippingItems) {
                ObjectNode shipping = createObjectNode();
                shipping.put("tracking_no", item.getTrackingNo());
                shipping.put("express_company", item.getExpressCompany().getValue()); //只要确认传的是 "SF"这种微信编码而不是 "顺丰速运"

                // 可选：收件人联系方式
                if (item.getReceiverContact() != null) {
                    ObjectNode contact = createObjectNode();
                    contact.put("receiver_contact", item.getReceiverContact());
                    shipping.set("contact", contact);
                }
                shippingList.add(shipping);
            }
        }
        // logistics_type=2 时传空数组，不能省略
        body.set("shipping_list", shippingList);

        // 5. item_desc（必须是数组！）
        ArrayNode itemDescArray = createArrayNode();
        for (ItemDesc desc : itemDescs) {
            ObjectNode item = createObjectNode();
            item.put("item_name", desc.getItemName());
            item.put("item_count", desc.getItemCount());
            itemDescArray.add(item);
        }
        body.set("item_desc", itemDescArray);

        // 6. 时间戳（秒）
        long now = System.currentTimeMillis() / 1000;
        body.put("delivery_time", now);
        body.put("upload_time", now);

        // 7. 付款人 openid
        body.put("payer_openid", payerOpenid);

        return body;
    }


    // ============ DTO 内部类 ============

    public static class ShippingItem {
        private String trackingNo;
        private DeliveryCompanyCodeEnum expressCompany;
        private String receiverContact; // 可选，如 "+86-138****8000"

        public ShippingItem(String trackingNo, DeliveryCompanyCodeEnum expressCompany) {
            this.trackingNo = trackingNo;
            this.expressCompany = expressCompany;
        }

        public ShippingItem(String trackingNo, DeliveryCompanyCodeEnum expressCompany, String receiverContact) {
            this.trackingNo = trackingNo;
            this.expressCompany = expressCompany;
            this.receiverContact = receiverContact;
        }

        // getters
        public String getTrackingNo() { return trackingNo; }
        public DeliveryCompanyCodeEnum getExpressCompany() { return expressCompany; }
        public String getReceiverContact() { return receiverContact; }
    }

    public static class ItemDesc {
        private String itemName;
        private int itemCount;

        public ItemDesc(String itemName, int itemCount) {
            this.itemName = itemName;
            this.itemCount = itemCount;
        }

        public String getItemName() { return itemName; }
        public int getItemCount() { return itemCount; }
    }



}
