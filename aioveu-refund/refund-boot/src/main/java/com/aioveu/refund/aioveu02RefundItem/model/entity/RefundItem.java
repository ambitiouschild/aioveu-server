package com.aioveu.refund.aioveu02RefundItem.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ibm.icu.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundItem
 * @Description TODO  退款商品明细实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:41
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("refund_item")
public class RefundItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 退款申请ID
     */
    private Long refundId;
    /**
     * 退款类型（冗余字段，与主表一致）
     */
    private Integer refundType;
    /**
     * 订单项ID
     */
    private Long orderItemId;
    /**
     * 商品ID
     */
    private Long spuId;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * SKU ID
     */
    private Long skuId;
    /**
     * SKU名称
     */
    private String skuName;
    /**
     * 商品图片
     */
    private String picUrl;
    /**
     * 商品单价（分）
     */
    private BigDecimal price;
    /**
     * 退款数量
     */
    private Integer quantity;
    /**
     * 退款金额（分）
     */
    private BigDecimal refundAmount;
    /**
     * 该商品的退款原因
     */
    private String refundReason;
    /**
     * 逻辑删除
     */
    private Integer deleted;
}
