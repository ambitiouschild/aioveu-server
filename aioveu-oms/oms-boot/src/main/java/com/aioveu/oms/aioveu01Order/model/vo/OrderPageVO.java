package com.aioveu.oms.aioveu01Order.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description: TODO App-订单分页视图对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

@Schema(description ="App-订单分页视图对象")
@Data
public class OrderPageVO {

    @Schema(description="订单ID")
    private Long id;

    @Schema(description="订单编号")
    private String orderSn;

    @Schema(description="订单总金额(分)")
    private BigDecimal totalAmount;

    @Schema(description="订单总金额(分)")
    private Long paymentAmount;

    @Schema(description="支付方式标签")
    private String paymentMethodLabel;

    @Schema(description="订单状态")
    private Integer status;

    @Schema(description="订单状态标签")
    private String statusLabel;

    @Schema(description="商品总数")
    private Integer totalQuantity;

    @Schema(description="订单创建时间")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date createTime;

    @Schema(description="订单来源标签")
    private String sourceLabel;

    @Schema(description="订单备注")
    private String remark;

    @Schema(description="订单商品集合")
    private List<OrderItem> orderItems;

    @Schema(description="商品名称（取第一个商品的 spuName）")
    // 确认是否有这两个字段
    private String spuName;  // 商品展示名
    private String picUrl;   // 商品展示图

    @Schema(description ="订单商品明细")
    @Data
    public static class OrderItem {


        /**
         * 订单ID
         */
        private Long orderId;


        @Schema(description="商品ID")
        private Long skuId;

        /**
         * SKU编号
         */
        private String skuSn;


        // 商品信息
        private String spuName;      // 商品名称

        @Schema(description="商品规格名称")
        private String skuName;

        @Schema(description="图片地址")
        private String picUrl;

        @Schema(description="商品价格")
        private Long price;

        @Schema(description="商品数量")
        private Integer quantity;

        @Schema(description="商品总金额(单位：分)")
        private Long totalAmount;

    }

}
