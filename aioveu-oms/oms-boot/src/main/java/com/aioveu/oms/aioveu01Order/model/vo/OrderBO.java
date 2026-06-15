package com.aioveu.oms.aioveu01Order.model.vo;

import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.enums.oms.OrderSourceEnum;
import com.aioveu.common.enums.oms.OrderStatusEnum;
import com.aioveu.common.enums.pay.PaymentChannelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: TODO 订单业务对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:09
 * @param
 * @return:
 **/

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderBO extends BaseEntity {

	/**
	 * 订单ID
	 */
	private Long id;
	/**
	 * 订单号
	 */
	private String orderSn;
	/**
	 * 订单总额（分）
	 */
	private Long totalAmount;

	@Schema(description="会员id")
	private Long memberId;

	/**
	 * 商品总数
	 */
	private Integer totalQuantity;

	/**
	 * 订单来源 {@link OrderSourceEnum}
	 */
	private OrderSourceEnum source;

	/**
	 * 订单状态 {@link OrderStatusEnum}
	 */
	private OrderStatusEnum status;

	/**
	 * 应付总额（分）
	 */
	private Long paymentAmount;

	/**
	 * 支付方式 {@link  PaymentChannelEnum}
	 */
	private PaymentChannelEnum paymentMethod;

	/**
	 * 订单创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 订单备注
	 */
	private String remark;

	// 订单展示名：取第一个商品的名称
	private String spuName;

	/**
	 * 订单展示图片：取第一个商品的图片
	 */
	private String picUrl;


	/**
	 * 订单商品明细列表
	 */
	private List<OrderItem> orderItems;

	/**
	 * 订单物流信息
	 */
	private OrderDelivery orderDelivery;

//	// Getter 和 Setter
//	public OrderDelivery getOrderDelivery() {
//		return orderDelivery;
//	}
//
//	public void setOrderDelivery(OrderDelivery orderDelivery) {
//		this.orderDelivery = orderDelivery;
//	}

	@Data
	public static class OrderItem{

		private Long id;

		/**
		 * 订单ID
		 */
		private Long orderId;

		/**
		 * 规格ID
		 */
		private Long skuId;

		/**
		 * SKU编号
		 */
		private String skuSn;

		@Schema(description="商品规格名称")
		private String spuName;

		/**
		 * 商品名称
		 */
		private String skuName;

		/**
		 * 商品sku图片
		 */
		private String picUrl;

		/**
		 * 商品单价(单位：分)
		 */
		private Long price;

		/**
		 * 商品数量
		 */
		private Integer quantity;

		/**
		 * 商品总金额(单位：分)(单价*数量)
		 */
		private Long totalAmount;


	}


	/**
	 * 订单物流信息
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderDelivery {
		/**
		 * id
		 */
		private Long id;

		/**
		 * 订单ID
		 */
		private Long orderId;

		/**
		 * 物流公司(配送方式)
		 */
		private String deliveryCompany;

		/**
		 * 物流单号
		 */
		private String deliverySn;

		/**
		 * 收货人姓名
		 */
		private String receiverName;

		/**
		 * 收货人电话
		 */
		private String receiverPhone;

		/**
		 * 收货人邮编
		 */
		private String receiverPostCode;

		/**
		 * 省份/直辖市
		 */
		private String receiverProvince;

		/**
		 * 城市
		 */
		private String receiverCity;

		/**
		 * 区
		 */
		private String receiverRegion;

		/**
		 * 详细地址
		 */
		private String receiverDetailAddress;

		/**
		 * 备注
		 */
		private String remark;

		/**
		 * 物流状态【0->运输中；1->已收货】
		 */
		private Integer deliveryStatus;

		/**
		 * 发货时间
		 */
		private LocalDateTime deliveryTime;

		/**
		 * 确认收货时间
		 */
		private LocalDateTime receiveTime;
	}

}
