package com.aioveu.pay.aioveu04PayReconciliation.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: PayReconciliationQuery
 * @Description TODO 支付对账分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:32
 * @Version 1.0
 **/

@Schema(description ="支付对账查询对象")
@Getter
@Setter
public class PayReconciliationQuery extends BasePageQuery {

    @Schema(description = "对账单号")
    private String reconciliationNo;
    @Schema(description = "渠道编码")
    private String channelCode;
    @Schema(description = "账单类型：PAYMENT-支付 REFUND-退款 ALL-全部")
    private String billType;
    @Schema(description = "对账状态：0-未对账 1-对账中 2-对账完成 3-对账异常")
    private Integer reconcileStatus;
    @Schema(description = "对账时间")
    private List<String> reconcileTime;
}
