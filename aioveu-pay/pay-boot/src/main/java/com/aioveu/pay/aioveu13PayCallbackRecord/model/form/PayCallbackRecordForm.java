package com.aioveu.pay.aioveu13PayCallbackRecord.model.form;


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
 * @ClassName: PayCallbackRecordForm
 * @Description TODO 支付回调记录表单对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:08
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "支付回调记录表单对象")
public class PayCallbackRecordForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "租户ID，0表示平台")
    @NotNull(message = "租户ID，0表示平台不能为空")
    private Long tenantId;

    @Schema(description = "内部支付单号")
    @NotBlank(message = "内部支付单号不能为空")
    @Size(max=64, message="内部支付单号长度不能超过64个字符")
    private String paymentNo;

    @Schema(description = "业务订单号")
    @NotBlank(message = "业务订单号不能为空")
    @Size(max=64, message="业务订单号长度不能超过64个字符")
    private String orderNo;

    @Schema(description = "支付渠道交易号（微信transaction_id / 支付宝trade_no）")
    @NotBlank(message = "支付渠道交易号（微信transaction_id / 支付宝trade_no）不能为空")
    @Size(max=64, message="支付渠道交易号（微信transaction_id / 支付宝trade_no）长度不能超过64个字符")
    private String transactionId;

    @Schema(description = "支付渠道：WECHAT / ALIPAY / UNION")
    @NotBlank(message = "支付渠道：WECHAT / ALIPAY / UNION不能为空")
    @Size(max=32, message="支付渠道：WECHAT / ALIPAY / UNION长度不能超过32个字符")
    private String channel;

    @Schema(description = "回调状态：0-接收 1-处理成功 2-处理失败")
    @NotNull(message = "回调状态：0-接收 1-处理成功 2-处理失败不能为空")
    private Integer notifyStatus;

    @Schema(description = "回调次数（防风暴）")
    @NotNull(message = "回调次数（防风暴）不能为空")
    private Integer notifyCount;

    @Schema(description = "最后一次回调时间")
    @NotNull(message = "最后一次回调时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastNotifyTime;

    @Schema(description = "实际支付金额")
    @NotNull(message = "实际支付金额不能为空")
    private BigDecimal paidAmount;

    @Schema(description = "商户号")
    @NotBlank(message = "商户号不能为空")
    @Size(max=32, message="商户号长度不能超过32个字符")
    private String mchId;

    @Schema(description = "应用ID")
    @NotBlank(message = "应用ID不能为空")
    @Size(max=64, message="应用ID长度不能超过64个字符")
    private String appId;

    @Schema(description = "原始回调报文（XML / JSON）")
    @NotBlank(message = "原始回调报文（XML / JSON）不能为空")
    @Size(max=65535, message="原始回调报文（XML / JSON）长度不能超过65535个字符")
    private String rawData;

    @Schema(description = "处理失败原因")
    @NotBlank(message = "处理失败原因不能为空")
    @Size(max=255, message="处理失败原因长度不能超过255个字符")
    private String errorMsg;
}
