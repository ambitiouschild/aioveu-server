package com.aioveu.oms.aioveu09MqDeadLetter.model.query;


import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: MqDeadLetterQuery
 * @Description TODO MQ死信队列分页查询对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:48
 * @Version 1.0
 **/
@Schema(description ="MQ死信队列查询对象")
@Getter
@Setter
public class MqDeadLetterQuery extends BasePageQuery {

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "Topic")
    private String topic;
    @Schema(description = "Tag")
    private String tag;
    @Schema(description = "业务ID")
    private String bizId;
    @Schema(description = "错误信息")
    private String errorMsg;
}
