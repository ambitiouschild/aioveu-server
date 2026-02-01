package com.aioveu.refund.aioveu02RefundItem.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundItemQuery
 * @Description TODO  退款商品明细分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:43
 * @Version 1.0
 **/

@Schema(description ="退款商品明细查询对象")
@Getter
@Setter
public class RefundItemQuery extends BasePageQuery {

    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "退款类型（冗余字段，与主表一致）")
    private Integer refundType;
    @Schema(description = "订单项ID")
    private Long orderItemId;
    @Schema(description = "商品ID")
    private Long spuId;
    @Schema(description = "商品名称")
    private String spuName;
    @Schema(description = "SKU ID")
    private Long skuId;
    @Schema(description = "SKU名称")
    private String skuName;
}
