package com.aioveu.service;

import com.aioveu.vo.CouponKafkaMessage;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/17 0017 23:18
 */
public interface KafkaService {

    /**
     * 修改优惠券状态，同步到数据库中
     * 本来是通过kafka进行异步实现，暂时未接入kafka，同步去做了
     * @param couponKafkaMessage
     * @return
     */
    boolean changeCouponStatus2Database(CouponKafkaMessage couponKafkaMessage);

}
