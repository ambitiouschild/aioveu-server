package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.form;

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
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryEnterpriseQualificationForm
 * @Description TODO 企业资质表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:31
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "企业资质表单对象")
public class RegistryEnterpriseQualificationForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "资质ID")
    private Long id;

    @Schema(description = "所属租户ID")
    @NotNull(message = "所属租户ID不能为空")
    private Long tenantId;

    @Schema(description = "企业名称（营业执照全称）")
    @NotBlank(message = "企业名称（营业执照全称）不能为空")
    @Size(max=200, message="企业名称（营业执照全称）长度不能超过200个字符")
    private String enterpriseName;

    @Schema(description = "营业执照注册号/统一社会信用代码")
    @NotBlank(message = "营业执照注册号/统一社会信用代码不能为空")
    @Size(max=50, message="营业执照注册号/统一社会信用代码长度不能超过50个字符")
    private String businessLicenseNo;

    @Schema(description = "营业执照照片路径")
    @Size(max=500, message="营业执照照片路径长度不能超过500个字符")
    private String businessLicenseUrl;

    @Schema(description = "其他证明材料（JSON数组）")
    private String otherCertificates;

    @Schema(description = "主体验证方式：1-小额打款，2-微信认证，3-电子营业执照")
    private Integer verificationType;

    @Schema(description = "小额打款金额")
    private BigDecimal verificationAmount;

    @Schema(description = "验证状态：0-未验证，1-验证中，2-验证成功，3-验证失败")
    @NotNull(message = "验证状态：0-未验证，1-验证中，2-验证成功，3-验证失败不能为空")
    private Integer verificationStatus;

    @Schema(description = "验证时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime verificationTime;
}
