package com.aioveu.refund.aioveu04RefundOperationLog.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundOperationLogQuery
 * @Description TODO  退款操作记录（用于审计）分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:16
 * @Version 1.0
 **/

@Schema(description ="退款操作记录（用于审计）查询对象")
@Getter
@Setter
public class RefundOperationLogQuery extends BasePageQuery {

    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "操作类型：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动")
    private Integer operationType;
    @Schema(description = "操作人ID")
    private Long operatorId;
    @Schema(description = "操作人名称")
    private String operatorName;
    @Schema(description = "操作人类型：1-用户 2-客服 3-商家 4-系统")
    private Integer operatorType;
}
