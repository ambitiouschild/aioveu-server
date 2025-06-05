package com.aioveu.oms.model.bo;

import com.aioveu.common.base.BaseEntity;
import com.aioveu.oms.enums.OrderSourceEnum;
import com.aioveu.oms.enums.OrderStatusEnum;
import com.aioveu.oms.enums.PaymentMethodEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
	/**
	 * 商品总数
	 */
	private Integer totalQuantity;

	/**
	 * 订单来源 {@link OrderSourceEnum}
	 */
	private Integer source;

	/**
	 * 订单状态 {@link OrderStatusEnum}
	 */
	private Integer status;

	/**
	 * 应付总额（分）
	 */
	private Long paymentAmount;

	/**
	 * 支付方式 {@link  PaymentMethodEnum}
	 */
	private Integer paymentMethod;

	/**
	 * 订单创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 订单备注
	 */
	private String remark;

	/**
	 * 订单商品明细列表
	 */
	private List<OrderItem> orderItems;

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

}
