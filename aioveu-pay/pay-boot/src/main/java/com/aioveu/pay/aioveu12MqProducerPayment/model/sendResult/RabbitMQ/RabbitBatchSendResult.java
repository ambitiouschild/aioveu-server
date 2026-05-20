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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RabbitMQ批量发送结果
 */
@Data
public class RabbitBatchSendResult {

    /** 批次ID */
    private String batchId;

    private Long tenantId;

    /** 批量发送状态 */
    private BatchSendStatus status;

    /** 总消息数 */
    private int totalCount = 0;

    /** 成功数 */
    private int successCount  = 0;

    /** 失败数 */
    private int failedCount = 0;

    private  Long  costTime;

    // 失败消息详情
    private List<FailedMessage> failedMessages = new ArrayList<>();

    // 成功消息
    private List<String> successMessageIds = new ArrayList<>();


    // 发送耗时
    private long totalCostTime = 0L;
    /** 单条消息发送结果列表 */
    private List<ItemResult> itemResults = new ArrayList<>();

    /** 是否异步批量发送 */
    private boolean async = false;

    /** 批量操作是否完成 */
    private boolean completed = false;



    /**
     * 增加成功计数
     */
    public void incrementSuccess() {
        this.successCount++;
        this.totalCount++;
    }

    /**
     * 增加成功计数（带消息ID）
     */
    public void incrementSuccess(String messageId) {
        this.successCount++;
        this.totalCount++;
        if (messageId != null) {
            this.successMessageIds.add(messageId);
        }
    }

    /**
     * 增加失败计数
     */
    public void incrementFailed() {
        this.failedCount++;
        this.totalCount++;
    }

    /**
     * 增加失败计数（带详情）
     */
    public void incrementFailed(String messageId, String errorMsg) {
        this.failedCount++;
        this.totalCount++;
        addFailedMessage(messageId, errorMsg);
    }

    /**
     * 添加失败消息
     */
    public void addFailedMessage(String messageId, String errorMsg) {
        FailedMessage failedMessage = new FailedMessage();
        failedMessage.setMessageId(messageId);
        failedMessage.setErrorMsg(errorMsg);
        this.failedMessages.add(failedMessage);
    }

    /**
     * 获取成功率
     */
    public double getSuccessRate() {
        return totalCount > 0 ? (double) successCount / totalCount * 100 : 0.0;
    }

    /**
     * 是否全部成功
     */
    public boolean isAllSuccess() {
        return failedCount == 0;
    }

    /**
     * 设置总耗时
     */
    public void setTotalCostTime(long totalCostTime) {
        this.totalCostTime = totalCostTime;
    }

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

    /**
     * 失败消息详情
     */
    @Data
    public static class FailedMessage {
        private String messageId;
        private String errorMsg;
        private long failTime = System.currentTimeMillis();
    }


}
