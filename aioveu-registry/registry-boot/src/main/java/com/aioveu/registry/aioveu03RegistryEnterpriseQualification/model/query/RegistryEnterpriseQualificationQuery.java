package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryEnterpriseQualificationQuery
 * @Description TODO 企业资质分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:31
 * @Version 1.0
 **/
@Schema(description ="企业资质查询对象")
@Getter
@Setter
public class RegistryEnterpriseQualificationQuery extends BasePageQuery {

    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "企业名称（营业执照全称）")
    private String enterpriseName;
    @Schema(description = "营业执照注册号/统一社会信用代码")
    private String businessLicenseNo;
    @Schema(description = "主体验证方式：1-小额打款，2-微信认证，3-电子营业执照")
    private Integer verificationType;
    @Schema(description = "验证状态：0-未验证，1-验证中，2-验证成功，3-验证失败")
    private Integer verificationStatus;
}
