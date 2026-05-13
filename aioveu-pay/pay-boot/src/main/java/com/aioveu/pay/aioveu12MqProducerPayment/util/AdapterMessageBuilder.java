package com.aioveu.pay.aioveu12MqProducerPayment.util;


import com.aioveu.pay.aioveu12MqProducerPayment.adapter.MessageRequestAdapter;
import com.aioveu.pay.aioveu12MqProducerPayment.enums.MessageQueueTypeEnum;
import com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ.RabbitMQMessageSendRequest;
import com.aioveu.pay.aioveu12MqProducerPayment.model.vo.MessageSendResult;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: MessageBuilder
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:07
 * @Version 1.0
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapterMessageBuilder {


    @Autowired
    private MessageRequestAdapter requestAdapter;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;  // Spring Cloud Stream

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;



    // 私有辅助方法
    public void validateRequest(RabbitMQMessageSendRequest request) {
        if (StringUtils.isBlank(request.getTopic())) {
            throw new IllegalArgumentException("主题不能为空");
        }
        if (StringUtils.isBlank(request.getBody())) {
            throw new IllegalArgumentException("消息体不能为空");
        }
        if (request.getDelayLevel() < 0 || request.getDelayLevel() > 18) {
            throw new IllegalArgumentException("延迟级别必须为0-18");
        }
    }


    // 3. 根据配置选择MQ
    public MessageQueueTypeEnum determineQueueType(RabbitMQMessageSendRequest request) {


        return null;
    }


    /*
    *  发送消息
    * */
    public SendResult doSend(MessageQueueTypeEnum queueType, RabbitMQMessageSendRequest request) throws Exception {

        SendResult result = null;


        try {
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
            logSendSuccess(request, result);
            return result;

        } catch (Exception e) {
            // 6. 失败处理
            logSendFailure(request, e);

            // 7. 重试
            if (shouldRetry(request, e)) {
                return retrySend(request);
            }

            throw new MessageSendException("消息发送失败", e, request.getBizId());
        }


        if (request.isAsync()) {
            return sendAsync(message, request.getTimeout());
        } else {
            return sendSync(message, request.getTimeout());
        }
    }


    /**
     * 发送到RocketMQ
     */
    private SendResult sendByRocketMQ(RabbitMQMessageSendRequest request) {
        org.apache.rocketmq.common.message.Message message =
                requestAdapter.toRocketMQMessage(request);

        if (request.getAsync()) {
            // 异步发送
            CompletableFuture<SendResult> future = rocketMQTemplate.asyncSend(
                    request.getTopic(), message, request.getTimeout()
            );
            return future.get();
        } else {
            // 同步发送
            return rocketMQTemplate.syncSend(
                    request.getTopic(), message, request.getTimeout()
            );
        }
    }

    /**
     * 发送到Kafka
     */
    private SendResult sendByKafka(RabbitMQMessageSendRequest request) {
        ProducerRecord<String, String> record =
                requestAdapter.toKafkaRecord(request);

        if (request.getAsync()) {
            // 异步发送
            CompletableFuture<SendResult> future = new CompletableFuture<>();
            kafkaTemplate.send(record).whenComplete((result, ex) -> {
                if (ex != null) {
                    future.completeExceptionally(ex);
                } else {
                    future.complete(convertToSendResult(result));
                }
            });
            return future.get();
        } else {
            // 同步发送
            ListenableFuture<SendResult> future = kafkaTemplate.send(record);
            return future.get();
        }
    }



    private void logSendSuccess(RabbitMQMessageSendRequest request, MessageSendResult result, long startTime) {
        if (log.isInfoEnabled()) {
            long costTime = System.currentTimeMillis() - startTime;
            log.info("消息发送成功: messageId={}, topic={}, tag={}, bizId={}, cost={}ms, queueId={}, offset={}",
                    result.getMessageId(),
                    request.getTopic(),
                    request.getTag(),
                    request.getBizId(),
                    costTime,
                    result.getQueueId(),
                    result.getOffset());
        }
    }

    private void logSendFailure(RabbitMQMessageSendRequest request, String messageId, Exception e, long startTime) {
        long costTime = System.currentTimeMillis() - startTime;
        log.error("消息发送失败: messageId={}, topic={}, tag={}, bizId={}, cost={}ms",
                messageId, request.getTopic(), request.getTag(), request.getBizId(), costTime, e);
    }
}
