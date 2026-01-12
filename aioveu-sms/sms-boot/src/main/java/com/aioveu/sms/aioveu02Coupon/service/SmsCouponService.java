package com.aioveu.sms.aioveu02Coupon.service;

import com.aioveu.sms.aioveu02Coupon.model.form.SmsCouponForm;
import com.aioveu.sms.aioveu02Coupon.model.vo.SmsCouponVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.sms.aioveu02Coupon.model.entity.SmsCoupon;
import com.aioveu.sms.aioveu02Coupon.model.query.SmsCouponQuery;

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
     * @return {@link IPage<SmsCouponVO>} 优惠券分页列表
     */
    IPage<SmsCouponVO> getSmsCouponPage(SmsCouponQuery queryParams);

    /**
     * 获取优惠券表单数据
     *
     * @param id 优惠券ID
     * @return 优惠券表单数据
     */
    SmsCouponForm getSmsCouponFormData(Long id);

    /**
     * 新增优惠券
     *
     * @param formData 优惠券表单对象
     * @return 是否新增成功
     */
    boolean saveSmsCoupon(SmsCouponForm formData);

    /**
     * 修改优惠券
     *
     * @param id   优惠券ID
     * @param formData 优惠券表单对象
     * @return 是否修改成功
     */
    boolean updateSmsCoupon(Long id, SmsCouponForm formData);

    /**
     * 删除优惠券
     *
     * @param ids 优惠券ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteSmsCoupons(String ids);


}
