package com.aioveu.ums.aioveu02MemberAddress.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.aioveu02MemberAddress.model.entity.UmsAddress;
import com.aioveu.ums.aioveu02MemberAddress.model.form.AddressForm;

import java.util.List;

/**
 * @Description: TODO 会员地址业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 19:00
 * @param
 * @return:
 **/

public interface UmsAddressService extends IService<UmsAddress> {

    /**
     * 新增地址
     *
     * @param addressForm
     * @return
     */
    boolean addAddress(AddressForm addressForm);

    /**
     * 修改地址
     *
     * @param addressForm
     * @return
     */
    boolean updateAddress(AddressForm addressForm);

    /**
     * 获取当前登录会员的地址列表
     *
     * @return
     */
    List<MemberAddressDTO> listCurrentMemberAddresses();
}
