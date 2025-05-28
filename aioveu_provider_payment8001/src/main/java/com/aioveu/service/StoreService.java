package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Store;
import com.aioveu.form.StoreBindUserPhoneForm;
import com.aioveu.form.StoreForm;
import com.aioveu.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface StoreService extends IService<Store> {

    /**
     * 获取用户列表
     * @param page
     * @param size
     * @param companyId
     * @param keyword
     * @param status
     * @return
     */
    IPage<StoreForm> getAll(int page, int size, Long companyId, String keyword, Integer status);

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(Long id, Integer status);

    /**
     * 获取详情
     * @param id
     * @return
     */
    StoreForm detail(Long id);

    /**
     * 通过公司id获取店铺
     * @param companyId
     * @return
     */
    List<Store> getByCompanyId(Long companyId);

    /**
     * 小程序获取店铺列表
     * @param param
     * @return
     */
    IPage<StoreMiniVO> getMiniList(Map<String, Object> param) throws Exception;

    /**
     * 小程序获取店铺详情
     * @param id
     * @return
     */
    StoreMiniDetailVO getMiniDetail(Long id);

    /**
     * 获取店铺名称
     * @return
     */
    List<Store> getMiniName();

    /**
     * 获取店铺的产品分类
     * @param storeId
     * @return
     */
    List<CategoryBaseVO> getStoreCategory(Long storeId);

    /**
     * 根据店铺和定位确定是否是同一个店铺
     * @param storeId
     * @param longitude
     * @param latitude
     * @return
     */
    Store getByLocation(Long storeId, Double longitude, Double latitude);

    /**
     * 查询附件10KM内的店铺
     * @param longitude
     * @param latitude
     * @return
     */
    List<NearbyStoreVO> getNearbyStore(Double longitude, Double latitude);

    /**
     * 店铺创建
     * @param store
     * @return
     */
    boolean createOrUpdate(StoreForm store);

    /**
     * 更新店铺主题光环logo
     * @param id
     * @return
     */
    boolean updateTopicLogo(Long id);

    /**
     * 获取店铺绑定的用户
     * @param storeId
     * @return
     */
    List<UserPhoneVO> getStoreBindUserPhone(Long companyId, Long storeId);

    /**
     * 修改店铺绑定的手机号
     * @param storeBindUserPhoneForm
     * @return
     */
    Boolean modifyStoreBindUserPhone(StoreBindUserPhoneForm storeBindUserPhoneForm);

    /**
     * 删除店铺绑定的手机号
     * @param id
     * @return
     */
    Boolean deleteStoreBindUserPhone(Long id);

    /**
     * 查询店铺绑定的手机号通过id
     * @param id
     * @return
     */
    List<UserPhoneVO> getStoreBindUserPhoneOne(Long id);


    /**
     * 获取店铺会员卡列表
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<StoreVipCardVO> getStoreCardList(int page, int size, Long storeId);

    /**
     * 通过店铺id查找公司id
     * @param storeId
     * @return
     */
    Long getCompanyIdByStoreId(Long storeId);

    /**
     * 通过id查找名称
     * @param storeId
     * @return
     */
    String getNameByStoreId(Long storeId);


    /**
     * 通过app_id查找店铺
     * @param appId
     * @return
     */
    List<Store> getByAPPId(String appId);



}
