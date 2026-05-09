package com.aioveu.pay.aioveu11MqCompensationTask.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: MqCompensationTaskVo
 * @Description TODO MQ补偿任务视图对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:50
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "MQ补偿任务视图对象")
public class MqCompensationTaskVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "任务类型:send_retry")
    private String taskType;
    @Schema(description = "业务ID")
    private String bizId;
    @Schema(description = "业务数据")
    private String bizData;
    @Schema(description = "状态:0-待处理,1-处理中,2-成功,3-失败")
    private Integer status;
    @Schema(description = "重试次数")
    private Integer retryCount;
    @Schema(description = "下次执行时间")
    private LocalDateTime nextExecuteTime;
    @Schema(description = "执行结果")
    private String executeResult;
    @Schema(description = "错误信息")
    private String errorMsg;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
