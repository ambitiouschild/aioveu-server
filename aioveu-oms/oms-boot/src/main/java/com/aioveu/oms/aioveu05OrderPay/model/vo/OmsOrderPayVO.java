package com.aioveu.oms.aioveu05OrderPay.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderPayVO
 * @Description TODO  支付信息视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 16:58
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "支付信息视图对象")
public class OmsOrderPayVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;
    @Schema(description = "订单id")
    private Long orderId;
    @Schema(description = "支付流水号")
    private String paySn;
    @Schema(description = "应付总额(分)")
    private Long payAmount;
    @Schema(description = "支付时间")
    private LocalDateTime payTime;
    @Schema(description = "支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】")
    private Integer payType;
    @Schema(description = "支付状态")
    private Integer payStatus;
    @Schema(description = "确认时间")
    private LocalDateTime confirmTime;
    @Schema(description = "回调内容")
    private String callbackContent;
    @Schema(description = "回调时间")
    private LocalDateTime callbackTime;
    @Schema(description = "交易内容")
    private String paySubject;
    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
