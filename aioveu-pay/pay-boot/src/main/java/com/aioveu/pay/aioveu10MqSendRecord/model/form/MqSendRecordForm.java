package com.aioveu.pay.aioveu10MqSendRecord.model.form;


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
 * @ClassName: MqSendRecordForm
 * @Description TODO MQ消息发送记录表单对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:12
 * @Version 1.0
 * @Param
 * @return
 **/
@Getter
@Setter
@Schema(description = "MQ消息发送记录表单对象")
public class MqSendRecordForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;

    @Schema(description = "消息ID")
    @NotBlank(message = "消息ID不能为空")
    @Size(max=64, message="消息ID长度不能超过64个字符")
    private String messageId;

    @Schema(description = "业务ID(支付单号)")
    @NotBlank(message = "业务ID(支付单号)不能为空")
    @Size(max=64, message="业务ID(支付单号)长度不能超过64个字符")
    private String bizId;

    @Schema(description = "业务类型:payment_success")
    @NotBlank(message = "业务类型:payment_success不能为空")
    @Size(max=50, message="业务类型:payment_success长度不能超过50个字符")
    private String bizType;

    @Schema(description = "Topic")
    @NotBlank(message = "Topic不能为空")
    @Size(max=100, message="Topic长度不能超过100个字符")
    private String topic;

    @Schema(description = "Tag")
    @Size(max=50, message="Tag长度不能超过50个字符")
    private String tag;

    @Schema(description = "分片Key")
    @Size(max=100, message="分片Key长度不能超过100个字符")
    private String shardingKey;

    @Schema(description = "消息体(JSON格式)")
    @NotBlank(message = "消息体(JSON格式)不能为空")
    private String messageBody;

    @Schema(description = "发送状态:0-未发送,1-发送中,2-发送成功,3-发送失败")
    @NotNull(message = "发送状态:0-未发送,1-发送中,2-发送成功,3-发送失败不能为空")
    private Integer sendStatus;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "最大重试次数")
    private Integer maxRetry;

    @Schema(description = "下次重试时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextRetryTime;

    @Schema(description = "发送时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendTime;

    @Schema(description = "确认时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmTime;

    @Schema(description = "错误信息")
    @Size(max=1000, message="错误信息长度不能超过1000个字符")
    private String errorMsg;
}
