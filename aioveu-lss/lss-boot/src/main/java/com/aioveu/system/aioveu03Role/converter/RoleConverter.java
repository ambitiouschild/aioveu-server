package com.aioveu.system.aioveu03Role.converter;

import com.aioveu.common.model.Option;
import com.aioveu.system.aioveu03Role.model.entity.Role;
import com.aioveu.system.aioveu03Role.model.form.RoleForm;
import com.aioveu.system.aioveu03Role.model.vo.RolePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @ClassName: RoleConverter
 * @Description TODO  角色对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:28
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface RoleConverter {

    Page<RolePageVO> toPageVo(Page<Role> page);

    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name")
    })
    Option<Long> toOption(Role role);

    List<Option<Long>> toOptions(List<Role> roles);

    Role toEntity(RoleForm roleForm);

    RoleForm toForm(Role entity);
}
