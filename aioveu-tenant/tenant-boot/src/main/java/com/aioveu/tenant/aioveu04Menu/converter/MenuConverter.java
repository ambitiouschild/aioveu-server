package com.aioveu.tenant.aioveu04Menu.converter;

import com.aioveu.tenant.aioveu04Menu.model.entity.Menu;
import com.aioveu.tenant.aioveu04Menu.model.form.MenuForm;
import com.aioveu.tenant.aioveu04Menu.model.vo.MenuVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @ClassName: MenuConverter
 * @Description TODO 菜单对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:13
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface MenuConverter {

    MenuVO toVo(Menu entity);

    @Mapping(target = "params", ignore = true)
    MenuForm toForm(Menu entity);

    @Mapping(target = "params", ignore = true)
    Menu toEntity(MenuForm menuForm);
}
