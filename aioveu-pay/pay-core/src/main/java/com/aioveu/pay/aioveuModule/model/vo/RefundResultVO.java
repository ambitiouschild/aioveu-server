package com.aioveu.pay.aioveuModule.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: RefundResultVO
 * @Description TODO 退款结果对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:18
 * @Version 1.0
 **/

@Getter
@Setter
@Builder //需要为 VO 类添加 Lombok 的构建器模式支持
@Schema( description = "退款结果对象")
@NoArgsConstructor
@AllArgsConstructor
public class RefundResultVO {


    private String refundNo;            // 退款单号
    private String thirdRefundNo;      // 第三方退款单号
    private BigDecimal refundAmount;   // 退款金额
    private Integer refundStatus;      // 退款状态
    private Date refundTime;           // 退款时间
    private String errorMessage;       // 错误信息
}
