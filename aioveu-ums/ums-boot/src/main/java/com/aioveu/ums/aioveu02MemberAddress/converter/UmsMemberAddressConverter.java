package com.aioveu.ums.aioveu02MemberAddress.converter;

import com.aioveu.ums.aioveu02MemberAddress.model.entity.UmsMemberAddress;
import com.aioveu.ums.aioveu02MemberAddress.model.form.UmsMemberAddressForm;
import com.aioveu.ums.dto.MemberAddressDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: TODO 会员收货地址对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:58
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface UmsMemberAddressConverter {

    MemberAddressDTO entity2Dto(UmsMemberAddress entity);

    List<MemberAddressDTO> entity2Dto(List<UmsMemberAddress> entities);

    UmsMemberAddressForm toForm(UmsMemberAddress entity);

    UmsMemberAddress toEntity(UmsMemberAddressForm formData);
}
