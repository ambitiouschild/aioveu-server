package com.aioveu.pay.aioveu02PayRefundRecord.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayRefundRecordVO
 * @Description TODO 退款记录视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:49
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "退款记录视图对象")
public class PayRefundRecordVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "退款单号，格式：RFyyyyMMddHHmmss+6位随机")
    private String refundNo;
    @Schema(description = "原支付单号")
    private String paymentNo;
    @Schema(description = "业务订单号")
    private String orderNo;
    @Schema(description = "业务类型：REFUND-退款 ORDER-订单退款")
    private String bizType;
    @Schema(description = "退款金额")
    private BigDecimal refundAmount;
    @Schema(description = "退款状态：0-待退款 1-退款中 2-退款成功 3-退款失败 4-已关闭")
    private Integer refundStatus;
    @Schema(description = "退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额")
    private String refundChannel;
    @Schema(description = "退款原因")
    private String refundReason;
    @Schema(description = "申请时间")
    private LocalDateTime applyTime;
    @Schema(description = "退款完成时间")
    private LocalDateTime refundTime;
    @Schema(description = "第三方退款单号")
    private String thirdRefundNo;
    @Schema(description = "第三方退款流水号")
    private String thirdTransactionNo;
    @Schema(description = "币种")
    private String currency;
    @Schema(description = "错误代码")
    private String errorCode;
    @Schema(description = "错误信息")
    private String errorMessage;
    @Schema(description = "通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败")
    private Integer notifyStatus;
    @Schema(description = "通知次数")
    private Integer notifyCount;
    @Schema(description = "最后通知时间")
    private LocalDateTime lastNotifyTime;
    @Schema(description = "下次通知时间")
    private LocalDateTime nextNotifyTime;
    @Schema(description = "回调数据")
    private String callbackData;
    @Schema(description = "回调时间")
    private LocalDateTime callbackTime;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建人")
    private String createBy;
    @Schema(description = "更新人")
    private String updateBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
