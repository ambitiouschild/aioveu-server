package com.aioveu.ums.aioveu01Member.converter;

import com.aioveu.ums.aioveu01Member.model.form.UmsMemberForm;
import com.aioveu.ums.dto.MemberAuthDTO;
import com.aioveu.ums.dto.MemberRegisterForm;
import com.aioveu.ums.aioveu01Member.model.entity.UmsMember;
import com.aioveu.ums.dto.MemberRegisterDTO;
import org.mapstruct.Mapper;

/**
 * @Description: TODO 会员对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:58
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface UmsMemberConverter {

    MemberAuthDTO entity2OpenidAuthDTO(UmsMember entity);

    MemberAuthDTO entity2MobileAuthDTO(UmsMember entity);

    MemberRegisterDTO entity2MemberInfoDTO(UmsMember entity);

    UmsMember dto2Entity(MemberRegisterForm memberRegisterForm);

    UmsMemberForm toForm(UmsMember entity);

    UmsMember toEntity(UmsMemberForm formData);

}
