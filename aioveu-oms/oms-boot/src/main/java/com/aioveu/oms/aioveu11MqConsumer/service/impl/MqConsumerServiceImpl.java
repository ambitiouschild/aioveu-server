package com.aioveu.oms.aioveu11MqConsumer.service.impl;


import com.aioveu.common.enums.pay.PaymentStatusEnum;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.rabbitmq.constant.PayCommonMqConstantWithBizName;
import com.aioveu.common.enums.oms.OrderStatusEnum;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.service.app.OrderService;
import com.aioveu.oms.aioveu04OrderLog.service.OmsOrderLogService;
import com.aioveu.oms.aioveu05OrderPay.mapper.OmsOrderPayMapper;
import com.aioveu.oms.aioveu05OrderPay.model.entity.OmsOrderPay;
import com.aioveu.oms.aioveu08MqConsumeRecord.enums.ConsumeStatusEnum;
import com.aioveu.oms.aioveu08MqConsumeRecord.service.MqConsumeRecordService;
import com.aioveu.oms.aioveu11MqConsumer.model.vo.OrderPaySuccessDTO;
import com.aioveu.oms.aioveu11MqConsumer.service.MqConsumerService;
import com.aioveu.oms.aioveu11MqConsumer.utils.MqConsumerUtils;
//import com.aioveu.pay.model.PaymentSuccessMessage;

