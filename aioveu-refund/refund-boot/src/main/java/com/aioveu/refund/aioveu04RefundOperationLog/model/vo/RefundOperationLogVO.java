package com.aioveu.refund.aioveu04RefundOperationLog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundOperationLogVO
 * @Description TODO  退款操作记录（用于审计）视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:17
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "退款操作记录（用于审计）视图对象")
public class RefundOperationLogVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "操作类型：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动")
    private Integer operationType;
    @Schema(description = "操作内容")
    private String operationContent;
    @Schema(description = "操作人ID")
    private Long operatorId;
    @Schema(description = "操作人名称")
    private String operatorName;
    @Schema(description = "操作人类型：1-用户 2-客服 3-商家 4-系统")
    private Integer operatorType;
    @Schema(description = "操作前状态")
    private Integer beforeStatus;
    @Schema(description = "操作后状态")
    private Integer afterStatus;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
