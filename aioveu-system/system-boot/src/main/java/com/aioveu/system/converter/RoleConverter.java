package com.aioveu.system.converter;

import com.aioveu.common.web.model.Option;
import com.aioveu.system.model.entity.SysRole;
import com.aioveu.system.model.form.RoleForm;
import com.aioveu.system.model.vo.RolePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
/**
 * @Description: TODO 角色对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 17:13
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface RoleConverter {

    Page<RolePageVO> entity2Page(Page<SysRole> page);

    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name")
    })
    Option entity2Option(SysRole role);


    List<Option> entities2Options(List<SysRole> roles);

    SysRole form2Entity(RoleForm roleForm);

    RoleForm entity2Form(SysRole entity);
}