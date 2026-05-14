package com.aioveu.pay.aioveu12MqProducerPayment.model.sendResult.RabbitMQ;


/**
 * @ClassName: RabbitBatchSendResult
 * @Description TODO RabbitMQ批量发送结果
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 19:22
 * @Version 1.0
 **/

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * RabbitMQ批量发送结果
 */
@Data
public class RabbitBatchSendResult {

    /** 批次ID */
    private String batchId;

    /** 批量发送状态 */
    private BatchSendStatus status;

    /** 总消息数 */
    private int totalCount;

    /** 成功数 */
    private int successCount;

    /** 失败数 */
    private int failedCount;

    /** 发送耗时（毫秒） */
    private long costTime;

    /** 单条消息发送结果列表 */
    private List<ItemResult> itemResults = new ArrayList<>();

    /** 是否异步批量发送 */
    private boolean async = false;

    /** 批量操作是否完成 */
    private boolean completed = false;

    /** 成功消息ID列表 */
    public List<String> getSuccessMessageIds() {
        List<String> ids = new ArrayList<>();
        for (ItemResult item : itemResults) {
            if (item.getSendResult() != null && item.getSendResult().isSuccess()) {
                ids.add(item.getMessageId());
            }
        }
        return ids;
    }

    /** 失败的消息列表 */
    public List<ItemResult> getFailedItems() {
        List<ItemResult> failed = new ArrayList<>();
        for (ItemResult item : itemResults) {
            if (item.getSendResult() == null || !item.getSendResult().isSuccess()) {
                failed.add(item);
            }
        }
        return failed;
    }

    /** 添加单个结果 */
    public void addItemResult(ItemResult itemResult) {
        this.itemResults.add(itemResult);
        this.totalCount = itemResults.size();

        // 统计成功失败
        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();

        itemResults.forEach(item -> {
            if (item.getSendResult() != null && item.getSendResult().isSuccess()) {
                success.incrementAndGet();
            } else {
                failure.incrementAndGet();
            }
        });

        this.successCount = success.get();
        this.failedCount = failure.get();

        // 更新状态
        if (this.totalCount == 0) {
            this.status = BatchSendStatus.EMPTY;
        } else if (this.failedCount == 0) {
            this.status = BatchSendStatus.ALL_SUCCESS;
        } else if (this.successCount == 0) {
            this.status = BatchSendStatus.ALL_FAILED;
        } else {
            this.status = BatchSendStatus.PARTIAL_SUCCESS;
        }

        // 如果所有结果都已处理，标记为完成
        if (itemResults.size() >= this.totalCount) {
            this.completed = true;
        }
    }

    /** 批量发送状态枚举 */
    public enum BatchSendStatus {
        ALL_SUCCESS,      // 全部成功
        PARTIAL_SUCCESS,  // 部分成功
        ALL_FAILED,       // 全部失败
        PROCESSING,       // 处理中
        EMPTY             // 空批次
    }

    /** 单条消息发送结果 */
    @Data
    public static class ItemResult {
        /** 消息ID */
        private String messageId;

        /** 发送请求 */
        private RabbitSendRequest sendRequest;

        /** 发送结果 */
        private RabbitSendResult sendResult;

        /** 在批次中的序号 */
        private int sequence;

        /** 错误信息 */
        private String errorMessage;

        /** 重试次数 */
        private int retryCount = 0;

        /** 是否成功 */
        public boolean isSuccess() {
            return sendResult != null && sendResult.isSuccess();
        }

        public static ItemResult success(String messageId, RabbitSendRequest request,
                                         RabbitSendResult result, int sequence) {
            ItemResult item = new ItemResult();
            item.setMessageId(messageId);
            item.setSendRequest(request);
            item.setSendResult(result);
            item.setSequence(sequence);
            return item;
        }

        public static ItemResult failure(String messageId, RabbitSendRequest request,
                                         String errorMessage, int sequence) {
            ItemResult item = new ItemResult();
            item.setMessageId(messageId);
            item.setSendRequest(request);
            item.setErrorMessage(errorMessage);
            item.setSequence(sequence);
            return item;
        }
    }
}
