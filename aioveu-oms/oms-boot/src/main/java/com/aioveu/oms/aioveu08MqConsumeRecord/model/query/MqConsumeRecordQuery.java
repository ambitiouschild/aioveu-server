package com.aioveu.oms.aioveu08MqConsumeRecord.model.query;


import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: MqConsumeRecordQuery
 * @Description TODO MQ消息消费记录分页查询对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:28
 * @Version 1.0
 **/
@Schema(description ="MQ消息消费记录查询对象")
@Getter
@Setter
public class MqConsumeRecordQuery extends BasePageQuery {

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "Topic")
    private String topic;
    @Schema(description = "Tag")
    private String tag;
    @Schema(description = "业务ID(订单号)")
    private String bizId;
    @Schema(description = "最大重试次数")
    private Integer maxRetry;
}
