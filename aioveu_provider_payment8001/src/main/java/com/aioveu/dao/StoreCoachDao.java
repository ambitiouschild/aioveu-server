package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.StoreCoach;
import com.aioveu.vo.CoachTagVO;
import com.aioveu.vo.StoreCoachUserVO;
import com.aioveu.vo.StoreCoachVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface StoreCoachDao extends BaseMapper<StoreCoach> {


    /**
     * 列表
     * @param page
     * @param storeId
     * @param hasBindUser
     * @param userType
     * @return
     */
    IPage<StoreCoachVO> getByStoreId(IPage<CoachTagVO> page, Long storeId, boolean hasBindUser, Integer userType);

    /**
     * 获取所有教练或销售数据
     * @param storeId
     * @param userType
     * @param hasBindUser
     * @return
     */
    List<StoreCoachVO> getByStoreId(Long storeId, Integer userType, boolean hasBindUser);

    /**
     * 获取当前门店或店铺的教练、销售列表
     * @param userType 类型 1是教练，4是销售
     * @param storeId  门店id
     * @param companyId 公司id
     * @return
     */
    List<StoreCoachUserVO> getCreateUserCoachList(Integer userType, String storeId, String companyId);

    /**
     * 获取店铺下指定用户
     * @param userId
     * @param storeId
     * @return
     */
    List<StoreCoach> getByUserAndStoreId(String userId, Long storeId);

    /**
     * 获取店铺下的用户 不区分教练销售
     * @param storeId
     * @return
     */
    List<StoreCoachVO> getStoreCoachUser(Long storeId);

}
