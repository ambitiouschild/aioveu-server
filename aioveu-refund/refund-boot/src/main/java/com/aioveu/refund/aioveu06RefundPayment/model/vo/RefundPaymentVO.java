package com.aioveu.refund.aioveu06RefundPayment.model.vo;

import com.ibm.icu.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundPaymentVO
 * @Description TODO 退款支付记录视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:28
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "退款支付记录视图对象")
public class RefundPaymentVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "退款支付单号")
    private String paymentSn;
    @Schema(description = "支付类型：1-微信 2-支付宝 3-银行卡 4-余额")
    private Integer paymentType;
    @Schema(description = "支付金额（分）")
    private BigDecimal paymentAmount;
    @Schema(description = "支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败")
    private Integer paymentStatus;
    @Schema(description = "支付时间")
    private LocalDateTime paymentTime;
    @Schema(description = "支付渠道")
    private String paymentChannel;
    @Schema(description = "支付交易号")
    private String paymentTradeNo;
    @Schema(description = "支付手续费（分）")
    private BigDecimal paymentFee;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
