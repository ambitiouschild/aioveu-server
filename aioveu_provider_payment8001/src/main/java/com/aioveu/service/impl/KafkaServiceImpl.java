package com.aioveu.service.impl;

import com.alibaba.fastjson.JSON;
import com.aioveu.entity.UserCoupon;
import com.aioveu.enums.UserCouponStatus;
import com.aioveu.service.KafkaService;
import com.aioveu.service.IUserCouponService;
import com.aioveu.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/17 0017 23:21
 */
@Slf4j
@Service
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    private IUserCouponService userCouponService;

    @Override
    public boolean changeCouponStatus2Database(CouponKafkaMessage couponKafkaMessage) {
        log.info("Receive CouponKafkaMessage: {}", couponKafkaMessage.toString());

        UserCouponStatus status = UserCouponStatus.of(couponKafkaMessage.getStatus());

        switch (status) {
            case USABLE:
                break;
            case USED:
                processUsedCoupons(couponKafkaMessage, status);
                break;
            case EXPIRED:
                processExpiredCoupons(couponKafkaMessage, status);
                break;
        }
        return false;
    }

    /**
     * <h2>处理已使用的用户优惠券</h2>
     * */
    private void processUsedCoupons(CouponKafkaMessage kafkaMessage,
                                    UserCouponStatus status) {
        // TODO 给用户发送短信
        processCouponsByStatus(kafkaMessage, status);
    }

    /**
     * <h2>处理过期的用户优惠券</h2>
     * */
    private void processExpiredCoupons(CouponKafkaMessage kafkaMessage,
                                       UserCouponStatus status) {
        // TODO 给用户发送推送
        processCouponsByStatus(kafkaMessage, status);
    }

    /**
     * <h2>根据状态处理优惠券信息</h2>
     * */
    private void processCouponsByStatus(CouponKafkaMessage kafkaMessage,
                                        UserCouponStatus status) {

        List<UserCoupon> coupons = userCouponService.listByIds(kafkaMessage.getIds());

        if (CollectionUtils.isEmpty(coupons)
                || coupons.size() != kafkaMessage.getIds().size()) {
            log.error("Can Not Find Right Coupon Info: {}",
                    JSON.toJSONString(kafkaMessage));
            // TODO 发送邮件
            return;
        }

        coupons.forEach(c -> c.setStatus(status));
        userCouponService.saveOrUpdateBatch(coupons);
        log.info("CouponKafkaMessage Op Coupon Count: {}", coupons.size());
    }
}
