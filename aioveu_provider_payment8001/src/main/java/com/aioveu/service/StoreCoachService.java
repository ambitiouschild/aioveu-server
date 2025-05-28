package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.StoreCoach;
import com.aioveu.vo.StoreCoachUserVO;
import com.aioveu.vo.StoreCoachVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface StoreCoachService extends IService<StoreCoach> {

    /**
     * 通过店铺id查询
     * @param storeId
     * @param hasBindUser
     * @param userType
     * @param page
     * @param size
     * @return
     */
    IPage<StoreCoachVO> getByStoreId(Long storeId, boolean hasBindUser, Integer userType, int page, int size);

    /**
     * 获取店铺销售、教练信息
     * @param storeId
     * @param userType
     * @return
     */
    List<StoreCoachVO> getListByStoreId(Long storeId, Integer userType);

    /**
     * 删除教练
     * @param id
     * @return
     */
    boolean deleteCoachById(Long id);

    /**
     * 创建或者更新教练
     * @param storeCoach
     * @return
     */
    Boolean createOrUpdate(StoreCoach storeCoach);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    StoreCoach detail(Long id);

    /**
     * 创建用户查询教练列表
     * 获取当前门店或店铺的教练、销售列表
     * @param userType 类型 1是教练，4是销售
     * @param storeId  门店id
     * @param companyId 公司id
     * @return
     */
    List<StoreCoachUserVO> getCreateUserCoachList(Integer userType, String storeId, String companyId);

    /**
     * 查询店铺下某个用户身份
     * @param userId
     * @param storeId
     * @return
     */
    List<StoreCoach> getByUserAndStoreId(String userId, Long storeId);

    /**
     * 获取店铺下的用户
     * @param storeId
     * @return
     */
    List<StoreCoachVO> getStoreCoachUser(Long storeId);

    /**
     * 根据用户id和店铺Id以及用户类型获取对应店铺用户
     * @param userId
     * @param storeId
     * @param userType
     * @return
     */
    StoreCoach getByUserIdAndStoreId(String userId, Long storeId, Integer userType);


}
