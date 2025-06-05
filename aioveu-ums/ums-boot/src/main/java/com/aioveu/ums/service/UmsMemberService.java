package com.aioveu.ums.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.pms.model.vo.ProductHistoryVO;
import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.dto.MemberAuthDTO;
import com.aioveu.ums.dto.MemberRegisterDto;
import com.aioveu.ums.model.entity.UmsMember;
import com.aioveu.ums.model.vo.MemberVO;

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
    MemberVO getCurrMemberInfo();

    /**
     * 获取会员地址列表
     *
     * @param memberId
     * @return
     */
    List<MemberAddressDTO> listMemberAddress(Long memberId);


}
