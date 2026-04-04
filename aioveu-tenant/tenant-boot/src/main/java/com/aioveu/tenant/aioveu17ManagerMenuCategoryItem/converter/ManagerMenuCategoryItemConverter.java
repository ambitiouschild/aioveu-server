package com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.converter;

import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.entity.ManagerMenuCategoryItem;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.form.ManagerMenuCategoryItemForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: ManagerMenuCategoryItemConverter
 * @Description TODO 管理系统工作台菜单项（多租户支持）对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:28
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface ManagerMenuCategoryItemConverter {

    ManagerMenuCategoryItemForm toForm(ManagerMenuCategoryItem entity);

    ManagerMenuCategoryItem toEntity(ManagerMenuCategoryItemForm formData);



}
