//package com.aioveu.pay.aioveu12MqProducerPayment.service.RocketMQ.impl;
//
//
//import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
//import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
//import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
//import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
//import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
//import com.aioveu.pay.aioveu10MqSendRecord.utils.MessageIdGenerator;
//import com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ.ProducerMetricsCollector;
//import com.aioveu.pay.aioveu12MqProducerPayment.Monitor.RabbitMQ.ProducerMonitor;
//import com.aioveu.pay.aioveu12MqProducerPayment.enums.MessageQueueTypeEnum;
//import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendRequest;
//import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RocketMQ.RocketBatchSendResult;
//import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
//import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RocketMQ.SendContext;
//import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentFailedMessage;
//import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.PaymentSuccessMessage;
//import com.aioveu.pay.aioveu12MqProducerPayment.service.RocketMQ.RocketMessageService;
//import com.aioveu.pay.aioveu12MqProducerPayment.util.AdapterMessageBuilder;
//import com.alibaba.nacos.common.utils.StringUtils;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.producer.SendResult;  // ✅
//import org.apache.rocketmq.common.message.MessageConst;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//
///**
// * @ClassName: MqSendRecordServiceImpl
// * @Description TODO RocketMQ专属消息发送服务
// *                    消息发送服务（使用自定义Request）
// *                    这个专属的 RocketMessageServiceImpl有以下特点：
// *                      纯粹性：只处理 RocketMQ 相关逻辑，不混用 RabbitMQ
// *                      完整性：包含各种发送模式（同步、异步、单向、顺序、延迟、事务）
// *                      可维护性：方法职责清晰，便于扩展
// *                      监控完善：集成 RocketMQ 专属的监控组件
// *                      异常处理：完善的异常处理和日志记录
// *                      配置化：支持配置驱动，便于管理
// * @Author aioveu
// * @Author 雒世松
// * @Date 2026/5/9 21:48
// * @Version 1.0
// **/
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class RocketMessageServiceImpl extends ServiceImpl<MqSendRecordMapper, MqSendRecord> implements RocketMessageService {
//
//
//    private final MessageIdGenerator messageIdGenerator;
//    private final MqSendRecordMapper mqSendRecordMapper;
//    private final RocketMQTemplate rocketMQTemplate;
//    private final MqSendRecordService mqSendRecordService;
//    private final RocketProducerMonitor rocketProducerMonitor;
//    private final RocketProducerMetricsCollector rocketMetricsCollector;
//    private final RocketMessageBuilder rocketMessageBuilder;
//
//    @Value("${rocketmq.topic.payment-success:payment_success_topic}")
//    private String paymentSuccessTopic;
//
//    @Value("${rocketmq.topic.payment-failed:payment_failed_topic}")
//    private String paymentFailedTopic;
//
//    @Value("${rocketmq.producer.timeout:3000}")
//    private Integer sendTimeout;
//
//    // 存储消息回调上下文，用于重试
//    private final Map<String, SendContext> sendContextMap = new ConcurrentHashMap<>();
//
//    /**
//     * 发送支付成功消息
//     */
//    @Override
//    public boolean sendPaymentSuccessMessage(PayOrder payOrder, Map<String, String> params) {
//        long startTime = System.currentTimeMillis();
//        boolean success = false;
//        String messageId = null;
//
//        try {
//            // 构建消息
//            PaymentSuccessMessage message = buildPaymentSuccessMessage(payOrder, params);
//
//            // 保存发送记录
//            messageId = saveMqSendRecord(payOrder, message, "payment_success");
//
//            // 发送顺序消息
//            SendResult sendResult = sendOrderlyMessage(message, payOrder.getOrderNo());
//
//            // 更新发送状态
//            success = sendResult.getSendStatus() == SendStatus.SUCCESS;
//            updateSendStatus(messageId, success, null);
//
//            logSendResult("支付成功", payOrder.getPaymentNo(), messageId, success);
//
//            return success;
//
//        } catch (Exception e) {
//            log.error("发送支付成功消息异常: paymentNo={}", payOrder.getPaymentNo(), e);
//
//            if (messageId != null) {
//                updateSendStatus(messageId, false, e.getMessage());
//            }
//
//            return false;
//        } finally {
//            recordMetrics(success, startTime);
//        }
//    }
//
//    /**
//     * 发送支付失败消息
//     */
//    @Override
//    public boolean sendPaymentFailedMessage(PayOrder payOrder, Map<String, String> params) {
//        long startTime = System.currentTimeMillis();
//        boolean success = false;
//        String messageId = null;
//
//        try {
//            // 构建消息
//            PaymentFailedMessage message = buildPaymentFailedMessage(payOrder, params);
//
//            // 保存发送记录
//            messageId = saveMqSendRecord(payOrder, message, "payment_failed");
//
//            // 发送普通消息
//            SendResult sendResult = sendNormalMessage(message);
//
//            // 更新发送状态
//            success = sendResult.getSendStatus() == SendStatus.SEND_OK;
//            updateSendStatus(messageId, success, null);
//
//            logSendResult("支付失败", payOrder.getPaymentNo(), messageId, success);
//
//            return success;
//
//        } catch (Exception e) {
//            log.error("发送支付失败消息异常: paymentNo={}", payOrder.getPaymentNo(), e);
//
//            if (messageId != null) {
//                updateSendStatus(messageId, false, e.getMessage());
//            }
//
//            return false;
//        } finally {
//            recordMetrics(success, startTime);
//        }
//    }
//
//    /**
//     * 发送单条消息（通用方法）
//     */
//    @Override
//    public RocketSendResult sendSingleMessage(RocketSendRequest request) {
//        long startTime = System.currentTimeMillis();
//        String messageId = request.getMessageId();
//
//        if (StringUtils.isEmpty(messageId)) {
//            messageId = messageIdGenerator.generateMessageId();
//            request.setMessageId(messageId);
//        }
//
//        try {
//            // 参数校验
//            rocketMessageBuilder.validateRequest(request);
//
//            // 发送消息
//            SendResult sendResult = rocketMessageBuilder.doSend(request);
//
//            // 保存发送记录
//            saveSendRecord(request, sendResult);
//
//            // 记录成功日志
//            rocketMessageBuilder.logSendSuccess(request, sendResult, startTime);
//
//            return RocketSendResult.success(messageId, sendResult.getMsgId());
//
//        } catch (Exception e) {
//            // 记录失败
//            saveFailRecord(request, e);
//
//            // 记录失败日志
//            rocketMessageBuilder.logSendFailure(request, messageId, e, startTime);
//
//            throw new RocketMessageSendException("RocketMQ消息发送失败", e, messageId);
//        } finally {
//            recordMetrics(startTime);
//        }
//    }
//
//    /**
//     * 批量发送消息
//     */
//    @Override
//    public RocketBatchSendResult batchSend(List<RocketSendRequest> requests) {
//        RocketBatchSendResult result = new RocketBatchSendResult();
//        long totalStartTime = System.currentTimeMillis();
//
//        for (RocketSendRequest request : requests) {
//            long startTime = System.currentTimeMillis();
//            boolean success = false;
//
//            try {
//                // 发送单条消息
//                sendSingleMessage(request);
//                success = true;
//                result.incrementSuccess();
//            } catch (Exception e) {
//                result.incrementFailed();
//                result.addFailedMessage(request.getMessageId(), e.getMessage());
//                log.warn("批量发送消息失败: messageId={}", request.getMessageId(), e);
//            } finally {
//                long costTime = System.currentTimeMillis() - startTime;
//                rocketMetricsCollector.recordSendResult(success, costTime);
//            }
//        }
//
//        long totalCostTime = System.currentTimeMillis() - totalStartTime;
//        rocketMetricsCollector.recordSendResult(result.getFailedCount() == 0, totalCostTime);
//
//        return result;
//    }
//
//    /**
//     * 同步发送消息
//     */
//    @Override
//    public SendResult syncSend(RocketSendRequest request) {
//        return rocketMessageBuilder.doSend(request);
//    }
//
//    /**
//     * 异步发送消息
//     */
//    @Override
//    public void asyncSend(RocketSendRequest request) {
//        rocketMessageBuilder.asyncSend(request);
//    }
//
//    /**
//     * 发送单向消息
//     */
//    @Override
//    public void sendOneWay(RocketSendRequest request) {
//        rocketMessageBuilder.sendOneWay(request);
//    }
//
//    /**
//     * 发送顺序消息
//     */
//    @Override
//    public SendResult sendOrderly(RocketSendRequest request, String shardingKey) {
//        return rocketMessageBuilder.sendOrderly(request, shardingKey);
//    }
//
//    /**
//     * 发送延迟消息
//     */
//    @Override
//    public SendResult sendDelay(RocketSendRequest request, int delayLevel) {
//        return rocketMessageBuilder.sendDelay(request, delayLevel);
//    }
//
//    /**
//     * 发送事务消息
//     */
//    @Override
//    public SendResult sendTransaction(RocketSendRequest request, Object arg) {
//        return rocketMessageBuilder.sendTransaction(request, arg);
//    }
//
//    /**
//     * 构建支付成功消息
//     */
//    private PaymentSuccessMessage buildPaymentSuccessMessage(PayOrder payOrder, Map<String, String> params) {
//        return PaymentSuccessMessage.builder()
//                .messageId(UUID.randomUUID().toString())
//                .paymentNo(payOrder.getPaymentNo())
//                .orderNo(payOrder.getOrderNo())
//                .transactionId(params.get("transaction_id"))
//                .amount(payOrder.getPaymentAmount())
//                .channel(payOrder.getPaymentChannel())
//                .paymentTime(LocalDateTime.now())
//                .memberId(payOrder.getUserId())
//                .build();
//    }
//
//    /**
//     * 构建支付失败消息
//     */
//    private PaymentFailedMessage buildPaymentFailedMessage(PayOrder payOrder, Map<String, String> params) {
//        return PaymentFailedMessage.builder()
//                .paymentNo(payOrder.getPaymentNo())
//                .orderNo(payOrder.getOrderNo())
//                .errorCode(params.get("err_code"))
//                .errorMsg(params.get("err_code_des"))
//                .channel(payOrder.getPaymentChannel())
//                .build();
//    }
//
//    /**
//     * 保存消息发送记录
//     */
//    private String saveMqSendRecord(PayOrder payOrder, Object message, String messageType) {
//        return mqSendRecordService.saveMqSendRecord(
//                getTopicByMessageType(messageType),
//                payOrder.getPaymentChannel(),
//                payOrder.getOrderNo(),
//                message
//        );
//    }
//
//    /**
//     * 发送顺序消息
//     */
//    private SendResult sendOrderlyMessage(Object message, String orderNo) {
//        return rocketMQTemplate.syncSendOrderly(
//                paymentSuccessTopic + ":wechat_pay",
//                MessageBuilder
//                        .withPayload(message)
//                        .setHeader(MessageConst.PROPERTY_KEYS, orderNo)
//                        .build(),
//                orderNo,
//                sendTimeout
//        );
//    }
//
//    /**
//     * 发送普通消息
//     */
//    private SendResult sendNormalMessage(Object message) {
//        return rocketMQTemplate.syncSend(
//                paymentFailedTopic,
//                MessageBuilder.withPayload(message).build(),
//                sendTimeout
//        );
//    }
//
//    /**
//     * 更新发送状态
//     */
//    private void updateSendStatus(String messageId, boolean success, String errorMsg) {
//        mqSendRecordService.updateSendStatus(
//                messageId,
//                success ? SendStatus.SUCCESS : SendStatus.FAILED,
//                errorMsg
//        );
//    }
//
//    /**
//     * 根据消息类型获取Topic
//     */
//    private String getTopicByMessageType(String messageType) {
//        if ("payment_success".equals(messageType)) {
//            return paymentSuccessTopic;
//        } else if ("payment_failed".equals(messageType)) {
//            return paymentFailedTopic;
//        }
//        return paymentSuccessTopic; // 默认
//    }
//
//    /**
//     * 记录发送日志
//     */
//    private void logSendResult(String messageType, String paymentNo, String messageId, boolean success) {
//        if (success) {
//            log.info("【RocketMQ发送】{}消息发送成功: paymentNo={}, messageId={}",
//                    messageType, paymentNo, messageId);
//        } else {
//            log.error("【RocketMQ发送】{}消息发送失败: paymentNo={}, messageId={}",
//                    messageType, paymentNo, messageId);
//        }
//    }
//
//    /**
//     * 记录指标
//     */
//    private void recordMetrics(boolean success, long startTime) {
//        long costTime = System.currentTimeMillis() - startTime;
//        rocketProducerMonitor.recordSendResult(success);
//        rocketMetricsCollector.recordSendResult(success, costTime);
//    }
//
//    /**
//     * 记录指标（重载）
//     */
//    private void recordMetrics(long startTime) {
//        long costTime = System.currentTimeMillis() - startTime;
//        rocketMetricsCollector.recordSendResult(true, costTime);
//    }
//
//    /**
//     * 保存发送成功记录
//     */
//    private void saveSendRecord(RocketSendRequest request, SendResult sendResult) {
//        MqSendRecord record = new MqSendRecord();
//        record.setMessageId(request.getMessageId());
//        record.setExchange(request.getTopic());
//        record.setRoutingKey(request.getTags());
//        record.setMessageBody(request.getBody());
//        record.setSendStatus(SendStatus.SUCCESS.getValue());
//        record.setSendTime(LocalDateTime.now());
//        record.setMsgId(sendResult.getMsgId());
//
//        mqSendRecordMapper.insert(record);
//    }
//
//    /**
//     * 保存发送失败记录
//     */
//    private void saveFailRecord(RocketSendRequest request, Exception e) {
//        MqSendRecord record = new MqSendRecord();
//        record.setMessageId(request.getMessageId());
//        record.setExchange(request.getTopic());
//        record.setRoutingKey(request.getTags());
//        record.setMessageBody(request.getBody());
//        record.setSendStatus(SendStatus.FAILED.getValue());
//        record.setSendTime(LocalDateTime.now());
//        record.setErrorMsg(e.getMessage());
//
//        mqSendRecordMapper.insert(record);
//    }
//
//
//
//
//
//
//}
