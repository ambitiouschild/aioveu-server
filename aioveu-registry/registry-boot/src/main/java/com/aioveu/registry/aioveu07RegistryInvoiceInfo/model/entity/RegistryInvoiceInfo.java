package com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RegistryInvoiceInfo
 * @Description TODO 发票信息实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:37
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_invoice_info")
public class RegistryInvoiceInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 认证记录ID
     */
    private Long certificationId;
    /**
     * 发票类型：1-电子发票，2-纸质发票
     */
    private Integer invoiceType;
    /**
     * 发票抬头
     */
    private String invoiceTitle;
    /**
     * 纳税人识别号
     */
    private String taxIdentificationNo;
    /**
     * 发票备注
     */
    private String invoiceRemark;
    /**
     * 电子发票接收邮箱
     */
    private String invoiceEmail;
    /**
     * 收件人姓名
     */
    private String receiverName;
    /**
     * 收件人电话
     */
    private String receiverPhone;
    /**
     * 收件地址
     */
    private String receiverAddress;
    /**
     * 开票状态：0-未开票，1-开票中，2-已开票，3-已寄送
     */
    private Integer invoiceStatus;
    /**
     * 开票时间
     */
    private LocalDateTime invoiceTime;
    /**
     * 电子发票URL
     */
    private String invoiceUrl;
    /**
     * 快递单号
     */
    private String expressNo;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
