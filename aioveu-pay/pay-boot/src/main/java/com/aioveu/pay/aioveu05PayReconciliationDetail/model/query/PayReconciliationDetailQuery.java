package com.aioveu.pay.aioveu05PayReconciliationDetail.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: PayReconciliationDetailQuery
 * @Description TODO 对账明细分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 14:24
 * @Version 1.0
 **/

@Schema(description ="对账明细查询对象")
@Getter
@Setter
public class PayReconciliationDetailQuery extends BasePageQuery {


    @Schema(description = "对账单ID")
    private Long reconciliationId;
    @Schema(description = "对账日期")
    private List<String> billDate;
    @Schema(description = "系统支付单号")
    private String paymentNo;
    @Schema(description = "业务订单号")
    private String orderNo;
    @Schema(description = "对账状态：0-未对账 1-成功 2-失败 3-系统多 4-渠道多")
    private Integer reconcileStatus;
    @Schema(description = "对账时间")
    private List<String> reconcileTime;
}
