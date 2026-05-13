package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult;


/**
 * @ClassName: RecordMetadata
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 16:44
 * @Version 1.0
 **/

/*
* RecordMetadata是 Apache Kafka Producer API 中表示消息发送结果的核心类。它包含了消息在 Kafka 集群中的确切存储位置信息。
* */
/**
 * Kafka RecordMetadata 的核心字段
 * 记录了消息在 Kafka 集群中的存储元数据
 */
public class RecordMetadata {

    /** 主题名称 */
    private final String topic;

    /** 分区编号（从0开始） */
    private final int partition;

    /** 消息在分区中的偏移量（递增的） */
    private final long offset;

    /** 消息时间戳（可以是创建时间或追加时间） */
    private final long timestamp;

    /** 序列化后消息的字节大小 */
    private final int serializedKeySize;
    private final int serializedValueSize;

    /** 校验和 */
    private final Long checksum;

    // 构造方法
    public RecordMetadata(
            String topic,
            int partition,
            long offset,
            long timestamp,
            Long checksum,
            int serializedKeySize,
            int serializedValueSize
    ) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.timestamp = timestamp;
        this.checksum = checksum;
        this.serializedKeySize = serializedKeySize;
        this.serializedValueSize = serializedValueSize;
    }

    // 常用方法
    public boolean hasOffset() {
        return offset >= 0;
    }

    public boolean hasTimestamp() {
        return timestamp != RecordBatch.NO_TIMESTAMP;
    }

    // Getters...
    public String topic() { return topic; }
    public int partition() { return partition; }
    public long offset() { return offset; }
    public long timestamp() { return timestamp; }
    public int serializedKeySize() { return serializedKeySize; }
    public int serializedValueSize() { return serializedValueSize; }
}
