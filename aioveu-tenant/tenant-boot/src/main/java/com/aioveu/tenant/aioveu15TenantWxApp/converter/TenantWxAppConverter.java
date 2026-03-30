package com.aioveu.tenant.aioveu15TenantWxApp.converter;

import com.aioveu.tenant.aioveu15TenantWxApp.model.entity.TenantWxApp;
import com.aioveu.tenant.aioveu15TenantWxApp.model.form.TenantWxAppForm;
import com.aioveu.tenant.aioveu15TenantWxApp.model.vo.TenantWxAppVo;
import org.mapstruct.Mapper;

/**
 * @ClassName: TenantWxAppConverter
 * @Description TODO 租户与微信小程序关联对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 17:06
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface TenantWxAppConverter {

    TenantWxAppForm toForm(TenantWxApp entity);

    TenantWxApp toEntity(TenantWxAppForm formData);

    TenantWxAppVo toVO(TenantWxApp entity);
}
