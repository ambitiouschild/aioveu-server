package com.aioveu.ums.aioveu01Member.service;


import com.aioveu.ums.aioveu01Member.model.form.UmsMemberForm;
import com.aioveu.ums.aioveu01Member.model.query.UmsMemberQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.model.vo.ProductHistoryVO;
import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.dto.MemberAuthDTO;
import com.aioveu.ums.dto.MemberRegisterDto;
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
     * 根据 openid 获取会员认证信息
     *
     * @param openid
     * @return
     */
    MemberAuthDTO getMemberByOpenid(String openid);

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
     * @param member
     * @return
     */
    Long addMember(MemberRegisterDto member);

    /**
     * 获取登录会员信息
     *
     * @return
     */
    UmsMemberVO getCurrMemberInfo();

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
