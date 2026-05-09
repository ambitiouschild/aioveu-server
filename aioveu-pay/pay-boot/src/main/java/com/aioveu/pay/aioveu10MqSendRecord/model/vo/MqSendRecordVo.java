package com.aioveu.pay.aioveu10MqSendRecord.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: MqSendRecordVo
 * @Description TODO MQ消息发送记录视图对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:37
 * @Version 1.0
 * @Param
 * @return
 **/
@Getter
@Setter
@Schema( description = "MQ消息发送记录视图对象")
public class MqSendRecordVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "业务ID(支付单号)")
    private String bizId;
    @Schema(description = "业务类型:payment_success")
    private String bizType;
    @Schema(description = "Topic")
    private String topic;
    @Schema(description = "Tag")
    private String tag;
    @Schema(description = "分片Key")
    private String shardingKey;
    @Schema(description = "消息体(JSON格式)")
    private String messageBody;
    @Schema(description = "发送状态:0-未发送,1-发送中,2-发送成功,3-发送失败")
    private Integer sendStatus;
    @Schema(description = "重试次数")
    private Integer retryCount;
    @Schema(description = "最大重试次数")
    private Integer maxRetry;
    @Schema(description = "下次重试时间")
    private LocalDateTime nextRetryTime;
    @Schema(description = "发送时间")
    private LocalDateTime sendTime;
    @Schema(description = "确认时间")
    private LocalDateTime confirmTime;
    @Schema(description = "错误信息")
    private String errorMsg;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
