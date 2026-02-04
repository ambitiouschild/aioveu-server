package com.aioveu.pay.aioveu02PayRefundRecord.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: PayRefundRecordQuery
 * @Description TODO 退款记录分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:49
 * @Version 1.0
 **/

@Schema(description ="退款记录查询对象")
@Getter
@Setter
public class PayRefundRecordQuery extends BasePageQuery {

    @Schema(description = "原支付单号")
    private String paymentNo;
    @Schema(description = "业务订单号")
    private String orderNo;
    @Schema(description = "退款状态：0-待退款 1-退款中 2-退款成功 3-退款失败 4-已关闭")
    private Integer refundStatus;
    @Schema(description = "退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额")
    private String refundChannel;
    @Schema(description = "退款完成时间")
    private List<String> refundTime;
    @Schema(description = "第三方退款流水号")
    private String thirdTransactionNo;
    @Schema(description = "创建时间")
    private List<String> createTime;
}
