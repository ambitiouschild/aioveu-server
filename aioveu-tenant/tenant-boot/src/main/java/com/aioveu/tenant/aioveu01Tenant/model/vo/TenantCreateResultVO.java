package com.aioveu.tenant.aioveu01Tenant.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: TenantCreateResultVO
 * @Description TODO 新增租户初始化结果
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:56
 * @Version 1.0
 **/
@Schema(description = "新增租户初始化结果")
@Data
public class TenantCreateResultVO implements Serializable {

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "租户编码")
    private String tenantCode;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户管理员用户名")
    private String adminUsername;

    @Schema(description = "租户管理员初始密码")
    private String adminInitialPassword;

    @Schema(description = "租户管理员角色编码")
    private String adminRoleCode;
}
