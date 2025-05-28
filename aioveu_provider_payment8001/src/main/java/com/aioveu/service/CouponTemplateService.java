package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CouponTemplate;
import com.aioveu.vo.IdNameVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CouponTemplateService extends IService<CouponTemplate> {


    /**
     * 根据名称查找优惠券模板
     * @param name
     * @return
     */
    CouponTemplate findByName(String name);


    /**
     * 获取可用的优惠券
     * @param available
     * @param expired
     * @return
     */
    List<CouponTemplate> findAllByAvailableAndExpired(boolean available, boolean expired);

    /**
     * 查找所有过期的优惠券模板
     * @param expired
     * @return
     */
    List<CouponTemplate> findAllByExpired(boolean expired);

    /**
     * 获取可用的简单的优惠券列表
     * @param storeId
     * @return
     */
    List<IdNameVO> getSimpleCouponByStoreId(Long storeId);

    List<CouponTemplate> getCouponListByStoreId(Long storeId);

    /**
     * 根据优惠券表id查询 优惠券
     * @param id
     * @param sharingUserId 分享人id
     * @return
     */
    String getShareCouponCode(Long id, String sharingUserId);

    /**
     * 获取店铺对应的优惠券
     * @param storeId
     * @return
     */
    List<Long> getByStoreId(Long storeId);

    /**
     * 下线过期的优惠券模板
     */
    void offlineCouponTemplate();


}
