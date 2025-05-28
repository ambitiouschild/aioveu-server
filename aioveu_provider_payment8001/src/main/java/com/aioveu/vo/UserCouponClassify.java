package com.aioveu.vo;

import com.aioveu.entity.UserCoupon;
import com.aioveu.enums.PeriodType;
import com.aioveu.enums.UserCouponStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <h1>用户优惠券的分类, 根据优惠券状态</h1>
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCouponClassify {

    /** 可以使用的 */
    private List<UserCoupon> usable;

    /** 已使用的 */
    private List<UserCoupon> used;

    /** 已过期的 */
    private List<UserCoupon> expired;

    /**
     * <h2>对当前的优惠券进行分类</h2>
     * */
    public static UserCouponClassify classify(List<UserCoupon> coupons) {

        List<UserCoupon> usable = new ArrayList<>(coupons.size());
        List<UserCoupon> used = new ArrayList<>(coupons.size());
        List<UserCoupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c -> {

            // 判断优惠券是否过期
            boolean isTimeExpire = false;
            long curTime = System.currentTimeMillis();

            if (c.getTemplateSDK().getRule().getExpiration().getPeriod().equals(
                    PeriodType.REGULAR.getCode())) {
                if (c.getTemplateSDK().getRule().getExpiration()
                        .getDeadline() != null) {
                    isTimeExpire = c.getTemplateSDK().getRule().getExpiration()
                            .getDeadline() <= curTime;
                }
            } else if (c.getTemplateSDK().getRule().getExpiration().getPeriod().equals(
                    PeriodType.SHIFT.getCode())){
                if (c.getCreateDate() == null) {
                    c.setCreateDate(new Date());
                }
                isTimeExpire = DateUtils.addDays(
                        c.getCreateDate(),
                        c.getTemplateSDK().getRule().getExpiration().getGap()
                ).getTime() <= curTime;
            }
            if (isTimeExpire || c.getStatus() == UserCouponStatus.EXPIRED) {
                c.setStatus(UserCouponStatus.EXPIRED);
                expired.add(c);
            } else if (c.getStatus() == UserCouponStatus.USED) {
                used.add(c);
            } else {
                usable.add(c);
            }
        });
        return new UserCouponClassify(usable, used, expired);
    }
}
