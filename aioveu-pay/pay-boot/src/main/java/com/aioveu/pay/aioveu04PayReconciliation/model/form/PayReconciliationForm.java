package com.aioveu.pay.aioveu04PayReconciliation.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName: PayReconciliationForm
 * @Description TODO 支付对账表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:30
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "支付对账表单对象")
public class PayReconciliationForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "对账单号")
    @NotBlank(message = "对账单号不能为空")
    @Size(max=32, message="对账单号长度不能超过32个字符")
    private String reconciliationNo;

    @Schema(description = "渠道编码")
    @NotBlank(message = "渠道编码不能为空")
    @Size(max=20, message="渠道编码长度不能超过20个字符")
    private String channelCode;

    @Schema(description = "对账日期")
    @NotNull(message = "对账日期不能为空")
    private LocalDate billDate;

    @Schema(description = "账单类型：PAYMENT-支付 REFUND-退款 ALL-全部")
    @NotBlank(message = "账单类型：PAYMENT-支付 REFUND-退款 ALL-全部不能为空")
    @Size(max=20, message="账单类型：PAYMENT-支付 REFUND-退款 ALL-全部长度不能超过20个字符")
    private String billType;

    @Schema(description = "总笔数")
    private Integer totalCount;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "成功笔数")
    private Integer successCount;

    @Schema(description = "成功金额")
    private BigDecimal successAmount;

    @Schema(description = "失败笔数")
    private Integer failureCount;

    @Schema(description = "失败金额")
    private BigDecimal failureAmount;

    @Schema(description = "差异笔数")
    private Integer differenceCount;

    @Schema(description = "对账状态：0-未对账 1-对账中 2-对账完成 3-对账异常")
    @NotNull(message = "对账状态：0-未对账 1-对账中 2-对账完成 3-对账异常不能为空")
    private Integer reconcileStatus;

    @Schema(description = "下载状态：0-未下载 1-下载中 2-下载完成 3-下载失败")
    private Integer downloadStatus;

    @Schema(description = "下载时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime downloadTime;

    @Schema(description = "对账时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reconcileTime;

    @Schema(description = "账单文件URL")
    @Size(max=500, message="账单文件URL长度不能超过500个字符")
    private String billFileUrl;

    @Schema(description = "错误信息")
    @Size(max=1000, message="错误信息长度不能超过1000个字符")
    private String errorMessage;

    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    @NotNull(message = "逻辑删除：0-未删除 1-已删除不能为空")
    private Integer isDeleted;

    @Schema(description = "创建人")
    @Size(max=64, message="创建人长度不能超过64个字符")
    private String createBy;

    @Schema(description = "更新人")
    @Size(max=64, message="更新人长度不能超过64个字符")
    private String updateBy;

    @Schema(description = "创建时间")
    @NotNull(message = "创建时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @NotNull(message = "更新时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
