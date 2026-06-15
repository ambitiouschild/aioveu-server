package com.aioveu.oms.aioveu11MqConsumer.utils;


import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu08MqConsumeRecord.enums.ConsumeStatusEnum;
import com.aioveu.oms.aioveu08MqConsumeRecord.mapper.MqConsumeRecordMapper;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.entity.MqConsumeRecord;
import com.aioveu.oms.aioveu08MqConsumeRecord.service.MqConsumeRecordService;
import com.aioveu.oms.aioveu11MqConsumer.model.vo.OrderPaySuccessDTO;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentSuccessMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: MqConsumerUtils
 * @Description TODO  解析支付消息
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/11 22:03
 * @Version 1.0
 **/


@Component
@Slf4j
@RequiredArgsConstructor
public class MqConsumerUtils {

    private final  MqConsumeRecordMapper mqConsumeRecordMapper;

    private final MqConsumeRecordService mqConsumeRecordService;


    /**
     * 解析支付消息
     */
    public OrderPaySuccessDTO parsePaymentMessage(String messageBody) {
        try {
            if (!StringUtils.hasText(messageBody)) {
                return null;
            }

            // 解析 JSON
            JSONObject json = JSON.parseObject(messageBody);

            OrderPaySuccessDTO dto = new OrderPaySuccessDTO();
            dto.setMessageId(json.getString("messageId"));
            dto.setPaymentNo(json.getString("paymentNo"));
            dto.setOrderNo(json.getString("orderNo"));
            dto.setTransactionId(json.getString("transactionId"));

            dto.setTopic(json.getString("topic"));

            dto.setTag(json.getString("tag"));

            // 金额处理
            BigDecimal amount = json.getBigDecimal("amount");
            if (amount != null) {
                dto.setAmount(amount);
            }

            String channelStr = json.getString("channel");

            dto.setChannel(
                    PaymentChannelEnum.valueOf(channelStr.toUpperCase())
            );


            dto.setMemberId(json.getLong("memberId"));

            // 时间处理
            String paymentTimeStr = json.getString("paymentTime");
            if (StringUtils.hasText(paymentTimeStr)) {
                dto.setPaymentTime(LocalDateTime.parse(paymentTimeStr));
            } else {
                dto.setPaymentTime(LocalDateTime.now());
            }

            return dto;

        } catch (Exception e) {
            log.error("解析支付消息异常: {}", messageBody, e);
            return null;
        }
    }



    /**
     * 验证支付金额
     */
    public boolean validatePaymentAmount(OmsOrder order, BigDecimal paymentAmount) {
        if (order == null || paymentAmount == null) {
            return false;
        }

        // 订单金额
        BigDecimal orderAmount = new BigDecimal(order.getTotalAmount().toString());
        if (orderAmount == null) {
            return false;
        }

        // 允许微小差异（0.01元以内）
        BigDecimal diff = orderAmount.subtract(paymentAmount).abs();
        BigDecimal tolerance = new BigDecimal("0.01");

        boolean valid = diff.compareTo(tolerance) <= 0;

        if (!valid) {
            log.warn("【验证】金额验证失败: 订单金额={}, 支付金额={}, 差异={}",
                    orderAmount, paymentAmount, diff);
        }

        return valid;
    }


    /**
     * 验证消息
     */
    public boolean validatePaymentMessage(PaymentSuccessMessage message) {
        if (message == null) {
            return false;
        }

        // 必填字段校验
        if (!StringUtils.hasText(message.getOrderNo())) {
            log.error("【验证】订单号不能为空");
            return false;
        }

        if (!StringUtils.hasText(message.getPaymentNo())) {
            log.error("【验证】支付单号不能为空");
            return false;
        }

        if (!StringUtils.hasText(message.getTransactionId())) {
            log.error("【验证】交易流水号不能为空");
            return false;
        }

        if (message.getAmount() == null || message.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("【验证】支付金额无效: {}", message.getAmount());
            return false;
        }

        if (message.getPaymentTime() == null) {
            log.error("【验证】支付时间不能为空");
            return false;
        }

        return true;
    }

}
