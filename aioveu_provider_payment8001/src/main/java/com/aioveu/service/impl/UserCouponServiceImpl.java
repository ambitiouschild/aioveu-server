package com.aioveu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.coupon.executor.ExecuteManager;
import com.aioveu.dao.UserCouponDao;
import com.aioveu.dto.UserAvailableCouponDTO;
import com.aioveu.dto.UserCouponDTO;
import com.aioveu.entity.*;
import com.aioveu.enums.CouponCategory;
import com.aioveu.enums.OrderStatus;
import com.aioveu.enums.PeriodType;
import com.aioveu.enums.UserCouponStatus;
import com.aioveu.exception.SportException;
import com.aioveu.form.CouponConvertForm;
import com.aioveu.form.OrderUserCouponForm;
import com.aioveu.form.SettlementForm;
import com.aioveu.qrcode.QrCodeGenWrapper;
import com.aioveu.service.*;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.utils.ListUtil;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description <h1>用户服务相关的接口实现</h1>
 * 所有的操作过程, 状态都保存在 Redis 中, 并通过 Kafka 把消息传递到 MySQL 中
 * 为什么使用 Kafka, 而不是直接使用 SpringBoot 中的异步处理 ?
 * @author: 雒世松
 * @date: 2025/2/1 0001 14:36
 */
@Slf4j
@Service
public class UserCouponServiceImpl extends ServiceImpl<UserCouponDao, UserCoupon> implements IUserCouponService {

    @Autowired
    private ITemplateBaseService templateBaseService;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseCouponService exerciseCouponService;

    @Autowired
    private ExecuteManager executeManager;

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CouponSharingService couponSharingService;

    @Autowired
    private UserService userService;

    @Autowired
    private GradeCouponTemplateService gradeCouponTemplateService;

    @Autowired
    private CouponVerifyService couponVerifyService;

    @Autowired
    private CouponChangeRecordService couponChangeRecordService;

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Autowired
    private StoreVenueService storeVenueService;

    @Override
    public List<UserCouponVO> useExpire(String userId) {
        List<UserCoupon> useCouponList = findUserCouponsByStatus(userId, UserCouponStatus.USED.getCode());
        List<UserCoupon> expireCouponList = findUserCouponsByStatus(userId, UserCouponStatus.EXPIRED.getCode());
        useCouponList.addAll(expireCouponList);
        List<UserCoupon> freezeCouponList = findUserCouponsByStatus(userId, UserCouponStatus.FREEZE.getCode());
        useCouponList.addAll(freezeCouponList);
        useCouponList = useCouponList.stream().sorted(Comparator.comparing(UserCoupon::getCreateDate)).collect(Collectors.toList());
        return userCoupon2UserCouponVO(useCouponList, true);
    }

    @Override
    public List<UserCoupon> findUserCouponsByStatus(String userId, Integer status) {
        return findUserCouponsByStatus(userId, null, status);
    }

    /**
     * <h2>根据用户 id 和状态查询优惠券记录</h2>
     *
     * @param userId 用户 id
     * @param status 优惠券状态
     * @return {@link UserCoupon}s
     */
    @Override
    public List<UserCoupon> findUserCouponsByStatus(String userId, Long companyId, Integer status) {
        List<UserCoupon> curCached = this.baseMapper.findUserCouponsByStatus(userId, companyId, status);

        if (CollectionUtils.isNotEmpty(curCached)) {
            // 补全TemplateSDK没值的情况
            // 填充 dbCoupons的 templateSDK 字段
            Map<Long, CouponTemplateSDK> id2TemplateSDK =
                    templateBaseService.findIds2TemplateSDK(
                            curCached.stream()
                                    .map(UserCoupon::getTemplateId)
                                    .collect(Collectors.toList())
                    );

            curCached.forEach(
                    dc -> {
                        if (!StringUtils.isEmpty(dc.getRuleStr())) {
                            dc.setRule(JSONObject.parseObject(dc.getRuleStr(), TemplateRule.class));
                        }
                        CouponTemplateSDK couponTemplateSDK = id2TemplateSDK.get(dc.getTemplateId());
                        if (dc.getRule() == null) {
                            dc.setRule(couponTemplateSDK.getRule());
                            dc.setTemplateSDK(couponTemplateSDK);
                        }
                        if (dc.getTemplateSDK() == null) {
                            dc.setTemplateSDK(new CouponTemplateSDK(dc.getTemplateId(),
                                    couponTemplateSDK.getName(),
                                    couponTemplateSDK.getLogo(),
                                    couponTemplateSDK.getDesc(),
                                    couponTemplateSDK.getCategory(),
                                    couponTemplateSDK.getProductLine(),
                                    couponTemplateSDK.getKey(),
                                    couponTemplateSDK.getTarget(),
                                    dc.getActivePrice(),
                                    dc.getRule(),
                                    couponTemplateSDK.getCompanyId(),
                                    couponTemplateSDK.getStoreId(),
                                    couponTemplateSDK.getProductId()));
                        }
                    }
            );
            // 如果当前获取的是可用优惠券, 还需要做对已过期优惠券的延迟处理
            if (UserCouponStatus.of(status) == UserCouponStatus.USABLE) {
                UserCouponClassify classify = UserCouponClassify.classify(curCached);
                // 如果已过期状态不为空, 需要做延迟处理
                if (CollectionUtils.isNotEmpty(classify.getExpired())) {
                    log.info("用户{}优惠券过期{}", userId, classify.getExpired().stream().map(UserCoupon::getId).collect(Collectors.toList()));
                    updateBatchById(classify.getExpired().stream().map(item -> {
                        UserCoupon uc = new UserCoupon();
                        uc.setId(item.getId());
                        uc.setStatus(UserCouponStatus.EXPIRED);
                        return uc;
                    }).collect(Collectors.toList()));
                    //TODO 发送到 kafka 中做异步处理
//                    CouponKafkaMessage couponKafkaMessage = new CouponKafkaMessage(
//                            UserCouponStatus.EXPIRED.getCode(),
//                            classify.getExpired().stream()
//                                    .map(UserCoupon::getId)
//                                    .collect(Collectors.toList()));
//                    kafkaService.changeCouponStatus2Database(couponKafkaMessage);
//                kafkaTemplate.send(
//                        Constant.TOPIC,
//                        JSON.toJSONString(couponKafkaMessage)
//                );
                }
                return classify.getUsable();
            }
        }
        return curCached;
    }

    /**
     * <h2>根据用户 id 查找当前可以领取的优惠券模板</h2>
     *
     * @param userId 用户 id
     * @return {@link CouponTemplateSDK}s
     */
    @Override
    public List<CouponTemplateSDK> findAvailableTemplate(String userId) {
        long curTime = System.currentTimeMillis();
        List<CouponTemplateSDK> templateSDKS =
                templateBaseService.findAllUsableTemplate();

        log.debug("Find All Template(From TemplateClient) Count: {}",
                templateSDKS.size());

        // 过滤过期的优惠券模板
        templateSDKS = templateSDKS.stream().filter(
                t -> t.getRule().getExpiration().getDeadline() > curTime
        ).collect(Collectors.toList());

        log.info("Find Usable Template Count: {}", templateSDKS.size());

        // key 是 TemplateId
        // value 中的 left 是 Template limitation, right 是优惠券模板
        Map<Long, Pair<Integer, CouponTemplateSDK>> limit2Template =
                new HashMap<>(templateSDKS.size());

        templateSDKS.forEach(
                t -> limit2Template.put(
                        t.getId(),
                        Pair.of(t.getRule().getLimitation(), t)
                )
        );

        List<CouponTemplateSDK> result =
                new ArrayList<>(limit2Template.size());

        List<UserCoupon> userUsableCoupons = findUserCouponsByStatus(
                userId, UserCouponStatus.USABLE.getCode()
        );

        log.debug("Current User Has Usable Coupons: {}, {}", userId,
                userUsableCoupons.size());

        // key 是 TemplateId
        Map<Long, List<UserCoupon>> templateId2Coupons = userUsableCoupons
                .stream()
                .collect(Collectors.groupingBy(UserCoupon::getTemplateId));

        // 根据 Template 的 Rule 判断是否可以领取优惠券模板
        limit2Template.forEach((k, v) -> {

            int limitation = v.getLeft();
            CouponTemplateSDK templateSDK = v.getRight();

            if (templateId2Coupons.containsKey(k)
                    && templateId2Coupons.get(k).size() >= limitation) {
                return;
            }

            result.add(templateSDK);

        });

        return result;
    }

