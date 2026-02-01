package com.aioveu.refund.aioveu01RefundOrder.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundOrderQuery
 * @Description TODO  订单退款申请分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:23
 * @Version 1.0
 **/

@Schema(description ="订单退款申请查询对象")
@Getter
@Setter
public class RefundOrderQuery extends BasePageQuery {

    @Schema(description = "订单ID")
    private Long orderId;
    @Schema(description = "订单编号")
    private String orderSn;
    @Schema(description = "退款单号")
    private String refundSn;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "退款类型：1-仅退款 2-退货退款 3-换货")
    private Integer refundType;
    @Schema(description = "退款状态：0-待处理 1-审核中 2-审核通过 3-审核拒绝 4-退款中 5-退款成功 6-退款失败")
    private Integer status;
}
