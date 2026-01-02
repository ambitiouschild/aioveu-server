package com.aioveu.system.aioveu02User.converter;

import com.aioveu.common.model.Option;
import com.aioveu.system.aioveu02User.model.entity.User;
import com.aioveu.system.aioveu02User.model.form.UserForm;
import com.aioveu.system.aioveu02User.model.form.UserProfileForm;
import com.aioveu.system.aioveu02User.model.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @ClassName: UserConverter
 * @Description TODO  用户对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/30 22:28
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserPageVO toPageVo(UserBO bo);

    Page<UserPageVO> toPageVo(Page<UserBO> bo);

    UserForm toForm(User entity);

    @InheritInverseConfiguration(name = "toForm")
    User toEntity(UserForm entity);

    @Mappings({
            @Mapping(target = "userId", source = "id")
    })
    CurrentUserDTO toCurrentUserDto(User entity);

    User toEntity(UserImportDTO vo);


    UserProfileVO toProfileVo(UserBO bo);

    User toEntity(UserProfileForm formData);

    @Mappings({
            @Mapping(target = "label", source = "nickname"),
            @Mapping(target = "value", source = "id")
    })
    Option<String> toOption(User entity);

    List<Option<String>> toOptions(List<User> list);
}
