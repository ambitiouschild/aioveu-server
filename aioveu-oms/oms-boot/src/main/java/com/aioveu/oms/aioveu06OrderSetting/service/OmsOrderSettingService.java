package com.aioveu.oms.aioveu06OrderSetting.service;

import com.aioveu.oms.aioveu06OrderSetting.model.form.OmsOrderSettingForm;
import com.aioveu.oms.aioveu06OrderSetting.model.query.OmsOrderSettingQuery;
import com.aioveu.oms.aioveu06OrderSetting.model.vo.OmsOrderSettingVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.oms.aioveu06OrderSetting.model.entity.OmsOrderSetting;

/**
 * @Description: TODO 订单配置信息服务类
 * @Author: 雒世松
 * @Date: 2026-01-10 17:10
 * @param
 * @return:
 **/

public interface OmsOrderSettingService extends IService<OmsOrderSetting> {

    /**
     *订单配置信息分页列表
     *
     * @return {@link IPage<OmsOrderSettingVO>} 订单配置信息分页列表
     */
    IPage<OmsOrderSettingVO> getOmsOrderSettingPage(OmsOrderSettingQuery queryParams);

    /**
     * 获取订单配置信息表单数据
     *
     * @param id 订单配置信息ID
     * @return 订单配置信息表单数据
     */
    OmsOrderSettingForm getOmsOrderSettingFormData(Long id);

    /**
     * 新增订单配置信息
     *
     * @param formData 订单配置信息表单对象
     * @return 是否新增成功
     */
    boolean saveOmsOrderSetting(OmsOrderSettingForm formData);

    /**
     * 修改订单配置信息
     *
     * @param id   订单配置信息ID
     * @param formData 订单配置信息表单对象
     * @return 是否修改成功
     */
    boolean updateOmsOrderSetting(Long id, OmsOrderSettingForm formData);

    /**
     * 删除订单配置信息
     *
     * @param ids 订单配置信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOmsOrderSettings(String ids);
}

