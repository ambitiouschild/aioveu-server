package com.aioveu.sms.aioveu04CouponSpu.service;

import com.aioveu.sms.aioveu04CouponSpu.model.form.SmsCouponSpuForm;
import com.aioveu.sms.aioveu04CouponSpu.model.query.SmsCouponSpuQuery;
import com.aioveu.sms.aioveu04CouponSpu.model.vo.SmsCouponSpuVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.sms.aioveu04CouponSpu.model.entity.SmsCouponSpu;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 优惠券适用的具体商品服务类
 * @Date  2026/1/12 12:17
 * @Param
 * @return
 **/

public interface SmsCouponSpuService extends IService<SmsCouponSpu> {


    /**
     *优惠券适用的具体商品分页列表
     *
     * @return {@link IPage<SmsCouponSpuVO>} 优惠券适用的具体商品分页列表
     */
    IPage<SmsCouponSpuVO> getSmsCouponSpuPage(SmsCouponSpuQuery queryParams);

    /**
     * 获取优惠券适用的具体商品表单数据
     *
     * @param id 优惠券适用的具体商品ID
     * @return 优惠券适用的具体商品表单数据
     */
    SmsCouponSpuForm getSmsCouponSpuFormData(Long id);

    /**
     * 新增优惠券适用的具体商品
     *
     * @param formData 优惠券适用的具体商品表单对象
     * @return 是否新增成功
     */
    boolean saveSmsCouponSpu(SmsCouponSpuForm formData);

    /**
     * 修改优惠券适用的具体商品
     *
     * @param id   优惠券适用的具体商品ID
     * @param formData 优惠券适用的具体商品表单对象
     * @return 是否修改成功
     */
    boolean updateSmsCouponSpu(Long id, SmsCouponSpuForm formData);

    /**
     * 删除优惠券适用的具体商品
     *
     * @param ids 优惠券适用的具体商品ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteSmsCouponSpus(String ids);

}
