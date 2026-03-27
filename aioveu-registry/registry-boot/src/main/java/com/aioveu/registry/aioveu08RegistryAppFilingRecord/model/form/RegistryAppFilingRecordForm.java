package com.aioveu.registry.aioveu08RegistryAppFilingRecord.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryAppFilingRecordForm
 * @Description TODO 小程序备案记录表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 19:09
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "小程序备案记录表单对象")
public class RegistryAppFilingRecordForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "备案ID")
    private Long id;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "小程序账号ID")
    @NotNull(message = "小程序账号ID不能为空")
    private Long appAccountId;

    @Schema(description = "备案类型：1-首次备案，2-变更备案，3-注销备案")
    @NotNull(message = "备案类型：1-首次备案，2-变更备案，3-注销备案不能为空")
    private Integer filingType;

    @Schema(description = "备案状态：0-未备案，1-备案中，2-备案通过，3-备案驳回，4-已注销")
    @NotNull(message = "备案状态：0-未备案，1-备案中，2-备案通过，3-备案驳回，4-已注销不能为空")
    private Integer filingStatus;

    @Schema(description = "备案编号")
    @Size(max=100, message="备案编号长度不能超过100个字符")
    private String filingNo;

    @Schema(description = "备案主体")
    @Size(max=200, message="备案主体长度不能超过200个字符")
    private String filingSubject;

    @Schema(description = "备案小程序名称")
    @Size(max=100, message="备案小程序名称长度不能超过100个字符")
    private String filingAppName;

    @Schema(description = "备案域名")
    @Size(max=500, message="备案域名长度不能超过500个字符")
    private String filingDomain;

    @Schema(description = "备案IP")
    @Size(max=200, message="备案IP长度不能超过200个字符")
    private String filingIp;

    @Schema(description = "备案证书路径")
    @Size(max=500, message="备案证书路径长度不能超过500个字符")
    private String filingCertificatePath;

    @Schema(description = "备案申请时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    @Schema(description = "备案审核时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    @Schema(description = "备案驳回原因")
    @Size(max=65535, message="备案驳回原因长度不能超过65535个字符")
    private String rejectionReason;

    @Schema(description = "备案到期时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expireTime;
}
