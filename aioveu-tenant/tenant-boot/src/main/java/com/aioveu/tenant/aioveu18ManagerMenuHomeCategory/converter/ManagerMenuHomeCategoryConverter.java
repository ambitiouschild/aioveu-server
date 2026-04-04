package com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.converter;

import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.entity.ManagerMenuHomeCategory;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.form.ManagerMenuHomeCategoryForm;
import com.aioveu.tenant.aioveu18ManagerMenuHomeCategory.model.vo.ManagerMenuHomeCategoryVo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @ClassName: ManagerMenuHomeCategoryConverter
 * @Description TODO 管理端app首页分类配置对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 13:40
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface ManagerMenuHomeCategoryConverter {

    ManagerMenuHomeCategoryForm toForm(ManagerMenuHomeCategory entity);

    ManagerMenuHomeCategory toEntity(ManagerMenuHomeCategoryForm formData);


    List<ManagerMenuHomeCategoryVo> entity2HomeCategoryVo(List<ManagerMenuHomeCategory> entities);
}
