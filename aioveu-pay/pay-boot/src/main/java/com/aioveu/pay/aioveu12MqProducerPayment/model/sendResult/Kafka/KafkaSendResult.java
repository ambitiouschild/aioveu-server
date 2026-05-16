package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.Kafka;


/**
 * @ClassName: KafkaSendResult
 * @Description TODO 结果封装类（KafkaSendResult）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/16 18:17
 * @Version 1.0
 **/
import lombok.experimental.Accessors;
import org.apache.kafka.clients.producer.RecordMetadata;  // 正确的导入
import lombok.Data;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Kafka 发送结果包装类
 * 当你使用 @Builder注解时，Lombok 生成的 Builder 用于构建对象，但你的代码中混合了 Builder 模式和 setter 链式调用，这是错误的。
 */
@Data
@Accessors(chain = true)
public class KafkaSendResult {


    private boolean success;
    private String messageId;
    private String topic;
    private Integer partition;
    private Long offset;
    private Long timestamp;
    private String errorMsg;
    private Long costTime = 0L;

    /**
     * 从 Spring Kafka 的 SendResult 转换
     */
    public static KafkaSendResult from(String messageId, SendResult<String, String> kafkaResult) {
        if (kafkaResult == null) {
            return failure(messageId, "Kafka返回结果为null");
        }

        RecordMetadata metadata = kafkaResult.getRecordMetadata();

        return new KafkaSendResult()
                .setSuccess(true)
                .setMessageId(messageId)
                .setTopic(metadata.topic())
                .setPartition(metadata.partition())
                .setOffset(metadata.offset())
                .setTimestamp(metadata.timestamp());
    }

    /**
     * 从 CompletableFuture 转换
     */
    public static KafkaSendResult from(String messageId,
                                       CompletableFuture<SendResult<String, String>> future,
                                       long timeoutMs) {
        try {
            SendResult<String, String> kafkaResult = future.get(timeoutMs, TimeUnit.MILLISECONDS);
            return from(messageId, kafkaResult);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return failure(messageId, "发送被中断: " + e.getMessage());
        } catch (ExecutionException e) {
            return failure(messageId, "发送执行失败: " + e.getCause().getMessage());
        } catch (TimeoutException e) {
            return failure(messageId, "发送超时");
        } catch (Exception e) {
            return failure(messageId, e.getMessage());
        }
    }

    /**
     * 成功结果
     */
    public static KafkaSendResult success(String messageId, SendResult<String, String> kafkaResult) {
        return from(messageId, kafkaResult);
    }

    /**
     * 失败结果
     */
    public static KafkaSendResult failure(String messageId, String errorMsg) {
        return new KafkaSendResult()
                .setSuccess(false)
                .setMessageId(messageId)
                .setErrorMsg(errorMsg);
    }

    /**
     * 失败结果（带异常）
     */
    public static KafkaSendResult failure(String messageId, Exception e) {
        return failure(messageId, e.getMessage());
    }
}
