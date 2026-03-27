package com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryAdministratorInfoQuery
 * @Description TODO 管理员信息分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:45
 * @Version 1.0
 **/
@Schema(description ="管理员信息查询对象")
@Getter
@Setter
public class RegistryAdministratorInfoQuery extends BasePageQuery {

    @Schema(description = "所属租户ID")
    private Long tenantId;
    @Schema(description = "管理员真实姓名")
    private String realName;
    @Schema(description = "身份证号码")
    private String idCardNo;
    @Schema(description = "手机号码")
    private String phone;
}
