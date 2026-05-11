package com.aioveu.oms.aioveu10MqConsumeIdempotent.model.form;


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
 * @ClassName: MqConsumeIdempotentForm
 * @Description TODO MQ消费幂等性表单对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 18:17
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "MQ消费幂等性表单对象")
public class MqConsumeIdempotentForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;

    @Schema(description = "业务唯一键")
    @NotBlank(message = "业务唯一键不能为空")
    @Size(max=128, message="业务唯一键长度不能超过128个字符")
    private String bizUniqueKey;

    @Schema(description = "业务类型")
    @NotBlank(message = "业务类型不能为空")
    @Size(max=50, message="业务类型长度不能超过50个字符")
    private String bizType;

    @Schema(description = "消息ID")
    @NotBlank(message = "消息ID不能为空")
    @Size(max=64, message="消息ID长度不能超过64个字符")
    private String messageId;

    @Schema(description = "状态:1-已处理")
    @NotNull(message = "状态:1-已处理不能为空")
    private Integer status;

    @Schema(description = "消费时间")
    @NotNull(message = "消费时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime consumeTime;
}
