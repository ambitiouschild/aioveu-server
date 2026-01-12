package com.aioveu.ums.aioveu02MemberAddress.service;


import com.aioveu.ums.aioveu02MemberAddress.model.entity.UmsMemberAddress;
import com.aioveu.ums.aioveu02MemberAddress.model.query.UmsMemberAddressQuery;
import com.aioveu.ums.aioveu02MemberAddress.model.vo.UmsMemberAddressVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.aioveu02MemberAddress.model.form.UmsMemberAddressForm;

import java.util.List;

/**
 * @Description: TODO 会员地址业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 19:00
 * @param
 * @return:
 **/

public interface UmsMemberAddressService extends IService<UmsMemberAddress> {

    /**
     * 新增地址
     *
     * @param umsMemberAddressForm
     * @return
     */
    boolean addAddress(UmsMemberAddressForm umsMemberAddressForm);

    /**
     * 修改地址
     *
     * @param umsMemberAddressForm
     * @return
     */
    boolean updateAddress(UmsMemberAddressForm umsMemberAddressForm);

    /**
     * 获取当前登录会员的地址列表
     *
     * @return
     */
    List<MemberAddressDTO> listCurrentMemberAddresses();


    /**
     *会员收货地址分页列表
     *
     * @return {@link IPage<UmsMemberAddressVO>} 会员收货地址分页列表
     */
    IPage<UmsMemberAddressVO> getUmsMemberAddressPage(UmsMemberAddressQuery queryParams);

    /**
     * 获取会员收货地址表单数据
     *
     * @param id 会员收货地址ID
     * @return 会员收货地址表单数据
     */
    UmsMemberAddressForm getUmsMemberAddressFormData(Long id);

    /**
     * 新增会员收货地址
     *
     * @param formData 会员收货地址表单对象
     * @return 是否新增成功
     */
    boolean saveUmsMemberAddress(UmsMemberAddressForm formData);

    /**
     * 修改会员收货地址
     *
     * @param id   会员收货地址ID
     * @param formData 会员收货地址表单对象
     * @return 是否修改成功
     */
    boolean updateUmsMemberAddress(Long id, UmsMemberAddressForm formData);

    /**
     * 删除会员收货地址
     *
     * @param ids 会员收货地址ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteUmsMemberAddresss(String ids);
}