    /**
     * 针对指定状态的优惠券进行校验
     *
     * @param userId
     * @param templateId
     * @param limit      每个人领取数量限制
     */
    private void checkCouponLimitByStatus(String userId, Long templateId, Integer limit) {
        // 获取用户可用的优惠券
        List<UserCoupon> userUsableUserCoupons = findUserCouponsByStatus(userId, UserCouponStatus.USABLE.getCode());
        // 获取用户已使用的优惠券
        List<UserCoupon> userUsedUserCoupons = findUserCouponsByStatus(userId, UserCouponStatus.USED.getCode());
        userUsableUserCoupons.addAll(userUsedUserCoupons);
        Map<Long, List<UserCoupon>> templateId2UserCoupons = userUsableUserCoupons
                .stream()
                .collect(Collectors.groupingBy(UserCoupon::getTemplateId));

        if (limit > 0 && templateId2UserCoupons.containsKey(templateId) && templateId2UserCoupons.get(templateId).size() >= limit) {
            log.error("用户:{}, 优惠券模板: {}, 领取限制:{}, 不能再领取优惠券", userId,
                    templateId, limit);
            throw new SportException("每个人只能领取" + limit + "张, 您已领过了哦!");
        }
    }

    /**
     * <h2>用户领取优惠券</h2>
     * 1. 从 TemplateClient 拿到对应的优惠券, 并检查是否过期
     * 2. 根据 limitation 判断用户是否可以领取
     * 3. save to db
     * 4. 填充 CouponTemplateSDK
     * 5. save to cache
     *
     * @param request {@link AcquireTemplateRequest}
     * @return {@link UserCoupon}
     */
    @Override
    public List<UserCoupon> acquireTemplate(AcquireTemplateRequest request) {

        Map<Long, CouponTemplateSDK> id2Template =
                templateBaseService.findIds2TemplateSDK(Collections.singletonList(request.getTemplateSDK().getId()));
        // 优惠券模板是需要存在的
        if (id2Template.size() <= 0) {
            log.error("Can Not Acquire Template From TemplateClient: {}",
                    request.getTemplateSDK().getId());
            throw new SportException("Can Not Acquire Template From TemplateClient");
        }

        if (request.isLimit()) {
            // 用户是否可以领取这张优惠券
            checkCouponLimitByStatus(request.getUserId(), request.getTemplateSDK().getId(), request.getTemplateSDK().getRule().getLimitation());
        }

        // 优惠券领取数量
        Integer orderLimit = request.getTemplateSDK().getRule().getOrderLimit();
        if (orderLimit == null || orderLimit == 0) {
            orderLimit = 1;
        }
        List<UserCoupon> userCouponList = new ArrayList<>();
        for (int i = 0; i < orderLimit; i++) {
            userCouponList.add(acquireOneCoupon(request));
        }
        // 保存到db
        saveBatch(userCouponList);
        return userCouponList;
    }

    /**
     * 创建一个优惠券对象
     *
     * @param request
     * @return
     */
    private UserCoupon acquireOneCoupon(AcquireTemplateRequest request) {
        // 尝试去获取优惠券码
        String couponCode = templateBaseService.getRandomCouponCode(request.getTemplateSDK().getId());
        if (StringUtils.isEmpty(couponCode)) {
            log.error("优惠券领取完毕: {}", request.getTemplateSDK().getId());
            throw new SportException("您来晚了, 优惠券已领完了!");
        }

        UserCouponStatus userCouponStatus;
        if (request.getTemplateSDK().getActivePrice().doubleValue() > 0) {
            userCouponStatus = UserCouponStatus.UN_ACTIVE;
        } else {
            userCouponStatus = UserCouponStatus.USABLE;
        }

        UserCoupon newUserCoupon = new UserCoupon(
                request.getTemplateSDK().getId(), request.getUserId(),
                couponCode, userCouponStatus
        );
        newUserCoupon.setRule(request.getTemplateSDK().getRule());
        newUserCoupon.setCreateDate(new Date());
        if (request.getOrderId() != null) {
            newUserCoupon.setOrderId(request.getOrderId());
        }
        newUserCoupon.setAmount(request.getSingleCouponAmount());
        return newUserCoupon;
    }

    @Override
    public SettlementResultVO preSettlement(SettlementForm settlementForm) {
        SettlementInfo info = new SettlementInfo();
        BeanUtils.copyProperties(settlementForm, info);
        info.setEmploy(false);
        List<Long> couponIds = settlementForm.getCouponIds();
        if (CollectionUtils.isNotEmpty(couponIds)) {
            List<SettlementInfo.CouponAndTemplateInfo> couponAndTemplateInfos = couponIds.stream().map(id -> {
                SettlementInfo.CouponAndTemplateInfo ct = new SettlementInfo.CouponAndTemplateInfo();
                ct.setId(id);
                UserCoupon coupon = getById(id);
                ct.setTemplate(templateBaseService.getCouponTemplateSdk(coupon.getTemplateId(), null, null));
                return ct;
            }).collect(Collectors.toList());
            info.setCouponAndTemplateInfos(couponAndTemplateInfos);
        }
        info = settlement(info);
        SettlementResultVO settlementResultVO = new SettlementResultVO();
        BeanUtils.copyProperties(info, settlementResultVO);
        return settlementResultVO;
    }

