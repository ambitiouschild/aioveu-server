package com.aioveu.oms.aioveu10MqConsumeIdempotent.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: MqConsumeIdempotentVo
 * @Description TODO MQ消费幂等性视图对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 18:18
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "MQ消费幂等性视图对象")
public class MqConsumeIdempotentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "业务唯一键")
    private String bizUniqueKey;
    @Schema(description = "业务类型")
    private String bizType;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "状态:1-已处理")
    private Integer status;
    @Schema(description = "消费时间")
    private LocalDateTime consumeTime;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
