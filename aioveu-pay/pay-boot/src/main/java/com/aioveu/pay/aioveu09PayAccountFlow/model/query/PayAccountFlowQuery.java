package com.aioveu.pay.aioveu09PayAccountFlow.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: PayAccountFlowQuery
 * @Description TODO 账户流水分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:28
 * @Version 1.0
 **/

@Schema(description ="账户流水查询对象")
@Getter
@Setter
public class PayAccountFlowQuery extends BasePageQuery {

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
    @Schema(description = "创建时间")
    private List<String> createTime;
}
