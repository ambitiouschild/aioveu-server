package com.aioveu.oms.aioveu05OrderPay.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderPayForm
 * @Description TODO  支付信息表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 16:56
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "支付信息表单对象")
public class OmsOrderPayForm  implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @Schema(description = "支付流水号")
    @NotBlank(message = "支付流水号不能为空")
    @Size(max=128, message="支付流水号长度不能超过128个字符")
    private String paySn;

    @Schema(description = "应付总额(分)")
    @NotNull(message = "应付总额(分)不能为空")
    private Long payAmount;

    @Schema(description = "支付时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime payTime;

    @Schema(description = "支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】")
    private Integer payType;

    @Schema(description = "支付状态")
    private Integer payStatus;

    @Schema(description = "确认时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime confirmTime;

    @Schema(description = "回调内容")
    @NotBlank(message = "回调内容不能为空")
    @Size(max=500, message="回调内容长度不能超过500个字符")
    private String callbackContent;

    @Schema(description = "回调时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime callbackTime;

    @Schema(description = "交易内容")
    @NotBlank(message = "交易内容不能为空")
    @Size(max=200, message="交易内容长度不能超过200个字符")
    private String paySubject;

    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
}
