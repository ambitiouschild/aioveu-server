package com.aioveu.ums.aioveu01Member.converter;

import com.aioveu.ums.aioveu01Member.model.form.UmsMemberForm;
import com.aioveu.ums.dto.MemberAuthDTO;
import com.aioveu.ums.dto.MemberInfoDTO;
import com.aioveu.ums.dto.MemberRegisterDto;
import com.aioveu.ums.aioveu01Member.model.entity.UmsMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @Description: TODO 会员对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:58
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface UmsMemberConverter {
    @Mappings({
            @Mapping(target = "username", source = "openid")
    })
    MemberAuthDTO entity2OpenidAuthDTO(UmsMember entity);

    @Mappings({
            @Mapping(target = "username", source = "mobile")
    })
    MemberAuthDTO entity2MobileAuthDTO(UmsMember entity);

    MemberInfoDTO entity2MemberInfoDTO(UmsMember entity);

    UmsMember dto2Entity(MemberRegisterDto memberRegisterDTO);

    UmsMemberForm toForm(UmsMember entity);

    UmsMember toEntity(UmsMemberForm formData);

}
