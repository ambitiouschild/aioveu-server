package com.aioveu.oms.aioveu01Order.service.app;

import com.aioveu.common.result.PageResult;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.model.vo.OrderPageWithStatsVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.aioveu.oms.aioveu05OrderPay.model.form.OrderPaymentForm;
import com.aioveu.oms.aioveu05OrderPay.model.form.OrderSubmitForm;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OrderConfirmVO;
import com.aioveu.oms.aioveu01Order.model.vo.OrderPageVO;

import java.util.Map;

/**
 * @Description: TODO 订单业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:15
 * @param
 * @return:
 **/

public interface OrderService extends IService<OmsOrder> {

    /**
     * 订单确认 → 进入创建订单页面
     * <p>
     * 获取购买商品明细、用户默认收货地址、防重提交唯一token
     * 进入订单创建页面有两个入口，1：立即购买；2：购物车结算
     *
     * @param skuId 直接购买必填，购物车结算不填
     * @return {@link OrderConfirmVO}
     */
    OrderConfirmVO confirmOrder(Long skuId);

    /**
     * 订单提交
     *
     * @param orderSubmitForm {@link OrderSubmitForm}
     * @return 订单编号
     */
    String submitOrder(OrderSubmitForm orderSubmitForm);

    /**
     * 订单支付
     */
    <T> T payOrder(OrderPaymentForm paymentForm);

    /**
     * 系统关闭订单
     */
    boolean closeOrder(String orderSn);

    /**
     * 删除订单
     */
    boolean deleteOrder(Long id);





    /**
     * 订单分页列表
     *
     * @param queryParams 订单分页查询参数
     * @return {@link OrderPageVO}
     */
    IPage<OrderPageVO> getOrderPage(OrderPageQuery queryParams);


    /**
     * 获取订单统计信息
     */
    Map<String, Object> getOrderStatistics(OrderPageQuery queryParams);



    /**
     *   TODO       获取订单分页和统计信息（业务编排）
                 1.接口目标明确：为App端返回“数据列表” + “全局统计”。
                 2.数据结构简化：移除所有分页元数据字段，使响应对象纯粹服务于业务数据展示。
                 3.查询逻辑调整：
                     列表查询：从分页查询改为列表查询（可通过LIMIT控制首批数据量，后续由App端通过参数请求更多）。
                     统计查询：保持不变，因为它本就基于全部筛选条件进行计算。
     *
     */
    OrderPageWithStatsVO getOrderPageWithStatistics(OrderPageQuery queryParams);


}

