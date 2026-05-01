package com.aioveu.oms.aioveu02OrderItem.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: OmsOrderDetailVO
 * @Description TODO  订单详情视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/5/1 21:37
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "订单商品信息视图对象")
public class OmsOrderDetailVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // ==================== 订单基本信息 ====================
    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderSn;

    @Schema(description = "订单状态 (1:待付款, 2:待发货, 3:已发货, 4:已完成)")
    private Integer status;

    @Schema(description = "订单状态文本")
    private String statusText;

    @Schema(description = "订单状态描述")
    private String statusDesc;

    @Schema(description = "订单总额（分）")
    private Long totalAmount;

    @Schema(description = "商品总数")
    private Integer totalQuantity;

    @Schema(description = "订单来源 (0:PC订单；1:app订单)")
    private Integer source;

    @Schema(description = "订单来源文本")
    private String sourceText;

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "会员id")
    private Long memberId;

    @Schema(description = "使用的优惠券")
    private Long couponId;

    @Schema(description = "优惠券抵扣金额（分）")
    private Long couponAmount;

    @Schema(description = "运费金额（分）")
    private Long freightAmount;

    @Schema(description = "应付总额（分）")
    private Long paymentAmount;

    @Schema(description = "支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    @Schema(description = "支付方式【1->微信jsapi；2->支付宝；3->余额；4->微信app；】")
    private Integer paymentMethod;

    @Schema(description = "支付方式文本")
    private String paymentMethodText;

    @Schema(description = "商户订单号")
    private String outTradeNo;

    @Schema(description = "微信支付订单号")
    private String transactionId;

    @Schema(description = "商户退款单号")
    private String outRefundNo;

    @Schema(description = "微信支付退款单号")
    private String refundId;

    @Schema(description = "发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    @Schema(description = "确认收货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @Schema(description = "评价时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commentTime;

    @Schema(description = "下单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    // ==================== 收货人信息 ====================
    @Schema(description = "收货人姓名")
    private String receiverName;

    @Schema(description = "收货人电话")
    private String receiverPhone;

    @Schema(description = "收货人邮编")
    private String receiverPostCode;

    @Schema(description = "省份/直辖市")
    private String receiverProvince;

    @Schema(description = "城市")
    private String receiverCity;

    @Schema(description = "区")
    private String receiverRegion;

    @Schema(description = "详细地址")
    private String receiverDetailAddress;

    @Schema(description = "完整地址")
    private String receiverFullAddress;

    // ==================== 物流信息 ====================
    @Schema(description = "物流公司(配送方式)")
    private String deliveryCompany;

    @Schema(description = "物流单号")
    private String deliverySn;

    @Schema(description = "物流状态【0->运输中；1->已收货】")
    private Integer deliveryStatus;

    @Schema(description = "物流状态文本")
    private String deliveryStatusText;

    // ==================== 商品信息 ====================
    @Schema(description = "订单商品明细列表")
    private List<OrderItemDetail> orderItems;

    // ==================== 操作权限 ====================
    @Schema(description = "是否可以申请退款")
    private Boolean canRefund = false;

    @Schema(description = "是否可以申请退货")
    private Boolean canReturn = false;

    @Schema(description = "是否可以重新购买")
    private Boolean canRebuy = true;

    @Schema(description = "是否已评价")
    private Boolean isCommented = false;

    // ==================== 内部类 ====================

    /**
     * 订单商品明细
     */
    @Data
    public static class OrderItemDetail {
        @Schema(description = "商品项ID")
        private Long id;

        @Schema(description = "订单ID")
        private Long orderId;

        @Schema(description = "商品名称")
        private String spuName;

        @Schema(description = "规格ID")
        private Long skuId;

        @Schema(description = "SKU 编号")
        private String skuSn;

        @Schema(description = "规格名称")
        private String skuName;

        @Schema(description = "商品sku图片")
        private String picUrl;

        @Schema(description = "商品单价(分)")
        private Long price;

        @Schema(description = "商品数量")
        private Integer quantity;

        @Schema(description = "商品总金额(分)")
        private Long totalAmount;
    }




}
