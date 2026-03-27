package com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryInvoiceInfoVo
 * @Description TODO 发票信息视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:39
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "发票信息视图对象")
public class RegistryInvoiceInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "发票ID")
    private Long id;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "认证记录ID")
    private Long certificationId;
    @Schema(description = "发票类型：1-电子发票，2-纸质发票")
    private Integer invoiceType;
    @Schema(description = "发票抬头")
    private String invoiceTitle;
    @Schema(description = "纳税人识别号")
    private String taxIdentificationNo;
    @Schema(description = "发票备注")
    private String invoiceRemark;
    @Schema(description = "电子发票接收邮箱")
    private String invoiceEmail;
    @Schema(description = "收件人姓名")
    private String receiverName;
    @Schema(description = "收件人电话")
    private String receiverPhone;
    @Schema(description = "收件地址")
    private String receiverAddress;
    @Schema(description = "开票状态：0-未开票，1-开票中，2-已开票，3-已寄送")
    private Integer invoiceStatus;
    @Schema(description = "开票时间")
    private LocalDateTime invoiceTime;
    @Schema(description = "电子发票URL")
    private String invoiceUrl;
    @Schema(description = "快递单号")
    private String expressNo;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