    /**
     * <h2>结算(核销)优惠券</h2>
     * 这里需要注意, 规则相关处理需要由 Settlement 系统去做, 当前系统仅仅做
     * 业务处理过程(校验过程)
     *
     * @param info {@link SettlementInfo}
     * @return {@link SettlementInfo}
     */
    @Override
    public SettlementInfo settlement(SettlementInfo info) {
        double goodsSum = 0.0;
        for (GoodsInfo gi : info.getGoodsInfos()) {
            goodsSum += gi.getPrice() * gi.getCount();
        }
        info.setTotalCost(BigDecimal.valueOf(retain2Decimals(goodsSum)));

        // 当没有传递优惠券时, 直接返回商品总价
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos =
                info.getCouponAndTemplateInfos();
        if (CollectionUtils.isEmpty(ctInfos)) {
            log.info("Empty Coupons For Settle.");
            // 没有优惠券也就不存在优惠券的核销, SettlementInfo 其他的字段不需要修改
            info.setCost(info.getTotalCost().doubleValue());
            info.setCouponCost(BigDecimal.ZERO.doubleValue());
            return info;
        }

        // 校验传递的优惠券是否是用户自己的
        List<UserCoupon> coupons = findUserCouponsByStatus(
                info.getUserId(), UserCouponStatus.USABLE.getCode()
        );
        Map<Long, UserCoupon> id2UserCoupon = coupons.stream()
                .collect(Collectors.toMap(
                        UserCoupon::getId,
                        Function.identity()
                ));
        if (MapUtils.isEmpty(id2UserCoupon) || !CollectionUtils.isSubCollection(
                ctInfos.stream().map(SettlementInfo.CouponAndTemplateInfo::getId)
                        .collect(Collectors.toList()), id2UserCoupon.keySet()
        )) {
            // 用户有的优惠券 1585, 1586
            log.info("{}", id2UserCoupon.keySet());
            // 用户下单选择的优惠券 1587
            log.info("{}", ctInfos.stream()
                    .map(SettlementInfo.CouponAndTemplateInfo::getId)
                    .collect(Collectors.toList()));
            log.error("User Coupon Has Some Problem, It Is Not SubCollection" +
                    "Of Coupons!");
            throw new SportException("此优惠券无效!");
        }

        log.debug("Current Settlement Coupons Is User's: {}", ctInfos.size());

        List<UserCoupon> settleUserCoupons = new ArrayList<>(ctInfos.size());
        ctInfos.forEach(ci -> settleUserCoupons.add(id2UserCoupon.get(ci.getId())));

        SettlementInfo processedInfo = executeManager.computeRule(info);
//                settlementClient.computeRule(info).getData();
        if (processedInfo.getEmploy() && CollectionUtils.isNotEmpty(
                processedInfo.getCouponAndTemplateInfos()
        )) {
            log.info("Settle User Coupon: {}, {}", info.getUserId(),
                    JSON.toJSONString(settleUserCoupons));
            //更新 db 优惠券信息
            CouponKafkaMessage couponKafkaMessage = new CouponKafkaMessage(
                    UserCouponStatus.USED.getCode(),
                    settleUserCoupons.stream()
                            .map(UserCoupon::getId)
                            .collect(Collectors.toList()));
            kafkaService.changeCouponStatus2Database(couponKafkaMessage);

//            kafkaTemplate.send(
//                    Constant.TOPIC,
//                    JSON.toJSONString(couponKafkaMessage)
//            );
        }

        return processedInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<Long> acquireByCouponTemplateId(String userId, Long couponTemplateId, String orderId, BigDecimal singleCouponAmount,
                                                Integer orderLimit, TemplateRule.Expiration expiration, boolean limit) {
        AcquireTemplateRequest acquireTemplateRequest = new AcquireTemplateRequest();
        acquireTemplateRequest.setUserId(userId);
        acquireTemplateRequest.setOrderId(orderId);
        acquireTemplateRequest.setSingleCouponAmount(singleCouponAmount);
        acquireTemplateRequest.setLimit(limit);
        // 重置优惠券本身的领取数量和有效期规则
        acquireTemplateRequest.setTemplateSDK(templateBaseService.getCouponTemplateSdk(couponTemplateId, orderLimit, expiration));
        return acquireTemplate(acquireTemplateRequest).stream().map(UserCoupon::getId).collect(Collectors.toList());
    }

    /**
     * <h2>保留两位小数</h2>
     */
    private double retain2Decimals(double value) {

        // BigDecimal.ROUND_HALF_UP 代表四舍五入
        return new BigDecimal(value)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    @Override
    public List<UserCouponVO> findByStatus(String userId, Long companyId, Integer status, Long categoryId, Long productId, Integer productLineId) {
        List<UserCoupon> userCouponList = findUserCouponsByStatus(userId, companyId, status);
        if (productLineId != null) {
            userCouponList = userCouponList.stream().filter(t -> {
                return t.getTemplateSDK() != null && t.getTemplateSDK().getProductLine().equals(productLineId);
            }).collect(Collectors.toList());
        }
        if (categoryId != null && productId != null) {
            if (productLineId == 3) {
                StoreVenue storeVenue = this.storeVenueService.getById(productId);
                userCouponList = userCouponList.stream().filter(item -> {
                    CouponTemplateSDK couponTemplateSDK = item.getTemplateSDK();
                    if (couponTemplateSDK != null) {
                        // 排除掉兑换券
                        if (!CouponCategory.DUIHUAN.getCode().equals(couponTemplateSDK.getCategory())) {
                            if (couponTemplateSDK.getStoreId() != null && couponTemplateSDK.getStoreId() > 0) {
                                return couponTemplateSDK.getStoreId().equals(storeVenue.getStoreId());
                            } else if (couponTemplateSDK.getCompanyId() != null && couponTemplateSDK.getCompanyId() > 0) {
                                return couponTemplateSDK.getCompanyId().equals(storeVenue.getCompanyId());
                            } else {
                                return true;
                            }
                        }
                    }
                    return false;
                }).collect(Collectors.toList());
            } else {
                ExerciseManagerDetailVO ed = exerciseService.managerDetail(productId);
                userCouponList = userCouponList.stream().filter(item -> {
                    CouponTemplateSDK couponTemplateSDK = item.getTemplateSDK();
                    if (couponTemplateSDK != null) {
                        //卡券：当 分类=优惠券，即productLine=2时，产品字段为空或等于入参productId时，才返回该卡券
                        //产品：=空，表示不限制产品使用
                        //      有值，表示指定产品才可使用
                        //产品后续升级为多个，用逗号隔开，判断等式逻辑需调整即可满足
                        if (couponTemplateSDK.getProductLine() == 2 && productId != null){
                            String couponProductId = couponTemplateSDK.getProductId();
                            boolean canUse = (StringUtils.isEmpty(couponProductId) || productId.toString().equals(couponProductId));
                            if (!canUse){
                                return false;
                            }
                        }
                        // 排除掉兑换券
                        if (!CouponCategory.DUIHUAN.getCode().equals(couponTemplateSDK.getCategory())) {
                            if (couponTemplateSDK.getStoreId() != null && couponTemplateSDK.getStoreId() > 0) {
                                return couponTemplateSDK.getStoreId().equals(ed.getStoreId());
                            } else if (couponTemplateSDK.getCompanyId() != null && couponTemplateSDK.getCompanyId() > 0) {
                                return couponTemplateSDK.getCompanyId().equals(ed.getCompanyId());
                            } else {
                                return true;
                            }
                        }
                    }
                    return false;
                }).collect(Collectors.toList());
            }
        }
        if (CollectionUtils.isEmpty(userCouponList)) {
            return null;
        }

        userCouponList = userCouponList.stream().sorted(Comparator.comparing(UserCoupon::getCreateDate)).collect(Collectors.toList());
        return userCoupon2UserCouponVO(userCouponList, false);
    }

    private List<UserCouponVO> userCoupon2UserCouponVO(List<UserCoupon> userCouponList, boolean useExpire) {
        return userCouponList.stream().map(item -> {
            UserCouponVO userCouponVO = new UserCouponVO();
            userCouponVO.setId(item.getId());
            userCouponVO.setActivePrice(item.getActivePrice());
            userCouponVO.setName(item.getTemplateSDK().getName());
            userCouponVO.setTemplateId(item.getTemplateId());
            userCouponVO.setCategory(CouponCategory.of(item.getTemplateSDK().getCategory()).getDescription());
            userCouponVO.setStatus(item.getStatus().getCode());
            userCouponVO.setAmount(item.getAmount());
            userCouponVO.setVipPriceCanUse(item.getVipPriceCanUse());

            Date start = item.getCreateDate();
            Date end = null;
            if (item.getTemplateSDK().getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                if (item.getTemplateSDK().getRule().getExpiration().getDeadline() != null) {
                    end = new Date(item.getTemplateSDK().getRule().getExpiration().getDeadline());
                }
            } else if (item.getTemplateSDK().getRule().getExpiration().getGap() != null) {
                end = DateUtils.addDays(start, item.getTemplateSDK().getRule().getExpiration().getGap());
            }
            if (end != null) {
                userCouponVO.setTime(DateFormatUtils.format(start, "yyyy-MM-dd") + " - " +
                        DateFormatUtils.format(end, "yyyy-MM-dd"));
            } else {
                userCouponVO.setTime("永久");
            }

            userCouponVO.setRemark("");
            if (!useExpire && userCouponVO.getCategory().equals(CouponCategory.DUIHUAN.getDescription())) {
                userCouponVO.setRemark("点击进行核销");
            }
            return userCouponVO;
        }).collect(Collectors.toList());
    }

    @Override
    public UserCoupon findUsableCoupon(String code) {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("coupon_code", code)
                .eq("status", UserCouponStatus.USABLE.getCode());

        UserCoupon userCoupon = getOne(queryWrapper);
        if (userCoupon == null) {
            throw new SportException("不存在的优惠券码");
        }
        CouponTemplateSDK couponTemplateSDK = templateBaseService.getCouponTemplateSdk(userCoupon.getTemplateId(), null, null);
        if (couponTemplateSDK == null) {
            throw new SportException("优惠券不存在或已下架");
        }
        userCoupon.setTemplateSDK(couponTemplateSDK);
        return userCoupon;
    }

    @Override
    public UserCouponDetailVO detail(Long id) {
        UserCoupon userCoupon = getById(id);
        UserCouponDetailVO userCouponDetailVO = new UserCouponDetailVO();
        userCouponDetailVO.setId(id);
        try {
            TemplateRule.Expiration expiration = null;
            if (userCoupon.getRule() != null) {
                expiration = userCoupon.getRule().getExpiration();
            }
            CouponTemplateSDK couponTemplateSDK = templateBaseService.getCouponTemplateSdk(userCoupon.getTemplateId(), null, expiration);
            if (couponTemplateSDK != null) {
                Date start = userCoupon.getCreateDate();
                Date end;
                if (couponTemplateSDK.getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                    end = new Date(couponTemplateSDK.getRule().getExpiration().getDeadline());
                } else {
                    end = DateUtils.addDays(start, couponTemplateSDK.getRule().getExpiration().getGap());
                }
                userCouponDetailVO.setTime(DateFormatUtils.format(start, "yyyy-MM-dd") + " - " +
                        DateFormatUtils.format(end, "yyyy-MM-dd"));
                userCouponDetailVO.setName(couponTemplateSDK.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            userCouponDetailVO.setCode(getWeMaQrCode(userCoupon.getCouponCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        userCouponDetailVO.setCodeNumber(userCoupon.getCouponCode());
        return userCouponDetailVO;
    }

    private String getWeMaQrCode(String consumeCode) throws Exception {
        String scanCode = "https://manager.highyundong.com/code?code=" + consumeCode;
        String scanQrCode = QrCodeGenWrapper.of(scanCode).asString();
        return "data:image/png;base64," + scanQrCode;
    }

    @Override
    public boolean syncDatabase2Redis() {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", UserCouponStatus.USABLE.getCode());
        List<UserCoupon> userCouponList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userCouponList)) {
            Map<String, List<UserCoupon>> userCouponMap = userCouponList.stream().collect(Collectors.groupingBy(UserCoupon::getUserId));
            for (Map.Entry<String, List<UserCoupon>> entry : userCouponMap.entrySet()) {
                log.info("userId:{}", entry.getKey());

                List<UserCoupon> userAllUsableList = entry.getValue();
                List<UserCoupon> saveList = new ArrayList<>(userAllUsableList.size());
//                List<UserCoupon> usableList = couponRedisService.getCachedUserCoupons(entry.getKey(), UserCouponStatus.USABLE.getCode());
//                if (CollectionUtils.isNotEmpty(usableList)) {
//                    for (UserCoupon item : userAllUsableList) {
//                        boolean hasSave = false;
//                        for (UserCoupon uc : usableList) {
//                            if (uc.getId().equals(item.getId())) {
//                                hasSave = true;
//                                break;
//                            }
//                        }
//                        if (!hasSave) {
//                            saveList.add(item);
//                        }
//                    }
//                } else {
//                    saveList = userAllUsableList;
//                }
                if (CollectionUtils.isNotEmpty(saveList)) {
                    Map<Long, List<UserCoupon>> userTemplateMap = saveList.stream().collect(Collectors.groupingBy(UserCoupon::getTemplateId));
                    for (Map.Entry<Long, List<UserCoupon>> userTemplateEntry : userTemplateMap.entrySet()) {
                        List<UserCoupon> templateSaveList = userTemplateEntry.getValue();
                        CouponTemplateSDK couponTemplateSDK = templateBaseService.getCouponTemplateSdk(userTemplateEntry.getKey(), null, null);
                        for (UserCoupon item : templateSaveList) {
                            item.setTemplateSDK(couponTemplateSDK);
                        }
//                        int expireTime = (int) ((couponTemplateSDK.getRule().getExpiration().getDeadline() - System.currentTimeMillis()) / 1000 / 3600);
//                        log.info("更新缓存:{}, 时间:{}", templateSaveList.size(), expireTime);
//                        if (expireTime < 0) {
//                            log.info("模板:{}", userTemplateEntry.getKey());
//                        }
                        // 放入缓存中
//                        couponRedisService.addUserCouponToCache(
//                                entry.getKey(),
//                                templateSaveList,
//                                UserCouponStatus.USABLE.getCode());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean couponBack(Long userCouponId, Long gradeId, Integer couponExtendDays) {
        UserCoupon userCoupon = getById(userCouponId);
        if (userCoupon.getStatus().equals(UserCouponStatus.USED) || userCoupon.getStatus().equals(UserCouponStatus.TEMP_USED) || userCoupon.getStatus().equals(UserCouponStatus.EXPIRED)) {
            UserCoupon uc = new UserCoupon();
            uc.setId(userCouponId);
            uc.setUpdateDate(new Date());
            uc.setStatus(UserCouponStatus.USABLE);

            if (couponExtendDays != null && couponExtendDays > 0 && userCoupon.getRule() != null) {
                TemplateRule.Expiration expiration = userCoupon.getRule().getExpiration();
                if (expiration.getPeriod() == 1) {
                    Date date = expiration.getDeadline() != null ? new Date(expiration.getDeadline()) : new Date();
                    expiration.setDeadline(DateUtils.addDays(date, couponExtendDays).getTime());
                    uc.setRule(userCoupon.getRule());
                } else if (expiration.getPeriod() == 2) {
                    expiration.setGap((expiration.getGap() == null ? 0 : expiration.getGap()) + couponExtendDays);
                    uc.setRule(userCoupon.getRule());
                }
            }
            String remark = "优惠券退回";
            if (userCoupon.getStatus().equals(UserCouponStatus.EXPIRED)) {
                remark = "过期券恢复";
            }
            couponChangeRecordService.create(userCoupon.getUserId(), userCouponId, remark, gradeId);
            return updateById(uc);
        }
        return false;
    }

    @Override
    public boolean couponBack(Long userCouponId) {
        return couponBack(userCouponId, null, 0);
    }

    @Override
    public boolean syncMySql2Redis(String userId) {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCoupon::getUserId, userId);
        List<UserCoupon> userCouponList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userCouponList)) {
//            String usableKey = couponRedisService.status2RedisKey(UserCouponStatus.USABLE.getCode(), userId);
//            String unActiveKey = couponRedisService.status2RedisKey(UserCouponStatus.UN_ACTIVE.getCode(), userId);
//            String usedKey = couponRedisService.status2RedisKey(UserCouponStatus.USED.getCode(), userId);
//            String expiredKey = couponRedisService.status2RedisKey(UserCouponStatus.EXPIRED.getCode(), userId);
            // 删除数据库中的缓存数据
//            redisTemplate.delete(Arrays.asList(usableKey, unActiveKey, usedKey, expiredKey));

            List<UserCoupon> usableList = new ArrayList<>();
            List<UserCoupon> unActiveList = new ArrayList<>();
            List<UserCoupon> usedList = new ArrayList<>();
            List<UserCoupon> expiredList = new ArrayList<>();
            for (UserCoupon uc : userCouponList) {
                uc.setTemplateSDK(templateBaseService.getCouponTemplateSdk(uc.getTemplateId(), null, null));
                if (uc.getStatus().equals(UserCouponStatus.USED)) {
                    usedList.add(uc);
                } else if (uc.getStatus().equals(UserCouponStatus.USABLE)) {
                    usableList.add(uc);
                } else if (uc.getStatus().equals(UserCouponStatus.UN_ACTIVE)) {
                    unActiveList.add(uc);
                } else if (uc.getStatus().equals(UserCouponStatus.EXPIRED)) {
                    expiredList.add(uc);
                }
            }
//            if (CollectionUtils.isNotEmpty(usableList)) {
//                log.info("用户:{}, 同步可用优惠券:{}", userId, usableList.size());
//                Map<String, String> usableMap = getCouponHashMap(usableList);
//                redisTemplate.opsForHash().putAll(usableKey, usableMap);
//            }
//            if (CollectionUtils.isNotEmpty(unActiveList)) {
//                log.info("用户:{}, 同步未激活优惠券:{}", userId, unActiveList.size());
//                redisTemplate.opsForHash().putAll(unActiveKey, getCouponHashMap(unActiveList));
//            }
//            if (CollectionUtils.isNotEmpty(usedList)) {
//                log.info("用户:{}, 同步已使用优惠券:{}", userId, usedList.size());
//                redisTemplate.opsForHash().putAll(usedKey, getCouponHashMap(usedList));
//            }
//            if (CollectionUtils.isNotEmpty(expiredList)) {
//                log.info("用户:{}, 同步过期优惠券:{}", userId, expiredList.size());
//                redisTemplate.opsForHash().putAll(expiredKey, getCouponHashMap(expiredList));
//            }
        }
        return true;
    }

    private Map<String, String> getCouponHashMap(List<UserCoupon> couponList) {
        Map<String, String> map = new HashMap<>(couponList.size());
        couponList.forEach(c -> map.put(
                c.getId().toString(),
                JSON.toJSONString(c)
        ));
        return map;
    }

    @Override
    public List<UserCoupon> getByOrderIds(List<String> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return new ArrayList<>();
        }
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(UserCoupon::getOrderId, orderIds);
        return list(queryWrapper);
    }


    @Override
    public List<UserCoupon> getByUserIdAndTemplateId(String userId, List<String> templateIds) {
        if (StringUtils.isEmpty(userId)) {
            return new ArrayList<>();
        }
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCoupon::getUserId, userId)
                .in(UserCoupon::getTemplateId, templateIds)
                .eq(UserCoupon::getStatus, 1);
        return list(queryWrapper);
    }

    @Override
    public boolean claimCoupons(Long id, String username) {
        String userId = userService.getUserIdFromCache(username);
        CouponTemplate couponTemplate = this.couponTemplateService.getById(id);
        checkCouponLimitByStatus(userId, id, couponTemplate.getRule().getLimitation());
        String couponCode = templateBaseService.getRandomCouponCode(id);
        if (StringUtils.isEmpty(couponCode)) {
            log.error("优惠券领取完毕: {}", id);
            throw new SportException("您来晚了, 优惠券已领完了!");
        }
        String shareCouponCode = this.couponTemplateService.getShareCouponCode(id, userId);
        this.receiveShareCouponByCode(username, shareCouponCode);
        return true;
    }

    /**
     * 设置数据库的优惠券状态为已使用
     *
     * @param userCouponIds
     * @return
     */
    private boolean toUsed(List<Long> userCouponIds) {
        return change2Status(userCouponIds, UserCouponStatus.USED);
    }


    /**
     * 设置优惠券为对应状态
     * @param userCouponIds
     * @param status
     * @return
     */
    @Override
    public boolean change2Status(List<Long> userCouponIds, UserCouponStatus status) {
        List<UserCoupon> userCouponList = userCouponIds.stream().map(item -> {
            UserCoupon uc = new UserCoupon();
            uc.setId(item);
            uc.setStatus(status);
            return uc;
        }).collect(Collectors.toList());
        return updateBatchById(userCouponList);
    }

    @Override
    public boolean gradeCouponAllUsed(Long gradeId) {
        // 查找班级绑定的优惠券模板id
        List<GradeCouponTemplate> couponTemplates = gradeCouponTemplateService.getCouponTemplateIdListByGradeId(gradeId);
        List<Long> couponTemplateIds = couponTemplates.stream().map(GradeCouponTemplate::getCouponTemplateId).collect(Collectors.toList());
        // 查询客户对应优惠券
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCoupon::getTemplateId, couponTemplateIds)
                .isNotNull(UserCoupon::getOrderId);
        List<UserCoupon> userCouponList = list(queryWrapper);

        Map<String, List<UserCoupon>> ucMap = userCouponList.stream().collect(Collectors.groupingBy(UserCoupon::getOrderId));

        for (Map.Entry<String, List<UserCoupon>> entry : ucMap.entrySet()) {
            if (entry.getValue().stream().anyMatch(item -> item.getStatus().equals(UserCouponStatus.USABLE))) {
                log.info("订单:{}优惠券未使用完毕", entry.getKey());
                break;
            }
            log.warn("订单:{}优惠券使用完毕", entry.getKey());
            Order update = new Order();
            update.setId(entry.getKey());
            update.setStatus(OrderStatus.ORDER_FINISH.getCode());
            orderService.updateById(update);
        }
        return false;
    }

    @Override
    public void testCouponSort(String userId, Integer templateNum, Long gradeId) {
        List<GradeCouponTemplate> couponTemplates = gradeCouponTemplateService.getCouponTemplateIdListByGradeId(gradeId);
        List<Long> couponTemplateIds = couponTemplates.stream().map(GradeCouponTemplate::getCouponTemplateId).collect(Collectors.toList());
        // 查询课程对应的用户优惠券
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(UserCoupon::getTemplateId, couponTemplateIds)
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getStatus, UserCouponStatus.USABLE);
        List<UserCoupon> uCList = list(queryWrapper);
        if (CollectionUtils.isEmpty(uCList)) {
            log.error("用户:{}没有可用的优惠券", userId);
            throw new SportException("没有可用的核销券!");
        }

        Map<Long, Integer> couponTemplateHour = new HashMap<>();
        for (GradeCouponTemplate gt : couponTemplates) {
            couponTemplateHour.put(gt.getCouponTemplateId(), gt.getClassHour());
        }
        Map<Long, CouponTemplateSDK> ids2TemplateSDK = templateBaseService.findIds2TemplateSDK(couponTemplateIds);
        // 根据优惠券进行分组
        Map<Long, List<UserCoupon>> ucMap = uCList.stream().collect(Collectors.groupingBy(UserCoupon::getTemplateId));
        for (Map.Entry<Long, Integer> entry : couponTemplateHour.entrySet()) {
            Integer num = entry.getValue();
            if (num == null) {
                num = templateNum;
            }
            List<UserCoupon> userCouponList = ucMap.get(entry.getKey());
            if (userCouponList == null) {
                userCouponList = new ArrayList<>();
            }
            if (userCouponList.size() < num) {
                log.error("用户:{}可用的优惠券:{}数量:{}小于:{}", userId, entry.getKey(), userCouponList.size(), num);
                continue;
            }
            for (UserCoupon uc : userCouponList) {
                if (uc.getTemplateSDK() == null) {
                    uc.setTemplateSDK(ids2TemplateSDK.get(uc.getTemplateId()));
                }
            }
            // 对用户的优惠券进行排序 快过期的优惠券先使用
            ListUtil.couponSort(userCouponList);
            log.info("用户可用的优惠券:{}", JacksonUtils.obj2Json(userCouponList));
        }
    }

    @Override
    public List<UserAvailableCouponDTO> getUserGradeCoupon() {
        return getBaseMapper().getUserGradeCoupon();
    }

    @Override
    public boolean gradeCheckCode(String userId, Integer templateNum, Long gradeId, Long storeId, Long gradeEnrollUserId) {
        List<GradeCouponTemplate> couponTemplates = gradeCouponTemplateService.getCouponTemplateIdListByGradeId(gradeId);
        List<Long> couponTemplateIds = couponTemplates.stream().map(GradeCouponTemplate::getCouponTemplateId).collect(Collectors.toList());
        // 查询课程对应的用户优惠券
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(UserCoupon::getTemplateId, couponTemplateIds)
                .eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getStatus, UserCouponStatus.USABLE);
        List<UserCoupon> uCList = list(queryWrapper);
        if (CollectionUtils.isEmpty(uCList)) {
            log.error("用户:{}没有可用的优惠券", userId);
            throw new SportException("没有可用的课程券!");
        }

        Map<Long, Integer> couponTemplateHour = new HashMap<>();
        for (GradeCouponTemplate gt : couponTemplates) {
            couponTemplateHour.put(gt.getCouponTemplateId(), gt.getClassHour());
        }
        Map<Long, CouponTemplateSDK> ids2TemplateSDK = templateBaseService.findIds2TemplateSDK(couponTemplateIds);
        // 根据优惠券进行分组
        Map<Long, List<UserCoupon>> ucMap = uCList.stream().collect(Collectors.groupingBy(UserCoupon::getTemplateId));

        for (Map.Entry<Long, Integer> entry : couponTemplateHour.entrySet()) {
            Integer num = entry.getValue();
            if (num == null) {
                num = templateNum;
            }
            List<UserCoupon> userCouponList = ucMap.get(entry.getKey());
            if (userCouponList == null) {
                userCouponList = new ArrayList<>();
            }
            if (userCouponList.size() < num) {
                log.error("用户:{}可用的优惠券:{}数量:{}小于:{}", userId, entry.getKey(), userCouponList.size(), num);
                continue;
            }
            // 补全确实的优惠券规则 否则影响下面的优惠券排序
            for (UserCoupon uc : userCouponList) {
                if (uc.getTemplateSDK() == null) {
                    uc.setTemplateSDK(ids2TemplateSDK.get(uc.getTemplateId()));
                }
            }
            // 对用户的优惠券进行排序 快过期的优惠券先使用
            ListUtil.couponSort(userCouponList);
            log.info("用户:{}可用的优惠券:{}", userId, userCouponList.size());
            // 保存用户约课优惠券
            List<UserCoupon> coupons = userCouponList.subList(0, num);
            List<GradeUserCoupon> gradeUserCouponList = new ArrayList<>();
            List<CouponChangeRecord> crList = new ArrayList<>();
            for (UserCoupon uc : coupons) {
                GradeUserCoupon gradeUserCoupon = new GradeUserCoupon();
                gradeUserCoupon.setUserId(userId);
                gradeUserCoupon.setGradeId(gradeId);
                gradeUserCoupon.setUserCouponId(uc.getId());
                gradeUserCoupon.setGradeEnrollUserId(gradeEnrollUserId);
                gradeUserCouponList.add(gradeUserCoupon);
                CouponChangeRecord cr = new CouponChangeRecord();
                cr.setGradeId(gradeId);
                cr.setUserCouponId(uc.getId());
                cr.setRemark("约课使用优惠券");
                cr.setUserId(userId);
                crList.add(cr);
            }
            log.info("用户:{}本次使用优惠券:{}", userId, gradeUserCouponList.size());
            // 添加课程使用优惠券的记录
            gradeEnrollUserService.batchSaveGradeCouponList(gradeUserCouponList);
            List<Long> userCouponIds = coupons.stream().map(UserCoupon::getId).collect(Collectors.toList());
            couponVerifyService.recordCouponVerifyBatch(userId, storeId, userCouponIds, "用户约课核销");
            couponChangeRecordService.saveBatch(crList);
            // 更新优惠券状态 为 临时使用状态
            if (change2Status(userCouponIds, UserCouponStatus.TEMP_USED)) {
                return true;
            } else {
                log.error("用户:{}优惠券缓存更新为使用失败", userId);
                throw new SportException("课程券核销失败!");
            }
        }
        throw new SportException("没有可用的优惠券!");
    }


    @Override
    public boolean batchBackCoupon(List<CouponChangeRecord> idList, int couponExtendDays) {
        log.info("退回优惠券:" + idList.toString());
        for (CouponChangeRecord item : idList) {
            couponBack(item.getUserCouponId(), item.getGradeId(), couponExtendDays);
        }
        return true;
    }

    /**
     * 通过优惠券码领取优惠券
     *
     * @param couponTemplateId
     * @param userId
     * @param code
     * @return
     */
    public boolean receiveCouponByCode(Long couponTemplateId, String userId, String code) {
        // 校验code是否存在
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setTemplateId(couponTemplateId);
        userCoupon.setUserId(userId);
        userCoupon.setCouponCode(code);
        userCoupon.setStatus(UserCouponStatus.USABLE);
        userCoupon.setTemplateSDK(templateBaseService.getCouponTemplateSdk(couponTemplateId, null, null));
        if (save(userCoupon)) {
            // 从优惠券码中删除对应的优惠券code
            return templateBaseService.removeCouponCode(couponTemplateId, code);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean receiveShareCouponByCode(String username, String code) {
        String userId = userService.getUserIdFromCache(username);
        // 通过优惠券码从库中查找分享记录
        CouponSharing couponSharing = couponSharingService.getByCode(code);
        if (couponSharing == null) {
            // 不存在
            throw new SportException("优惠券码已失效!");
        } else if (couponSharing.getReceiveUserId() != null) {
            throw new SportException("该优惠券已领取!");
        } else {
            // 领取优惠券
            if (receiveCouponByCode(couponSharing.getTemplateId(), userId, code)) {
                // 修改分享记录
                return couponSharingService.changeReceive(couponSharing.getId(), userId);
            }
        }
        throw new SportException("优惠券领取失败!");
    }

    @Override
    public int getOrderAvailableCouponCount(String orderId) {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(UserCoupon::getOrderId, orderId)
                .eq(UserCoupon::getStatus, UserCouponStatus.USABLE);
        return count(queryWrapper);
    }

    @Override
    public List<OrderCouponVO> getOrderCoupon(String phone) {
        return getBaseMapper().getOrderCoupon(phone);
    }

    @Override
    public List<UserCouponVO> getOrderCouponList(String orderId) {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCoupon::getOrderId, orderId)
                .eq(UserCoupon::getStatus, UserCouponStatus.USABLE);
        List<UserCoupon> userCouponList = list(queryWrapper);

        //补充sdk
        if (CollectionUtils.isNotEmpty(userCouponList)) {
            Map<Long, CouponTemplateSDK> id2TemplateSDK =
                    templateBaseService.findIds2TemplateSDK(
                            userCouponList.stream()
                                    .map(UserCoupon::getTemplateId)
                                    .collect(Collectors.toList())
                    );

            userCouponList.forEach(
                    dc -> {
                        CouponTemplateSDK couponTemplateSDK = id2TemplateSDK.get(dc.getTemplateId());
                        if (dc.getRule() != null) {
                            couponTemplateSDK.setRule(dc.getRule());
                        }
                        dc.setTemplateSDK(couponTemplateSDK);
                    }
            );
        }
        return userCoupon2UserCouponVO(userCouponList, false);
    }

    @Override
    public boolean orderCouponAdd(OrderUserCouponForm form) {
        TemplateRule.Expiration expiration = new TemplateRule.Expiration();
        if (form.getCouponExpireDay() != null) {
            expiration.setGap(form.getCouponExpireDay());
            expiration.setPeriod(PeriodType.SHIFT.getCode());
        } else if (form.getCouponExpireTime() != null) {
            expiration.setDeadline(form.getCouponExpireTime().getTime());
            expiration.setPeriod(PeriodType.REGULAR.getCode());
        } else {
            expiration.setPeriod(PeriodType.NOT_OVER.getCode());
        }
        List<Long> idList = acquireByCouponTemplateId(form.getUserId(), form.getCouponTemplateId(), form.getOrderId(), form.getSingleCouponAmount(),
                form.getOrderLimit(), expiration, false);
        return CollectionUtils.isNotEmpty(idList);
    }

    @Override
    public List<UserCouponDTO> getCouponByUserList(Long storeId, List<String> userIdList) {
        return getBaseMapper().getCouponByUserList(storeId, userIdList);
    }

    @Override
    public IPage<UserCouponTotalVO> getTotalList(int page, int size, String phone, String userName, Long templateId, Integer status, Integer countFrom, Integer countTo, Long storeId,Integer orderStatus,String orderStatusDesc,String coachId) {
        List<Long> couponTemplateIdList = new ArrayList<>();
        if (templateId != null) {
            couponTemplateIdList.add(templateId);
        } else {
            couponTemplateIdList = couponTemplateService.getByStoreId(storeId);
        }

        if (CollectionUtils.isNotEmpty(couponTemplateIdList)) {
            IPage<UserCouponTotalVO> iPage = getBaseMapper().getTotalList(new Page<>(page, size), phone, userName, couponTemplateIdList, status, countFrom, countTo, orderStatus, orderStatusDesc, storeId.toString(),coachId);
            List<UserCouponTotalVO> records = iPage.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                // 获取当前页用户所有的优惠券，包括待使用 已使用 和 过期
                List<UserCouponDTO> userCouponDTOList = getCouponByUserList(storeId, records.stream().map(UserCouponTotalVO::getUserId).collect(Collectors.toList()));
                // 按用户进行分组
                Map<String, List<UserCouponDTO>> userCouponMap = userCouponDTOList.stream().collect(Collectors.groupingBy(UserCouponDTO::getUserId));
                // 遍历用户 设置优惠券信息
                for (UserCouponTotalVO ut : records) {
                    Date lastClassTime = gradeEnrollUserService.getLastClassTime(ut.getUserId());
                    if(lastClassTime != null) {
                        ut.setLastClassTime(DateFormatUtils.format(lastClassTime, "yyyy-MM-dd HH:mm"));
                    }
                    // 获取单个用户的优惠券列表
                    List<UserCouponDTO> userCouponItemList = userCouponMap.get(ut.getUserId());
                    // 对单个用户 根据优惠券名称进行分组
                    Map<String, List<UserCouponDTO>> couponNameMap = userCouponItemList.stream().collect(Collectors.groupingBy(UserCouponDTO::getName));
                    // 设置单个用户的优惠券数量统计
                    List<UserCouponNumberVO> couponList = new ArrayList<>(couponNameMap.size());
                    for (Map.Entry<String, List<UserCouponDTO>> item : couponNameMap.entrySet()) {
                        UserCouponNumberVO number = new UserCouponNumberVO();
                        number.setCouponName(item.getKey());
                        number.setCouponId(item.getValue().get(0).getId());

                        List<UserCouponDTO> userCouponList = new ArrayList<>();
                        for (UserCouponDTO userCouponDTO : item.getValue()) {
                            if (userCouponDTO.getStatus().equals(UserCouponStatus.USABLE.getCode()) || userCouponDTO.getStatus().equals(UserCouponStatus.FREEZE.getCode())) {
                                Date start = userCouponDTO.getCreateDate();
                                Date end = null;
                                if (!StringUtils.isEmpty(userCouponDTO.getRule())) {
                                    TemplateRule rule = JSONObject.parseObject(userCouponDTO.getRule(), TemplateRule.class);
                                    if (rule.getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                                        if (rule.getExpiration().getDeadline() != null) {
                                            end = new Date(rule.getExpiration().getDeadline());
                                        }
                                    } else if (rule.getExpiration().getGap() != null) {
                                        end = DateUtils.addDays(start, rule.getExpiration().getGap());
                                    }
                                }
                                if (end != null) {
                                    Date date = new Date();
                                    if (userCouponDTO.getStatus().equals(UserCouponStatus.FREEZE.getCode())) {
                                        date = userCouponDTO.getUpdateDate();
                                    }
                                    Date o = null;
                                    try {
                                        o = DateUtils.parseDate(DateFormatUtils.format(date, "yyyy-MM-dd"), "yyyy-MM-dd");
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    int gap = (int) ((end.getTime() - o.getTime()) / 1000 / 3600 / 24);
                                    userCouponDTO.setTime(String.format("%s天", gap));
                                } else {
                                    userCouponDTO.setTime("永久");
                                }
                                userCouponDTO.setUpdateDateStr(DateFormatUtils.format(userCouponDTO.getUpdateDate(), "yyyy-MM-dd"));
                                userCouponList.add(userCouponDTO);
                            }
                        }

                        Map<Integer, Long> collect = item.getValue().stream().collect(Collectors.groupingBy(UserCouponDTO::getStatus, Collectors.counting()));
                        Long used = collect.get(UserCouponStatus.USED.getCode());
                        if (used == null) {
                            used = 0L;
                        }
                        if (collect.get(UserCouponStatus.TEMP_USED.getCode()) != null) {
                            used += collect.get(UserCouponStatus.TEMP_USED.getCode());
                        }
                        number.setUsed(used);
                        number.setUnUsed(collect.get(UserCouponStatus.USABLE.getCode()));
                        number.setExpired(collect.get(UserCouponStatus.EXPIRED.getCode()));
                        number.setFreeze(collect.get(UserCouponStatus.FREEZE.getCode()));
                        number.setUserCouponList(userCouponList);
                        couponList.add(number);
                    }
                    ut.setCouponList(couponList);
                }
            }
            return iPage;
        }
        return new Page<>();
    }

    @Override
    public boolean sendByExercise(String userId, Long exerciseId) {
        Exercise exercise = exerciseService.getById(exerciseId);
        return exerciseCouponService.sendCouponByExercise(exerciseId, userId, exercise.getCategoryId());
    }

    @Override
    public void checkValidity() {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCoupon::getStatus, UserCouponStatus.USABLE.getCode());

        List<UserCoupon> allUsableCoupon = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(allUsableCoupon)) {
            log.info("有效优惠券:{}", allUsableCoupon.size());
            // 补全TemplateSDK没值的情况
            // 填充 dbCoupons的 templateSDK 字段
            Map<Long, CouponTemplateSDK> id2TemplateSDK =
                    templateBaseService.findIds2TemplateSDK(
                            allUsableCoupon.stream()
                                    .map(UserCoupon::getTemplateId)
                                    .distinct()
                                    .collect(Collectors.toList())
                    );

            allUsableCoupon.forEach(
                    dc -> {
                        CouponTemplateSDK couponTemplateSDK = id2TemplateSDK.get(dc.getTemplateId());
                        if (dc.getRule() == null) {
                            dc.setRule(couponTemplateSDK.getRule());
                            dc.setTemplateSDK(couponTemplateSDK);
                        }
                        if (dc.getTemplateSDK() == null) {
                            dc.setTemplateSDK(new CouponTemplateSDK(dc.getTemplateId(),
                                    couponTemplateSDK.getName(),
                                    couponTemplateSDK.getLogo(),
                                    couponTemplateSDK.getDesc(),
                                    couponTemplateSDK.getCategory(),
                                    couponTemplateSDK.getProductLine(),
                                    couponTemplateSDK.getKey(),
                                    couponTemplateSDK.getTarget(),
                                    dc.getActivePrice(),
                                    dc.getRule(),
                                    couponTemplateSDK.getCompanyId(),
                                    couponTemplateSDK.getStoreId(),
                                    couponTemplateSDK.getProductId()));
                        }
                    }
            );
            UserCouponClassify classify = UserCouponClassify.classify(allUsableCoupon);
            List<UserCoupon> oldList = classify.getExpired();
            // 将已过期的优惠券状态更新为过期
            if (CollectionUtils.isNotEmpty(oldList)) {
                log.info("本次过期优惠券:{}", oldList.size());
                updateBatchById(oldList.stream().map(item -> {
                    UserCoupon uc = new UserCoupon();
                    uc.setId(item.getId());
                    uc.setStatus(UserCouponStatus.EXPIRED);
                    return uc;
                }).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public boolean setExpireValidity(String userId, Long templateId, int status, String validityDay, int num, Integer changeStatus, String userCouponIds) {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(userCouponIds)) {
            List<Long> ids = Arrays.stream(userCouponIds.split(",")).map(t -> Long.parseLong(t)).collect(Collectors.toList());
            queryWrapper.lambda().in(UserCoupon::getId, ids);
        } else {
            queryWrapper.lambda().eq(UserCoupon::getUserId, userId)
                    .eq(UserCoupon::getTemplateId, templateId)
                    .eq(UserCoupon::getStatus, status);

            if (num > 0) {
                queryWrapper.last("limit " + num);
            }
        }

        List<UserCoupon> userCouponList = list(queryWrapper);
        List<UserCoupon> updateList = new ArrayList<>(userCouponList.size());
        UserCouponStatus userCouponStatus = UserCouponStatus.USABLE;
        if (changeStatus != null) {
            userCouponStatus = UserCouponStatus.of(changeStatus);
        }
        for (UserCoupon uc : userCouponList) {
            UserCoupon item = new UserCoupon();
            item.setId(uc.getId());
            item.setStatus(userCouponStatus);
            if (userCouponStatus != UserCouponStatus.USABLE) {
                updateList.add(item);
                continue;
            }
            TemplateRule rule = uc.getRule();
            if (rule == null) {
                CouponTemplateSDK couponTemplateSDK = templateBaseService.getCouponTemplateSdk(templateId, null, null);
                rule = couponTemplateSDK.getRule();
            }
            if (rule != null) {
                try {
                    TemplateRule.Expiration expiration = rule.getExpiration();
                    if (expiration != null && expiration.getPeriod() != null) {
                        if (expiration.getPeriod().equals(PeriodType.SHIFT.getCode())) {
                            Date f = DateUtils.parseDate(validityDay, "yyyy-MM-dd");
                            Date o = DateUtils.parseDate(DateFormatUtils.format(uc.getCreateDate(), "yyyy-MM-dd"), "yyyy-MM-dd");
                            int gap = (int) ((f.getTime() - o.getTime()) / 1000 / 3600 / 24);
                            expiration.setGap(gap);
                            log.info("设置优惠券:{}, 过期间隔:{}", uc.getId(), gap);
                            item.setRule(rule);
                            updateList.add(item);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                log.warn("优惠券:{}没有获取到规则, 无法进行更新", uc.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(updateList)) {
            log.info("本次更新优惠券:" + updateList.size());
            return updateBatchById(updateList);
        }
        return false;
    }

    @Override
    public boolean recalculateAmount(String orderId, BigDecimal totalAmount) {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCoupon::getOrderId, orderId);
        List<UserCoupon> userCouponList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(userCouponList)) {
            BigDecimal singleCouponAmount = totalAmount.divide(BigDecimal.valueOf(userCouponList.size()), 2, BigDecimal.ROUND_HALF_DOWN);
            List<UserCoupon> updateCouponList = new ArrayList<>(userCouponList.size());
            for (int i = 0; i < userCouponList.size(); i++) {
                UserCoupon uc = new UserCoupon();
                uc.setId(userCouponList.get(i).getId());
                uc.setAmount(singleCouponAmount);
                updateCouponList.add(uc);
            }
            return updateBatchById(updateCouponList);
        }
        return true;
    }

    @Override
    public List<UserCouponTotalVO> queryCouponsBelowThreshold(Long storeId, Integer threshold) {
        return getBaseMapper().queryCouponsBelowThreshold(storeId, threshold);
    }

    @Override
    public List<UserCouponTotalVO> queryUserReceiveCallByUserIds(Long companyId, Long storeId, List<String> userIds) {
        return getBaseMapper().queryUserReceiveCallByUserIds(companyId, storeId, userIds);
    }

    @Override
    public boolean couponConvert(CouponConvertForm form) {
        QueryWrapper<UserCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserCoupon::getUserId, form.getUserId())
                .eq(UserCoupon::getTemplateId, form.getCouponTemplateId())
                .eq(UserCoupon::getStatus, UserCouponStatus.USABLE);
        List<UserCoupon> sourceList = list(queryWrapper);
        if (sourceList.size() < form.getSourceCount()) {
            throw new SportException("原始可用优惠券数量小于" + form.getSourceCount() + "张");
        }
        CouponTemplate targetTemplate = couponTemplateService.getById(form.getTargetTemplateId());
        if (targetTemplate == null) {
            throw new SportException("目标优惠券不存在");
        }
        TemplateRule templateRule = targetTemplate.getRule();
        TemplateRule.Expiration expiration = form.getExpiration();
        if (expiration != null) {
            templateRule.setExpiration(expiration);
        }
        List<UserCoupon> updateList = new ArrayList<>();
        // 待转换的课券
        List<UserCoupon> convertList = sourceList.subList(0, form.getSourceCount());
        for (UserCoupon uc : convertList) {
            UserCoupon item = new UserCoupon();
            item.setId(uc.getId());
            item.setStatus(UserCouponStatus.CONVERT);
            updateList.add(item);
        }
        updateBatchById(updateList);
        List<UserCoupon> adddList = new ArrayList<>();
        for (int i = 0; i < form.getTargetCount(); i++) {
            UserCoupon uc = new UserCoupon();
            uc.setTemplateId(form.getTargetTemplateId());
            uc.setUserId(form.getUserId());
            uc.setStatus(UserCouponStatus.USABLE);
            uc.setAmount(form.getCouponAmount());
            uc.setRule(templateRule);
            adddList.add(uc);
        }
        return saveBatch(adddList);
    }
}
