package com.aioveu.pay.aioveu01PayOrder.service;

import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.form.PayOrderForm;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQuery;
import com.aioveu.pay.aioveu01PayOrder.model.vo.PayOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayOrderService
 * @Description TODO 支付订单服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 17:32
 * @Version 1.0
 **/
public interface PayOrderService extends IService<PayOrder> {

    /**
     *支付订单分页列表
     *
     * @return {@link IPage<PayOrderVO>} 支付订单分页列表
     */
    IPage<PayOrderVO> getPayOrderPage(PayOrderQuery queryParams);

    /**
     * 获取支付订单表单数据
     *
     * @param id 支付订单ID
     * @return 支付订单表单数据
     */
    PayOrderForm getPayOrderFormData(Long id);

    /**
     * 新增支付订单
     *
     * @param formData 支付订单表单对象
     * @return 是否新增成功
     */
    boolean savePayOrder(PayOrderForm formData);

    /**
     * 修改支付订单
     *
     * @param id   支付订单ID
     * @param formData 支付订单表单对象
     * @return 是否修改成功
     */
    boolean updatePayOrder(Long id, PayOrderForm formData);

    /**
     * 删除支付订单
     *
     * @param ids 支付订单ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayOrders(String ids);
}
