package com.aioveu.sms.aioveu03CouponHistory.service;

import com.aioveu.sms.aioveu03CouponHistory.model.form.SmsCouponHistoryForm;
import com.aioveu.sms.aioveu03CouponHistory.model.query.SmsCouponHistoryQuery;
import com.aioveu.sms.aioveu03CouponHistory.model.vo.SmsCouponHistoryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.sms.aioveu03CouponHistory.model.entity.SmsCouponHistory;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 优惠券领取/使用记录服务类
 * @Date  2026/1/12 11:59
 * @Param
 * @return
 **/

public interface SmsCouponHistoryService extends IService<SmsCouponHistory> {

    /**
     *优惠券领取/使用记录分页列表
     *
     * @return {@link IPage<SmsCouponHistoryVO>} 优惠券领取/使用记录分页列表
     */
    IPage<SmsCouponHistoryVO> getSmsCouponHistoryPage(SmsCouponHistoryQuery queryParams);

    /**
     * 获取优惠券领取/使用记录表单数据
     *
     * @param id 优惠券领取/使用记录ID
     * @return 优惠券领取/使用记录表单数据
     */
    SmsCouponHistoryForm getSmsCouponHistoryFormData(Long id);

    /**
     * 新增优惠券领取/使用记录
     *
     * @param formData 优惠券领取/使用记录表单对象
     * @return 是否新增成功
     */
    boolean saveSmsCouponHistory(SmsCouponHistoryForm formData);

    /**
     * 修改优惠券领取/使用记录
     *
     * @param id   优惠券领取/使用记录ID
     * @param formData 优惠券领取/使用记录表单对象
     * @return 是否修改成功
     */
    boolean updateSmsCouponHistory(Long id, SmsCouponHistoryForm formData);

    /**
     * 删除优惠券领取/使用记录
     *
     * @param ids 优惠券领取/使用记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteSmsCouponHistorys(String ids);

}
