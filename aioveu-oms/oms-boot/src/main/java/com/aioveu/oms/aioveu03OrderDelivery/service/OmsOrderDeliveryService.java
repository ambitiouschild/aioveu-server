package com.aioveu.oms.aioveu03OrderDelivery.service;

import com.aioveu.oms.aioveu03OrderDelivery.model.form.OmsOrderDeliveryForm;
import com.aioveu.oms.aioveu03OrderDelivery.model.query.OmsOrderDeliveryQuery;
import com.aioveu.oms.aioveu03OrderDelivery.model.vo.OmsOrderDeliveryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.oms.aioveu03OrderDelivery.model.entity.OmsOrderDelivery;

/**
 * @Description: TODO 订单物流记录服务类
 * @Author: 雒世松
 * @Date: 2026-01-08 20:10
 * @param
 * @return:
 **/

public interface OmsOrderDeliveryService extends IService<OmsOrderDelivery> {

    /**
     *订单物流记录分页列表
     *
     * @return {@link IPage<OmsOrderDeliveryVO>} 订单物流记录分页列表
     */
    IPage<OmsOrderDeliveryVO> getOmsOrderDeliveryPage(OmsOrderDeliveryQuery queryParams);

    /**
     * 获取订单物流记录表单数据
     *
     * @param id 订单物流记录ID
     * @return 订单物流记录表单数据
     */
    OmsOrderDeliveryForm getOmsOrderDeliveryFormData(Long id);

    /**
     * 新增订单物流记录
     *
     * @param formData 订单物流记录表单对象
     * @return 是否新增成功
     */
    boolean saveOmsOrderDelivery(OmsOrderDeliveryForm formData);

    /**
     * 修改订单物流记录
     *
     * @param id   订单物流记录ID
     * @param formData 订单物流记录表单对象
     * @return 是否修改成功
     */
    boolean updateOmsOrderDelivery(Long id, OmsOrderDeliveryForm formData);

    /**
     * 删除订单物流记录
     *
     * @param ids 订单物流记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOmsOrderDeliverys(String ids);
}

