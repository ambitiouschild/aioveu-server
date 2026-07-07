package com.aioveu.ums.aioveu01Member.service;


import com.aioveu.ums.aioveu01Member.model.form.UmsMemberForm;
import com.aioveu.ums.aioveu01Member.model.query.UmsMemberQuery;
import com.aioveu.ums.dto.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.model.vo.ProductHistoryVO;
import com.aioveu.ums.aioveu01Member.model.entity.UmsMember;
import com.aioveu.ums.aioveu01Member.model.vo.UmsMemberVO;

import java.util.List;
import java.util.Set;

/**
 * @Description: TODO 会员业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 19:00
 * @param
 * @return:
 **/

public interface UmsMemberService extends IService<UmsMember> {

    IPage<UmsMember> list(Page<UmsMember> page, String nickname);

    void addProductViewHistory(ProductHistoryVO product, Long userId);

    Set<ProductHistoryVO> getProductViewHistory(Long userId);

    /**
     * 根据 openId 获取会员认证信息
     *
     * @param openId
     * @return
     */
    MemberAuthDTO getMemberByopenId(String openId);


    /**
     * 根据 openId 和 tenantId获取会员认证信息
     *
     * @param openId
     * @param tenantId
     * @return
     */
    MemberAuthDTO getMemberByopenIdAndTenantId(String openId,Long tenantId);


    /**
     * 根据手机号获取会员认证信息
     *
     * @param mobile
     * @return
     */
    MemberAuthDTO getMemberByMobile(String mobile);

    /**
     * 新增会员
     *
     * @param memberRegisterForm
     * @return
     */
    MemberRegisterDTO registerMember(MemberRegisterForm memberRegisterForm);



    /*
    * 用 openId + tenant_id 查当前用户
    * */
    UmsMemberVO getCurrMemberInfoByopenIdAndTenantId();

    /**
     * 获取登录会员信息
     *
     * @return
     */
    UmsMemberVO getCurrMemberInfoByMemberId();

    /**
     * 获取会员地址列表
     *
     * @param memberId
     * @return
     */
    List<MemberAddressDTO> listMemberAddress(Long memberId);

    /**
     *会员分页列表
     *
     * @return {@link IPage<UmsMemberVO>} 会员分页列表
     */
    IPage<UmsMemberVO> getUmsMemberPage(UmsMemberQuery queryParams);

    /**
     * 获取会员表单数据
     *
     * @param id 会员ID
     * @return 会员表单数据
     */
    UmsMemberForm getUmsMemberFormData(Long id);

    /**
     * 新增会员
     *
     * @param formData 会员表单对象
     * @return 是否新增成功
     */
    boolean saveUmsMember(UmsMemberForm formData);

    /**
     * 修改会员
     *
     * @param id   会员ID
     * @param formData 会员表单对象
     * @return 是否修改成功
     */
    boolean updateUmsMember(Long id, UmsMemberForm formData);

    /**
     * 删除会员
     *
     * @param ids 会员ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteUmsMembers(String ids);

}
