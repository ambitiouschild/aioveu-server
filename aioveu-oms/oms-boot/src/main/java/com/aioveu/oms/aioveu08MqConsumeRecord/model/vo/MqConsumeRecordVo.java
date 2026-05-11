package com.aioveu.oms.aioveu08MqConsumeRecord.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: MqConsumeRecordVo
 * @Description TODO MQ消息消费记录视图对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:28
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "MQ消息消费记录视图对象")
public class MqConsumeRecordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "Topic")
    private String topic;
    @Schema(description = "Tag")
    private String tag;
    @Schema(description = "消费者组")
    private String consumerGroup;
    @Schema(description = "业务ID(订单号)")
    private String bizId;
    @Schema(description = "消费状态:0-未消费,1-消费中,2-消费成功,3-消费失败,4-进入死信")
    private Integer consumeStatus;
    @Schema(description = "重试次数")
    private Integer retryCount;
    @Schema(description = "最大重试次数")
    private Integer maxRetry;
    @Schema(description = "下次重试时间")
    private LocalDateTime nextRetryTime;
    @Schema(description = "消费时间")
    private LocalDateTime consumeTime;
    @Schema(description = "完成时间")
    private LocalDateTime finishTime;
    @Schema(description = "错误信息")
    private String errorMsg;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
