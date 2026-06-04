package com.aioveu.oms.aioveu01Order.service.impl;


import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.service.WeChatShippingService;
import com.aioveu.oms.aioveu01Order.service.admin.OmsOrderService;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import com.aioveu.oms.aioveu01Order.utils.WeChatApiClient;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;
import com.aioveu.oms.aioveu02OrderItem.service.OmsOrderItemService;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import com.aioveu.oms.aioveu03OrderDelivery.service.OmsOrderDeliveryService;
import com.aioveu.ums.api.MemberFeignClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: WeChatShippingServiceImpl
 * @Description TODO 微信发货管理的三个核心接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 17:15
 * @Version 1.0
 **/
/**
 * 微信发货管理服务实现（生产级）
 */
/**
 * @description 以自有订单系统为主的微信发货同步服务
 *               对外只暴露 orderId，内部聚合数据
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WeChatShippingServiceImpl  implements WeChatShippingService {

    private final WeChatApiClient weChatApiClient;

    private final OrderService orderService;                 // 订单DAO
    private final OmsOrderDeliveryService omsOrderDeliveryService; // 发货单DAO
    private final OmsOrderItemService omsOrderItemService;

    // 会员服务Feign客户端
    private final MemberFeignClient memberFeignClient;

    /**
     * 1. 录入发货信息（以自有系统为准）
     * @param orderSn 自家系统的订单orderSn
     */
    @Override
    public JsonNode uploadShipping(String orderSn) {

        // ====== Step 1: 从自家数据库取订单 （唯一真理源）======
        OmsOrder order = orderService.getByOrderNo(orderSn);
        if (order == null) {
            throw new RuntimeException("订单不存在，orderSn: " + orderSn);
        }

        Long orderId = orderService.getOrderIdByOrderNo(orderSn);
        if (orderId == null) {
            throw new RuntimeException("订单不存在，orderId: " + orderId);
        }

        // ====== Step 2: 取发货信息 ======
        OmsOrderDelivery delivery = omsOrderDeliveryService.selectByOrderId(orderId);
        if (delivery == null) {
            throw new RuntimeException("发货信息不存在，订单ID: " + orderId);
        }

        // 	 * 物流状态【0->运输中；1->已收货】
        if (delivery.getDeliveryStatus() == 0) {
            log.warn("订单已同步微信，跳过。orderId={}", orderId);
            ObjectNode skipNode = weChatApiClient.createObjectNode();
            skipNode.put("code", "SKIPPED");
            skipNode.put("msg", "already synced");
            return skipNode;
        }

        // ====== Step 3: 查订单商品 ======
        List<OmsOrderItem> orderItems=  omsOrderItemService.listByOrderId(orderId);

        String itemDesc = orderItems.stream()
                .map(i -> i.getSpuName()
                        + (StringUtils.isNotBlank(i.getSkuName())
                        ? "【" + i.getSkuName() + "】"
                        : "")
                        + " x" + i.getQuantity())
                .collect(Collectors.joining("；"));

        //iPhone 15 Pro【黑色 256G】 x1；官方硅胶壳【午夜色】 x2
        log.info("生成 item_desc（微信推荐格式）:{}",itemDesc);

        // ====== Step 4: 从订单中获取用户OpenID======
        // 1. 从订单中获取会员ID
        Long memberId = order.getMemberId();
        // 2. 根据 memberId 远程调用或查库获取用户信息,获取用户的微信OpenID
        log.info("【会员微服务】获取用户OpenID，会员ID: {}", memberId);
        Result<String> openIdResult = memberFeignClient.getOpenIdByMemberId(memberId);

        String openId = openIdResult.getData();
        log.info("【会员微服务】用户OpenID获取成功: {}", openId);

        if (StringUtils.isBlank(openId)) {
            throw new RuntimeException(
                    "用户openid不存在，memberId=" + order.getMemberId()
            );
        }


        // ====== Step 5: 组装微信需要的 Body ======
        ObjectNode body = weChatApiClient.createObjectNode();

        // order_key 订单标识（用微信支付单号）
        ObjectNode orderKey = body.putObject("order_key");
        orderKey.put("order_number_type", 2); // 2: 微信支付单号
        orderKey.put("transaction_id", order.getTransactionId());

        body.put("logistics_type", 1); // 1: 实体物流
        body.put("delivery_mode", 1);  // 1: 统一发货

        // shipping_list 物流信息
        ArrayNode shippingList = body.putArray("shipping_list");
        ObjectNode shippingItem = shippingList.addObject();
        shippingItem.put("tracking_no", delivery.getDeliverySn());
        shippingItem.put("express_company", delivery.getDeliveryCompany());
        shippingItem.put("item_desc", itemDesc); // 商品概述

        //  微信发货参数 time & payer  时间 & 支付人
        body.put("upload_time", java.time.OffsetDateTime.now().toString());
        ObjectNode payer = body.putObject("payer");
        payer.put("openid", openId);


        // 发货时直接使用 clientId（✅ 正确）
        return weChatApiClient.uploadShippingInfo(order.getClientId(), body);
    }

    /**
     * 2. 提醒用户确认收货
     * @param orderSn 自家系统的订单orderSn
     */
    @Override
    public JsonNode notifyConfirmReceive(String orderSn) {

        // ====== Step 1: 从自家数据库取订单 ======
        OmsOrder order = orderService.getByOrderNo(orderSn);
        if (order == null) {
            throw new RuntimeException("订单不存在，orderSn: " + orderSn);
        }

        ObjectNode body = weChatApiClient.createObjectNode();

        ObjectNode orderKey = body.putObject("order_key");
        orderKey.put("order_number_type", 2);
        orderKey.put("transaction_id", order.getTransactionId());

        // 秒级时间戳
        body.put("received_time", System.currentTimeMillis() / 1000);

        return weChatApiClient.notifyConfirmReceive(order.getClientId(), body);
    }

    /**
     * 3. 查询订单状态
     * @param orderSn 自家系统的订单orderSn
     */
    @Override
    public JsonNode queryOrder( String orderSn) {

        // ====== Step 1: 从自家数据库取订单 ======
        OmsOrder order = orderService.getByOrderNo(orderSn);
        if (order == null) {
            throw new RuntimeException("订单不存在，orderSn: " + orderSn);
        }


        ObjectNode body = weChatApiClient.createObjectNode();
        body.put("transaction_id", order.getTransactionId());

        return weChatApiClient.getOrderStatus(order.getClientId(), body);
    }
}
