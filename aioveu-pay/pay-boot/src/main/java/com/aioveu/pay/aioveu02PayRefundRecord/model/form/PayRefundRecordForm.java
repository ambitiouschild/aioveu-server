package com.aioveu.pay.aioveu02PayRefundRecord.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayRefundRecordForm
 * @Description TODO 退款记录表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:48
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "退款记录表单对象")
public class PayRefundRecordForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退款单号，格式：RFyyyyMMddHHmmss+6位随机")
    @NotBlank(message = "退款单号，格式：RFyyyyMMddHHmmss+6位随机不能为空")
    @Size(max=32, message="退款单号，格式：RFyyyyMMddHHmmss+6位随机长度不能超过32个字符")
    private String refundNo;

    @Schema(description = "原支付单号")
    @NotBlank(message = "原支付单号不能为空")
    @Size(max=32, message="原支付单号长度不能超过32个字符")
    private String paymentNo;

    @Schema(description = "业务订单号")
    @NotBlank(message = "业务订单号不能为空")
    @Size(max=32, message="业务订单号长度不能超过32个字符")
    private String orderNo;

    @Schema(description = "业务类型：REFUND-退款 ORDER-订单退款")
    @NotBlank(message = "业务类型：REFUND-退款 ORDER-订单退款不能为空")
    @Size(max=20, message="业务类型：REFUND-退款 ORDER-订单退款长度不能超过20个字符")
    private String bizType;

    @Schema(description = "退款金额")
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundAmount;

    @Schema(description = "退款状态：0-待退款 1-退款中 2-退款成功 3-退款失败 4-已关闭")
    @NotNull(message = "退款状态：0-待退款 1-退款中 2-退款成功 3-退款失败 4-已关闭不能为空")
    private Integer refundStatus;

    @Schema(description = "退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额")
    @NotBlank(message = "退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额不能为空")
    @Size(max=20, message="退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额长度不能超过20个字符")
    private String refundChannel;

    @Schema(description = "退款原因")
    @Size(max=500, message="退款原因长度不能超过500个字符")
    private String refundReason;

    @Schema(description = "申请时间")
    @NotNull(message = "申请时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    @Schema(description = "退款完成时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refundTime;

    @Schema(description = "第三方退款单号")
    @Size(max=128, message="第三方退款单号长度不能超过128个字符")
    private String thirdRefundNo;

    @Schema(description = "第三方退款流水号")
    @Size(max=128, message="第三方退款流水号长度不能超过128个字符")
    private String thirdTransactionNo;

    @Schema(description = "币种")
    @Size(max=3, message="币种长度不能超过3个字符")
    private String currency;

    @Schema(description = "错误代码")
    @Size(max=50, message="错误代码长度不能超过50个字符")
    private String errorCode;

    @Schema(description = "错误信息")
    @Size(max=500, message="错误信息长度不能超过500个字符")
    private String errorMessage;

    @Schema(description = "通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败")
    private Integer notifyStatus;

    @Schema(description = "通知次数")
    private Integer notifyCount;

    @Schema(description = "最后通知时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastNotifyTime;

    @Schema(description = "下次通知时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextNotifyTime;

    @Schema(description = "回调数据")
    @Size(max=65535, message="回调数据长度不能超过65535个字符")
    private String callbackData;

    @Schema(description = "回调时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime callbackTime;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @NotNull(message = "逻辑删除：0-未删除 1-已删除不能为空")
    private Integer isDeleted;

    @Schema(description = "创建人")
    @Size(max=64, message="创建人长度不能超过64个字符")
    private String createBy;

    @Schema(description = "更新人")
    @Size(max=64, message="更新人长度不能超过64个字符")
    private String updateBy;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
