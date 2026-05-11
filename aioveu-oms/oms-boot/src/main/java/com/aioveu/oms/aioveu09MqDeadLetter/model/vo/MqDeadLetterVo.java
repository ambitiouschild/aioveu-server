package com.aioveu.oms.aioveu09MqDeadLetter.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: MqDeadLetterVo
 * @Description TODO MQ死信队列视图对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:49
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "MQ死信队列视图对象")
public class MqDeadLetterVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    @Schema(description = "租户ID，0表示平台默认")
    private Long tenantId;
    @Schema(description = "消息ID")
    private String messageId;
    @Schema(description = "Topic")
    private String topic;
    @Schema(description = "Tag")
    private String tag;
    @Schema(description = "消费者组")
    private String consumerGroup;
    @Schema(description = "业务ID")
    private String bizId;
    @Schema(description = "消息体")
    private String messageBody;
    @Schema(description = "消费次数")
    private Integer consumeTimes;
    @Schema(description = "错误信息")
    private String errorMsg;
    @Schema(description = "死信原因")
    private String deadReason;
    @Schema(description = "处理状态:0-未处理,1-已处理")
    private Integer handleStatus;
    @Schema(description = "处理时间")
    private LocalDateTime handleTime;
    @Schema(description = "处理结果")
    private String handleResult;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
