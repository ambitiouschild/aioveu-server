package com.aioveu.tenant.aioveu01Tenant.converter;

import com.aioveu.tenant.aioveu01Tenant.model.entity.TenantPlan;
import com.aioveu.tenant.aioveu01Tenant.model.form.TenantPlanForm;
import com.aioveu.tenant.aioveu01Tenant.model.vo.TenantPlanPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * @ClassName: TenantPlanConverter
 * @Description TODO  租户套餐对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:15
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface TenantPlanConverter {

    Page<TenantPlanPageVO> toPageVo(Page<TenantPlan> page);

    TenantPlan toEntity(TenantPlanForm formData);

    TenantPlanForm toForm(TenantPlan entity);
}
