package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.dto.UserAvailableCouponDTO;
import com.aioveu.dto.UserCouponDTO;
import com.aioveu.entity.CouponChangeRecord;
import com.aioveu.entity.UserCoupon;
import com.aioveu.enums.UserCouponStatus;
import com.aioveu.form.CouponConvertForm;
import com.aioveu.form.OrderUserCouponForm;
import com.aioveu.form.SettlementForm;
import com.aioveu.vo.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <h1>用户服务相关的接口定义</h1>
 * 1. 用户三类状态优惠券信息展示服务
 * 2. 查看用户当前可以领取的优惠券模板 - coupon-template 微服务配合实现
 * 3. 用户领取优惠券服务
 * 4. 用户消费优惠券服务 - coupon-settlement 微服务配合实现
 * Created by Qinyi.
 */
public interface IUserCouponService extends IService<UserCoupon> {

    /**
     * <h2>根据用户 id 和状态查询优惠券记录</h2>
     * @param userId 用户 id
     * @param status 优惠券状态
     * @return {@link UserCoupon}s
     * */
    List<UserCoupon> findUserCouponsByStatus(String userId, Integer status);

    List<UserCoupon> findUserCouponsByStatus(String userId, Long companyId, Integer status);

    /**
     * <h2>根据用户 id 查找当前可以领取的优惠券模板</h2>
     * @param userId 用户 id
     * @return {@link CouponTemplateSDK}s
     * */
    List<CouponTemplateSDK> findAvailableTemplate(String userId);

    /**
     * <h2>用户领取优惠券</h2>
     * @param request {@link AcquireTemplateRequest}
     * @return {@link UserCoupon}
     * */
    List<UserCoupon> acquireTemplate(AcquireTemplateRequest request);

    /**
     * <h2>结算(核销)优惠券</h2>
     * @param info {@link SettlementInfo}
     * @return {@link SettlementInfo}
     * */
    SettlementInfo settlement(SettlementInfo info);

    /**
     * 根据优惠券模板id领取优惠券
     * @param userId 用户id
     * @param couponTemplateId 优惠券模板id
     * @param orderId 订单id
     * @param singleCouponAmount 单个优惠券金额
     * @param orderLimit 活动关联优惠券的领取数量 优先级大于优惠券本身
     * @param expiration 活动关联优惠券的过期规则 优先级大于优惠券本身
     * @param limit 是否限制用户领取
     * @return
     */
    List<Long> acquireByCouponTemplateId(String userId, Long couponTemplateId, String orderId, BigDecimal singleCouponAmount,
                                   Integer orderLimit, TemplateRule.Expiration expiration, boolean limit);

    /**
     * <h2>根据用户 id 和状态查询优惠券记录</h2>
     * @param userId 用户 id
     * @param status 优惠券状态
     * @param categoryId 分类id
     * @param productId 商品id
     * @return {@link UserCouponVO}s
     * */
    List<UserCouponVO> findByStatus(String userId, Long companyId, Integer status, Long categoryId, Long productId, Integer productLineId);

    /**
     * 根据码查找用户可用的优惠券
     * @param code
     * @return
     */
    UserCoupon findUsableCoupon(String code);

    /**
     * 查找详情
     * @param id
     * @return
     */
    UserCouponDetailVO detail(Long id);

    /**
     * 同步数据库和Redis记录
     * @return
     */
    boolean syncDatabase2Redis();

    /**
     * 预结算
     * @param settlementForm
     * @return
     */
    SettlementResultVO preSettlement(SettlementForm settlementForm);

    /**
     * <h2>根据用户 id 获取使用和过期的优惠券</h2>
     * @param userId 用户 id
     * @return {@link UserCouponVO}s
     * */
    List<UserCouponVO> useExpire(String userId);

    /**
     * 优惠券退回
     * @param userCouponId 用户优惠券id
     * @param gradeId 课程id 非必传
     * @param couponExtendDays 延期天数
     * @return
     */
    boolean couponBack(Long userCouponId, Long gradeId, Integer couponExtendDays);

    /**
     * 优惠券退回
     * @param userCouponId
     * @return
     */
    boolean couponBack(Long userCouponId);

    /**
     * 同步mysql的优惠券到redis
     * @param userId
     * @return
     */
    boolean syncMySql2Redis(String userId);

