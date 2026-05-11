package com.aioveu.oms.aioveu10MqConsumeIdempotent.model.query;


import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: MqConsumeIdempotentQuery
 * @Description TODO MQ消费幂等性分页查询对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 18:17
 * @Version 1.0
 **/
@Schema(description ="MQ消费幂等性查询对象")
@Getter
@Setter
public class MqConsumeIdempotentQuery extends BasePageQuery {

    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "业务唯一键")
    private String bizUniqueKey;
    @Schema(description = "业务类型")
    private String bizType;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "消费时间")
    private LocalDateTime consumeTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
