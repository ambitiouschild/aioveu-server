package com.aioveu.tenant.aioveu16ManagerMenuCategory.converter;

import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.entity.ManagerMenuCategory;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.form.ManagerMenuCategoryForm;
import com.aioveu.tenant.aioveu16ManagerMenuCategory.model.vo.ManagerMenuCategoryWithItemsVO;
import com.aioveu.tenant.aioveu17ManagerMenuCategoryItem.model.entity.ManagerMenuCategoryItem;
import org.mapstruct.Mapper;

/**
 * @ClassName: ManagerMenuCategoryConverter
 * @Description TODO 管理端菜单分类（多租户）对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/3 17:13
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface ManagerMenuCategoryConverter {

    ManagerMenuCategoryForm toForm(ManagerMenuCategory entity);

    ManagerMenuCategory toEntity(ManagerMenuCategoryForm formData);


    ManagerMenuCategoryWithItemsVO.MenuItemVO convertToMenuItemVO(ManagerMenuCategoryItem item);
}
