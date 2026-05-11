package com.aioveu.oms.aioveu09MqDeadLetter.model.form;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: MqDeadLetterForm
 * @Description TODO MQ死信队列表单对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:48
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "MQ死信队列表单对象")
public class MqDeadLetterForm implements Serializable {

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

    @Schema(description = "业务ID")
    @NotBlank(message = "业务ID不能为空")
    @Size(max=64, message="业务ID长度不能超过64个字符")
    private String bizId;

    @Schema(description = "消息体")
    @NotBlank(message = "消息体不能为空")
    private String messageBody;

    @Schema(description = "消费次数")
    private Integer consumeTimes;

    @Schema(description = "错误信息")
    @Size(max=65535, message="错误信息长度不能超过65535个字符")
    private String errorMsg;

    @Schema(description = "死信原因")
    @Size(max=500, message="死信原因长度不能超过500个字符")
    private String deadReason;

    @Schema(description = "处理状态:0-未处理,1-已处理")
    private Integer handleStatus;

    @Schema(description = "处理时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    @Size(max=1000, message="处理结果长度不能超过1000个字符")
    private String handleResult;
}
