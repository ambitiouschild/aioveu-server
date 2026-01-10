package com.aioveu.oms.aioveu02OrderItem.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: OmsOrderItemQuery
 * @Description TODO  订单商品信息分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 19:41
 * @Version 1.0
 **/

@Schema(description ="订单商品信息查询对象")
@Getter
@Setter
public class OmsOrderItemQuery extends BasePageQuery {

    @Schema(description = "商品名称")
    private String spuName;
    @Schema(description = "商品编码")
    private String skuSn;
    @Schema(description = "规格名称")
    private String skuName;
    @Schema(description = "逻辑删除标识(1:已删除；0:正常)")
    private Integer deleted;
}
