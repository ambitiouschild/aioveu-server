package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.constant.SportConstant;
import com.aioveu.dao.AnalysisDao;
import com.aioveu.dto.AnalysisGradeFieldPlanDto;
import com.aioveu.entity.*;
import com.aioveu.enums.ChangeTypeStatus;
import com.aioveu.enums.GradeStatus;
import com.aioveu.enums.OrderStatus;
import com.aioveu.enums.PayType;
import com.aioveu.exception.SportException;
import com.aioveu.form.StoreAnalysisForm;
import com.aioveu.service.*;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:26
 */
@Slf4j
@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private AnalysisDao analysisDao;

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    @Autowired
    private GradeSignEvaluateService gradeSignEvaluateService;

    @Autowired
    private UserBalanceChangeService userBalanceChangeService;

    @Autowired
    private StoreCoachService storeCoachService;

    @Autowired
    private UserCallService userCallService;

    @Autowired
    private UserVipCardService userVipCardService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderRefundService orderRefundService;


    /**
     * @param storeId
     * @param startStr
     * @param endStr
     * @return
     * @throws Exception
     */
    @Override
    public StoreAnalysisVO storeAnalysis(Long storeId, String startStr, String endStr, String coachUserId) throws Exception {
        Date start = DateUtils.parseDate(startStr, "yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.parseDate(endStr, "yyyy-MM-dd"));
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date end = calendar.getTime();

        Store store = storeService.getById(storeId);

//        if (DateUtils.addDays(start, 31).compareTo(end) < 0){
//            throw new SportException("最多只允许查一个月内统计");
//        }

        StoreAnalysisVO storeAnalysis = new StoreAnalysisVO();
        storeAnalysis.setFieldActiveAmount(BigDecimal.ZERO);
        storeAnalysis.setFieldVipActiveAmount(BigDecimal.ZERO);
        // 指定时间的已支付并激活的的订单
        List<Order> orderList = orderService.getAvailableDateRangeAndStatus(storeId, start, end, coachUserId, true, OrderStatus.USING.getCode(), OrderStatus.ORDER_FINISH.getCode());
        storeAnalysis.setOrderNumber(orderList.size());
        // 指定时间的已支付订单的总金额
        storeAnalysis.setAmount(BigDecimal.valueOf(orderList.stream().map(Order::getActualAmount).mapToDouble(BigDecimal::doubleValue).sum()));
        // 指定时间的会员卡的订单
        List<Order> vipActiveOrderList = orderService.getAvailableDateRangeAndStatus(storeId, start, end, coachUserId, SportConstant.VIP_CATEGORY_CODE, OrderStatus.USING.getCode());
        storeAnalysis.setVipActiveOrderNumber(vipActiveOrderList.size());
        BigDecimal vipActiveAmount = BigDecimal.valueOf(vipActiveOrderList.stream().map(Order::getActualAmount).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP);
        storeAnalysis.setVipActiveAmount(vipActiveAmount);

        // 订场收款统计
        // 指定时间 付款订单 包含待使用 已完成 和 退款的
        List<Order> fieldOrderList = orderService.getAvailableDateRangeAndStatus(storeId, start, end, null, SportConstant.FIELD_CATEGORY_CODE, OrderStatus.PAY.getCode(), OrderStatus.USING.getCode(), OrderStatus.ORDER_FINISH.getCode(), OrderStatus.REFUND.getCode());
        if (CollectionUtils.isNotEmpty(fieldOrderList)) {
            storeAnalysis.setFieldOrderNumber(fieldOrderList.size());

            List<Order> onlineFieldOrderList = new ArrayList<>();
            List<Order> vipFieldOrderList = new ArrayList<>();
            BigDecimal onlineFieldOrderAmount = BigDecimal.ZERO;
            BigDecimal vipFieldOrderAmount = BigDecimal.ZERO;
            BigDecimal fieldOrderAmount = BigDecimal.ZERO;
            for (Order order : fieldOrderList) {
                // 总金额计算
                fieldOrderAmount = fieldOrderAmount.add(order.getConsumeAmount());
                if (order.getPayType().equals(PayType.Wechat.getCode())) {
                    // 在线支付
                    onlineFieldOrderList.add(order);
                    onlineFieldOrderAmount = onlineFieldOrderAmount.add(order.getConsumeAmount());
                } else if (order.getPayType().equals(PayType.MIX.getCode())) {
                    // 混合支付 部分在线支付 部分会员卡支付
                    vipFieldOrderList.add(order);
                    onlineFieldOrderList.add(order);
                    // 混合支付 实际金额就是在线支付的金额
                    onlineFieldOrderAmount = onlineFieldOrderAmount.add(order.getActualAmount());
                    // 消费金额 - 实际金额 = 会员卡支付的金额
                    vipFieldOrderAmount = vipFieldOrderAmount.add(order.getConsumeAmount().subtract(order.getActualAmount()));
                } else {
                    vipFieldOrderAmount = vipFieldOrderAmount.add(order.getConsumeAmount());
                    vipFieldOrderList.add(order);
                }
            }
            storeAnalysis.setOnlineFieldOrderNumber(onlineFieldOrderList.size());
            storeAnalysis.setVipFieldOrderNumber(vipFieldOrderList.size());

            storeAnalysis.setFieldOrderAmount(fieldOrderAmount.setScale(2, RoundingMode.HALF_UP));
            storeAnalysis.setOnlineFieldOrderAmount(onlineFieldOrderAmount.setScale(2, RoundingMode.HALF_UP));
            storeAnalysis.setVipFieldOrderAmount(vipFieldOrderAmount.setScale(2, RoundingMode.HALF_UP));
        } else {
            storeAnalysis.setFieldOrderNumber(0);
            storeAnalysis.setOnlineFieldOrderNumber(0);
            storeAnalysis.setVipFieldOrderNumber(0);

            storeAnalysis.setFieldOrderAmount(BigDecimal.ZERO);
            storeAnalysis.setOnlineFieldOrderAmount(BigDecimal.ZERO);
            storeAnalysis.setVipFieldOrderAmount(BigDecimal.ZERO);
        }

        List<BasicRefundOrderVO> fieldRefundOrderList = orderRefundService.getDateRangeRefundOrder(storeId, start, end, SportConstant.FIELD_CATEGORY_CODE);
        if (CollectionUtils.isNotEmpty(fieldRefundOrderList)) {
            storeAnalysis.setRefundFieldOrderNumber(fieldRefundOrderList.size());
            storeAnalysis.setRefundFieldOrderAmount(BigDecimal.valueOf(fieldRefundOrderList.stream().map(BasicOrderVO::getRefundAmount).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
        } else {
            storeAnalysis.setRefundFieldOrderNumber(0);
            storeAnalysis.setRefundFieldOrderAmount(BigDecimal.ZERO);
        }
        // 实收金额
        storeAnalysis.setActualFieldOrderAmount(storeAnalysis.getFieldOrderAmount().subtract(storeAnalysis.getRefundFieldOrderAmount()));

        //订场使用统计
        BigDecimal fieldActiveAmount = BigDecimal.ZERO;
        BigDecimal fieldOnlineActiveAmount = BigDecimal.ZERO;
        BigDecimal fieldVipActiveAmount = BigDecimal.ZERO;
        // 查询订场 在指定时间范围内 已完成的订单
        IPage<BasicOrderVO> fieldActiveOrderPage = orderDetailService.getFieldOrderDetailRangeAndStatus(1, 1000000, storeId, start, end, null, OrderStatus.ORDER_FINISH.getCode());
        List<BasicOrderVO> fieldActiveOrderList = fieldActiveOrderPage.getRecords();
        if(CollectionUtils.isNotEmpty(fieldActiveOrderList)) {
            for (BasicOrderVO orderDetail : fieldActiveOrderList) {
                fieldActiveAmount = fieldActiveAmount.add(orderDetail.getConsumeAmount());
                if (orderDetail.getPayType().equals(PayType.Wechat.getCode())) {
                    // 在线支付
                    fieldOnlineActiveAmount = fieldOnlineActiveAmount.add(orderDetail.getActualAmount());
                } else if (orderDetail.getPayType().equals(PayType.MIX.getCode())) {
                    // 混合支付 实际金额就是在线支付的金额
                    fieldOnlineActiveAmount = fieldOnlineActiveAmount.add(orderDetail.getActualAmount());
                    // 消费金额 - 实际金额 = 会员卡支付的金额
                    fieldVipActiveAmount = fieldVipActiveAmount.add(orderDetail.getConsumeAmount().subtract(orderDetail.getActualAmount()));
                } else {
                    fieldVipActiveAmount = fieldVipActiveAmount.add(orderDetail.getConsumeAmount());
                }
            }
            storeAnalysis.setFieldActiveAmount(fieldActiveAmount);
            storeAnalysis.setFieldOnlineActiveAmount(fieldOnlineActiveAmount);
            storeAnalysis.setFieldVipActiveAmount(fieldVipActiveAmount);
        }
        storeAnalysis.setFieldActiveAmount(fieldActiveAmount);
        storeAnalysis.setFieldOnlineActiveAmount(fieldOnlineActiveAmount);
        storeAnalysis.setFieldVipActiveAmount(fieldVipActiveAmount);

        // 指定时间的已支付未激活的的订单
        IPage<BasicOrderVO> unActiveOrderPage = orderDetailService.getFieldOrderDetailRangeAndStatus(1, 1000000, storeId, start, end, null, OrderStatus.PAY.getCode());
        List<BasicOrderVO> unActiveOrderList = unActiveOrderPage.getRecords();
        BigDecimal unActiveAmount = BigDecimal.ZERO;
        BigDecimal unActiveOnlineAmount = BigDecimal.ZERO;
        BigDecimal unActiveVipActiveAmount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(unActiveOrderList)) {
            for (BasicOrderVO orderDetail : unActiveOrderList) {
                unActiveAmount = unActiveAmount.add(orderDetail.getConsumeAmount());
                if (orderDetail.getPayType().equals(PayType.Wechat.getCode())) {
                    // 在线支付
                    unActiveOnlineAmount = unActiveOnlineAmount.add(orderDetail.getConsumeAmount());
                } else if (orderDetail.getPayType().equals(PayType.MIX.getCode())) {
                    // 混合支付 实际金额就是在线支付的金额
                    unActiveVipActiveAmount = unActiveVipActiveAmount.add(orderDetail.getActualAmount());
                    // 消费金额 - 实际金额 = 会员卡支付的金额
                    unActiveVipActiveAmount = unActiveVipActiveAmount.add(orderDetail.getConsumeAmount().subtract(orderDetail.getActualAmount()));
                } else {
                    unActiveVipActiveAmount = unActiveVipActiveAmount.add(orderDetail.getConsumeAmount());
                }
            }
        }
        storeAnalysis.setUnActiveAmount(unActiveAmount);
        storeAnalysis.setUnActiveOnlineAmount(unActiveOnlineAmount);
        storeAnalysis.setUnActiveVipActiveAmount(unActiveVipActiveAmount);

        // 订场会员卡统计
        BigDecimal vipBackendAmount = BigDecimal.ZERO;
        if (StringUtils.isEmpty(coachUserId)) {
            List<UserBalanceChange> vipStatistics = userBalanceChangeService.findByVipStatistics(null, start, end, "后台操作");
            for (UserBalanceChange item : vipStatistics) {
                if (item.getChangeType().equals(ChangeTypeStatus.Recharge.getCode())) {
                    vipBackendAmount = vipBackendAmount.add(item.getAmount());
                } else if (item.getChangeType().equals(ChangeTypeStatus.Payment.getCode())) {
                    vipBackendAmount = vipBackendAmount.subtract(item.getAmount());
                }
            }
        }
        storeAnalysis.setVipBackendAmount(vipBackendAmount);
        // 获取公司对应的会员卡总余额
        storeAnalysis.setVipCardAmount(userVipCardService.getVipCardAmount(store.getCompanyId()));

        // 新签续费统计
        Map<String, StoreAnalysisItemVO> renewalMap = new LinkedHashMap<>();
        Map<String, Order> newSignatureMap = new HashMap<>();
        List<Integer> statusList = Arrays.asList(OrderStatus.USING.getCode(), OrderStatus.ORDER_FINISH.getCode(), OrderStatus.USED.getCode(), OrderStatus.PAY.getCode(), OrderStatus.REFUND.getCode());
        List<AnalysisOrderVO> renewalOrderList = orderService.getRenewalOrderList(start, end, storeId, statusList, coachUserId);
        for (AnalysisOrderVO orderVO : renewalOrderList) {
            if (StringUtils.isEmpty(orderVO.getCategoryName())) {
                continue;
            }
            if (!renewalMap.containsKey(orderVO.getCategoryName())) {
                renewalMap.put(orderVO.getCategoryName(), new StoreAnalysisItemVO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
            }
            StoreAnalysisItemVO itemVO = renewalMap.get(orderVO.getCategoryName());
            String orderKey = String.format("%s_%s", orderVO.getCategoryId(), orderVO.getUserId());
            if (!newSignatureMap.containsKey(orderKey)) {
                Order order = this.orderService.getNewSignatureOrder(storeId, orderVO.getCategoryId(), orderVO.getUserId(), statusList, start);
                newSignatureMap.put(orderKey, order);
                if (order == null) {
                    itemVO.setNewSignatureAmount(itemVO.getNewSignatureAmount().add(orderVO.getAmount()));
                    itemVO.getNewSignatureOrderIdList().add(orderVO.getOrderId());
                    continue;
                }
            }
            if (StringUtils.isEmpty(coachUserId) || StringUtils.isEmpty(orderVO.getSaleUserId())) {
                itemVO.setRenewalAmount(itemVO.getRenewalAmount().add(orderVO.getAmount()));
                itemVO.getRenewalOrderIdList().add(orderVO.getOrderId());
            } else {
                if (orderVO.getSaleUserId().equals(coachUserId)) {
                    itemVO.setRenewalAmount(itemVO.getRenewalAmount().add(orderVO.getAmount()));
                    itemVO.getRenewalOrderIdList().add(orderVO.getOrderId());
                } else {
                    itemVO.setPassiveRenewalAmount(itemVO.getPassiveRenewalAmount().add(orderVO.getAmount()));
                    itemVO.getPassiveRenewalOrderIdList().add(orderVO.getOrderId());
                }
            }
        }
        //各个场馆订场分类分别统计新签金额、续费金额、被动续费金额
        //新签金额：之前没有在该场馆订场
        //续费金额：之前在该场地订场过
        //被动续费金额: 0 (之前订单销售，与订单当前销售不一致)
        if(StringUtils.isBlank(coachUserId)){
            List<AnalysisOrderVO> storeVenueOrderList = analysisDao.getStoreVenueFieldOrderList(storeId, start, end, statusList);
            if (CollectionUtils.isNotEmpty(storeVenueOrderList)){
                Map<String, Order> newSignatureFieldMap = new HashMap<>();
                for (AnalysisOrderVO orderVO : storeVenueOrderList) {
                    String key = orderVO.getVenueName()+"订场";
                    if (!renewalMap.containsKey(key)) {
                        renewalMap.put(key, new StoreAnalysisItemVO(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                    }
                    StoreAnalysisItemVO itemVO = renewalMap.get(key);
                    String orderKey = String.format("%s_%s", orderVO.getVenueId(), orderVO.getUserId());
                    if (!newSignatureFieldMap.containsKey(orderKey)) {
                        Order order = this.analysisDao.getVenueNewSignatureFieldOrder(storeId, orderVO.getVenueId(), orderVO.getUserId(), start, statusList);
                        newSignatureFieldMap.put(orderKey, order);
                        if (order == null){
                            itemVO.setNewSignatureAmount(itemVO.getNewSignatureAmount().add(orderVO.getAmount()));
                            itemVO.getNewSignatureOrderIdList().add(orderVO.getOrderId());
                            continue;
                        }
                    }
                    itemVO.setRenewalAmount(itemVO.getRenewalAmount().add(orderVO.getAmount()));
                    itemVO.getRenewalOrderIdList().add(orderVO.getOrderId());
                }
            }
        }
        storeAnalysis.setRenewalMap(renewalMap);

        // 指定店铺 指定时间范围内 体验课数量
        storeAnalysis.setExperienceNumber(orderService.getExperienceNumber(start, end, storeId, coachUserId));

        // 店铺取消班级数量  一段时间内家长取消课或者老师取消课返还的课券数
        List<Grade> cancelGradeList = gradeService.getCancelGradeByRange(storeId, start, end, coachUserId);
        storeAnalysis.setStoreCancelCount(cancelGradeList.size());
        storeAnalysis.setStoreCancelAmount(new BigDecimal(0));
        if (CollectionUtils.isNotEmpty(cancelGradeList)) {
            // 取消班级数量返还课券金额：家长取消课和老师取消课返还的所有课程券金额累加（可查明细）
            List<UserCoupon> cancelGradeCouponList = gradeEnrollUserService.getUsedCouponListByGradeId(start, end, cancelGradeList.stream().map(Grade::getId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(cancelGradeCouponList)) {
                storeAnalysis.setStoreCancelAmount(BigDecimal.valueOf(cancelGradeCouponList.stream()
                        .filter(item -> item.getAmount() != null)
                        .map(UserCoupon::getAmount).mapToDouble(BigDecimal::doubleValue).sum()).setScale(2, RoundingMode.HALF_UP));
            }
        }

        List<UserCoupon> usedCouponList = null;
        if (StringUtils.isNotEmpty(coachUserId)) {
            List<StoreCoach> storeCoachList = storeCoachService.getByUserAndStoreId(coachUserId, storeId);
            if (CollectionUtils.isNotEmpty(storeCoachList)) {
                usedCouponList = new ArrayList<>();
                for (StoreCoach item : storeCoachList) {
                    List<UserCoupon> childList = null;
                    // 教练 核销优惠券  教练所属班级 对应核销的课券 进行计算
                    if (item.getUserType() == 1){
                        childList = gradeEnrollUserService.getCoachVerifyList(start, end, storeId, item.getId());
                    }
                    if (CollectionUtils.isNotEmpty(childList)) {
                        usedCouponList.addAll(childList);
                    }
                }
            }
        } else {
            usedCouponList = gradeEnrollUserService.getGradeUsedCouponList(start, end, storeId, coachUserId);
        }

        BigDecimal couponUsedAmount = new BigDecimal(0);
        BigDecimal couponUsedAndSignAmount = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(usedCouponList)) {
            Set<Long> signUserCouponIds = gradeSignEvaluateService.getSignUserCouponIds(usedCouponList.stream().map(UserCoupon::getId).collect(Collectors.toSet()));
            for (UserCoupon uc : usedCouponList) {
                couponUsedAmount = couponUsedAmount.add(uc.getAmount());
                if (signUserCouponIds != null && signUserCouponIds.contains(uc.getId())){
                    couponUsedAndSignAmount = couponUsedAndSignAmount.add(uc.getAmount());
                }
            }
            // 店铺课程 优惠券核销 数量
            storeAnalysis.setCouponUsedNumber(usedCouponList.size());
        } else {
            storeAnalysis.setCouponUsedNumber(0);
        }
        // 店铺课程优惠券核销对应的总金额
        storeAnalysis.setCouponUsedAmount(couponUsedAmount);
        storeAnalysis.setCouponUsedAndSignAmount(couponUsedAndSignAmount);

        if (StringUtils.isNotEmpty(coachUserId)) {
            storeAnalysis.setCouponUnUsedAmount(BigDecimal.ZERO);
            storeAnalysis.setUserCancelAmount(BigDecimal.ZERO);

            // 店铺指定范围已上课班级数量
            Set<Long> gradeIdSet = gradeService.getCoachFinishGradeId(coachUserId, storeId, start, end);
            if (CollectionUtils.isNotEmpty(gradeIdSet)) {
                storeAnalysis.setHasClassGradeNum(gradeIdSet.size());
                storeAnalysis.setHasSignUserGradeNum(gradeSignEvaluateService.getHasSignUserGradeNum(gradeIdSet));
                storeAnalysis.setGradeSignUserNum(gradeSignEvaluateService.getGradeSignUserNum(gradeIdSet));
                storeAnalysis.setGradeCommentUserNum(gradeSignEvaluateService.getGradeCommentUserNum(gradeIdSet));
                storeAnalysis.setGradeEnrollUserNum(gradeEnrollUserService.getGradeEnrollUserNum(gradeIdSet));
            }
            storeAnalysis.setUserCancelCount(0);
            // 教练/用户 取消的班级
            if (CollectionUtils.isNotEmpty(cancelGradeList)) {
                // 店铺用户取消约课数
                List<Long> gradeIdList = cancelGradeList.stream().map(Grade::getId).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(gradeIdList)) {
                    storeAnalysis.setUserCancelCount(analysisDao.getGradeCancelUserCountByGrade(gradeIdList));
                }
            }
        } else {
            BigDecimal couponUnUsedAmount = new BigDecimal(0);
            List<UserCoupon> storeUnUsedCouponList = gradeEnrollUserService.getStoreUnUsedCouponList(storeId);
            if (CollectionUtils.isNotEmpty(storeUnUsedCouponList)) {
                for (UserCoupon uc : storeUnUsedCouponList) {
                    couponUnUsedAmount = couponUnUsedAmount.add(uc.getAmount());
                }
                // 未核销课程券数：全部剩余课程券
                storeAnalysis.setCouponUnUsedNumber(storeUnUsedCouponList.size());
            } else {
                storeAnalysis.setCouponUnUsedNumber(0);
            }
            // 未核销课程券总金额：全部剩余课程券的总金额
            storeAnalysis.setCouponUnUsedAmount(couponUnUsedAmount);

            // 用户取消约课返还课券金额
            storeAnalysis.setUserCancelAmount(analysisDao.getUserCancelGradeCouponAmount(storeId, start, end));
            if (storeAnalysis.getUserCancelAmount() == null) {
                storeAnalysis.setUserCancelAmount(BigDecimal.ZERO);
            }
            // 店铺用户取消约课数
            storeAnalysis.setUserCancelCount(analysisDao.getUserCancelGradeCount(storeId, start, end));
            // 店铺指定范围已上课班级数量
            Set<Long> gradeIdSet = gradeService.getHasClassGradeNum(storeId, start, end);
            if (CollectionUtils.isNotEmpty(gradeIdSet)) {
                storeAnalysis.setHasClassGradeNum(gradeIdSet.size());
                storeAnalysis.setHasSignUserGradeNum(gradeSignEvaluateService.getHasSignUserGradeNum(gradeIdSet));
                storeAnalysis.setGradeSignUserNum(gradeSignEvaluateService.getGradeSignUserNum(gradeIdSet));
                storeAnalysis.setGradeCommentUserNum(gradeSignEvaluateService.getGradeCommentUserNum(gradeIdSet));
                storeAnalysis.setGradeEnrollUserNum(gradeEnrollUserService.getGradeEnrollUserNum(gradeIdSet));
            }
        }

        //教练满班率
        storeAnalysis.setFullGradeRate(fullGradeRate(storeId,start,end,coachUserId,false));
        //教练多人班级满班率
        storeAnalysis.setMultiUserFullGradeRate(fullGradeRate(storeId,start,end,coachUserId,true));

        IPage<NewUserInfoVO> userInfoVOIPage = userCallService.newUserInfoList(1, 10000, null, coachUserId, startStr, endStr, null, null);
        List<NewUserInfoVO> userInfoList = userInfoVOIPage.getRecords();
        if(!CollectionUtils.isEmpty(userInfoList)) {
            Integer newUserTotal = userInfoList.size();
            Integer experienceCount = 0;
            Integer completionCount = 0;
            Integer followCount = 0;
            for (NewUserInfoVO record : userInfoList) {
                if (record.getHasExperience().equals("Y")) {
                    experienceCount++;
                }
                if (record.getOrderInfoStatus() == 4) {
                    completionCount++;
                }
                if (record.getLastCall() != null && !record.getLastCall().equals("未联系")) {
                    followCount++;
                }
            }
            storeAnalysis.setNewUserTotal(newUserTotal);
            storeAnalysis.setExperienceRate(new BigDecimal(experienceCount).divide(new BigDecimal(newUserTotal), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            storeAnalysis.setCompletionRate(new BigDecimal(completionCount).divide(new BigDecimal(newUserTotal), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
            storeAnalysis.setFollowRate(new BigDecimal(followCount).divide(new BigDecimal(newUserTotal), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
        }


        List<AnalysisGradeFieldPlanDto> gradeUsedFieldTotal = analysisDao.getGradeUsedFieldTotal(storeId, start, end, coachUserId);
        Set<Long> grades = new HashSet<>();
        Set<Long> fieldPlanIdsNoShare = new HashSet<>();
        BigDecimal gradeHours = new BigDecimal(0);
        BigDecimal gradeTotalAmount = new BigDecimal(0);
        BigDecimal gradeFieldTotalAmount = new BigDecimal(0);
        BigDecimal gradeFieldHours = new BigDecimal(0);
        for (AnalysisGradeFieldPlanDto item : gradeUsedFieldTotal){
            if (!grades.contains(item.getGradeId())){
                grades.add(item.getGradeId());
                gradeHours = gradeHours.add(BigDecimal.valueOf((item.getGradeEndTime().getTime()-item.getGradeStartTime().getTime())/(1000.0*60*60)));
            }
            //10-11:30，算10-11+11-12/2费用
            if ((item.getGradeStartTime().before(item.getFieldPlanEndTime()) && item.getGradeStartTime().after(item.getFieldPlanStartTime()) ) ||
                    (item.getGradeEndTime().before(item.getFieldPlanEndTime()) && item.getGradeEndTime().after(item.getFieldPlanStartTime()) ) ){
                gradeTotalAmount = gradeTotalAmount.add(item.getCoachPrice().divide(BigDecimal.valueOf(2)));
            }else {
                gradeTotalAmount = gradeTotalAmount.add(item.getCoachPrice());
            }
            //共享场地不计算
            if (item.getSharedVenue() == 0 && !fieldPlanIdsNoShare.contains(item.getFieldPlanId())){
                fieldPlanIdsNoShare.add(item.getFieldPlanId());
                //兼容半小时订场、及0点
                String fieldPlanEndTime = DateFormatUtils.format(item.getFieldPlanEndTime(), "HH:mm:ss");
                if (fieldPlanEndTime.equals("00:00:00")){
                    item.getFieldPlanEndTime().setHours(24);
                }
                gradeFieldHours = gradeFieldHours.add(BigDecimal.valueOf((item.getFieldPlanEndTime().getTime()-item.getFieldPlanStartTime().getTime()) / (1000.0*60*60)));
                gradeFieldTotalAmount = gradeFieldTotalAmount.add(item.getCoachPrice());
            }
        }
        //班级总时长
        storeAnalysis.setGradeTotalHours(gradeHours.setScale(2, RoundingMode.HALF_UP));
        //班级场地总金额
        storeAnalysis.setGradeTotalAmount(gradeTotalAmount.setScale(2, RoundingMode.HALF_UP));
        //班级使用场地总时长
        storeAnalysis.setGradeUsedFieldTotalHours(gradeFieldHours.setScale(2, RoundingMode.HALF_UP));
        //班级使用场地总金额
        storeAnalysis.setGradeUsedFieldTotalAmount(gradeFieldTotalAmount.setScale(2, RoundingMode.HALF_UP));

        //手动核销的券
        Map<String, Object> maps = analysisDao.manualUsedCouponVerifyStatistics(storeId,start,end,coachUserId);
        storeAnalysis.setVerifyCouponNumber(maps.get("countNum") == null ? 0 : Integer.valueOf(maps.get("countNum").toString()));
        storeAnalysis.setVerifyCouponAmount(maps.get("countAmount") == null ? BigDecimal.ZERO : (BigDecimal) maps.get("countAmount"));
        return storeAnalysis;
    }

    @Override
    public List<StoreCheckCouponItemVO> getStoreCheckedCouponList(StoreAnalysisForm form) {
        try {
            if (form.getStoreId() == null) {
                throw new SportException("店铺id不能为空");
            }
            Date start = DateUtils.parseDate(form.getStart(), "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(form.getEnd(), "yyyy-MM-dd"));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date end = calendar.getTime();

            List<UserCoupon> usedCouponList = new ArrayList<>();
            if (StringUtils.isNotEmpty(form.getCoachUserId())) {
                List<StoreCoach> storeCoachList = storeCoachService.getByUserAndStoreId(form.getCoachUserId(), form.getStoreId());
                if (CollectionUtils.isNotEmpty(storeCoachList)) {
                    usedCouponList = new ArrayList<>();
                    for (StoreCoach item : storeCoachList) {
                        List<UserCoupon> childList = null;
                        // 教练 核销优惠券  教练所属班级 对应核销的课券 进行计算
                        if (item.getUserType() == 1){
                            childList = gradeEnrollUserService.getCoachVerifyList(start, end, form.getStoreId(), item.getId());
                        }
                        if (CollectionUtils.isNotEmpty(childList)) {
                            usedCouponList.addAll(childList);
                        }
                    }
                }
            } else {
                usedCouponList = gradeEnrollUserService.getGradeUsedCouponList(start, end, form.getStoreId(), form.getCoachUserId());
            }
            if (CollectionUtils.isEmpty(usedCouponList)) {
                return new ArrayList<>();
            }
            return analysisDao.getStoreCheckedCouponByOrder(usedCouponList.stream().map(UserCoupon::getId).collect(Collectors.toList()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AnalysisOrderVO> getOrderList(StoreAnalysisForm form) {
        try {
            Date start = DateUtils.parseDate(form.getStart(), "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(form.getEnd(), "yyyy-MM-dd"));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date end = calendar.getTime();
            return orderService.getAnalysisOrderList(start, end, form.getStoreId(), OrderStatus.USING.getCode(), form.getCoachUserId());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AnalysisOrderVO> getUnActiveOrderList(StoreAnalysisForm form) {
        try {
            Date start = DateUtils.parseDate(form.getStart(), "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(form.getEnd(), "yyyy-MM-dd"));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date end = calendar.getTime();
            return orderService.getAnalysisOrderList(start, end, form.getStoreId(), OrderStatus.PAY.getCode(), null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AnalysisCouponVO> getCancelGradeCoupon(StoreAnalysisForm form) {
        try {
            Date start = DateUtils.parseDate(form.getStart(), "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(form.getEnd(), "yyyy-MM-dd"));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Date end = calendar.getTime();

            List<Grade> cancelGradeList = gradeService.getCancelGradeByRange(form.getStoreId(), start, end, form.getCoachUserId());
            if (CollectionUtils.isNotEmpty(cancelGradeList)) {
                return gradeEnrollUserService.getAnalysisCancelGradeCoupon(start, end, cancelGradeList.stream().map(Grade::getId).collect(Collectors.toList()));
            } else {
                return new ArrayList<>();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getFullGradeRateList(Long storeId, String startStr, String endStr, String coachUserId, boolean multiUserGrade) {
        Date start = null;
        Date end = null;
        List<Integer> gradeStatus = new ArrayList<>();
        gradeStatus.add(GradeStatus.NORMAL.getCode());
        gradeStatus.add(GradeStatus.FINISH.getCode());
        try {
            start = DateUtils.parseDate(startStr, "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(endStr, "yyyy-MM-dd"));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            end = calendar.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return analysisDao.getFullGradeRateList(storeId,start,end,coachUserId,gradeStatus,multiUserGrade);
    }

    @Override
    public List<Map<String, Object>> getCountGradeLimitGroup(Long storeId, String startStr, String endStr, String coachUserId,boolean multiUserGrade) {
        Date start = null;
        Date end = null;
        List<Integer> gradeStatus = new ArrayList<>();
        gradeStatus.add(GradeStatus.NORMAL.getCode());
        gradeStatus.add(GradeStatus.FINISH.getCode());
        try {
            start = DateUtils.parseDate(startStr, "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.parseDate(endStr, "yyyy-MM-dd"));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            end = calendar.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return analysisDao.getCountGradeLimitGroup(storeId,start,end,coachUserId,gradeStatus, multiUserGrade);
    }

    /**
     * 获取教练满班率
     * @param storeId
     * @param start
     * @param end
     * @param coachUserId
     * @param multiUserGrade 是否多人班级，false统计所有班级，true只统计多人班级
     * @return
     */
    public BigDecimal fullGradeRate(Long storeId, Date start, Date end, String coachUserId,boolean multiUserGrade){
        List<Integer> gradeStatus = new ArrayList<>();
        gradeStatus.add(GradeStatus.NORMAL.getCode());
        gradeStatus.add(GradeStatus.FINISH.getCode());
        List<Map<String, Object>> fullGradeRateList = analysisDao.getFullGradeRateList(storeId, start, end, coachUserId, gradeStatus, multiUserGrade);
        if (CollectionUtils.isNotEmpty(fullGradeRateList)) {
            BigDecimal fullGradeRateCount = new BigDecimal(0);
            BigDecimal fullCount = new BigDecimal(0);
            for (int i = 0; i < fullGradeRateList.size(); i++) {
                Map<String, Object> objectMap = fullGradeRateList.get(i);
                BigDecimal rate = BigDecimal.valueOf(Double.parseDouble(objectMap.get("rate").toString()));
                BigDecimal rateCount = BigDecimal.valueOf(Long.parseLong(objectMap.get("rateCount").toString()));
                fullCount = fullCount.add(rateCount);
                fullGradeRateCount = fullGradeRateCount.add(rate.multiply(rateCount));
            }
            return (fullGradeRateCount.divide(fullCount, 2, RoundingMode.HALF_UP));
        }
        return BigDecimal.ZERO;
    }

    @Override
    public List<Map<String, Object>> storeChartUserCouponAnalysis(Long storeId) {
        return analysisDao.storeChartUserCouponAnalysis(storeId);
    }
}
