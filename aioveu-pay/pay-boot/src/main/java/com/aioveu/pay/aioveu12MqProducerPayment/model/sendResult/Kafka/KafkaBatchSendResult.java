package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.Kafka;


/**
 * @ClassName: KafkaBatchSendResult
 * @Description TODO Apache Kafka 风格
 *                      Kafka 的批量发送更关注分区、偏移量和时间戳。
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 16:04
 * @Version 1.0
 **/

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Kafka 批量发送结果
 */
@Data
public class KafkaBatchSendResult {


    /** 发送结果列表（每个消息的发送结果） */
    private List<Future<RecordMetadata>> sendFutures = new ArrayList<>();

    /** 成功发送的消息数量 */
    private int successCount;

    /** 失败的消息数量 */
    private int failedCount;

    /** 批处理大小 */
    private int batchSize;

    /** 批次创建时间戳 */
    private long batchCreateTime;

    /** 批次发送时间戳 */
    private long batchSendTime;

    /** 最后一个成功的消息偏移量 */
    private long lastOffset = -1;

    /** 是否已全部确认（acknowledged） */
    private boolean allAcked = false;

    /** 异常列表（发送失败的异常） */
    private List<Throwable> exceptions = new ArrayList<>();

    /**
     * 获取分区的元数据结果
     */
    public List<PartitionResult> getPartitionResults() {
        List<PartitionResult> results = new ArrayList<>();
        // 这里可以解析 sendFutures 获取详细的分区信息
        return results;
    }

    @Data
    public static class PartitionResult {
        private String topic;
        private int partition;
        private long offset;
        private long timestamp;
        private boolean success;
    }
}
