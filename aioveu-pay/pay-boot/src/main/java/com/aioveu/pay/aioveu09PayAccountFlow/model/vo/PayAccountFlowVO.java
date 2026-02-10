package com.aioveu.pay.aioveu09PayAccountFlow.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayAccountFlowVO
 * @Description TODO 账户流水视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:28
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "账户流水视图对象")
public class PayAccountFlowVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "流水号")
    private String flowNo;
    @Schema(description = "账户编号")
    private String accountNo;
    @Schema(description = "业务单号")
    private String bizNo;
    @Schema(description = "业务类型：PAYMENT-支付 REFUND-退款 RECHARGE-充值 WITHDRAW-提现")
    private String bizType;
    @Schema(description = "流水类型：INCOME-收入 EXPEND-支出 FREEZE-冻结 UNFREEZE-解冻")
    private String flowType;
    @Schema(description = "变动金额")
    private BigDecimal amount;
    @Schema(description = "变动前余额")
    private BigDecimal balanceBefore;
    @Schema(description = "变动后余额")
    private BigDecimal balanceAfter;
    @Schema(description = "变动前冻结")
    private BigDecimal frozenBefore;
    @Schema(description = "变动后冻结")
    private BigDecimal frozenAfter;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
