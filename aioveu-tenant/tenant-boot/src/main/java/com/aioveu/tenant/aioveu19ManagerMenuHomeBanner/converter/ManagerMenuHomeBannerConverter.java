package com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.converter;

import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.entity.ManagerMenuHomeBanner;
import com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.form.ManagerMenuHomeBannerForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: ManagerMenuHomeBannerConverter
 * @Description TODO 管理端app首页滚播栏对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 15:40
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface ManagerMenuHomeBannerConverter {

    ManagerMenuHomeBannerForm toForm(ManagerMenuHomeBanner entity);

    ManagerMenuHomeBanner toEntity(ManagerMenuHomeBannerForm formData);
}
