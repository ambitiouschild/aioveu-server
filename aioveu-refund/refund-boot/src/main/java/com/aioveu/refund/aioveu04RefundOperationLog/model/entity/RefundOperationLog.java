package com.aioveu.refund.aioveu04RefundOperationLog.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundOperationLog
 * @Description TODO  退款操作记录（用于审计）实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:15
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("refund_operation_log")
public class RefundOperationLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 退款申请ID
     */
    private Long refundId;
    /**
     * 操作类型：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动
     */
    private Integer operationType;
    /**
     * 操作内容
     */
    private String operationContent;
    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 操作人名称
     */
    private String operatorName;
    /**
     * 操作人类型：1-用户 2-客服 3-商家 4-系统
     */
    private Integer operatorType;
    /**
     * 操作前状态
     */
    private Integer beforeStatus;
    /**
     * 操作后状态
     */
    private Integer afterStatus;
    /**
     * 备注
     */
    private String remark;
}