import com.aioveu.common.rabbitmq.producer.model.payment.PaymentSuccessMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: MqConsumerServiceImpl
 * @Description TODO MQ消费者服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 20:22
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class MqConsumerServiceImpl implements MqConsumerService {

    private final MqConsumerUtils mqConsumerUtils;
    private final MqConsumeRecordService mqConsumeRecordService;
    private final OrderService orderService;
    private final OmsOrderLogService omsOrderLogService;
    private final ObjectMapper objectMapper;

    private final OmsOrderPayMapper omsOrderPayMapper;

    @Value("${spring.application.name}")
    private String applicationName;

    private final String paymentSuccessTopic =
            PayCommonMqConstantWithBizName.RoutingKey.SUCCESS;
    /**
     * 处理支付成功消息（JSON字符串版本）
     * 消息体格式示例：
     * {
     *   "messageId": "PAY_20250316143025123_01_0001_456",
     *   "paymentNo": "PAY20250316143025",
     *   "orderNo": "ORDER20250316143025",
     *   "transactionId": "4200001972202503151234567890",
     *   "amount": 100.00,
     *   "channel": "wechat",
     *   "memberId": 1001,
     *   "paymentTime": "2025-03-16T14:30:25"
     * }
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentSuccess(String messageBody) {

        log.info("【MQ消费者】开始处理支付成功消息: {}", messageBody);

        try {

            // 1. 解析消息体
            OrderPaySuccessDTO paySuccess = mqConsumerUtils.parsePaymentMessage(messageBody);

            if (paySuccess == null) {
                log.error("【MQ消费者】解析支付消息失败: {}", messageBody);
                return false;
            }

            // 2. 调用对象版本处理
            return handlePaymentSuccess(paySuccess);

        } catch (Exception e) {
            log.error("【MQ消费者】处理消息异常: {}", messageBody, e);
            return false;
        }
    }

    /**
     * 处理支付成功消息（对象版本）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentSuccess(PaymentSuccessMessage message) {

        log.info("✅ Service 里统一做幂等（你现在的代码是对的）");
        log.info("✅ 幂等只在 Service 层做");
        log.info("✅ Consumer 不再关心业务");
        log.info("✅ MqConsumerServiceImpl\n" +
                "   ├── 幂等校验\n" +
                "   ├── 订单状态校验\n" +
                "   ├── 金额校验\n" +
                "   ├── 更新订单\n" +
                "   ├── 记录消费状态\n" +
                "   └── 后置业务");

        if (message == null) {
            log.error("【MQ消费者】消息对象为空");
            return false;
        }

        Long tenantId = message.getTenantId(); // ✅ 消息里必须有
        if (tenantId == null) {
            log.error("【MQ消费者】tenantId为空");
            return false;
        }

        long startTime = System.currentTimeMillis();
        String messageId = message.getMessageId();
        String orderSn = message.getOrderNo();
        String consumerGroup = applicationName + "-payment-consumer";

        log.info("【MQ消费者】开始处理支付成功消息: messageId={}, orderNo={}", messageId, orderSn);

        try {

            // 1. PaymentSuccessMessage对象无需解析


            // 2. 消费记录幂等性检查
            //检查消息是否已消费

            boolean alreadyProcessed = mqConsumeRecordService.isMessageConsumed(messageId, consumerGroup);

            if (alreadyProcessed) {
                log.warn("【MQ消费者】消息已处理，跳过: messageId={}", messageId);
                return true;
            }

            // 2. 记录消费开始
            mqConsumeRecordService.recordConsumeStart(messageId, paymentSuccessTopic,
                    consumerGroup, orderSn);

            // 3. 验证消息
            if (!mqConsumerUtils.validatePaymentMessage(message)) {
                log.error("【MQ消费者】消息验证失败: orderSn={}", orderSn);
                mqConsumeRecordService.updateConsumeStatus(messageId, consumerGroup,
                        ConsumeStatusEnum.FAILED, "消息验证失败");
                return false;
            }


            // 4. 查询订单
            OmsOrder order = orderService.getByOrderNo(orderSn);
            if (order == null) {
                log.error("【订单消费】订单不存在: orderSn={}", orderSn);
                mqConsumeRecordService.updateConsumeStatus(messageId, consumerGroup,
                        ConsumeStatusEnum.FAILED, "订单不存在");
                return false;
            }

            // 5. 幂等性检查订单状态
            if (order.getStatus() != OrderStatusEnum.UNPAID) {
                log.warn("【MQ消费者】订单状态不是待支付，跳过: orderSn={}, status={}",
                        orderSn, order.getStatus());
                mqConsumeRecordService.updateConsumeStatus(messageId, consumerGroup,
                        ConsumeStatusEnum.SUCCESS, "订单已处理");
                return true;
            }


            // 6. 验证支付金额
            if (!mqConsumerUtils.validatePaymentAmount(order, message.getAmount())) {
                log.error("【MQ消费者】支付金额不匹配: orderSn={}, 订单金额={}, 支付金额={}",
                        orderSn, order.getTotalAmount(), message.getAmount());
                mqConsumeRecordService.updateConsumeStatus(messageId, consumerGroup,
                        ConsumeStatusEnum.FAILED, "支付金额不匹配");
                return false;
            }


            // 3. 更新订单状态
            boolean updateSuccess = orderService.updateOrderPaymentStatus(order, message);

            if (!updateSuccess) {
                log.error("【MQ消费者】更新订单状态失败: orderSn={}", orderSn);
                mqConsumeRecordService.updateConsumeStatus(messageId, consumerGroup,
                        ConsumeStatusEnum.FAILED, "更新订单状态失败");
                return false;
            }

            // 8. 记录操作日志
            omsOrderLogService.addOrderLogs(
                    order.getId(),
                    OrderStatusEnum.PAID.getValue(),
                    "系统",
                    "【MQ消费者】"
            );


            // 8. 更新消费状态
            mqConsumeRecordService.updateConsumeStatus(messageId, consumerGroup,
                    ConsumeStatusEnum.SUCCESS, null);

            //已经记录并保存
//            mqConsumeRecordService.markConsumed(messageId, orderNo);

            // 10. 触发后续业务
            triggerPostPaymentLogic(order, message);

            long costTime = System.currentTimeMillis() - startTime;
            log.info("【MQ消费者】处理完成: orderSn={}, 耗时={}ms", orderSn, costTime);

            return true;
        } catch (Exception e) {
            log.error("【MQ消费者】处理异常: messageId={}, orderSn={}", messageId, orderSn, e);


            mqConsumeRecordService.updateConsumeStatus(messageId, consumerGroup,
                    ConsumeStatusEnum.FAILED, e.getMessage());


            // 根据重试次数决定是否重试 ✅ 正确做法：从消费记录表查
            //✅ 不要在 PaymentSuccessMessage里放 retryCount
            //✅ 重试次数永远来自「消费记录表」
            int retryCount =
                    mqConsumeRecordService.getRetryCount(messageId, consumerGroup);


            if (retryCount < 3) {
                throw new RuntimeException("处理失败，需要重试", e);
            } else {
                log.error("消息超过最大重试次数，进入死信队列: orderNo={}", orderSn);
                // 可以发送到死信队列
                mqConsumeRecordService.updateConsumeStatus(
                        messageId, consumerGroup, ConsumeStatusEnum.DEAD, "超过最大重试次数");
            }
            throw new BusinessException("处理支付成功消息异常", e);
        }
    }


    /**
     * 处理支付成功消息（DTO版本）
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentSuccess(OrderPaySuccessDTO paySuccess) {
        if (paySuccess == null) {
            log.error("【MQ消费者】消息DTO为空");
            return false;
        }

        // 转换为 PaymentSuccessMessage
        PaymentSuccessMessage message = convertToPaymentSuccessMessage(paySuccess);
        return handlePaymentSuccess(message);
    }






    /**
     * 触发后续业务逻辑
     */
    private void triggerPostPaymentLogic(OmsOrder order, PaymentSuccessMessage message) {
        try {
            // 1. 发送支付成功通知
            sendPaymentSuccessNotify(order, message);

            // 2. 扣减库存（如果下单时没扣）
            // inventoryService.deductStock(order.getId());

            // 3. 增加商品销量
            // productService.increaseSales(order.getOrderItems());

            // 4. 发放积分
            // memberService.grantPoints(order.getMemberId(), order.getTotalAmount());

            // 5. 使用优惠券
            // couponService.useCoupon(order.getCouponId());

            // Pay 回调 → OMS
//            OmsOrderPay pay = OmsOrderPay.builder()
//                    .orderId(order.getId())
//                    .paySn(payOrder.getPaymentNo())
//                    .payAmount(payOrder.getAmount())
//                    .payType(payOrder.getPaymentChannel().getCode())
//                    .payStatus(PaymentStatusEnum.PAID)
//                    .payTime(now)
//                    .build();
//
//            omsOrderPayMapper.insert(pay);


            log.info("支付后业务逻辑处理完成: orderNo={}", order.getOrderSn());

        } catch (Exception e) {
            log.error("支付后业务逻辑处理异常", e);
            // 这里不抛异常，避免影响主流程
        }
    }

    /**
     * 发送支付成功通知
     */
    private void sendPaymentSuccessNotify(OmsOrder order, PaymentSuccessMessage message) {
        try {
            // 发送模板消息
            // wechatService.sendPaySuccessTemplateMsg(
            //     order.getMemberId(),
            //     order.getOrderNo(),
            //     order.getTotalAmount(),
            //     paySuccess.getPaymentTime()
            // );

            // 发送短信
            // smsService.sendPaySuccessSms(order.getMemberPhone(), order.getOrderNo());

            log.info("【通知】支付成功通知已发送: orderNo={}", order.getOrderSn());

        } catch (Exception e) {
            log.error("【通知】发送支付成功通知异常", e);
        }
    }

    /**
     * 转换 DTO 为 PaymentSuccessMessage
     */
    private PaymentSuccessMessage convertToPaymentSuccessMessage(OrderPaySuccessDTO dto) {
        PaymentSuccessMessage message = new PaymentSuccessMessage();
        message.setMessageId(dto.getMessageId());
        message.setPaymentNo(dto.getPaymentNo());
        message.setOrderNo(dto.getOrderNo());
        message.setTransactionId(dto.getTransactionId());
        message.setAmount(dto.getAmount());
        message.setChannel(dto.getChannel());
        message.setMemberId(dto.getMemberId());
        message.setPaymentTime(dto.getPaymentTime());
        return message;
    }


}
