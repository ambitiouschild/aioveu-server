package com.aioveu.tenant.aioveu01Tenant.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: TenantPlanQuery
 * @Description TODO 租户套餐分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:47
 * @Version 1.0
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "租户套餐分页查询对象")
public class TenantPlanQuery extends BasePageQuery {

    @Schema(description = "关键字(套餐名称/套餐编码)")
    private String keywords;

    @Schema(description = "套餐状态(1-启用 0-停用)")
    private Integer status;
}