    /**
     * 通过订单id获取对应的优惠券
     * @param orderIds
     * @return
     */
    List<UserCoupon> getByOrderIds(List<String> orderIds);

    /**
     * 约课核销优惠券
     * @param userId
     * @param num
     * @param gradeId
     * @param storeId
     * @param gradeEnrollUserId  用户班级约课id
     * @return
     */
    boolean gradeCheckCode(String userId, Integer num, Long gradeId, Long storeId, Long gradeEnrollUserId);

    /**
     * 批量退回优惠券
     * @param idList
     * @return
     */
    boolean batchBackCoupon(List<CouponChangeRecord> idList, int couponExtendDays);

    /**
     * 根据模板id获取用户对应的优惠券
     * @param userId
     * @param templateIds
     * @return
     */
    List<UserCoupon> getByUserIdAndTemplateId(String userId, List<String> templateIds);

    boolean claimCoupons(Long id, String username);

    /**
     * 查询订单的优惠券是否全部使用完成
     * @param gradeId
     * @return
     */
    boolean gradeCouponAllUsed(Long gradeId);

    /**
     * 用户端优惠券领取
     * @param username 用户名
     * @param code 优惠券码
     * @return
     */
    Boolean receiveShareCouponByCode(String username, String code);

    /**
     * 获取订单下可用优惠券的数量
     * @param orderId
     * @return
     */
    int getOrderAvailableCouponCount(String orderId);

    /**
     * 获取订单的优惠券
     * @param phone
     * @return
     */
    List<OrderCouponVO> getOrderCoupon(String phone);

    /**
     * 获取订单的优惠券
     * @param orderId
     * @return
     */
    List<UserCouponVO> getOrderCouponList(String orderId);

    /**
     * 订单优惠券领取
     * @param form
     * @return
     */
    boolean orderCouponAdd(OrderUserCouponForm form);

    /**
     * 获取用户优惠券统计列表
     * @return
     */
    IPage<UserCouponTotalVO> getTotalList(int page, int size, String phone, String userName, Long templateId, Integer status, Integer countFrom, Integer countTo, Long storeId,Integer orderStatus,String orderStatusDesc,String coachId);


    /**
     * 根据活动赠送优惠券
     * @param userId
     * @param exerciseId
     * @return
     */
    boolean sendByExercise(String userId, Long exerciseId);


    /**
     * 获取指定用户的优惠券列表
     * @param userIdList
     * @return
     */
    List<UserCouponDTO> getCouponByUserList(Long storeId, List<String> userIdList);

    /**
     * 检查优惠券有效期
     */
    void checkValidity();

    /**
     * 延长优惠券的优惠券
     * @param userId
     * @param validityDay
     * @param num
     * @return 结果
     */
    boolean setExpireValidity(String userId, Long templateId, int status, String validityDay, int num, Integer type, String userCouponIds);

    /**
     * 测试优惠券排序问题
     */
    void testCouponSort(String userId, Integer templateNum, Long gradeId);

    /**
     * 获取用户可用课程券
     * @return
     */
    List<UserAvailableCouponDTO> getUserGradeCoupon();

    /**
     * 重新计算对应订单的优惠券金额
     * @param orderId
     * @param totalAmount
     * @return
     */
    boolean recalculateAmount(String orderId, BigDecimal totalAmount);

    /**
     * 传入课券数阈值threshold，查询门店下所有课券数低于改阈值的人员信息，不包括课券数为0的客户
     * 课券数>0 and 课券数 <= threshold
     * @param storeId
     * @param threshold  阈值
     * @return
     */
    List<UserCouponTotalVO> queryCouponsBelowThreshold(Long storeId, Integer threshold);

    /**
     * 获取传入的用户id对应的销售、教练姓名信息
     * @param companyId
     * @param storeId
     * @param userIds
     * @return
     */
    List<UserCouponTotalVO> queryUserReceiveCallByUserIds(Long companyId,Long storeId,List<String> userIds);

    /**
     * 更新优惠券状态
     * @param userCouponIds
     * @param status
     * @return
     */
    boolean change2Status(List<Long> userCouponIds, UserCouponStatus status);

    /**
     * 优惠券转换
     * @param couponConvertForm
     * @return
     */
    boolean couponConvert(CouponConvertForm couponConvertForm);
}
