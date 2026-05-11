package com.aioveu.oms.aioveu08MqConsumeRecord.model.form;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: MqConsumeRecordForm
 * @Description TODO MQ消息消费记录表单对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:27
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "MQ消息消费记录表单对象")
public class MqConsumeRecordForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;

    @Schema(description = "消息ID")
    @NotBlank(message = "消息ID不能为空")
    @Size(max=64, message="消息ID长度不能超过64个字符")
    private String messageId;

    @Schema(description = "Topic")
    @NotBlank(message = "Topic不能为空")
    @Size(max=100, message="Topic长度不能超过100个字符")
    private String topic;

    @Schema(description = "Tag")
    @Size(max=50, message="Tag长度不能超过50个字符")
    private String tag;

    @Schema(description = "消费者组")
    @NotBlank(message = "消费者组不能为空")
    @Size(max=100, message="消费者组长度不能超过100个字符")
    private String consumerGroup;

    @Schema(description = "业务ID(订单号)")
    @NotBlank(message = "业务ID(订单号)不能为空")
    @Size(max=64, message="业务ID(订单号)长度不能超过64个字符")
    private String bizId;

    @Schema(description = "消费状态:0-未消费,1-消费中,2-消费成功,3-消费失败,4-进入死信")
    @NotNull(message = "消费状态:0-未消费,1-消费中,2-消费成功,3-消费失败,4-进入死信不能为空")
    private Integer consumeStatus;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "最大重试次数")
    private Integer maxRetry;

    @Schema(description = "下次重试时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextRetryTime;

    @Schema(description = "消费时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consumeTime;

    @Schema(description = "完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finishTime;

    @Schema(description = "错误信息")
    @Size(max=65535, message="错误信息长度不能超过65535个字符")
    private String errorMsg;
}
