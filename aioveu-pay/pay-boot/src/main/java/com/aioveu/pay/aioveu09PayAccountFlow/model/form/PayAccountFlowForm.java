package com.aioveu.pay.aioveu09PayAccountFlow.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayAccountFlowForm
 * @Description TODO 账户流水表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:27
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "账户流水表单对象")
public class PayAccountFlowForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "流水号")
    @NotBlank(message = "流水号不能为空")
    @Size(max=32, message="流水号长度不能超过32个字符")
    private String flowNo;

    @Schema(description = "账户编号")
    @NotBlank(message = "账户编号不能为空")
    @Size(max=32, message="账户编号长度不能超过32个字符")
    private String accountNo;

    @Schema(description = "业务单号")
    @NotBlank(message = "业务单号不能为空")
    @Size(max=32, message="业务单号长度不能超过32个字符")
    private String bizNo;

    @Schema(description = "业务类型：PAYMENT-支付 REFUND-退款 RECHARGE-充值 WITHDRAW-提现")
    @NotBlank(message = "业务类型：PAYMENT-支付 REFUND-退款 RECHARGE-充值 WITHDRAW-提现不能为空")
    @Size(max=20, message="业务类型：PAYMENT-支付 REFUND-退款 RECHARGE-充值 WITHDRAW-提现长度不能超过20个字符")
    private String bizType;

    @Schema(description = "流水类型：INCOME-收入 EXPEND-支出 FREEZE-冻结 UNFREEZE-解冻")
    @NotBlank(message = "流水类型：INCOME-收入 EXPEND-支出 FREEZE-冻结 UNFREEZE-解冻不能为空")
    @Size(max=20, message="流水类型：INCOME-收入 EXPEND-支出 FREEZE-冻结 UNFREEZE-解冻长度不能超过20个字符")
    private String flowType;

    @Schema(description = "变动金额")
    @NotNull(message = "变动金额不能为空")
    private BigDecimal amount;

    @Schema(description = "变动前余额")
    @NotNull(message = "变动前余额不能为空")
    private BigDecimal balanceBefore;

    @Schema(description = "变动后余额")
    @NotNull(message = "变动后余额不能为空")
    private BigDecimal balanceAfter;

    @Schema(description = "变动前冻结")
    private BigDecimal frozenBefore;

    @Schema(description = "变动后冻结")
    private BigDecimal frozenAfter;

    @Schema(description = "备注")
    @Size(max=500, message="备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @NotNull(message = "逻辑删除：0-未删除 1-已删除不能为空")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
