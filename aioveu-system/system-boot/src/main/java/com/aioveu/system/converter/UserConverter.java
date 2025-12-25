package com.aioveu.system.converter;

import com.aioveu.system.model.bo.UserBO;
import com.aioveu.system.model.bo.UserFormBO;
import com.aioveu.system.model.bo.UserProfileBO;
import com.aioveu.system.model.entity.SysUser;
import com.aioveu.system.model.form.UserForm;
import com.aioveu.system.model.vo.UserImportVO;
import com.aioveu.system.model.vo.UserInfoVO;
import com.aioveu.system.model.vo.UserPageVO;
import com.aioveu.system.model.vo.UserProfileVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Description: TODO 用户对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 17:13
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mappings({
            @Mapping(target = "genderLabel", expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(bo.getGender(), com.aioveu.common.enums.GenderEnum.class))")
    })
    UserPageVO bo2Vo(UserBO bo);

    Page<UserPageVO> bo2Vo(Page<UserBO> bo);

    UserForm bo2Form(UserFormBO bo);

    UserForm entity2Form(SysUser entity);

    @InheritInverseConfiguration(name = "entity2Form")
    SysUser form2Entity(UserForm entity);

    @Mappings({
            @Mapping(target = "userId", source = "id")
    })
    UserInfoVO entity2UserInfoVo(SysUser entity);

    SysUser importVo2Entity(UserImportVO vo);

    @Mappings({
            @Mapping(target = "genderLabel", expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(bo.getGender(), com.aioveu.common.enums.GenderEnum.class))")
    })
    UserProfileVO userProfileBo2Vo(UserProfileBO bo);
}
