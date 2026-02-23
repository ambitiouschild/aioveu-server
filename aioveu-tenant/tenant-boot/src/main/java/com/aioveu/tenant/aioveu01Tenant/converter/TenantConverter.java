package com.aioveu.tenant.aioveu01Tenant.converter;

import com.aioveu.tenant.aioveu01Tenant.model.entity.Tenant;
import com.aioveu.tenant.aioveu01Tenant.model.form.TenantForm;
import com.aioveu.tenant.aioveu01Tenant.model.vo.TenantPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * @ClassName: TenantConverter
 * @Description TODO 租户对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:15
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface TenantConverter {

    Page<TenantPageVO> toPageVo(Page<Tenant> page);

    TenantForm toForm(Tenant entity);

    Tenant toEntity(TenantForm formData);
}
