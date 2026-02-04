package com.aioveu.pay.aioveu04PayReconciliation.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName: PayReconciliationVO
 * @Description TODO 支付对账视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:33
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "支付对账视图对象")
public class PayReconciliationVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "对账单号")
    private String reconciliationNo;
    @Schema(description = "渠道编码")
    private String channelCode;
    @Schema(description = "对账日期")
    private LocalDate billDate;
    @Schema(description = "账单类型：PAYMENT-支付 REFUND-退款 ALL-全部")
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
    private Integer reconcileStatus;
    @Schema(description = "下载状态：0-未下载 1-下载中 2-下载完成 3-下载失败")
    private Integer downloadStatus;
    @Schema(description = "下载时间")
    private LocalDateTime downloadTime;
    @Schema(description = "对账时间")
    private LocalDateTime reconcileTime;
    @Schema(description = "账单文件URL")
    private String billFileUrl;
    @Schema(description = "错误信息")
    private String errorMessage;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建人")
    private String createBy;
    @Schema(description = "更新人")
    private String updateBy;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
