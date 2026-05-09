package com.aioveu.pay.aioveu10MqSendRecord.model.query;


import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: MqSendRecordQuery
 * @Description TODO MQ消息发送记录分页查询对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:36
 * @Version 1.0
 * @Param
 * @return
 **/
@Schema(description ="MQ消息发送记录查询对象")
@Getter
@Setter
public class MqSendRecordQuery extends BasePageQuery {

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "业务ID(支付单号)")
    private String bizId;
    @Schema(description = "业务类型:payment_success")
    private String bizType;
    @Schema(description = "Tag")
    private String tag;
    @Schema(description = "发送状态:0-未发送,1-发送中,2-发送成功,3-发送失败")
    private Integer sendStatus;
    @Schema(description = "发送时间")
    private LocalDateTime sendTime;
    @Schema(description = "确认时间")
    private LocalDateTime confirmTime;
    @Schema(description = "错误信息")
    private String errorMsg;
}
