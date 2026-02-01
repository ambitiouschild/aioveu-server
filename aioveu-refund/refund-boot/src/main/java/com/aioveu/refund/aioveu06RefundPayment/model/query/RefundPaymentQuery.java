package com.aioveu.refund.aioveu06RefundPayment.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundPaymentQuery
 * @Description TODO 退款支付记录分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:27
 * @Version 1.0
 **/

@Schema(description ="退款支付记录查询对象")
@Getter
@Setter
public class RefundPaymentQuery extends BasePageQuery {

    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "退款支付单号")
    private String paymentSn;
    @Schema(description = "支付类型：1-微信 2-支付宝 3-银行卡 4-余额")
    private Integer paymentType;
    @Schema(description = "支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败")
    private Integer paymentStatus;
    @Schema(description = "支付渠道")
    private String paymentChannel;
    @Schema(description = "支付交易号")
    private String paymentTradeNo;
}
