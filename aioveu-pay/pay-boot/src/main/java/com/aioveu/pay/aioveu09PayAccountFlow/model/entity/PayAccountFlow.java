package com.aioveu.pay.aioveu09PayAccountFlow.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @ClassName: PayAccountFlow
 * @Description TODO 账户流水实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:26
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("pay_account_flow")
public class PayAccountFlow  extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    private String flowNo;
    /**
     * 账户编号
     */
    private String accountNo;
    /**
     * 业务单号
     */
    private String bizNo;
    /**
     * 业务类型：PAYMENT-支付 REFUND-退款 RECHARGE-充值 WITHDRAW-提现
     */
    private String bizType;
    /**
     * 流水类型：INCOME-收入 EXPEND-支出 FREEZE-冻结 UNFREEZE-解冻
     */
    private String flowType;
    /**
     * 变动金额
     */
    private BigDecimal amount;
    /**
     * 变动前余额
     */
    private BigDecimal balanceBefore;
    /**
     * 变动后余额
     */
    private BigDecimal balanceAfter;
    /**
     * 变动前冻结
     */
    private BigDecimal frozenBefore;
    /**
     * 变动后冻结
     */
    private BigDecimal frozenAfter;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
