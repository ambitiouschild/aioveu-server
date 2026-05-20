package com.aioveu.pay.aioveu12MqProducerPayment.util;


import com.aioveu.pay.aioveu10MqSendRecord.enums.AckType;
import com.aioveu.pay.aioveu12MqProducerPayment.adapter.MessageRequestAdapter;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.MessageQueueTypeEnum;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.Kafka.KafkaSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendRequest;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RocketMQ.RocketMQSendResult;
import com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.AdapterMessageBuilder;
import com.aioveu.pay.aioveu12MqProducerPayment.service.RabbitMQ.RabbitMessageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

//这是错误的 Java 语法。 Java 不支持在 import语句中使用 as关键字（这是 Kotlin 或 TypeScript 的语法）。
//import org.apache.rocketmq.client.producer.SendResult as RocketMQNativeSendResult;
//import org.springframework.kafka.support.SendResult as KafkaNativeSendResult;
//你说得对，我之前的建议是错的。在Java中，不能通过 as关键字来给导入的类起别名。这是我的错误，抱歉。
//import org.apache.rocketmq.client.producer.SendResult;
//import org.springframework.kafka.support.SendResult ;

import org.apache.kafka.common.errors.TimeoutException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.apache.rocketmq.client.exception.MQClientException;
import java.io.IOException;
/**
 * @ClassName: MessageBuilder
 * @Description TODO AdapterMessageBuilder
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:07
 * @Version 1.0
 **/

/*
*    TODO       总结
            问题的根本原因：
            1.RabbitMessageServicePaymentImpl依赖AdapterMessageBuilderImpl
            2.但AdapterMessageBuilderImpl没有正确注册为Spring Bean
            3.或者AdapterMessageBuilderImpl自身有依赖注入问题
            解决方案优先级：
            1.首先确保AdapterMessageBuilderImpl类有@Component或@Service注解
            2.其次修复AdapterMessageBuilderImpl中的@Autowired注解，添加required=false
            3.然后检查包扫描配置
            4.最后考虑手动注册Bean
*
*
* */


@Slf4j
@Component  // 确保有这个注解
@RequiredArgsConstructor
//方案2：使用条件注解重构AdapterMessageBuilderImpl
// 移除这行：@ConditionalOnBean(RocketMQTemplate.class)  // 这会导致Bean无法创建
//@ConditionalOnBean(RocketMQTemplate.class)  // 只在有RocketMQTemplate时才创建Bean
public class AdapterMessageBuilderImpl implements AdapterMessageBuilder {


    @Autowired
    private MessageRequestAdapter requestAdapter;

    // 修改前（错误的）
    // @Autowired(required=true)  // 强制依赖
    // private RocketMQTemplate rocketMQTemplate;

//    方案1：移除或修复AdapterMessageBuilderImpl中的强制依赖
    // 修改后（正确的）
    @Autowired(required=false)  // 改为非强制依赖
    private RocketMQTemplate rocketMQTemplate;




    // 注意：如果发送 Object，需要配置合适的序列化器
    // 或者改为 KafkaTemplate<String, String> 并手动序列化
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

 //修复3：添加初始化方法
    @PostConstruct
    public void init() {
        log.info("AdapterMessageBuilderImpl初始化完成，RocketMQTemplate={}",
                rocketMQTemplate != null ? "已注入" : "未注入");
    }

    // 私有辅助方法
    @Override
    public void validateRequest(RabbitSendRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("请求不能为空");
        }


        if (StringUtils.isBlank(request.getTopic())) {
            throw new IllegalArgumentException("主题不能为空");
        }
        // 如果是 String 类型，检查是否为空
        if (request.getBody() instanceof String) {
            String bodyStr = (String) request.getBody();
            if (StringUtils.isBlank(bodyStr)) {
                throw new IllegalArgumentException("消息体内容不能为空");
            }
        }


