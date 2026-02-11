package com.aioveu.pay.aioveu07PayNotify.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: PayNotifyDTO
 * @Description TODO 异步支付通知视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 17:06
 * @Version 1.0
 **/

@Getter
@Setter
@Builder
@Schema( description = "异步支付通知视图对象")
public class PayNotifyDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "通知编号")
    private String notifyNo;
    @Schema(description = "支付单号")
    private String paymentNo;
    @Schema(description = "退款单号")
    private String refundNo;
    @Schema(description = "通知类型：PAYMENT-支付 REFUND-退款")
    private String notifyType;
    @Schema(description = "通知地址")
    private String notifyUrl;
    @Schema(description = "请求数据")
    private String requestData;
    @Schema(description = "响应数据")
    private String responseData;
    @Schema(description = "通知状态：0-待通知 1-通知中 2-通知成功 3-通知失败")
    private Integer notifyStatus;
    @Schema(description = "通知次数")
    private Integer notifyCount;
    @Schema(description = "最大通知次数")
    private Integer maxNotifyCount;
    @Schema(description = "下次通知时间")
    private LocalDateTime nextNotifyTime;
    @Schema(description = "最后通知时间")
    private LocalDateTime lastNotifyTime;
    @Schema(description = "成功时间")
    private LocalDateTime successTime;
    @Schema(description = "错误信息")
    private String errorMessage;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
