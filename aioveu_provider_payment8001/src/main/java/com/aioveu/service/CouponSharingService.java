package com.aioveu.service;

import com.aioveu.entity.CouponSharing;

public interface CouponSharingService {


    /**
     * 添加一条分享记录
     * @param sharingUserId
     * @param couponTemplateId
     * @param couponCode
     * @return
     */
    boolean create(String sharingUserId, Long couponTemplateId, String couponCode);


    /**
     * 通过优惠券码从库查找分享记录
     * @param couponCode
     * @return
     */
    CouponSharing getByCode(String couponCode);

    /**
     *  添加领取人id
     * @param id
     * @param receiveUserId
     * @return
     */
    boolean changeReceive(Long id, String receiveUserId);

}