        // 检查其他必填字段
        if (request.getDelayLevel() != null && (request.getDelayLevel() < 0 || request.getDelayLevel() > 18)) {
            throw new IllegalArgumentException("延迟级别必须为0-18");
        }
    }


    // 3. 根据配置选择MQ
    @Override
    public MessageQueueTypeEnum determineQueueType(RabbitSendRequest request) {
        // 这里可以根据业务规则决定使用哪种MQ
        // 例如：根据消息类型、优先级、延迟要求等
        if (StringUtils.isNotBlank(request.getExchange()) || StringUtils.isNotBlank(request.getRoutingKey())) {
            return MessageQueueTypeEnum.RABBITMQ;
        }

        if (request.getTopic() != null && request.getTopic().contains("rocket")) {
            return MessageQueueTypeEnum.ROCKETMQ;
        }

        if (request.getTopic() != null && request.getTopic().contains("kafka")) {
            return MessageQueueTypeEnum.KAFKA;
        }

        // 默认使用 RocketMQ
        return MessageQueueTypeEnum.ROCKETMQ;
    }


    /*
    *  发送消息  - 返回各自MQ的结果
    * */
    @Override
    public Object doSend(MessageQueueTypeEnum queueType, RabbitSendRequest request) throws Exception {

        long startTime = System.currentTimeMillis();


        try {

            Object result = null;

            switch (queueType) {
                case ROCKETMQ:
                    result = sendByRocketMQ(request);
                    break;
                case KAFKA:
                    result = sendByKafka(request);
                    break;
                case RABBITMQ:
                    result = sendByRabbitMQ(request);
                    break;
                default:
                    throw new UnsupportedOperationException("不支持的队列类型");
            }

            // 5. 记录成功日志
            // 记录成功日志
            this.logSendSuccess(request, result, startTime);

            return result;

        } catch (Exception e) {
            log.error("消息发送失败: messageId={}, queueType={}", request.getMessageId(), queueType, e);

            // 6. 失败处理
            // 记录失败日志
            logSendFailure(request, request.getMessageId(), e, startTime);

            // 7. 重试
            if (shouldRetry(request, e)) {
                return retrySend(request, queueType);
            }

            throw new Exception("消息发送失败", e);
        }

    }


    // ========== RocketMQ 发送方法 ==========

    /**
     * 发送到RocketMQ - 返回 RocketMQ 的 SendResult
     */

    @Override
    public RocketMQSendResult sendByRocketMQ(RabbitSendRequest request) throws Exception {


        long startTime = System.currentTimeMillis();
        try {


            // 1. 适配：构建 Spring Message (最新版推荐方式)
            // 1. 构建 Spring Message (注意：是 Spring Messaging 的 Message，不是 RabbitMQ 的)
            org.springframework.messaging.Message<?> springMessage = MessageBuilder
                    .withPayload(request.getBody())
                    .setHeader("KEYS", request.getMessageId())
                    .build();

            // 1. 基础参数准备
            String topic = request.getTopic();

            // 1. 获取 payload
            Object payload = request.getBody();  // 直接使用 body

            // 2. 获取超时时间
            long timeout = getTimeout(request);

            // 3. 根据模式发送
            // 使用完整的 RocketMQ SendResult
            org.apache.rocketmq.client.producer.SendResult  mqResult;
            if (request.isAsync()) {

          /*      你使用的 2.3.4 属于较新的版本，其 API 设计较为严格。
                如果你希望代码更简洁（不使用回调），可以考虑降级到 2.0.x 版本，
                但不建议这样做，因为 2.3.4 修复了大量稳定性问题。当前方案是标准做法。*/


                // 创建 CompletableFuture 用于等待回调结果
                CompletableFuture<org.apache.rocketmq.client.producer.SendResult> future = new CompletableFuture<>();

                // 调用 2.3.4 正确的 API: asyncSend(destination, payload, callback, timeout)
                rocketMQTemplate.asyncSend(topic, payload, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        future.complete(sendResult); // 成功时完成 Future
                    }
                    @Override
                    public void onException(Throwable throwable) {
                        future.completeExceptionally(throwable); // 失败时抛出异常
                    }
                }, timeout); // 注意：timeout 是最后一个参数

                // 等待异步结果（这里会阻塞直到回调触发或超时）
                mqResult = future.get(timeout, TimeUnit.MILLISECONDS);

                // 4. 封装结果
                RocketMQSendResult result = RocketMQSendResult.from(mqResult, request.getMessageId());
                result.setCostTime(System.currentTimeMillis() - startTime);
                return result;


            } else {
                // 3. 同步模式（syncSend 在 2.3.4 中支持 timeout）
                mqResult = rocketMQTemplate.syncSend(topic, payload, timeout);

                // 4. 封装结果
                RocketMQSendResult result = RocketMQSendResult.from(mqResult, request.getMessageId());
                result.setCostTime(System.currentTimeMillis() - startTime);
                return result;
            }




        } catch (Exception e) {
            log.error("RocketMQ发送失败: messageId={}", request.getMessageId(), e);
            RocketMQSendResult result = RocketMQSendResult.failure(request.getMessageId(), e.getMessage());
            result.setCostTime(System.currentTimeMillis() - startTime);
            return result;
        }
    }




    private long getTimeout(RabbitSendRequest request) {
        if (request == null) {
            return 3000L; // 默认3秒
        }
        Long timeout = request.getTimeout();
        return (timeout != null && timeout > 0) ? timeout : 3000L;
    }




    // ========== Kafka 发送方法 ==========

    /*
    *    TODO       与 RocketMQ 版本的差异说明
    *           1.API 风格：Spring Kafka 的 KafkaTemplate原生返回 CompletableFuture，
    *           不像 RocketMQ 那样有 syncSend和 asyncSend的明显区分。上述代码通过 future.get(timeout)统一了两种模式。
    *           2.Message 对象：Kafka 通常直接发送 Object负载（Payload），
    *           由 JsonSerializer自动序列化，无需像 RocketMQ 那样构建 Message对象（除非你需要精细控制 Headers）。
    *           3.Key 的重要性：Kafka 的 key直接影响消息路由到哪个分区。如果你的 RabbitSendRequest没有 key字段，建议添加，这对于有序性消费很重要。
    *
    *
    * */

    /**
     * 发送到Kafka - 返回 Kafka 的 SendResult
     */
    @Override
    public KafkaSendResult sendByKafka(RabbitSendRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            // 1. 获取参数
            String topic = request.getTopic();
            String payload = convertToString(request.getBody());  // 转换为字符串
            long timeout = getTimeout(request);

            // 2. 发送消息（Spring Kafka 3.x 返回 CompletableFuture）
            // 2. 发送消息 - 使用全限定类名
            CompletableFuture<org.springframework.kafka.support.SendResult<String, String>> future;

            // 注意：RabbitSendRequest 可能没有 getKey() 方法
            // 可以使用 bizId 或 messageId 作为 key
            String messageKey = getMessageKey(request);


            if (StringUtils.isNotBlank(messageKey)) {
                // 有 key 发送
                future = kafkaTemplate.send(topic, messageKey, payload);
            } else {
                // 无 key 发送
                future = kafkaTemplate.send(topic, payload);
            }

            // 3. 等待结果 - 使用全限定类名
            org.springframework.kafka.support.SendResult<String, String> kafkaNativeResult =
                    future.get(timeout, TimeUnit.MILLISECONDS);

            // 3. 使用 KafkaSendResult 的转换方法
            // 4. 封装结果
            KafkaSendResult result = KafkaSendResult.success(
                    request.getMessageId(),
                    kafkaNativeResult
            );
            result.setCostTime(System.currentTimeMillis() - startTime);

            // 4. 记录日志
            if (result.isSuccess()) {
                log.info("Kafka消息发送成功: messageId={}, topic={}, partition={}, offset={}",
                        request.getMessageId(), topic,
                        result.getPartition(),
                        result.getOffset());
            } else {
                log.warn("Kafka消息发送失败: messageId={}, topic={}, error={}",
                        request.getMessageId(), topic, result.getErrorMsg());
            }

            return result;

        } catch (Exception e) {
            // 5. 封装失败结果
            log.error("Kafka消息发送失败: messageId={}, topic={}",
                    request.getMessageId(), request.getTopic(), e);

            KafkaSendResult result = KafkaSendResult.failure(request.getMessageId(), e.getMessage());
            result.setCostTime(System.currentTimeMillis() - startTime);
            return result;
        }
    }


    /**
     * 获取消息 Key
     */
    private String getMessageKey(RabbitSendRequest request) {
        // 从 RabbitSendRequest 中提取 key
        if (StringUtils.isNotBlank(request.getKey())) {
            return request.getKey();
        }
        if (StringUtils.isNotBlank(request.getBizId())) {
            return request.getBizId();
        }
        if (StringUtils.isNotBlank(request.getMessageId())) {
            return request.getMessageId();
        }
        return null;
    }

    /**
     * 将消息体转换为字符串
     */
    private String convertToString(Object body) {
        if (body == null) {
            return "";
        }
        if (body instanceof String) {
            return (String) body;
        }
        // 如果是其他对象，可以序列化为 JSON
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper =
                    new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.writeValueAsString(body);
        } catch (Exception e) {
            return body.toString();
        }
    }




    // ========== RabbitMQ 发送方法 ==========

    /**
     * 发送到RabbitMQ - 返回 RabbitSendResult
     */
    @Override
    public RabbitSendResult sendByRabbitMQ(RabbitSendRequest request) throws Exception {

        long startTime = System.currentTimeMillis();

        try {
            // 转换消息
            org.springframework.amqp.core.Message message =
                    requestAdapter.toRabbitMQMessage(request);

            // 获取交换机、路由键
            String exchange = StringUtils.isNotBlank(request.getExchange())
                    ? request.getExchange()
                    : "";

            String routingKey = StringUtils.isNotBlank(request.getRoutingKey())
                    ? request.getRoutingKey()
                    : request.getTopic();


            // 3. 创建 correlationId (如果没有则使用 messageId)
            String correlationId = StringUtils.isNotBlank(request.getCorrelationId())
                    ? request.getCorrelationId()
                    : request.getMessageId();

            // 3. 获取超时时间
            long timeout = getTimeout(request);


            // 4. 创建关联数据
            CorrelationData correlationData = new CorrelationData(request.getMessageId());




            if (request.isAsync()) {
                // 异步发送
                return sendRabbitMQAsync(request, message, exchange, routingKey,
                        correlationId, correlationData, timeout, startTime);


            } else {

                // 同步发送
                return sendRabbitMQSync(request, message, exchange, routingKey,
                        correlationId, correlationData, timeout, startTime);

            }

        } catch (Exception e) {


            long costTime = System.currentTimeMillis() - startTime;

            log.error("RabbitMQ消息发送异常: messageId={}, exchange={}, routingKey={}",
                    request.getMessageId(), request.getExchange(), request.getRoutingKey(), e);

            // 返回失败结果
            return RabbitSendResult.failure(
                    request.getMessageId(),
                    "RabbitMQ发送失败: " + e.getMessage(),
                    costTime,
                    request.getExchange() != null ? request.getExchange() : "",
                    request.getRoutingKey() != null ? request.getRoutingKey() : request.getTopic()
            );

        }
    }



    /**
     * 异步发送RabbitMQ消息
     */
    private RabbitSendResult sendRabbitMQAsync(RabbitSendRequest request,
                                               org.springframework.amqp.core.Message message,
                                               String exchange, String routingKey,
                                               String correlationId, CorrelationData correlationData,
                                               long timeout, long startTime) throws Exception {

        CompletableFuture<RabbitSendResult> future = new CompletableFuture<>();

        correlationData.getFuture().whenComplete((confirm, ex) -> {
            long costTime = System.currentTimeMillis() - startTime;
            long confirmTime = System.currentTimeMillis();

            if (ex != null) {
                // 异常情况
                RabbitSendResult result = RabbitSendResult.failure(
                        request.getMessageId(),
                        "异步发送异常: " + ex.getMessage(),
                        costTime,
                        exchange,
                        routingKey
                );
                future.complete(result);
            } else {
                // 处理确认结果
                RabbitSendResult result = processConfirmResult(
                        request, correlationId, confirm, costTime, confirmTime, exchange, routingKey
                );
                future.complete(result);
            }
        });

        // 发送消息
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);

        // 等待结果
        RabbitSendResult result = future.get(timeout, TimeUnit.MILLISECONDS);

        log.info("RabbitMQ异步消息发送完成: messageId={}, correlationId={}, success={}",
                request.getMessageId(), correlationId, result.isSuccess());

        return result;
    }

    /**
     * 同步发送RabbitMQ消息
     */
    private RabbitSendResult sendRabbitMQSync(RabbitSendRequest request,
                                              org.springframework.amqp.core.Message message,
                                              String exchange, String routingKey,
                                              String correlationId, CorrelationData correlationData,
                                              long timeout, long startTime) throws Exception {

        // 发送消息
        rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);

        long costTime = System.currentTimeMillis() - startTime;
        long confirmTime = System.currentTimeMillis();

        RabbitSendResult result;

        if (request.isWaitForConfirms()) {
            // 等待确认
            try {
                CorrelationData.Confirm confirm =
                        correlationData.getFuture().get(timeout, TimeUnit.MILLISECONDS);

                result = processConfirmResult(
                        request, correlationId, confirm, costTime, confirmTime, exchange, routingKey
                );
            } catch (TimeoutException e) {
                // 超时情况
                result = RabbitSendResult.failure(
                        request.getMessageId(),
                        "等待确认超时",
                        costTime,
                        exchange,
                        routingKey
                );
            }
        } else {
            // 不等待确认，直接返回成功
            result = RabbitSendResult.success(
                    request.getMessageId(),
                    correlationId,
                    costTime,
                    exchange,
                    routingKey
            );
            // 因为没有等待确认，所以没有确认时间
            result.setAckReceived(false);
            result.setAckTime(null);
            result.setConfirmTime(null);
            result.setAckType(AckType.UNKNOWN);
            result.setRoutedToQueue(false);
        }

        // 记录日志
        if (result.isSuccess()) {
            log.info("RabbitMQ同步消息发送成功: messageId={}, correlationId={}, waitForConfirms={}",
                    request.getMessageId(), correlationId, request.isWaitForConfirms());
        } else {
            log.warn("RabbitMQ消息发送失败: messageId={}, correlationId={}, error={}, waitForConfirms={}",
                    request.getMessageId(), correlationId, result.getErrorMessage(), request.isWaitForConfirms());
        }

        return result;
    }


    /**
     * 处理确认结果
     */
    private RabbitSendResult processConfirmResult(RabbitSendRequest request,
                                                  String correlationId,
                                                  CorrelationData.Confirm confirm,
                                                  long costTime, long confirmTime,
                                                  String exchange, String routingKey) {

        if (confirm == null) {
            // 没有收到确认
            return RabbitSendResult.failure(
                    request.getMessageId(),
                    "未收到确认",
                    costTime,
                    exchange,
                    routingKey
            );
        }

        boolean isAck = confirm.isAck();

        if (isAck) {
            // ACK 确认
            RabbitSendResult result = RabbitSendResult.success(
                    request.getMessageId(),
                    correlationId,
                    costTime,
                    exchange,
                    routingKey
            );
            result.setAckTime(new Date(confirmTime));
            result.setConfirmTime(new Date(confirmTime));
            result.setAckReceived(true);
            result.setAckType(AckType.ACK);
            return result;
        } else {
            // NACK 确认
            RabbitSendResult result = RabbitSendResult.failure(
                    request.getMessageId(),
                    "消息被NACK",
                    costTime,
                    exchange,
                    routingKey
            );
            result.setAckReceived(true);
            result.setAckType(AckType.NACK);
            result.setAckTime(new Date(confirmTime));
            result.setConfirmTime(new Date(confirmTime));
            return result;
        }

    }


    // ========== 日志方法 ==========

    /**
     * 记录成功日志
     */
    @Override
    public void logSendSuccess(RabbitSendRequest request, Object result, long startTime) {
        if (log.isInfoEnabled()) {
            long costTime = System.currentTimeMillis() - startTime;

            if (result instanceof RocketMQSendResult) {
                RocketMQSendResult mqResult = (RocketMQSendResult) result;
                log.info("RocketMQ消息发送成功: messageId={}, topic={}, status={}, cost={}ms, queueId={}, offset={}",
                        request.getMessageId(), mqResult.getTopic(), mqResult.getSendStatus(),
                        costTime, mqResult.getQueueId(), mqResult.getQueueOffset());

            } else if (result instanceof KafkaSendResult) {
                KafkaSendResult kafkaResult = (KafkaSendResult) result;
                log.info("Kafka消息发送成功: messageId={}, topic={}, partition={}, offset={}, cost={}ms",
                        request.getMessageId(), kafkaResult.getTopic(),
                        kafkaResult.getPartition(), kafkaResult.getOffset(), costTime);

            } else if (result instanceof RabbitSendResult) {
                RabbitSendResult rabbitResult = (RabbitSendResult) result;
                String exchange = rabbitResult.getExchange() != null ? rabbitResult.getExchange() : "";
                String routingKey = rabbitResult.getRoutingKey() != null ? rabbitResult.getRoutingKey() : "";
                log.info("RabbitMQ消息发送成功: messageId={}, correlationId={}, exchange={}, routingKey={}, ackType={}, cost={}ms",
                        request.getMessageId(), rabbitResult.getCorrelationId(),
                        exchange, routingKey, rabbitResult.getAckType(), costTime);
            }
        }
    }

    /**
     * 记录失败日志
     */
    @Override
    public void logSendFailure(RabbitSendRequest request, String messageId, Exception e, long startTime) {
        long costTime = System.currentTimeMillis() - startTime;

        if (log.isErrorEnabled()) {
            String errorMsg = e.getMessage();
            String simpleMsg = errorMsg != null && errorMsg.length() > 200
                    ? errorMsg.substring(0, 200) + "..."
                    : errorMsg;

            log.error("消息发送失败: messageId={}, topic={}, cost={}ms, error={}",
                    messageId, request.getTopic(), costTime, simpleMsg, e);
        }
    }

    /**
     * 是否应该重试
     */
    private boolean shouldRetry(RabbitSendRequest request, Exception e) {
        // 检查重试次数
        if (request.getRetryCount() >= request.getMaxRetryCount()) {
            return false;
        }

        // 根据异常类型决定是否重试
        if (e instanceof IllegalArgumentException) {
            // 参数错误，不需要重试
            return false;
        }

        if (e instanceof TimeoutException) {
            // 超时，可以重试
            return true;
        }

        if (e instanceof IOException || e instanceof MQClientException) {
            // 网络或客户端异常，可以重试
            return true;
        }

        // 默认情况下，可以重试
        return true;
    }

    /**
     * 重试发送
     */
    private Object retrySend(RabbitSendRequest request, MessageQueueTypeEnum queueType) throws Exception {
        if (request == null) {
            throw new IllegalArgumentException("请求不能为空");
        }

        Integer retryCount = request.getRetryCount() != null ? request.getRetryCount() : 0;
        Integer maxRetryCount = request.getMaxRetryCount() != null ? request.getMaxRetryCount() : 3;

        int newRetryCount = retryCount + 1;

        // 记录重试日志
        if (log.isWarnEnabled()) {
            log.warn("消息重试发送: messageId={}, retryCount={}/{}, queueType={}",
                    request.getMessageId(), newRetryCount, maxRetryCount, queueType);
        } else {
            log.info("消息重试发送: messageId={}, retryCount={}", request.getMessageId(), newRetryCount);
        }

        // 设置重试次数
        request.setRetryCount(newRetryCount);

        // 指数退避延迟
        long delay = calculateRetryDelay(newRetryCount);
        if (delay > 0) {
            log.debug("消息重试延迟: messageId={}, delay={}ms", request.getMessageId(), delay);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new Exception("重试被中断", ie);
            }
        }

        // 重试发送
        return doSend(queueType, request);
    }

    /**
     * 计算重试延迟
     */
    private long calculateRetryDelay(int retryCount) {
        // 指数退避: 1s, 2s, 4s, 8s, 16s, 最多30s
        long delay = (long) (Math.pow(2, retryCount) * 1000);
        return Math.min(delay, 30000L);
    }


}
