package com.aioveu.registry.aioveu03RegistryEnterpriseQualification.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName: RegistryEnterpriseQualification
 * @Description TODO 企业资质实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:30
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_enterprise_qualification")
public class RegistryEnterpriseQualification extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 企业名称（营业执照全称）
     */
    private String enterpriseName;
    /**
     * 营业执照注册号/统一社会信用代码
     */
    private String businessLicenseNo;
    /**
     * 营业执照照片路径
     */
    private String businessLicenseUrl;
    /**
     * 其他证明材料（JSON数组）
     */
    private String otherCertificates;
    /**
     * 主体验证方式：1-小额打款，2-微信认证，3-电子营业执照
     */
    private Integer verificationType;
    /**
     * 小额打款金额
     */
    private BigDecimal verificationAmount;
    /**
     * 验证状态：0-未验证，1-验证中，2-验证成功，3-验证失败
     */
    private Integer verificationStatus;
    /**
     * 验证时间
     */
    private LocalDateTime verificationTime;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
