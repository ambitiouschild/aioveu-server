package com.aioveu.pay.aioveu06PayFlow.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: PayFlowQuery
 * @Description TODO 支付流水分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:48
 * @Version 1.0
 **/

@Schema(description ="支付流水查询对象")
@Getter
@Setter
public class PayFlowQuery extends BasePageQuery {

    @Schema(description = "流水号")
    private String flowNo;
    @Schema(description = "支付单号")
    private String paymentNo;
    @Schema(description = "退款单号")
    private String refundNo;
    @Schema(description = "业务订单号")
    private String orderNo;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账")
    private String flowType;
    @Schema(description = "资金方向：IN-入金 OUT-出金")
    private String flowDirection;
    @Schema(description = "流水状态：0-处理中 1-成功 2-失败")
    private Integer flowStatus;
    @Schema(description = "交易时间")
    private List<String> tradeTime;
    @Schema(description = "创建时间")
    private List<String> createTime;
}
