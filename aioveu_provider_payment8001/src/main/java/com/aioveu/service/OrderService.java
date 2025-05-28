package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Company;
import com.aioveu.entity.Order;
import com.aioveu.feign.vo.WeChatPayParamVO;
import com.aioveu.form.FieldForm;
import com.aioveu.form.OrderForm;
import com.aioveu.form.StoreExperienceOrderForm;
import com.aioveu.form.VipOrderForm;
import com.aioveu.vo.*;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface OrderService extends IService<Order> {

    /**
     * 订单设为已使用
     * @param orderIdList
     * @return
     */
    void toUsed(List<String> orderIdList);

    /**
     * 创建订单
     * @param form
     * @return
     */
    WxMaPayVO create(OrderForm form);

    /**
     * 创建活动订单
     * @param form
     * @return
     */
    WxMaPayVO createExercise(OrderForm form);

    /**
     * 更新健身订单
     * @param orderId
     * @param payFinishDate
     * @return
     * @throws Exception
     */
    boolean updateOrder(String orderId, Date payFinishDate);

    /**
     * 获取订单列表
     * @param status
     * @param userId
     * @return
     */
    List<OrderVO> getList(Integer status, Long companyId, String userId);

    /**
     * 根据公司id和商铺id，获取我的对应的订单列表
     * @param status
     * @param userId
     * @return
     */
    List<OrderVO> getList(Integer status, Long companyId, String userId, Long storeId);

    /**
     * 检查订单是否支付成功
     * @param orderId
     * @param payAppId
     * @return 支付成功时间
     * @throws Exception
     */
    Date checkOrderPayFinishTime(String orderId, String payAppId, String payType, String payDate) throws Exception;

    /**
     * 根据公司id、店铺id
     * 查询我的定场信息
     * @param status
     * @param companyId
     * @param userId
     * @param storeId
     * @return
     */
    List<FieldOrderVO> getFieldOrderList(Integer status, Long companyId, String userId, Long storeId);

    /**
     * 订场订单 小程序管理端
     * @param param
     * @return
     */
    IPage<FieldOrderManagerVO> getFieldManagerList(Map<String, Object> param);

    /**
     * 获取订单列表
     * @param param
     * @return
     */
    IPage<OrderManagerVO> getManagerList(Map<String, Object> param);

    /**
     * 详情
     * @param orderId
     * @return
     */
    OrderManagerDetailVO detail(String orderId);

    /**
     * 取消订单
     * @param orderId
     * @param reason
     * @return
     */
    Boolean cancelOrder(String orderId, String reason);

    /**
     * 退款
     * @param order
     * @param reason
     * @param changeFieldStatus
     * @return
     */
    boolean orderRefund(Order order, String reason, boolean changeFieldStatus);

    /**
     * 继续支付
     * @param orderId
     * @return
     */
    WxMaPayVO continuePay(String orderId);

    /**
     * 取消未支付的订单
     * @param id
     * @return
     */
    boolean cancelUnPayOrder(String id);

    /**
     * 获取待支付和待使用的订单
     * @param userId
     * @return
     */
    Integer getUnPayAndUnUseOrderNumber(String userId);

    /**
     * 获取地推待使用的订单
     * @param userId
     * @return
     */
    Integer getUnUseOrderNumber(String userId, Long companyId);

    List<Order> getAvailableDateRangeAndStatus(Long storeId, Date start, Date end, String saleUserId, String categoryByCode, Integer... status);

    /**
     * 获取指定店铺时间范围内的有效订单
     * @param storeId
     * @param start
     * @param end
     * @param saleUserId
     * @param isActive
     * @param status
     * @return
     */
    List<Order> getAvailableDateRangeAndStatus(Long storeId, Date start, Date end, String saleUserId, boolean isActive, Integer... status);


    /**
     * 判断用户在startDateStr之前是否下categoryId类型的订单
     * 没有：是categoryId类型产品的新用户
     * 有：是categoryId类型产品的续签用户
     * @param storeId
     * @param categoryId
     * @param userId
     * @param status
     * @param startDate
     * @return
     */
    Order getNewSignatureOrder(Long storeId, Long categoryId, String userId, List<Integer> status, Date startDate);

    /**
     * 查找用户所有订单
     * @param userId
     * @return
     */
    List<Order> getByUserId(String userId);

    /**
     * 核销码 订单设为使用中
     * @param orderId
     * @param code 核销码
     * @return
     */
    boolean checkCodeToUsing(String orderId, String code);

    /**
     * 创建微信小程序支付参数
     * @param userId
     * @param userOpenId
     * @param appId
     * @param wxPayId
     * @param orderId
     * @param name
     * @param actualAmount
     * @param noticeUrl
     * @param payType
     * @param tradeType
     * @param payCallCategory
     * @param closeMinutes 超时关闭订单 分钟
     * @return
     */
    WeChatPayParamVO getPayParam(String userId, String userOpenId, String appId, String wxPayId, String orderId, String name,
                                 BigDecimal actualAmount, String noticeUrl, String payType, String tradeType, String payCallCategory, int closeMinutes);

    /**
     * 更新订单成功
     * @param orderId
     * @param payFinishTime
     * @return
     */
    boolean updateOrder2Success(String orderId, Date payFinishTime);


    /**
     * 查询体验课订单数量
     * @param start
     * @param end
     * @param storeId
     * @param saleUserId
     * @return
     */
    Integer getExperienceNumber(Date start, Date end, Long storeId, String saleUserId);

    /**
     * 根据时间查询约课列表
     * @param form
     * @return
     */
    IPage<UserInfoOrderVO> getExperiential(StoreExperienceOrderForm form);

    /**
     * 创建完成订单
     * @param form
     * @return
     */
    Order createFinishOrder(OrderForm form);

    /**
     * 测试报名
     * @param exerciseId
     * @param number
     */
    void testOrder(Long exerciseId, int number);

    /**
     * 获取分析订单列表
     * @param start
     * @param end
     * @param storeId
     * @param status
     * @param saleUserId
     * @return
     */
    List<AnalysisOrderVO> getAnalysisOrderList(Date start, Date end, Long storeId, Integer status, String saleUserId);

    List<AnalysisOrderVO> getOrderListById(List<String> orderIdList);

    List<AnalysisOrderVO> getRenewalOrderList(Date start, Date end, Long storeId, List<Integer> statusList, String userCouponId);

    List<InvoiceRequestOrderVO> getInvoiceRequestOrderList(String startStr, String endStr, Long companyId, String username) throws ParseException;

    /**
     * 合同预览
     * @param userId
     * @param categoryId
     * @param productId
     * @return
     */
    String previewAgreement(String userId, Long categoryId, String productId);

    /**
     * 订单合同预览
     * @param orderId
     * @return
     */
    String previewOrderAgreement(String orderId);

    /**
     * 获取订单的数量统计
     * @param storeId
     * @return
     */
    OrderNumberVO getOrderNumber(Long storeId);

    /**
     * 创建订场订单
     * @param form
     * @return
     */
    WxMaPayVO createField(FieldForm form);

    /**
     * 订场支付
     * @param orderId
     * @param company
     * @return
     */
    WxMaPayVO fieldPay(String orderId, Company company);

    /**
     * 更新订场订单状态
     * @param orderId
     * @param payFinishDate
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    boolean updateFieldOrder(String orderId, Date payFinishDate);

    @Transactional(rollbackFor = Exception.class)
    boolean updateVipOrder(String orderId, Date payFinishDate);

    /**
     * 更新订场订单状态
     * @param orderId
     * @param payFinishTime
     * @return
     */
    boolean updateFieldOrder2Success(String orderId, Date payFinishTime);

    /**
     * 订场订单详情
     * @param orderId
     * @return
     */
    FieldOrderDetailVO fieldDetail(String orderId);

    /**
     * 创建会员卡订单
     * @param form
     * @return
     */
    WxMaPayVO createVipOrder(VipOrderForm form);

    boolean updateVipOrder2Success(String orderId, Date payFinishTime);

    /**
     * 订单自动完成
     * @param orderId
     * @return
     */
    BigDecimal autoFinishOrder(String orderId);

    /**
     * 管理员取消订单
     * @param orderId
     * @return
     */
    boolean adminCancelOrder(String orderId);

    /**
     * 获取基本的订单信息列表 用于展示统计的订单明细
     * @param param
     * @return
     */
    IPage<BasicOrderVO> getBasicOrderList(Map<String, Object> param);

}
