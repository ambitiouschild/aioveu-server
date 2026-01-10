package com.aioveu.oms.aioveu01Order.service.admin;

import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.model.form.OmsOrderForm;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OmsOrderPageVO;

/**
 * @Description: TODO Admin-订单业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:13
 * @param
 * @return:
 **/

public interface OmsOrderService extends IService<OmsOrder> {
    /**
     * 订单分页列表
     *
     * @param queryParams {@link OrderPageQuery}
     * @return
     */
    IPage<OmsOrderPageVO> getOmsOrderPage(OrderPageQuery queryParams);

    /**
     * 获取订单详情表单数据
     *
     * @param id 订单详情ID
     * @return 订单详情表单数据
     */
    OmsOrderForm getOmsOrderFormData(Long id);

    /**
     * 新增订单详情
     *
     * @param formData 订单详情表单对象
     * @return 是否新增成功
     */
    boolean saveOmsOrder(OmsOrderForm formData);

    /**
     * 修改订单详情
     *
     * @param id   订单详情ID
     * @param formData 订单详情表单对象
     * @return 是否修改成功
     */
    boolean updateOmsOrder(Long id, OmsOrderForm formData);

    /**
     * 删除订单详情
     *
     * @param ids 订单详情ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOmsOrders(String ids);


}

