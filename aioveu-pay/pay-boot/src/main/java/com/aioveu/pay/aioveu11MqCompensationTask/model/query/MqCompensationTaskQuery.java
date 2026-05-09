package com.aioveu.pay.aioveu11MqCompensationTask.model.query;


import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: MqCompensationTaskQuery
 * @Description TODO MQ补偿任务分页查询对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:49
 * @Version 1.0
 **/
@Schema(description ="MQ补偿任务查询对象")
@Getter
@Setter
public class MqCompensationTaskQuery extends BasePageQuery {

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "任务类型:send_retry")
    private String taskType;
    @Schema(description = "业务ID")
    private String bizId;
    @Schema(description = "业务数据")
    private String bizData;
    @Schema(description = "重试次数")
    private Integer retryCount;
    @Schema(description = "错误信息")
    private String errorMsg;
}
