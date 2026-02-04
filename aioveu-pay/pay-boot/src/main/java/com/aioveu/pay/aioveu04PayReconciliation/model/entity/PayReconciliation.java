package com.aioveu.pay.aioveu04PayReconciliation.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName: PayReconciliation
 * @Description TODO 支付对账实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:29
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("pay_reconciliation")
public class PayReconciliation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 对账单号
     */
    private String reconciliationNo;
    /**
     * 渠道编码
     */
    private String channelCode;
    /**
     * 对账日期
     */
    private LocalDate billDate;
    /**
     * 账单类型：PAYMENT-支付 REFUND-退款 ALL-全部
     */
    private String billType;
    /**
     * 总笔数
     */
    private Integer totalCount;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 成功笔数
     */
    private Integer successCount;
    /**
     * 成功金额
     */
    private BigDecimal successAmount;
    /**
     * 失败笔数
     */
    private Integer failureCount;
    /**
     * 失败金额
     */
    private BigDecimal failureAmount;
    /**
     * 差异笔数
     */
    private Integer differenceCount;
    /**
     * 对账状态：0-未对账 1-对账中 2-对账完成 3-对账异常
     */
    private Integer reconcileStatus;
    /**
     * 下载状态：0-未下载 1-下载中 2-下载完成 3-下载失败
     */
    private Integer downloadStatus;
    /**
     * 下载时间
     */
    private LocalDateTime downloadTime;
    /**
     * 对账时间
     */
    private LocalDateTime reconcileTime;
    /**
     * 账单文件URL
     */
    private String billFileUrl;
    /**
     * 错误信息
     */
    private String errorMessage;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新人
     */
    private String updateBy;
}
