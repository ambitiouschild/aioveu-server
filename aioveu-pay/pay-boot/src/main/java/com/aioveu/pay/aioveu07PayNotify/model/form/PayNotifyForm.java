package com.aioveu.pay.aioveu07PayNotify.model.form;

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
 * @ClassName: PayNotifyForm
 * @Description TODO 支付通知表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 15:55
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "支付通知表单对象")
public class PayNotifyForm implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @NotNull(message = "主键ID不能为空")
    private Long id;

    @Schema(description = "通知编号")
    @NotBlank(message = "通知编号不能为空")
    @Size(max=32, message="通知编号长度不能超过32个字符")
    private String notifyNo;

    @Schema(description = "支付单号")
    @Size(max=32, message="支付单号长度不能超过32个字符")
    private String paymentNo;

    @Schema(description = "退款单号")
    @Size(max=32, message="退款单号长度不能超过32个字符")
    private String refundNo;

    @Schema(description = "通知类型：PAYMENT-支付 REFUND-退款")
    @NotBlank(message = "通知类型：PAYMENT-支付 REFUND-退款不能为空")
    @Size(max=20, message="通知类型：PAYMENT-支付 REFUND-退款长度不能超过20个字符")
    private String notifyType;

    @Schema(description = "通知地址")
    @NotBlank(message = "通知地址不能为空")
    @Size(max=500, message="通知地址长度不能超过500个字符")
    private String notifyUrl;

    @Schema(description = "请求数据")
    @Size(max=65535, message="请求数据长度不能超过65535个字符")
    private String requestData;

    @Schema(description = "响应数据")
    @Size(max=65535, message="响应数据长度不能超过65535个字符")
    private String responseData;

    @Schema(description = "通知状态：0-待通知 1-通知中 2-通知成功 3-通知失败")
    @NotNull(message = "通知状态：0-待通知 1-通知中 2-通知成功 3-通知失败不能为空")
    private Integer notifyStatus;

    @Schema(description = "通知次数")
    @NotNull(message = "通知次数不能为空")
    private Integer notifyCount;

    @Schema(description = "最大通知次数")
    @NotNull(message = "最大通知次数不能为空")
    private Integer maxNotifyCount;

    @Schema(description = "下次通知时间")
    @NotNull(message = "下次通知时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime nextNotifyTime;

    @Schema(description = "最后通知时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastNotifyTime;

    @Schema(description = "成功时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime successTime;

    @Schema(description = "错误信息")
    @Size(max=1000, message="错误信息长度不能超过1000个字符")
    private String errorMessage;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @NotNull(message = "逻辑删除：0-未删除 1-已删除不能为空")
    private Integer isDeleted;

    @Schema(description = "创建时间")
    @NotNull(message = "创建时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @NotNull(message = "更新时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
