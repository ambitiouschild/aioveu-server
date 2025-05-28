package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.BusinessUserInfo;
import com.aioveu.vo.StoreSimpleVO;
import com.aioveu.vo.StoreUserPublicInfoVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface BusinessUserInfoDao extends BaseMapper<BusinessUserInfo> {

    /**
     * 查询商户客资
     * @param page
     * @param storeList
     * @return
     */
    IPage<StoreUserPublicInfoVO> getList(IPage<StoreUserPublicInfoVO> page, List<StoreSimpleVO> storeList);

    /**
     * 获取失效的手机号
     * @param page
     * @param id
     * @return
     */
    IPage<StoreUserPublicInfoVO> getPhoneInvalid(IPage<StoreUserPublicInfoVO> page, Long id, String name);
}
