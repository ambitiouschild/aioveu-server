package com.aioveu.refund.aioveu01RefundOrder.service;

import com.aioveu.refund.aioveu01RefundOrder.model.entity.RefundOrder;
import com.aioveu.refund.aioveu01RefundOrder.model.form.RefundOrderForm;
import com.aioveu.refund.aioveu01RefundOrder.model.query.RefundOrderQuery;
import com.aioveu.refund.aioveu01RefundOrder.model.vo.RefundOrderVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RefundOrderService
 * @Description TODO  订单退款申请服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 16:31
 * @Version 1.0
 **/
public interface RefundOrderService extends IService<RefundOrder> {

    /**
     *订单退款申请分页列表
     *
     * @return {@link IPage<RefundOrderVO>} 订单退款申请分页列表
     */
    IPage<RefundOrderVO> getRefundOrderPage(RefundOrderQuery queryParams);

    /**
     * 获取订单退款申请表单数据
     *
     * @param id 订单退款申请ID
     * @return 订单退款申请表单数据
     */
    RefundOrderForm getRefundOrderFormData(Long id);

    /**
     * 新增订单退款申请
     *
     * @param formData 订单退款申请表单对象
     * @return 是否新增成功
     */
    boolean saveRefundOrder(RefundOrderForm formData);

    /**
     * 修改订单退款申请
     *
     * @param id   订单退款申请ID
     * @param formData 订单退款申请表单对象
     * @return 是否修改成功
     */
    boolean updateRefundOrder(Long id, RefundOrderForm formData);

    /**
     * 删除订单退款申请
     *
     * @param ids 订单退款申请ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRefundOrders(String ids);
}
