package com.aioveu.oms.aioveu01Order.model.entity;

import com.aioveu.common.base.BaseEntityWithTenantId;
import com.aioveu.common.enums.oms.OrderSourceEnum;
import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.common.enums.oms.OrderStatusEnum;
import com.aioveu.common.enums.pay.PaymentMethodEnum;
import com.aioveu.oms.aioveu01Order.utils.PaymentChannelEnumCodeTypeHandler;
import com.aioveu.oms.aioveu01Order.utils.PaymentMethodEnumCodeTypeHandler;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO 订单详情表
 * @Author: 雒世松
 * @Date: 2025/6/5 18:10
 * @param
 * @return:
 **/

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("oms_order")
public class OmsOrder extends BaseEntityWithTenantId {


	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 商户订单号 = orderSn
	 */
	private String orderSn;

	/**
	 * 租户ID（数据隔离） tenantId = 数据隔离
	 */
//	private Long tenantId;

	/**
	 * 微信应用ID（发货身份）  clientId = 微信能力
	 */
	private String clientId;

	/**
	 * 订单总额（分）
	 */
	private Long totalAmount;
	/**
	 * 商品总数
	 */
	private Integer totalQuantity;
	/**
	 * 订单来源(0-PC订单；1-app订单)
	 */
	private OrderSourceEnum source;

	/**
	 * 订单状态(1-待付款;2-待发货;3-已发货;4-已完成;)
	 */
	private OrderStatusEnum status;
	/**
	 * 订单备注
	 */
	private String remark;
	/**
	 * 会员id
	 */
	private Long memberId;

//	/**
//	 * 订单令牌
//	 */
//	private String orderToken;


	/**
	 * 使用的优惠券
	 */
	private Long couponId;
	/**
	 * 优惠券抵扣金额（分）
	 */
	private Long couponAmount;
	/**
	 * 运费金额（分）
	 */
	private Long freightAmount;
	/**
	 * 应付总额（分）
	 */
	private Long paymentAmount;
	/**
	 * 支付时间
	 */
	private LocalDateTime paymentTime;

	/**
	 * 数据库：INT
	 * 使用 code（1/2/3）
	 */
	/**
	 * 支付渠道【1->支付宝；2->微信支付；3->银联；4->余额；5->模拟支付；6->未知；】
	 */
	@TableField(value = "payment_channel",
			typeHandler = PaymentChannelEnumCodeTypeHandler.class)
	private PaymentChannelEnum paymentChannel;

	/**
	 * 数据库：INT
	 * 使用 code（1/2/3）
	 */
	/**
	 * 支付方式【1->APP支付；2->H5支付；3->小程序/公众号支付；4->扫码支付；】
	 */
	@TableField(value = "payment_method",
			typeHandler = PaymentMethodEnumCodeTypeHandler.class)
	private PaymentMethodEnum paymentMethod;

	/**
	 * 商户侧支付订单号 = outTradeNo = paymentNo
	 * 商户侧支付订单号（支付服务生成的 paymentNo）
	 * 对应微信/支付宝 out_trade_no
	 * outTradeNo 是“微信/支付宝眼里的 paymentNo”
	 * paymentNo 是“你支付服务眼里的 outTradeNo”
	 */
//	@TableField(updateStrategy = FieldStrategy.IGNORED)
			//支付订单号一旦写入，绝不允许修改
	@TableField(updateStrategy = FieldStrategy.NEVER)
	private String outTradeNo;
	/**
	 * 微信支付订单号
	 * 发货时：oms → 查 pay → 拿 transaction_id,transaction_id是“钱”的属性.弃用
	 * transactionId ≠ paymentNo / outTradeNo
	 * 它是“钱已经落袋”之后，微信/支付宝给你的“银行回单号”。
	 */
	private String transactionId;
	/**
	 * 商户退款单号
	 */
	private String outRefundNo;
	/**
	 * 微信支付退款单号
	 */
	private String refundId;
	/**
	 * 发货时间
	 */
	private Date deliveryTime;
	/**
	 * 确认收货时间
	 */
	private Date receiveTime;
	/**
	 * 评价时间
	 */
	private Date commentTime;

	/**
	 * 逻辑删除标识(1:已删除；0:正常)
	 */
	private Integer deleted;

	@TableField(exist = false)
	private List<OmsOrderItem> orderItems;

}
