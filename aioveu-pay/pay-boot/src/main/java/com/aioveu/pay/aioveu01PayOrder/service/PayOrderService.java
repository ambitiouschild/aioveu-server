package com.aioveu.pay.aioveu01PayOrder.service;

import com.aioveu.common.result.PageResult;
import com.aioveu.common.result.Result;
import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu01PayOrder.model.form.PayOrderForm;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQuery;
import com.aioveu.pay.aioveu01PayOrder.model.query.PayOrderQueryDTO;
import com.aioveu.pay.aioveu01PayOrder.model.vo.*;
import com.aioveu.pay.aioveuModule.model.vo.PaymentCallbackDTO;
import com.aioveu.pay.aioveuModule.model.vo.PaymentParamsVO;
import com.aioveu.pay.aioveuModule.model.vo.PaymentRequestDTO;
import com.aioveu.pay.aioveuModule.model.vo.PaymentStatusVO;
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


    /**
     * 查询支付订单
     *
     * @param paymentNo 支付单号
     * @return PayOrder
     */
    PayOrder selectByPaymentNo(String paymentNo);


    /**
     *  更新订单状态
     *
     * @param order 支付单号,callback
     * @return Boolean
     */
    Boolean updateOrderStatus(PayOrder order, PaymentCallbackDTO callback);

    /**
     *  处理业务逻辑
     *
     * @param order 支付单号,callback
     * @return Boolean
     */
    void handleBusinessLogic(PayOrder order, PaymentCallbackDTO callback);


//    // 请求构建为支付订单
//    PayOrderCreateDTO buildPayOrderDTO(PaymentRequestDTO request);
//
//    // 创建支付订单
//    Result<String> createPayOrder(PayOrderCreateDTO dto);
//
//    // 获取支付参数
//    Result<PaymentParamsVO> getPaymentParams(String paymentNo);
//
//    // 处理支付回调
//    Result<Void> handlePaymentCallback(PaymentCallbackDTO dto);
//
//    // 查询支付状态
//    Result<PaymentStatusVO> queryPaymentStatus(String paymentNo);
//
//    // 关闭支付订单
//    Result<Void> closePayment(String paymentNo);
//
//    // 分页查询支付订单
//    Result<PageResult<PayOrderVO>> queryPayOrderPage(PayOrderQueryDTO queryDTO);
//
//    // 同步支付状态
//    Result<Void> syncPaymentStatus(String paymentNo);
}
