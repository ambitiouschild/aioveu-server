package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeUserCoupon;

/**
 * @Author： yao
 * @Date： 2024/12/2 11:31
 * @Describe：
 */
public interface GradeUserCouponService extends IService<GradeUserCoupon> {

    /**
     * 更新课程对应用户优惠券状态为使用
     * @param gradeId
     * @return
     */
    boolean gradeUserCoupon2Use(Long gradeId);

}
