package com.aioveu.sms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.sms.model.entity.SmsCoupon;
import com.aioveu.sms.model.form.CouponForm;
import com.aioveu.sms.model.query.CouponPageQuery;
import com.aioveu.sms.model.vo.CouponPageVO;

/**
 * @Description: TODO 优惠券业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:49
 * @param
 * @return:
 **/

public interface SmsCouponService extends IService<SmsCoupon> {

    /**
     * 优惠券分页列表
     *
     * @param queryParams
     * @return
     */
    Page<CouponPageVO> getCouponPage(CouponPageQuery queryParams);

    /**
     * 新增优惠券
     *
     * @param couponForm
     * @return
     */
    boolean saveCoupon(CouponForm couponForm);

    /**
     * 修改优惠券
     *
     * @param couponId 优惠券ID
     * @param couponForm 优惠券表单
     * @return
     */
    boolean updateCoupon(Long couponId, CouponForm couponForm);

    /**
     * 删除优惠券
     *
     * @param ids 优惠券ID，多个以英文逗号(,)分割
     * @return
     */
    boolean deleteCoupons(String ids);

    /**
     * 优惠券表单数据
     * 
     * @param couponId
     * @return
     */
    CouponForm getCouponFormData(Long couponId);
}
