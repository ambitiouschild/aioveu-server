package com.aioveu.oms.aioveu01Order.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderForm
 * @Description TODO   订单详情表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 18:26
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "订单详情表单对象")
public class OmsOrderForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "订单号")
    @NotBlank(message = "订单号不能为空")
    @Size(max=64, message="订单号长度不能超过64个字符")
    private String orderSn;

    @Schema(description = "订单总额（分）")
    @NotNull(message = "订单总额（分）不能为空")
    private Long totalAmount;

    @Schema(description = "商品总数")
    @NotNull(message = "商品总数不能为空")
    private Integer totalQuantity;

    @Schema(description = "订单来源(1:APP；2:网页)")
    private Integer source;

    @Schema(description = "订单状态：")
    @NotNull(message = "订单状态：")
    private Integer status;

    @Schema(description = "订单备注")
    @NotBlank(message = "订单备注不能为空")
    @Size(max=500, message="订单备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "会员id")
    @NotNull(message = "会员id不能为空")
    private Long memberId;

    @Schema(description = "使用的优惠券")
    @NotNull(message = "使用的优惠券不能为空")
    private Long couponId;

    @Schema(description = "优惠券抵扣金额（分）")
    @NotNull(message = "优惠券抵扣金额（分）不能为空")
    private Long couponAmount;

    @Schema(description = "运费金额（分）")
    @NotNull(message = "运费金额（分）不能为空")
    private Long freightAmount;

    @Schema(description = "应付总额（分）")
    @NotNull(message = "应付总额（分）不能为空")
    private Long paymentAmount;

    @Schema(description = "支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "支付方式(1：微信JSAPI；2：支付宝；3：余额；4：微信APP)")
    private Integer paymentMethod;

    @Schema(description = "微信支付等第三方支付平台的商户订单号")
    @Size(max=32, message="微信支付等第三方支付平台的商户订单号长度不能超过32个字符")
    private String outTradeNo;

    @Schema(description = "微信支付订单号")
    @Size(max=32, message="微信支付订单号长度不能超过32个字符")
    private String transactionId;

    @Schema(description = "商户退款单号")
    @Size(max=32, message="商户退款单号长度不能超过32个字符")
    private String outRefundNo;

    @Schema(description = "微信退款单号")
    @Size(max=32, message="微信退款单号长度不能超过32个字符")
    private String refundId;

    @Schema(description = "发货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryTime;

    @Schema(description = "确认收货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    @Schema(description = "评价时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime commentTime;

    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
}
