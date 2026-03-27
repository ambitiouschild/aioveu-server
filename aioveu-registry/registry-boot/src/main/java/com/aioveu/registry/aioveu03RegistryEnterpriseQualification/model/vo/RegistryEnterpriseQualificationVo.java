package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryEnterpriseQualificationVo
 * @Description TODO 企业资质视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:32
 * @Version 1.0
 **/
@Getter
@Setter
@Schema( description = "企业资质视图对象")
public class RegistryEnterpriseQualificationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "资质ID")
    private Long id;
    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "企业名称（营业执照全称）")
    private String enterpriseName;
    @Schema(description = "营业执照注册号/统一社会信用代码")
    private String businessLicenseNo;
    @Schema(description = "营业执照照片路径")
    private String businessLicenseUrl;
    @Schema(description = "其他证明材料（JSON数组）")
    private String otherCertificates;
    @Schema(description = "主体验证方式：1-小额打款，2-微信认证，3-电子营业执照")
    private Integer verificationType;
    @Schema(description = "小额打款金额")
    private BigDecimal verificationAmount;
    @Schema(description = "验证状态：0-未验证，1-验证中，2-验证成功，3-验证失败")
    private Integer verificationStatus;
    @Schema(description = "验证时间")
    private LocalDateTime verificationTime;
    @Schema(description = "逻辑删除：0-未删除 1-已删除")
    private Integer isDeleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
