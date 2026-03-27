package com.aioveu.registry.aioveu06RegistryCertificationContact.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryCertificationContactQuery
 * @Description TODO 认证联系人分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:25
 * @Version 1.0
 **/
@Schema(description ="认证联系人查询对象")
@Getter
@Setter
public class RegistryCertificationContactQuery extends BasePageQuery {

    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "认证记录ID")
    private Long certificationId;
    @Schema(description = "联系人姓名")
    private String contactName;
    @Schema(description = "联系人身份证号")
    private String contactIdCard;
    @Schema(description = "联系人手机号")
    private String contactPhone;
}
