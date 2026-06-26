package com.aioveu.oms.aioveu01Order.service.app;

import com.aioveu.common.result.Result;
import com.aioveu.oms.aioveu01Order.model.entity.OmsOrder;
import com.aioveu.oms.aioveu01Order.model.form.ShipOrderDTO;
import com.aioveu.oms.aioveu01Order.model.vo.OrderPageWithStatsVO;
import com.aioveu.common.rabbitmq.producer.model.payment.PaymentSuccessMessage;
import com.aioveu.oms.aioveu01Order.model.vo.OrderStatisticsVO;
import com.aioveu.order.model.aioveu01Order.form.OmsOrderForm;
import com.aioveu.order.model.aioveu01Order.vo.OrderSubmitVO;
import com.aioveu.order.model.aioveu05OrderPay.form.OrderPaymentForm;
import com.aioveu.pay.model.aioveuPayment.PaymentParamsVO;
import com.aioveu.pay.model.aioveuPayment.PaymentResultVO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestFEToOmsDTO;
import com.aioveu.pay.model.aioveuPayment.request.PaymentRequestOmsToPayDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.databind.JsonNode;
import com.aioveu.order.model.aioveu05OrderPay.form.OrderSubmitForm;
import com.aioveu.oms.aioveu01Order.model.query.OrderPageQuery;
import com.aioveu.oms.aioveu01Order.model.vo.OrderConfirmVO;
import com.aioveu.oms.aioveu01Order.model.vo.OrderPageVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;

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
    OrderSubmitVO submitOrder(OrderSubmitForm orderSubmitForm, String clientId);



    /**
     * 系统关闭订单
     */
    boolean closeOrder(String orderSn);

    /**
     * 删除订单
     */
    boolean deleteOrder(Long id);


    /**
     * 根据orderSn获取到订单
     */
    OmsOrderForm getOmsOrderByOrderNo(String orderSn);


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
    OrderStatisticsVO getOrderStatistics(OrderPageQuery queryParams);



    /**
     *   TODO       获取订单分页和统计信息（业务编排）
                 1.接口目标明确：为App端返回“数据列表” + “全局统计”。
                 2.数据结构简化：移除所有分页元数据字段，使响应对象纯粹服务于业务数据展示。
                 3.查询逻辑调整：
                     列表查询：从分页查询改为列表查询（可通过LIMIT控制首批数据量，后续由App端通过参数请求更多）。
                     统计查询：保持不变，因为它本就基于全部筛选条件进行计算。
                一个接口干两件事，违反单一职责
                 如果一定要“一个接口返回”，正确姿势是「组合 VO」
     *
     */
    OrderPageWithStatsVO getOrderPageWithStatistics(OrderPageQuery queryParams);


    /**
     * 更新订单支付状态
     */
    boolean updateOrderPaymentStatus(OmsOrder order, PaymentSuccessMessage message);


    /**
     * 根据订单号查询订单
     */
    OmsOrder getByOrderNo(String orderNo);

    /**
     * 根据订单号获取订单ID
     */
    Long getOrderIdByOrderNo(String orderSn);


    /**
     * 状态变为【已发货】
     */
    void markAsShipped(String orderSn,ShipOrderDTO dto);




    /*
    * 微信发货管理的三个核心接口------------------------------------------------------------------
    * */
    /**
     * 1. 录入发货信息
     * @param orderSn 自家系统的订单orderSn
     */
    JsonNode uploadShipping(String orderSn, ShipOrderDTO dto);



    /*
    * （✅ 强烈推荐）：自动发货不用 DTO
    * */
    JsonNode uploadShipping(String orderSn);

    /**
     * 2. 提醒用户确认收货（以自有系统为准）
     * @param orderSn 自家系统的订单orderSn
     */
    JsonNode notifyConfirmReceive(String orderSn);

    /**
     * 3. 查询订单状态
     * @param orderSn 自家系统的订单orderSn
     */
    JsonNode queryOrder(String orderSn);

    /*
     * --------------------------------------------------------------------------
     * */

    /*
    * 订单支付
    * */
    PaymentParamsVO payOrder(PaymentRequestFEToOmsDTO form);
}

