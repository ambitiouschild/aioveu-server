package com.aioveu.pay.aioveu07PayNotify.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: PayNotifyQuery
 * @Description TODO 支付通知分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 15:56
 * @Version 1.0
 **/

@Schema(description ="支付通知查询对象")
@Getter
@Setter
public class PayNotifyQuery extends BasePageQuery {

    @Schema(description = "通知编号")
    private String notifyNo;
    @Schema(description = "支付单号")
    private String paymentNo;
    @Schema(description = "退款单号")
    private String refundNo;
    @Schema(description = "通知类型：PAYMENT-支付 REFUND-退款")
    private String notifyType;
    @Schema(description = "通知状态：0-待通知 1-通知中 2-通知成功 3-通知失败")
    private Integer notifyStatus;
    @Schema(description = "下次通知时间")
    private List<String> nextNotifyTime;
    @Schema(description = "创建时间")
    private List<String> createTime;
}
