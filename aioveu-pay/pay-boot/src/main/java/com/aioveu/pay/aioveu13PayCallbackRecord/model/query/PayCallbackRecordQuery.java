package com.aioveu.pay.aioveu13PayCallbackRecord.model.query;


import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: PayCallbackRecordQuery
 * @Description TODO 支付回调记录分页查询对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:09
 * @Version 1.0
 **/
@Schema(description ="支付回调记录查询对象")
@Getter
@Setter
public class PayCallbackRecordQuery extends BasePageQuery {

    @Schema(description = "租户ID，0表示平台")
    private Long tenantId;
    @Schema(description = "内部支付单号")
    private String paymentNo;
    @Schema(description = "业务订单号")
    private String orderNo;
    @Schema(description = "支付渠道交易号（微信transaction_id / 支付宝trade_no）")
    private String transactionId;
    @Schema(description = "支付渠道：WECHAT / ALIPAY / UNION")
    private String channel;
    @Schema(description = "回调状态：0-接收 1-处理成功 2-处理失败")
    private Integer notifyStatus;
    @Schema(description = "最后一次回调时间")
    private LocalDateTime lastNotifyTime;
    @Schema(description = "实际支付金额")
    private BigDecimal paidAmount;
    @Schema(description = "商户号")
    private String mchId;
    @Schema(description = "应用ID")
    private String appId;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
