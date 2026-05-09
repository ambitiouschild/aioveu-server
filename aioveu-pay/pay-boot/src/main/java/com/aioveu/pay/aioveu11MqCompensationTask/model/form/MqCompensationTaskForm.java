package com.aioveu.pay.aioveu11MqCompensationTask.model.form;


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
 * @ClassName: MqCompensationTaskForm
 * @Description TODO MQ补偿任务表单对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:48
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "MQ补偿任务表单对象")
public class MqCompensationTaskForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;

    @Schema(description = "任务类型:send_retry")
    @NotBlank(message = "任务类型:send_retry不能为空")
    @Size(max=50, message="任务类型:send_retry长度不能超过50个字符")
    private String taskType;

    @Schema(description = "业务ID")
    @NotBlank(message = "业务ID不能为空")
    @Size(max=64, message="业务ID长度不能超过64个字符")
    private String bizId;

    @Schema(description = "业务数据")
    private String bizData;

    @Schema(description = "状态:0-待处理,1-处理中,2-成功,3-失败")
    @NotNull(message = "状态:0-待处理,1-处理中,2-成功,3-失败不能为空")
    private Integer status;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "下次执行时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextExecuteTime;

    @Schema(description = "执行结果")
    @Size(max=1000, message="执行结果长度不能超过1000个字符")
    private String executeResult;

    @Schema(description = "错误信息")
    @Size(max=65535, message="错误信息长度不能超过65535个字符")
    private String errorMsg;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
}
