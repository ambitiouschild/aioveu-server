package com.aioveu.oms.aioveu01Order.utils;


import cn.hutool.core.collection.CollUtil;
import com.aioveu.oms.aioveu01Order.enums.OrderStatusEnum;
import com.aioveu.oms.aioveu01Order.mapper.OmsOrderMapper;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.service.admin.OmsOrderService;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import com.aioveu.oms.aioveu03OrderDelivery.enums.DeliveryStatusEnum;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;
import com.aioveu.oms.aioveu03OrderDelivery.service.OmsOrderDeliveryService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: ShippingTaskScheduler
 * @Description TODO 定时任务完整实现 （⭐重点：定时兜底）
 *                          自动兜底  防止人工漏点发货
 *                          幂等     同一订单只发一次
 *                          容错     微信失败不影响下次
 *                          可监控   日志清晰
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/9 10:26
 * @Version 1.0
 **/
@Component
@Slf4j
@RequiredArgsConstructor
public class ShippingTaskScheduler {

    private final OrderService orderService;

    private final OmsOrderMapper omsorderMapper;

    private final OmsOrderDeliveryService omsOrderDeliveryService;


    /**
     * 每 5 分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void autoUploadShipping() {

        log.info("【定时任务】开始自动上传发货信息");

        // 1️查询【已支付但未上传发货】的订单
        List<OmsOrder> orders =
                omsorderMapper.listPaidButNotShipped();

        if (CollUtil.isEmpty(orders)) {
            log.info("【定时任务】暂无需要上传发货的订单");
            return;
        }

        for (OmsOrder order : orders) {

            String orderSn = order.getOrderSn();
            Long orderId = order.getId();

            try {
                log.info("【定时任务】准备上传发货 orderSn={}", orderSn);

                // ✅ 查发货信息（下单时已保存）
                OmsOrderDelivery delivery =
                        omsOrderDeliveryService.selectByOrderId(orderId);

                if (delivery == null) {
                    log.warn("❌ 发货信息不存在，跳过 orderSn={}", orderSn);
                    continue;
                }

                // ✅ 已发货直接跳过（幂等）
                if (delivery.getDeliveryStatus() == DeliveryStatusEnum.SYNCED) {
                    log.info("⏭ 已上传微信，跳过 orderSn={}", orderSn);
                    continue;
                }

                // ✅ 自动发货（不依赖前端）
                JsonNode result =
                        orderService.uploadShipping(orderSn);

                if (result.has("errcode")
                        && result.get("errcode").asInt() == 0) {

                    order.setStatus(OrderStatusEnum.SHIPPED);
                    omsorderMapper.updateById(order);

                    // ✅ 标记发货已同步
                    delivery.setDeliveryStatus(DeliveryStatusEnum.SYNCED);
                    delivery.setDeliveryTime(new Date());
                    omsOrderDeliveryService.updateById(delivery);

                    log.info("✅ 定时任务发货成功 orderSn={}", orderSn);
                } else {
                    log.warn("❌ 微信发货失败 orderSn={}, errmsg={}",
                            orderSn, result.get("errmsg"));
                }

            } catch (Exception e) {
                log.error("❌ 定时任务发货异常 orderSn=" + orderSn, e);
            }
        }
    }
}
