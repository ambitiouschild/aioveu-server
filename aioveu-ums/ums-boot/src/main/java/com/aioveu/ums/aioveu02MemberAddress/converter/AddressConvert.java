package com.aioveu.ums.aioveu02MemberAddress.converter;

import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.aioveu02MemberAddress.model.entity.UmsAddress;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Description: TODO 会员地址对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:58
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface AddressConvert {

    MemberAddressDTO entity2Dto(UmsAddress entity);

    List<MemberAddressDTO> entity2Dto(List<UmsAddress> entities);
}
