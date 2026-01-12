package com.aioveu.sms.aioveu05CouponSpuCategory.service;

import com.aioveu.sms.aioveu05CouponSpuCategory.model.form.SmsCouponSpuCategoryForm;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.query.SmsCouponSpuCategoryQuery;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.vo.SmsCouponSpuCategoryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.entity.SmsCouponSpuCategory;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 优惠券适用的具体分类服务类
 * @Date  2026/1/12 13:11
 * @Param
 * @return
 **/
public interface SmsCouponSpuCategoryService extends IService<SmsCouponSpuCategory> {


    /**
     *优惠券适用的具体分类分页列表
     *
     * @return {@link IPage<SmsCouponSpuCategoryVO>} 优惠券适用的具体分类分页列表
     */
    IPage<SmsCouponSpuCategoryVO> getSmsCouponSpuCategoryPage(SmsCouponSpuCategoryQuery queryParams);

    /**
     * 获取优惠券适用的具体分类表单数据
     *
     * @param id 优惠券适用的具体分类ID
     * @return 优惠券适用的具体分类表单数据
     */
    SmsCouponSpuCategoryForm getSmsCouponSpuCategoryFormData(Long id);

    /**
     * 新增优惠券适用的具体分类
     *
     * @param formData 优惠券适用的具体分类表单对象
     * @return 是否新增成功
     */
    boolean saveSmsCouponSpuCategory(SmsCouponSpuCategoryForm formData);

    /**
     * 修改优惠券适用的具体分类
     *
     * @param id   优惠券适用的具体分类ID
     * @param formData 优惠券适用的具体分类表单对象
     * @return 是否修改成功
     */
    boolean updateSmsCouponSpuCategory(Long id, SmsCouponSpuCategoryForm formData);

    /**
     * 删除优惠券适用的具体分类
     *
     * @param ids 优惠券适用的具体分类ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteSmsCouponSpuCategorys(String ids);

}
