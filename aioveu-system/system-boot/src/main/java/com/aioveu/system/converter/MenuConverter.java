package com.aioveu.system.converter;

import com.aioveu.system.model.entity.SysMenu;
import com.aioveu.system.model.form.MenuForm;
import com.aioveu.system.model.vo.MenuVO;
import org.mapstruct.Mapper;

/**
 * @Description: TODO 菜单对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 17:12
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface MenuConverter {

    MenuVO entity2Vo(SysMenu entity);


    MenuForm entity2Form(SysMenu entity);

    SysMenu form2Entity(MenuForm menuForm);

}