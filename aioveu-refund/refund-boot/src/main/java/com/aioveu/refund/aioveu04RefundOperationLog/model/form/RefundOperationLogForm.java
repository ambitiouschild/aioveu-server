package com.aioveu.refund.aioveu04RefundOperationLog.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundOperationLogForm
 * @Description TODO 退款操作记录（用于审计）表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:16
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "退款操作记录（用于审计）表单对象")
public class RefundOperationLogForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退款申请ID")
    @NotNull(message = "退款申请ID不能为空")
    private Long refundId;

    @Schema(description = "操作类型：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动")
    @NotNull(message = "操作类型：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动不能为空")
    private Integer operationType;

    @Schema(description = "操作内容")
    @NotBlank(message = "操作内容不能为空")
    @Size(max=500, message="操作内容长度不能超过500个字符")
    private String operationContent;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "操作人名称")
    @Size(max=50, message="操作人名称长度不能超过50个字符")
    private String operatorName;

    @Schema(description = "操作人类型：1-用户 2-客服 3-商家 4-系统")
    private Integer operatorType;

    @Schema(description = "操作前状态")
    private Integer beforeStatus;

    @Schema(description = "操作后状态")
    private Integer afterStatus;

    @Schema(description = "备注")
    @Size(max=500, message="备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
