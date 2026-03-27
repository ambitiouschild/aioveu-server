package com.aioveu.registry.aioveu07RegistryInvoiceInfo.model.form;

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
 * @ClassName: RegistryInvoiceInfoForm
 * @Description TODO 发票信息表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:38
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "发票信息表单对象")
public class RegistryInvoiceInfoForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "发票ID")
    private Long id;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "认证记录ID")
    @NotNull(message = "认证记录ID不能为空")
    private Long certificationId;

    @Schema(description = "发票类型：1-电子发票，2-纸质发票")
    @NotNull(message = "发票类型：1-电子发票，2-纸质发票不能为空")
    private Integer invoiceType;

    @Schema(description = "发票抬头")
    @NotBlank(message = "发票抬头不能为空")
    @Size(max=200, message="发票抬头长度不能超过200个字符")
    private String invoiceTitle;

    @Schema(description = "纳税人识别号")
    @Size(max=50, message="纳税人识别号长度不能超过50个字符")
    private String taxIdentificationNo;

    @Schema(description = "发票备注")
    @Size(max=500, message="发票备注长度不能超过500个字符")
    private String invoiceRemark;

    @Schema(description = "电子发票接收邮箱")
    @Size(max=100, message="电子发票接收邮箱长度不能超过100个字符")
    private String invoiceEmail;

    @Schema(description = "收件人姓名")
    @Size(max=50, message="收件人姓名长度不能超过50个字符")
    private String receiverName;

    @Schema(description = "收件人电话")
    @Size(max=20, message="收件人电话长度不能超过20个字符")
    private String receiverPhone;

    @Schema(description = "收件地址")
    @Size(max=500, message="收件地址长度不能超过500个字符")
    private String receiverAddress;

    @Schema(description = "开票状态：0-未开票，1-开票中，2-已开票，3-已寄送")
    @NotNull(message = "开票状态：0-未开票，1-开票中，2-已开票，3-已寄送不能为空")
    private Integer invoiceStatus;

    @Schema(description = "开票时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime invoiceTime;

    @Schema(description = "电子发票URL")
    @Size(max=500, message="电子发票URL长度不能超过500个字符")
    private String invoiceUrl;

    @Schema(description = "快递单号")
    @Size(max=100, message="快递单号长度不能超过100个字符")
    private String expressNo;

}
