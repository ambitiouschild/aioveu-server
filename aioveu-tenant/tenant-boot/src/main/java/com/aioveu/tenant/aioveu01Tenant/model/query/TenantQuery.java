package com.aioveu.tenant.aioveu01Tenant.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: TenantQuery
 * @Description TODO 租户分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:48
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "租户分页查询对象")
public class TenantQuery extends BasePageQuery {

    @Schema(description = "关键字(租户名称/租户编码/域名)")
    private String keywords;

    @Schema(description = "租户状态(1-正常 0-禁用)")
    private Integer status;
}
