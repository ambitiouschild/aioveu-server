package com.aioveu.oms.model.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.aioveu.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class OmsOrder extends BaseEntity {


	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 订单号
	 */
	private String orderSn;
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
	private Integer source;

	/**
	 * 订单状态(1-待付款;2-待发货;3-已发货;4-已完成;5-已关闭;6-已取消;)
	 */
	private Integer status;
	/**
	 * 订单备注
	 */
	private String remark;
	/**
	 * 会员id
	 */
	private Long memberId;
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
	private Date paymentTime;
	/**
	 * 支付方式【1->微信jsapi；2->支付宝；3->余额；4->微信app；】
	 */
	private Integer paymentMethod;
	/**
	 * 商户订单号
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String outTradeNo;
	/**
	 * 微信支付订单号
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
