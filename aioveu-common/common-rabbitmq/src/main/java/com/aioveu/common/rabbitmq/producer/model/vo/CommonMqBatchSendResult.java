package com.aioveu.common.rabbitmq.producer.model.vo;


/**
 * @ClassName: CommonMqBatchSendResult
 * @Description TODO 通用消息队列版
 *                  兼容多种消息队列，提供统一的批量操作结果。
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 16:21
 * @Version 1.0
 **/

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用消息队列批量发送结果
 */
@Data
public class CommonMqBatchSendResult {

    /** 批次ID（唯一标识） */
    private String batchId;

    /** 发送状态 */
    private BatchSendStatus status;

    /** 消息条目结果列表 */
    private List<MessageResult> messageResults = new ArrayList<>();

    /** 总消息数 */
    private int totalCount;

    /** 成功数 */
    private int successCount;

    /** 失败数 */
    private int failedCount;

    /** 发送耗时（毫秒） */
    private long costTime;

    /** 错误信息（如果有） */
    private String errorMessage;

    /** 是否异步发送 */
    private boolean async = false;

    /** 是否事务消息 */
    private boolean transactional = false;

    /**
     * 单个消息发送结果
     */
    @Data
    public static class MessageResult {
        /** 消息ID */
        private String messageId;
        /** 消息键（用于分区路由） */
        private String messageKey;
        /** 分区/队列ID */
        private Integer partitionOrQueueId;
        /** 偏移量/序列号 */
        private Long offset;
        /** 发送状态 */
        private SendStatus sendStatus;
        /** 时间戳 */
        private Long timestamp;
        /** 错误信息 */
        private String errorMsg;
    }

    public enum BatchSendStatus {
        SUCCESS,        // 全部成功
        PARTIAL_SUCCESS, // 部分成功
        FAILED,         // 全部失败
        TIMEOUT,        // 超时
        UNKNOWN         // 未知状态
    }

    public enum SendStatus {
        SENT_OK,
        SENT_FAILED,
        TIMEOUT,
        RETRYING
    }
}
