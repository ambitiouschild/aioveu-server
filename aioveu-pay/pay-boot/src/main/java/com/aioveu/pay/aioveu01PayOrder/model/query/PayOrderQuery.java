package com.aioveu.pay.aioveu01PayOrder.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
/**
 * @ClassName: PayOrderQuery
 * @Description TODO 支付订单分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:27
 * @Version 1.0
 **/

@Schema(description ="支付订单查询对象")
@Getter
@Setter
public class PayOrderQuery extends BasePageQuery{

    @Schema(description = "支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机")
    private String paymentNo;
    @Schema(description = "业务订单号（如退款单号、订单号）")
    private String orderNo;
    @Schema(description = "业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值")
    private String bizType;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款")
    private Integer paymentStatus;
    @Schema(description = "支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额")
    private String paymentChannel;
    @Schema(description = "支付时间")
    private List<String> paymentTime;
    @Schema(description = "第三方支付单号")
    private String thirdPaymentNo;
    @Schema(description = "创建时间")
    private List<String> createTime;
}
