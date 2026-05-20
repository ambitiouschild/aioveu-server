package com.aioveu.kafka.producer.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName: KafkaMessageServiceImpl
 * @Description TODO Kafka完整的消息发送服务
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:45
 * @Version 1.0
 **/
@Slf4j
@Service
public class KafkaMessageServiceImpl {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 同步发送
     */
    public SendResult<String, String> sendSync(String topic, String message) {
        try {
            // 同步发送，会阻塞直到发送完成
            return kafkaTemplate.send(topic, message).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("同步发送消息失败: topic={}, message={}", topic, message, e);
            throw new RuntimeException("发送消息失败", e);
        }
    }

    /**
     * 同步发送（带Key）
     */
    public SendResult<String, String> sendSync(String topic, String key, String message) {
        try {
            return kafkaTemplate.send(topic, key, message).get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("同步发送消息失败: topic={}, key={}", topic, key, e);
            throw new RuntimeException("发送消息失败", e);
        }
    }

    /**
     * 异步发送
     */
//    public ListenableFuture<SendResult<String, String>> sendAsync(String topic, String message) {
//        ListenableFuture<SendResult<String, String>> kafkaFuture = kafkaTemplate.send(topic, message);
//
//        kafkaFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
//            @Override
//            public void onSuccess(SendResult<String, String> result) {
//                log.info("异步发送成功: topic={}, partition={}, offset={}",
//                        topic,
//                        result.getRecordMetadata().partition(),
//                        result.getRecordMetadata().offset());
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                log.error("异步发送失败: topic={}", topic, ex);
//            }
//        });
//
//        return kafkaFuture;  // ✅ 返回 ListenableFuture
//    }


    /**
     * 带回调的异步发送
     */
//    public void sendWithCallback(String topic, String message) {
//        kafkaTemplate.send(topic, message).addCallback(
//                result -> {
//                    if (result != null) {
//                        log.info("发送成功: topic={}, partition={}, offset={}, timestamp={}",
//                                topic,
//                                result.getRecordMetadata().partition(),
//                                result.getRecordMetadata().offset(),
//                                result.getRecordMetadata().timestamp());
//                    }
//                },
//                ex -> {
//                    log.error("发送失败: topic={}", topic, ex);
//                    // 可以在这里实现重试逻辑
//                    retrySend(topic, message, ex);
//                }
//        );
//    }

    /**
     * 发送到指定分区
     */
    public void sendToPartition(String topic, Integer partition, String key, String message) {
        kafkaTemplate.send(topic, partition, key, message);
    }

    /**
     * 使用事务发送
     */
    @Transactional
    public void sendInTransaction(String topic, String message) {
        // 在事务中发送消息
        kafkaTemplate.send(topic, message);

        // 其他数据库操作也会在同一个事务中
        // userRepository.save(user);

        // 如果这里抛出异常，消息发送会回滚
    }

    private void retrySend(String topic, String message, Throwable ex) {
        // 重试逻辑
        log.warn("准备重试发送: topic={}, message={}", topic, message);
        // 实现重试逻辑
    }
}
