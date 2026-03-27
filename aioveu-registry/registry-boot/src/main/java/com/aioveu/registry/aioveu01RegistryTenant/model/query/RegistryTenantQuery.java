package com.aioveu.registry.aioveu01RegistryTenant.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryTenantQuery
 * @Description TODO 租户注册小程序基本信息分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:29
 * @Version 1.0
 **/
@Schema(description ="租户注册小程序基本信息查询对象")
@Getter
@Setter
public class RegistryTenantQuery extends BasePageQuery {

    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "租户唯一编码")
    private String tenantCode;
    @Schema(description = "租户注册状态：0-未注册，1-已注册，2-已认证，3-已备案，4-已禁用")
    private Integer tenantRegistryStatus;
}
