package com.aioveu.oms.aioveu05OrderPay.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: OmsOrderPayQuery
 * @Description TODO 支付信息分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 16:57
 * @Version 1.0
 **/

@Schema(description ="支付信息查询对象")
@Getter
@Setter
public class OmsOrderPayQuery extends BasePageQuery {

    @Schema(description = "订单id")
    private Long orderId;
    @Schema(description = "支付流水号")
    private String paySn;
    @Schema(description = "应付总额(分)")
    private Long payAmount;
    @Schema(description = "支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】")
    private Integer payType;
    @Schema(description = "支付状态")
    private Integer payStatus;
    @Schema(description = "回调时间")
    private List<String> callbackTime;
    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
}
