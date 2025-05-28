package com.aioveu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.constant.RedisConstant;
import com.aioveu.constant.ResultEnum;
import com.aioveu.constant.SportConstant;
import com.aioveu.constant.SysDictConstant;
import com.aioveu.dao.OrderDao;
import com.aioveu.data.sync.FieldSyncMessage;
import com.aioveu.dto.ShareConfigDTO;
import com.aioveu.entity.*;
import com.aioveu.enums.*;
import com.aioveu.exception.SportException;
import com.aioveu.feign.WxMaUserClient;
import com.aioveu.feign.form.RefundMoneyForm;
import com.aioveu.feign.form.WeChatPayForm;
import com.aioveu.feign.vo.WeChatPayParamVO;
import com.aioveu.feign.vo.WxPayOrderQueryResultVO;
import com.aioveu.form.FieldForm;
import com.aioveu.form.OrderForm;
import com.aioveu.form.StoreExperienceOrderForm;
import com.aioveu.form.VipOrderForm;
import com.aioveu.service.*;
import com.aioveu.utils.*;
import com.aioveu.utils.word.WordUtil;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.function.Tuple2;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {

    @Autowired
    private MQMessageService mqMessageService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private IUserCouponService iUserCouponService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreVenueService storeVenueService;

    @Resource
    private WxMaUserClient wxMaUserClient;

    @Autowired
    private UserService userService;

    @Value(value = "${sport.server.prefix}")
    private String sportServerPrefix;

    @Autowired
    private ShareConfigService shareConfigService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ITemplateBaseService templateBaseService;

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private ExerciseCouponService exerciseCouponService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserBalanceChangeService userBalanceChangeService;

    @Value(value = "${sport.agreement.path}")
    private String sportAgreementPath;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ExerciseVipCardService exerciseVipCardService;

    @Autowired
    private UserVipCardService userVipCardService;

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private VenueFieldService venueFieldService;

    @Autowired
    private FieldBookUserService fieldBookUserService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private VipCardService vipCardService;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private ExerciseCountTemplateService exerciseCountTemplateService;

    @Autowired
    private StoreConfigService storeConfigService;

    @Override
    public void toUsed(List<String> orderIdList) {
        for (String orderId : orderIdList) {
            Order order = getById(orderId);
            if (order == null) {
                throw new SportException(orderId + "订单不存在");
            }
            if (order.getStatus().equals(OrderStatus.PAY.getCode()) ||
                    order.getStatus().equals(OrderStatus.USING.getCode())) {
                int count = iUserCouponService.getOrderAvailableCouponCount(orderId);
                log.warn("用户:{}, 订单:{},可用优惠券:{}", order.getUserId(), orderId, count);
                if (count == 0) {
                    Order update = new Order();
                    update.setId(orderId);
                    update.setStatus(OrderStatus.ORDER_FINISH.getCode());
                    updateById(update);
                }
            } else {
                log.error(orderId + "订单状态异常");
            }
        }
    }

    @Override
    public WxMaPayVO create(OrderForm form) {
        String[] phoneArgs = form.getPhoneList().split(",");
        if (phoneArgs.length == 0) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "报名人手机号码不能为空!");
        }
        return createExercise(form);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WxMaPayVO createExercise(OrderForm form) {
        log.info("下单:{}", JSON.toJSONString(form));
        Exercise exercise = exerciseService.getById(form.getProductId());
        if (exercise == null) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), form.getProductId() + "活动不存在!");
        }
        if (exercise.getStatus() == 15) {
            throw new SportException("该商品已失效!");
        }
        Date now = new Date();
        if (exercise.getExerciseStartTime() != null && exercise.getExerciseStartTime().after(now)) {
            throw new SportException("活动未开始!");
        }
        if (exercise.getExerciseEndTime() != null && exercise.getExerciseEndTime().before(now)) {
            throw new SportException("活动已结束!");
        }
        if (exercise.getStatus() != 1 && exercise.getStatus() != 10) {
            throw new SportException("该商品已被下架!");
        }
        if (exercise.getNeedAddress() && StringUtils.isEmpty(form.getAddress())) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "地址不能为空!");
        }
        if (exercise.getCategoryId().equals(categoryService.getByCode("custom_course")) ||
                exercise.getCategoryId().equals(categoryService.getByCode("course"))) {
            if (StringUtils.isEmpty(form.getCoachUserId())) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "请选择服务教练!");
            }
        }
        // 对于会员卡活动进行会员卡校验
        if (categoryService.getByCode(ExerciseCategory.VIP_CARD.getCode()).equals(exercise.getCategoryId())) {
            if (exerciseVipCardService.getExerciseVip(exercise.getId()) == null) {
                log.error("活动：{}未配置会员卡", exercise.getId());
                throw new SportException("系统异常请联系客服!");
            }
        }

        Store store = storeService.getById(exercise.getStoreId());
        User user = userService.getById(form.getUserId());

        //TODO 雒世松12 其他判断逻辑后续完善
        String[] phoneArgs = form.getPhoneList().split(",");
        if (phoneArgs.length == 0) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "报名人数不能为空");
        }
        Integer count = form.getNumber();
        if (count == null) {
            count = phoneArgs.length;
        }
        if (count > 1) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单数量不能大于1");
        }

        if (exercise.getSingleNumber() != null && exercise.getSingleNumber() > 0) {
            if (orderDetailService.getEnrollUserCount(exercise.getId() + "", exercise.getCategoryId(), form.getUserId())
                    >= exercise.getSingleNumber()) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "每个人只能买" + exercise.getSingleNumber() + "件哦!");
            }
            if (count > exercise.getSingleNumber()) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "每个人只能买" + exercise.getSingleNumber() + "件哦!");
            }
        }
        if (exercise.getLimitNumber() != null && exercise.getLimitNumber() > 0 && exercise.getEnrollNumber() >= exercise.getLimitNumber()) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "购买人数已满!");
        }

        SettlementInfo settlementInfo = new SettlementInfo();
        settlementInfo.setUserId(form.getUserId());
        settlementInfo.setEmploy(true);

        // 会员卡 优惠处理
        if (categoryService.getByCode(ExerciseCategory.COURSE.getCode()).equals(exercise.getCategoryId())) {
            UserVipCard userVipCard = userVipCardService.getUserVipCard(form.getUserId(), exercise.getCategoryId(), store.getCompanyId(), store.getId());
            if (userVipCard != null) {
                form.setUserCouponId(null);
                if (exercise.getVipPrice() != null) {
                    exercise.setPrice(exercise.getVipPrice());
                }
                form.setRemark(form.getRemark() + " 会员卡优惠");
            }
        }

        // 优惠券信息
        Long couponId = form.getUserCouponId();
        List<SettlementInfo.CouponAndTemplateInfo> couponAndTemplateInfos = new ArrayList<>();
        if (couponId != null) {
            SettlementInfo.CouponAndTemplateInfo ct = new SettlementInfo.CouponAndTemplateInfo();
            ct.setId(couponId);
            UserCoupon coupon = iUserCouponService.getById(couponId);
            ct.setTemplate(templateBaseService.getCouponTemplateSdk(coupon.getTemplateId(), null, null));
            couponAndTemplateInfos.add(ct);
        }
        settlementInfo.setCouponAndTemplateInfos(couponAndTemplateInfos);
        // 设置商品信息
        List<GoodsInfo> goodsInfos = new ArrayList<>();
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setCount(count);
        goodsInfo.setPrice(exercise.getPrice().doubleValue());
        goodsInfos.add(goodsInfo);
        settlementInfo.setGoodsInfos(goodsInfos);

        settlementInfo = iUserCouponService.settlement(settlementInfo);

        String orderId;
        if (categoryService.getByCode(ExerciseCategory.COUNT.getCode()).equals(exercise.getCategoryId())) {
            orderId = IdUtils.getStrNumberId("CD");
        } else {
            orderId = IdUtils.getStrNumberId("ED");
        }
        Order order = new Order();
        order.setId(orderId);
        order.setName(exercise.getName());
        order.setCategoryId(exercise.getCategoryId());
        order.setUserId(form.getUserId());
        order.setProductId(form.getProductId());
        order.setCount(count);
        order.setRemark(form.getRemark());
        order.setSaleUserId(form.getCoachUserId());

        // 设置地址
        if (StringUtils.isNotEmpty(form.getAddress())) {
            order.setAddressId(userAddressService.createByAddress(user, form.getAddress()));
        }
        if (exercise.getCategoryId().equals(categoryService.getByCode(ExerciseCategory.COUNT.getCode()))) {
            order.setStartTime(exercise.getExerciseStartTime());
            order.setEndTime(exercise.getExerciseEndTime());
        } else {
            order.setStartTime(exercise.getStartTime());
            order.setEndTime(exercise.getEndTime());
        }
        order.setAddress(store.getAddress());
        order.setStoreId(store.getId());
        order.setCompanyId(store.getCompanyId());

        order.setUserCouponId(couponId);
        // 增加1元软件服务费
        order.setAmount(settlementInfo.getTotalCost().add(BigDecimal.ONE));
        order.setCouponAmount(BigDecimal.valueOf(settlementInfo.getCouponCost()));
        order.setConsumeAmount(BigDecimal.valueOf(settlementInfo.getCost()).add(BigDecimal.ONE));
        order.setActualAmount(order.getConsumeAmount());
        order.setCanRefundAmount(order.getActualAmount());

        if (!save(order)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }

        WxMaPayVO wxMaPayVO;
        if (order.getActualAmount().doubleValue() > 0) {
            wxMaPayVO = getWxMaPayVO(null, order, "wechat-pay", PayCategoryEnum.EXERCISE.getCode());
        } else {
            wxMaPayVO = new WxMaPayVO();
            Order updateOrder = new Order();
            updateOrder.setId(orderId);
            wxMaPayVO.setOrderId(orderId);
            updateOrder.setPayType(PayType.NONE.getCode());
            updateById(updateOrder);
        }

        // 创建订单明细
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setCategoryId(order.getCategoryId());
            orderDetail.setOrderId(order.getId());
            orderDetail.setProductId(order.getProductId());
            orderDetail.setProductName(exercise.getName());
            orderDetail.setProductPrice(exercise.getPrice());
            orderDetail.setProductQuantity(1);
            orderDetail.setProductImage(exercise.getImage());
            orderDetail.setUserId(order.getUserId());
            if (phoneArgs.length > 1) {
                orderDetail.setPhone(phoneArgs[i]);
            } else {
                orderDetail.setPhone(phoneArgs[0]);
            }
            orderDetail.setPayType(order.getPayType());
            orderDetail.setStartTime(order.getStartTime());
            orderDetail.setEndTime(order.getEndTime());

            orderDetail.setAmount(exercise.getPrice());
            orderDetail.setConsumeAmount(orderDetail.getAmount());

            BigDecimal odCost = BigDecimal.ZERO;
            if (order.getCouponAmount().doubleValue() > 0) {
                if (order.getConsumeAmount().doubleValue() == 0) {
                    odCost = orderDetail.getAmount();
                } else {
                    // 根据单个商品的价格比重分配优惠比例
                    odCost = order.getCouponAmount().multiply(orderDetail.getConsumeAmount().divide(order.getConsumeAmount(), 2, RoundingMode.HALF_UP));
                }
            }
            orderDetail.setConsumeAmount(orderDetail.getAmount().subtract(odCost));
            orderDetail.setActualAmount(orderDetail.getConsumeAmount());
            orderDetail.setCanRefundAmount(orderDetail.getActualAmount());
            orderDetail.setCouponAmount(orderDetail.getAmount().subtract(orderDetail.getConsumeAmount()));

            orderDetail.setStoreId(exercise.getStoreId());
            orderDetail.setCompanyId(store.getCompanyId());
//            orderDetail.setConsumeCode(IdUtils.getConsumeCode("ED"));
            orderDetailList.add(orderDetail);
        }
        if (!orderDetailService.saveBatch(orderDetailList)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }
        // 检查商品分享
        if (StringUtils.isNotEmpty(form.getShareUserId()) && !form.getShareUserId().equals(form.getUserId()) && !form.getShareUserId().equals("undefined")) {
            ShareConfig shareConfig = shareConfigService.getByCategoryIdAndProductId(store.getCompanyId(), form.getCategoryId(), form.getProductId(), exercise.getStoreId(), 0);
            if (shareConfig != null) {
                ShareConfigDTO shareConfigDTO = new ShareConfigDTO();
                BeanUtils.copyProperties(shareConfig, shareConfigDTO);
                shareConfigDTO.setOrderId(orderId);
                shareConfigDTO.setShareUserId(form.getShareUserId());
                shareConfigDTO.setUserId(order.getUserId());
                redisUtil.set(String.format("%s%s", RedisConstant.ORDER_SHARE_CONFIG, orderId), shareConfigDTO, 60 * 60);
            } else {
                log.info("未找到类型0分享设置, 公司:{}, 分类:{}, 商品:{}, 店铺:{}", store.getCompanyId(), form.getCategoryId(), form.getProductId(), exercise.getStoreId());
            }
        }
        return wxMaPayVO;
    }

    private WeChatPayParamVO getRepayParam(String prepayId, String nonceStr, String payAppId, String appId) {
        CommonResponse<WeChatPayParamVO> response = wxMaUserClient.repay(prepayId, nonceStr, payAppId, appId);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new SportException(response.getCode(), response.getMessage());
        }
    }

    /**
     * 获取支付参数
     *
     * @param userId
     * @param userOpenId
     * @param appId
     * @param payId
     * @param orderId
     * @param name
     * @param actualAmount
     * @return
     */
    @Override
    public WeChatPayParamVO getPayParam(String userId, String userOpenId, String appId, String payId, String orderId, String name,
                                        BigDecimal actualAmount, String noticeUrl, String payType, String tradeType, String payCallCategory, int closeMinutes) {

        if (closeMinutes > 1) {
            // 超时未支付 关闭订单
            Map<String, Object> msgMap = new LinkedHashMap<>();
            msgMap.put("type", DelayMessageType.UNPAY_CANCEL.getCode());
            msgMap.put("orderId", orderId);
            mqMessageService.sendDelayMsgByDate(msgMap, DateUtils.addMinutes(new Date(), closeMinutes));
        }

        WeChatPayForm payForm = new WeChatPayForm();
        payForm.setAppId(appId);
        payForm.setPayAppId(payId);
        payForm.setPayType(payType);
        payForm.setRemark(payCallCategory);
        payForm.setTradeType(tradeType);
        if (PayCategoryEnum.FIELD.getCode().equals(payCallCategory) || PayCategoryEnum.VIP.getCode().equals(payCallCategory)) {
            payForm.setDelayAcctFlag(false);
        } else {
            payForm.setDelayAcctFlag(true);
        }

        if ("NATIVE".equals(tradeType)) {
            // 微信二维码付款
            payForm.setProductId(orderId);
        } else {
            payForm.setMiniOpenId(userOpenId);
        }
        payForm.setAmount(actualAmount);
        payForm.setDetail(name + "订单支付");
        payForm.setName(name);
        payForm.setOrderId(orderId);
        payForm.setNotifyUrl(sportServerPrefix + "/api/v1/notice/" + noticeUrl);
        log.info("notice-url:{}", payForm.getNotifyUrl());
        payForm.setNonceStr(UUID.randomUUID().toString().replace("-", ""));
        CommonResponse<WeChatPayParamVO> response = wxMaUserClient.unifiedOrder(payForm);
        if (response.isSuccess()) {
            return response.getData();
        } else {
            throw new SportException(response.getCode(), response.getMessage());
        }
    }

    @Override
    public WxMaPayVO continuePay(String orderId) {
        Order order = getById(orderId);
        if (!order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            throw new SportException("订单状态错误");
        }
        WxMaPayVO wxMaPayVO = new WxMaPayVO();
        Store store = storeService.getById(order.getStoreId());
        Company company = companyService.getById(store.getCompanyId());
        WeChatPayParamVO weChatPayParamVO = getRepayParam(order.getPrepayId(), order.getNonceStr(), company.getMiniAppPayId(), order.getAppId());
        BeanUtils.copyProperties(weChatPayParamVO, wxMaPayVO);
        wxMaPayVO.setOrderId(orderId);
        return wxMaPayVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(String orderId, Date payFinishDate) {
        try {
            Order order = getById(orderId);
            if (order == null) {
                throw new SportException(orderId + "订单不存在");
            }
            if (order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
                Date payFinishTime;
                if (payFinishDate == null) {
                    if (order.getActualAmount().doubleValue() > 0) {
                        payFinishTime = checkOrderPayFinishTime(orderId, order.getPayAppId(), order.getPayType(), DateFormatUtils.format(order.getCreateDate(), "yyyyMMdd"));
                    } else {
                        payFinishTime = new Date();
                    }
                } else {
                    payFinishTime = payFinishDate;
                }
                log.info("用户自己查询更新未完成订单{}", orderId);
                boolean checkStatus = updateOrder2Success(orderId, payFinishTime);
                log.info("结果:" + checkStatus);
                return checkStatus;
            } else if (order.getStatus().equals(OrderStatus.PAY.getCode()) || order.getStatus().equals(OrderStatus.USING.getCode())) {
                log.info("用户自己查询更新订单{}已支付完成", orderId);
                return true;
            } else {
                throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单状态异常");
            }
        } catch (SportException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单状态异常");
        } finally {
            log.info("订单:{}, finally执行", orderId);
//                distributedLock.releaseLock(orderId);
        }
    }

    /**
     * 更新活动报名人数
     *
     * @param exercise
     * @return
     */
    private boolean updateExerciseEnrollNumber(Exercise exercise) {
        // 更新活动报名数量
        Exercise ue = new Exercise();
        ue.setId(exercise.getId());
        ue.setEnrollNumber(exercise.getEnrollNumber() + 1);
        return exerciseService.updateById(ue);
    }

    /**
     * 课程 体验课 会员卡 按次活动订单支付成功 逻辑处理
     *
     * @param order
     */
    private void createExerciseOrderSuccess(Order order) {
        Exercise exercise = exerciseService.getById(order.getProductId());
        // 如果是下单领取优惠券 则立即领取优惠券
        if (exercise.getCouponReceiveType() != null && exercise.getCouponReceiveType() == 1) {
            Integer orderCount = order.getCount();
            for (int i = 0; i < orderCount; i++) {
                try {
                    exerciseCouponService.sendAndNoticeUserCouponByExerciseId(NumberUtils.toLong(order.getProductId()),
                            order.getUserId(), order.getCategoryId(), order.getId(), order.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    // 解决内层事务异常，外面事务也回滚
                    throw new SportException(e.getMessage());
                }
            }
        }
        // 更新活动购买人数
        updateExerciseEnrollNumber(exercise);
        // 会员卡活动 领取会员卡
        if (categoryService.getByCode(ExerciseCategory.VIP_CARD.getCode()).equals(order.getCategoryId())) {
            // 会员卡活动
            exerciseVipCardService.receiveVip(exercise.getId(), order.getUserId());
        }
        // 生成订单协议
        if (StringUtils.isNotEmpty(exercise.getAgreementTemplate())) {
            String outFile = "doc/order/" + order.getUserId() + "/" + System.currentTimeMillis() + "_" + order.getProductId() + ".docx";
            if (createAgreement(order.getUserId(), order.getProductId(), exercise.getAgreementTemplate(), outFile)) {
                Order agreementOrder = new Order();
                agreementOrder.setId(order.getId());
                agreementOrder.setAgreementUrl(outFile);
                updateById(agreementOrder);
            }
        }
        // 分享记录处理
        ShareConfigDTO shareConfigDTO = redisUtil.get(String.format("%s%s", RedisConstant.ORDER_SHARE_CONFIG, order.getId()));
        if (shareConfigDTO != null) {
            // 检查问卷分享
            shareConfigDTO = redisUtil.get(String.format("%s%s", RedisConstant.FORM_ENROLL_SHARE_CONFIG, order.getUserId()));
            redisUtil.del(String.format("%s%s", RedisConstant.FORM_ENROLL_SHARE_CONFIG, order.getUserId()));
            shareConfigService.shareReward(shareConfigDTO, order);
        } else {
            log.info("该订单:{}未分享", order.getId());
        }
        Store store = storeService.getById(order.getStoreId());
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("orderId", order.getId());
        msgMap.put("storeName", store.getName());
        msgMap.put("productName", order.getName());
        msgMap.put("count", order.getCount());
        msgMap.put("amount", order.getAmount().setScale(2, RoundingMode.HALF_UP));
        mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.ORDER_PAY_SUCCESS.getCode(), order.getStoreId());
    }

    @Override
    public List<OrderVO> getList(Integer status, Long companyId, String userId) {
        List<Store> stores = storeService.getByCompanyId(companyId);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId, userId);
        Long categoryId = categoryService.getByCode(SportConstant.FIELD_CATEGORY_CODE);
        Long vipCategoryId = categoryService.getByCode(SportConstant.VIP_CATEGORY_CODE);
        queryWrapper.lambda().ne(Order::getCategoryId, categoryId).ne(Order::getCategoryId, vipCategoryId);
        if (status == -1) {
            queryWrapper.lambda().in(Order::getStatus, 1, 2, 4, 6);
        } else {
            queryWrapper.lambda().eq(Order::getStatus, status);
        }
        queryWrapper.lambda().in(Order::getStoreId, stores.stream().map(IdNameEntity::getId).collect(Collectors.toList()));
        queryWrapper.lambda().orderByDesc(Order::getCreateDate);
        return list(queryWrapper).stream().map(item -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(item, orderVO);
            return orderVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderVO> getList(Integer status, Long companyId, String userId, Long storeId) {
        List<Store> stores = storeService.getByCompanyId(companyId);
        if (stores ==null || stores.size()==0 ){
            return Collections.emptyList();
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        //兼容原来不传storeId逻辑
        //不传storeId，查询我的全部订单
        //传storeId，查询当前店铺下，我的订单
        if (storeId != null){
            List<Store> collect = stores.stream().filter(bean -> bean.getId().equals(storeId)).collect(Collectors.toList());
            if (collect == null || collect.size() == 0){
                return Collections.emptyList();
            }
            queryWrapper.lambda().eq(Order::getStoreId, storeId);
        }else {
            queryWrapper.lambda().in(Order::getStoreId, stores.stream().map(t -> t.getId()).collect(Collectors.toList()));
        }
        queryWrapper.lambda().eq(Order::getUserId, userId);
        Long categoryId = categoryService.getByCode(SportConstant.FIELD_CATEGORY_CODE);
        Long vipCategoryId = categoryService.getByCode(SportConstant.VIP_CATEGORY_CODE);
        queryWrapper.lambda().ne(Order::getCategoryId, categoryId).ne(Order::getCategoryId, vipCategoryId);
        if (status == -1) {
            queryWrapper.lambda().in(Order::getStatus, 1, 2, 4, 6);
        } else {
            queryWrapper.lambda().eq(Order::getStatus, status);
        }
        queryWrapper.lambda().orderByDesc(Order::getCreateDate);
        return list(queryWrapper).stream().map(item -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(item, orderVO);
            return orderVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<FieldOrderVO> getFieldOrderList(Integer status, Long companyId, String userId, Long storeId) {
        List<Store> stores = storeService.getByCompanyId(companyId);
        if (CollectionUtils.isEmpty(stores) ){
            return Collections.emptyList();
        }
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        //兼容原来不传storeId逻辑
        //不传storeId，查询我的全部订单
        //传storeId，查询当前店铺下，我的订单
        if (storeId != null){
            List<Store> collect = stores.stream().filter(bean -> bean.getId().equals(storeId)).collect(Collectors.toList());
            if (collect == null || collect.size() == 0){
                return Collections.emptyList();
            }
            queryWrapper.lambda().eq(Order::getStoreId, storeId);
        }else {
            queryWrapper.lambda().in(Order::getStoreId, stores.stream().map(t -> t.getId()).collect(Collectors.toList()));
        }
        queryWrapper.lambda().eq(Order::getUserId, userId);
        Long categoryId = categoryService.getByCode(SportConstant.FIELD_CATEGORY_CODE);
        queryWrapper.lambda().eq(Order::getCategoryId, categoryId);
        if (status == -1) {
            queryWrapper.lambda().in(Order::getStatus, 2, 6, 9);
        } else {
            queryWrapper.lambda().eq(Order::getStatus, status);
        }
        queryWrapper.lambda().orderByDesc(Order::getCreateDate);
        List<Order> list = list(queryWrapper);

        List<FieldOrderVO> fieldOrderVOList = new ArrayList<>();
        for (Order item : list) {
            FieldOrderVO orderVO = new FieldOrderVO();
            Store store = stores.stream().filter(bean -> bean.getId().equals(item.getStoreId())).findFirst().get();
            orderVO.setStoreName(store.getName());
            orderVO.setStoreAddress(store.getAddress());
            BeanUtils.copyProperties(item, orderVO);
            Map<String, Map<String, List<FieldOrderDetailVO>>> fieldOrderDetailMap = new HashMap<>();
            List<FieldOrderDetailVO> orderDetailList = this.orderDetailService.getFieldOrderDetailList(item.getId());
            for (FieldOrderDetailVO orderDetail : orderDetailList) {
                String fieldDay = orderDetail.getFieldDay();
                if (!fieldOrderDetailMap.containsKey(fieldDay)) {
                    fieldOrderDetailMap.put(fieldDay, new HashMap<>());
                }
                if (StringUtils.isEmpty(orderVO.getVenueName())) {
                    orderVO.setVenueName(orderDetail.getVenueName());
                }
                Map<String, List<FieldOrderDetailVO>> fieldMap = fieldOrderDetailMap.get(fieldDay);
                if (!fieldMap.containsKey(orderDetail.getFieldName())) {
                    fieldMap.put(orderDetail.getFieldName(), new ArrayList<>());
                }
                fieldMap.get(orderDetail.getFieldName()).add(orderDetail);
            }
            orderVO.setFieldOrderDetailMap(fieldOrderDetailMap);
            fieldOrderVOList.add(orderVO);
        }

        return fieldOrderVOList;
    }

    @Override
    public IPage<FieldOrderManagerVO> getFieldManagerList(Map<String, Object> param) {
        //TODO 做权限验证
        int page = NumberUtils.toInt(param.get("page") + "", 1);
        int size = NumberUtils.toInt(param.get("size") + "", 10);
        if (param.get("phone") != null) {
            User user = userService.getByUserPhone(param.get("phone") + "");
            if (user != null) {
                param.put("orderUserId", user.getId());
            } else {
                param.put("orderUserId", "-1");
            }
        }
        IPage<FieldOrderManagerVO> orderPage = getBaseMapper().getStoreFieldOrderList(new Page<>(page, size), param);

        for (FieldOrderManagerVO orderVO : orderPage.getRecords()) {
            Map<String, Map<String, List<FieldOrderDetailVO>>> fieldOrderDetailMap = new HashMap<>();
            List<FieldOrderDetailVO> orderDetailList = this.orderDetailService.getFieldOrderDetailList(orderVO.getId());
            for (FieldOrderDetailVO orderDetail : orderDetailList) {
                String fieldDay = orderDetail.getFieldDay();
                if (!fieldOrderDetailMap.containsKey(fieldDay)) {
                    fieldOrderDetailMap.put(fieldDay, new HashMap<>());
                }
                if (StringUtils.isEmpty(orderVO.getVenueName())) {
                    orderVO.setVenueName(orderDetail.getVenueName());
                }
                Map<String, List<FieldOrderDetailVO>> fieldMap = fieldOrderDetailMap.get(fieldDay);
                if (!fieldMap.containsKey(orderDetail.getFieldName())) {
                    fieldMap.put(orderDetail.getFieldName(), new ArrayList<>());
                }
                fieldMap.get(orderDetail.getFieldName()).add(orderDetail);
            }
            orderVO.setFieldOrderDetailMap(fieldOrderDetailMap);
        }
        return orderPage;
    }

    @Override
    public IPage<OrderManagerVO> getManagerList(Map<String, Object> param) {
        int page = NumberUtils.toInt(param.get("page") + "", 1);
        int size = NumberUtils.toInt(param.get("size") + "", 10);
        if (param.get("phone") != null) {
            User user = userService.getByUserPhone(param.get("phone") + "");
            if (user != null) {
                param.put("orderUserId", user.getId());
            }
        }
        return getBaseMapper().getStoreOrderList(new Page<>(page, size), param);
    }


    @Override
    public OrderManagerDetailVO detail(String orderId) {
        Order order = getById(orderId);
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<OrderDetailVO> orderDetailList = orderDetailService.getByOrderIdList(orderId);
        OrderManagerDetailVO orderManagerDetailVO = new OrderManagerDetailVO();
        BeanUtils.copyProperties(order, orderManagerDetailVO);
        orderManagerDetailVO.setProductName(orderDetailList.get(0).getProductName());
        orderManagerDetailVO.setUserList(orderDetailList.stream().map(item -> {
            OrderUserVO orderUserVO = new OrderUserVO();
            orderUserVO.setPhone(item.getPhone());
            orderUserVO.setNickName(item.getUserName());
            // 获取userID 根据id查询name
           /* QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id",item.getUserId());
            User u = userService.getOne(userQueryWrapper);
            if ( u!=null ){
                orderUserVO.setNickName(u.getName());
            }*/
            return orderUserVO;
        }).collect(Collectors.toList()));
        return orderManagerDetailVO;
    }

    @Override
    public Date checkOrderPayFinishTime(String orderId, String payAppId, String payType, String payDate) throws Exception {
        CommonResponse<WxPayOrderQueryResultVO> response = wxMaUserClient.checkStatus(orderId, payAppId, payType, payDate);
        if (response.isSuccess()) {
            WxPayOrderQueryResultVO wxPayOrderQueryResultVO = response.getData();
            if (wxPayOrderQueryResultVO.getReturnCode().equalsIgnoreCase("SUCCESS") &&
                    wxPayOrderQueryResultVO.getResultCode().equalsIgnoreCase("SUCCESS") &&
                    wxPayOrderQueryResultVO.getTradeState().equalsIgnoreCase("SUCCESS")) {
                try {
                    return DateUtils.parseDate(wxPayOrderQueryResultVO.getTimeEnd(), "yyyyMMddHHmmss");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                log.error("微信支付结果查询:{}, code:{}, orderId:{}", wxPayOrderQueryResultVO.getErrCodeDes(), wxPayOrderQueryResultVO.getErrCode(), orderId);
//                OrderFail orderFail = new OrderFail();
//                orderFail.setOrderId(orderId);
//                orderFail.setErrorCode(result.getErrCode());
//                if (StringUtils.isNotBlank(result.getTradeStateDesc())){
//                    orderFail.setErrorMsg(result.getTradeStateDesc());
//                }else {
//                    orderFail.setErrorMsg(result.getErrCodeDes());
//                }
//                orderFailService.insert(orderFail);
            }
        }
        return null;
    }

    @Autowired
    private OrderTradeConfirmService orderTradeConfirmService;

    /**
     * 更新订单状态为成功
     *
     * @param orderId
     * @param payFinishTime
     * @return
     */
    @Override
    public synchronized boolean updateOrder2Success(String orderId, Date payFinishTime) {
        log.info("{}进行状态更新", orderId);
        if (payFinishTime == null) {
            return false;
        }
        // 活动项目订单
        Order order = getById(orderId);
        if (order == null || !order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            return true;
        }
        if (orderPaySuccess(order, payFinishTime)) {
            try {
                // 针对课程相关订单 进行交易分账确认
                orderTradeConfirmService.confirm(order, payFinishTime);
            }catch (Exception e) {
                e.printStackTrace();
                log.error("交易分账确认失败:{}", e);
            }
            if (orderId.startsWith("ED")) {
                // 课程 体验课 订单
                createExerciseOrderSuccess(order);
                // 针对灵活课包 将灵活课包更新为失效
                exerciseService.status2Invalid(Long.parseLong(order.getProductId()));
                return true;
            }else if (orderId.startsWith("CD")) {
                // 按此活动 报名人数更新
                Exercise exercise = exerciseService.getById(order.getProductId());
                updateExerciseEnrollNumber(exercise);
            }
            if (order.getSaleUserId() != null) {
                User user = userService.getById(order.getUserId());
                mqMessageService.sendUserAttributeModifyMessage(user.getPhone(), order.getSaleUserId(), 2, order.getStoreId(), "用户下单系统");
            }

        }
        return false;
    }

    /**
     * 订单支付成功操作
     *
     * @param order
     * @param payFinishTime
     * @return
     */
    private boolean orderPaySuccess(Order order, Date payFinishTime) {
        Exercise exercise = exerciseService.getById(order.getProductId());
        if (exercise.getCouponReceiveType() != null && exercise.getCouponReceiveType() == 1) {
            order.setStatus(OrderStatus.USING.getCode());
        } else {
            order.setStatus(OrderStatus.PAY.getCode());
        }
        order.setPayFinishTime(payFinishTime);
        if (updateById(order)) {
            List<OrderDetail> orderDetailList = orderDetailService.getByOrderId(order.getId());
            if (CollectionUtils.isNotEmpty(orderDetailList)) {
                for (OrderDetail orderDetail : orderDetailList) {
                    String consumeCode;
                    if (order.getCategoryId().equals(categoryService.getByCode(ExerciseCategory.COUNT.getCode()))) {
                        consumeCode = IdUtils.getConsumeCode("CD");
                    } else {
                        consumeCode = IdUtils.getConsumeCode("OD");
                    }
                    orderDetail.setConsumeCode(consumeCode);
                    if (OrderStatus.USING.getCode().equals(order.getStatus())) {
                        orderDetail.setStatus(OrderStatus.USED.getCode());
                    } else {
                        orderDetail.setStatus(OrderStatus.PAY.getCode());
                    }
                }
                return orderDetailService.updateBatchById(orderDetailList);
            }
            return true;
        }
        throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单更新失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean cancelOrder(String orderId, String reason) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        if (currentUser == null) {
            throw new SportException("登录状态异常");
        }
        Order order = getById(orderId); // 权限校验逻辑保持不变
        // 非用户自己取消，校验是否是管理员，非管理员不允许操作
        if (!order.getUserId().equals(currentUser.getUserId()) && !OauthUtils.isAdmin()) {
            throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "当前操作非法!");
        }
        if (order.getStatus().equals(OrderStatus.PAY.getCode())) {
            Category category = categoryService.getById(order.getCategoryId());
            if (category.getCode().equals(SportConstant.FIELD_CATEGORY_CODE)) {  //订场分类Code
                Store store = storeService.getById(order.getStoreId());
                Company company = companyService.getById(store.getCompanyId());

                StoreConfig limitConfig = null;
                if (OauthUtils.isAdmin()) {
                    limitConfig = storeConfigService.getStoreConfig(SysDictConstant.FIELD_ORDER_ADMIN_CANCEL_NOT_LIMIT, order.getStoreId()); // 修改配置键
                }
                if (limitConfig != null && Boolean.parseBoolean(limitConfig.getValue())) {
                    log.info("订场取消，已设置管理员不限制取消时间");
                } else {
                    List<FieldOrderDetailVO> fieldOrderDetailList = this.orderDetailService.getFieldOrderDetailList(order.getId());
                    Date date = null;
                    for (FieldOrderDetailVO item : fieldOrderDetailList) {
                        try {
                            Date fieldDay = DateUtils.parseDate(item.getFieldDay() + " " + item.getStartTime(), "yyyy-MM-dd HH:mm");
                            if (date == null || fieldDay.compareTo(date) < 0)
                                date = fieldDay;
                        } catch (ParseException e) {
                            throw new SportException(e.getMessage());
                        }
                    }

                    // 修改配置键名并调整时间计算逻辑
                    StoreConfig storeConfig = storeConfigService.getCompanyStoreConfig(company.getId(), SysDictConstant.FIELD_ORDER_CANCEL_HOURS_LIMIT);// 修改配置键

                    // 默认值改为小时单位（原1天=24小时）
//                    int cancelBookingDays = 1;
                    int cancelBookingHours = 24;
                    if (storeConfig != null) {
                        cancelBookingHours = Integer.parseInt(storeConfig.getValue());
                    }


                    // 计算最晚允许取消时间
                    Date latestCancelTime = DateUtils.addHours(date, -cancelBookingHours);
                    Date now = new Date();

                    if (now.after(latestCancelTime)) {
                        throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(),
                                String.format("需要提前%s小时取消订单, 在%s前可取消", cancelBookingHours, DateFormatUtils.format(latestCancelTime, "yyyy-MM-dd HH:mm")));
                    }
                }
            }
            return orderRefund(order, reason, true);
        } else if (order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            return cancelUnPayOrder(orderId, order.getPayAppId(), order.getActualAmount(), order.getUserCouponId(), order.getCreateDate(), order.getPayType());
        } else {
            throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "当前订单不可取消");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public boolean adminCancelOrder(String orderId) {
        LoginVal currentUser = OauthUtils.getCurrentUser();
        if (currentUser == null) {
            throw new SportException("非法操作");
        }
        Order order = getById(orderId);
        return orderRefund(order, "商家取消订单", Objects.equals(order.getStatus(), OrderStatus.PAY.getCode()) ||
                Objects.equals(order.getStatus(), OrderStatus.USING.getCode()) || Objects.equals(order.getStatus(), OrderStatus.UN_PAY.getCode()));
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public Boolean cancelFieldOrder(Order order, String reason) {
        return this.orderRefund(order, reason, false);
    }

    /**
     * 订单退款
     *
     * @param order
     * @param reason
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderRefund(Order order, String reason, boolean changeFieldStatus) {
        order.setStatus(OrderStatus.ORDER_REFUNDING.getCode());
        order.setRefundAmount(order.getCanRefundAmount());
        order.setCanRefundAmount(BigDecimal.ZERO);
        Order upOrder = new Order();
        upOrder.setId(order.getId());
        upOrder.setStatus(order.getStatus());
        upOrder.setRefundAmount(order.getRefundAmount());
        upOrder.setCanRefundAmount(order.getCanRefundAmount());
        if (!updateById(upOrder)) {
            throw new SportException("订单更新失败");
        }
        List<OrderDetail> odDetailList = orderDetailService.getByOrderId(upOrder.getId());
        List<OrderDetail> updateOrderDetailList = new ArrayList<>(odDetailList.size());
        for (OrderDetail orderDetail : odDetailList) {
            OrderDetail od = new OrderDetail();
            od.setId(orderDetail.getId());
            od.setStatus(upOrder.getStatus());
            od.setRefundAmount(orderDetail.getCanRefundAmount());
            od.setCanRefundAmount(BigDecimal.ZERO);
            updateOrderDetailList.add(od);
        }
        // 修改订单明细状态
        orderDetailService.updateBatchById(updateOrderDetailList);

        Category category = categoryService.getById(order.getCategoryId());
        List<FieldPlan> fieldPlanArrayList = new ArrayList<>();
        List<OrderDetail> orderDetailList = null;
        if (category.getCode().equals(SportConstant.FIELD_CATEGORY_CODE)) {
            if (changeFieldStatus) {
                orderDetailList = this.orderDetailService.getByOrderId(order.getId());
                for (OrderDetail orderDetail : orderDetailList) {
                    // 订场状态修改
                    FieldPlan fieldPlan = fieldPlanService.getById(Long.parseLong(orderDetail.getProductId()));
                    if (fieldPlan != null && !fieldPlan.getStatus().equals(FieldPlanStatus.Normal.getCode())) {
                        fieldPlan.setStatus(FieldPlanStatus.Normal.getCode());
                        this.fieldPlanService.updateById(fieldPlan);
                        fieldPlanArrayList.add(fieldPlan);
                    }
                }
            }
            // 储值卡 消费退回
            if (order.getUserVipCardId() != null) {
                List<UserBalanceChange> userBalanceChangeList = userBalanceChangeService.findByOrderId(order.getId());
                for (UserBalanceChange userBalanceChange : userBalanceChangeList) {
                    if (!userBalanceChange.getChangeType().equals(ChangeTypeStatus.Payment.getCode())) {
                        continue;
                    }
                    userVipCardService.refund(userBalanceChange);
                }
            }
        }
        //拼单产品，只退款，不退卡券类
        //其他产品，退款同时退还卡券
        if (!category.getCode().equals(ExerciseCategory.JOIN_EXERCISE.getCode())){
            if (order.getUserCouponId() != null) {
                iUserCouponService.couponBack(order.getUserCouponId());
            }
        }

        if (order.getRefundAmount().doubleValue() > 0) {
            // 创建退款申请记录
            OrderRefund orderRefund = orderRefundService.getByOrderId(order.getId());
            if (orderRefund == null) {
                orderRefund = new OrderRefund();
                orderRefund.setId(IdUtils.getStrNumberId("RD"));
                orderRefund.setOrderId(order.getId());
                orderRefund.setRefundAmount(order.getRefundAmount());
                orderRefund.setStatus(OrderStatus.ORDER_REFUNDING.getCode());
                orderRefund.setErrorCode(OrderStatus.ORDER_REFUNDING.getCode() + "");
                orderRefund.setErrorMsg("退款提交成功");
                orderRefund.setRemark(reason);

                if (!orderRefundService.save(orderRefund)) {
                    log.error("订单退款记录创建失败:{}", JacksonUtils.obj2Json(orderRefund));
                    throw new SportException("系统错误");
                }
            }

            if (order.getTradeConfirmId() != null) {
                // 对于已发起交易分账的订单，退款前 需要先进行交易分账的取消
                boolean confirmCancelResult = orderTradeConfirmService.confirmCancel(order.getTradeConfirmId());
                if (!confirmCancelResult) {
                    log.error("交易分账确认取消失败:{}", order.getTradeConfirmId());
                    throw new SportException("订单退款失败, 请联系商户处理");
                }
            }

            // 先进行微信退款发起
            RefundMoneyForm refundMoneyForm = new RefundMoneyForm();
            refundMoneyForm.setAppId(order.getPayAppId());
            refundMoneyForm.setRefundId(orderRefund.getId());
            refundMoneyForm.setPayType(order.getPayType());
            refundMoneyForm.setOrderId(order.getId());
            refundMoneyForm.setPayDate(order.getPayFinishTime());
            // 退款总金额 是该订单实际微信支付的总金额 就是实付金额
            refundMoneyForm.setTotalFee(order.getActualAmount());
            refundMoneyForm.setRefundFee(order.getRefundAmount());
//            refundMoneyForm.setTotalFee(new BigDecimal("0.01"));
//            refundMoneyForm.setRefundFee(new BigDecimal("0.01"));
            refundMoneyForm.setRefundDesc(reason);
            if (order.getPayType().equals(PayType.Wechat.getCode())) {
                refundMoneyForm.setNotifyUrl(sportServerPrefix + "/api/v1/notice/wxRefund");
            } else {
                refundMoneyForm.setNotifyUrl(sportServerPrefix + "/api/v1/notice/hf-refund-callback");
            }
            CommonResponse<Boolean> response = wxMaUserClient.refundMoney(refundMoneyForm);
            if (response.isSuccess()) {
                log.info("退款创建成功:" + JacksonUtils.obj2Json(refundMoneyForm));
            } else {
                log.error("退款创建失败:" + JacksonUtils.obj2Json(refundMoneyForm));
                log.error("失败编号:{}, 失败错误:{}", response.getCode(), response.getMessage());
                throw new SportException("退款失败, 请联系商户处理");
            }
        } else {
            order.setStatus(OrderStatus.REFUND.getCode());
            Order updateOrder = new Order();
            updateOrder.setId(order.getId());
            updateOrder.setStatus(OrderStatus.REFUND.getCode());
            if (!updateById(updateOrder)) {
                throw new SportException("订单更新失败");
            }
            orderDetailService.changeStatusByOrderId(updateOrder.getId(), updateOrder.getStatus());
        }
        if(CollectionUtils.isNotEmpty(fieldPlanArrayList)){
            //场地状态变动，发送mq消息通知进行订场同步
            FieldSyncMessage fieldSyncMessage = new FieldSyncMessage();
            fieldSyncMessage.setStatus(fieldPlanArrayList.get(0).getStatus());
            fieldSyncMessage.setChannel(FieldPlanLockChannels.Current.getCode());
            fieldSyncMessage.setFieldPlanIdList(fieldPlanArrayList.stream().map(FieldPlan::getId).collect(Collectors.toList()));
            mqMessageService.sendFieldSyncMessage(fieldSyncMessage);

        }
        // 发送订单取消通知 只针对 订场订单
        if (CollectionUtils.isNotEmpty(orderDetailList)) {
            for (OrderDetail od : orderDetailList) {
                FieldPlan fieldPlan = fieldPlanService.getById(Long.parseLong(od.getProductId()));
                // 获取订场的场地名称
                String[] productNames = od.getProductName().split(" ");
                if (productNames.length != 3) {
                    log.error("历史订单，不发生消息通知");
                    continue;
                }
                // 发送订场取消通知
                StoreVenue storeVenue = storeVenueService.getById(fieldPlan.getVenueId());
                Store store = storeService.getById(order.getStoreId());
                Map<String, Object> msgMap = new HashMap<>();
                msgMap.put("orderId", order.getId());
                msgMap.put("userId", order.getUserId());
                msgMap.put("storeName", store.getName() + " " + storeVenue.getName());
                msgMap.put("fieldName", productNames[0]);
                msgMap.put("time", productNames[1] + " " + productNames[2]);
                msgMap.put("user", "尾号" + od.getPhone().substring(od.getPhone().length() - 4));
                mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.FIELD_CANCEL.getCode(), order.getStoreId());
            }
        }
        return true;
    }

    /**
     * 取消未支付的订单
     *
     * @param id
     * @param appId
     * @param actualAmount
     * @param userCouponId
     * @return
     */
    private boolean cancelUnPayOrder(String id, String appId, BigDecimal actualAmount, Long userCouponId, Date payDate, String payType) {
        Order updateOrder = new Order();
        updateOrder.setId(id);
        updateOrder.setStatus(OrderStatus.ORDER_UN_PAY_CANCEL.getCode());
        if (updateById(updateOrder)) {
            if (userCouponId != null) {
                iUserCouponService.couponBack(userCouponId);
            }
            if (actualAmount.doubleValue() > 0 && StringUtils.isNotEmpty(appId)) {
                if (!closeWxOrder(id, appId, DateFormatUtils.format(payDate, "yyyyMMdd"), payType)) {
                    log.error("订单关闭失败:{}", id);
                    throw new SportException("订单关闭失败");
                } else {
                    log.info("订单:{}已关闭", id);
                }
            }
            //TODO 后续可发送取消订单的通知
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelUnPayOrder(String id) {
        Order updateOrder = this.getById(id);
        if (!updateOrder.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            return false;
        }
        return cancelUnPayOrder(updateOrder.getId(), updateOrder.getPayAppId(), updateOrder.getActualAmount(), updateOrder.getUserCouponId(), updateOrder.getCreateDate(), updateOrder.getPayType());
    }

    /**
     * 关闭微信的订单
     *
     * @param id
     * @return
     */
    private boolean closeWxOrder(String id, String appId, String payDate, String payType) {
        CommonResponse<Boolean> response = wxMaUserClient.closeOrder(id, appId, payDate, payType);
        if (response.isSuccess()) {
            return true;
        } else {
            log.error("微信订单:{}, appId:{}关闭失败:", id, appId);
        }
        return false;
    }

    @Override
    public Integer getUnPayAndUnUseOrderNumber(String userId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId, userId)
                .in(Order::getStatus, OrderStatus.UN_PAY.getCode(), OrderStatus.PAY.getCode());
        return count(queryWrapper);
    }

    @Override
    public Integer getUnUseOrderNumber(String userId, Long companyId) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetail::getUserId, userId)
                .eq(OrderDetail::getCategoryId, categoryService.getByCode(ExerciseCategory.OFFLINE_PUSH.getCode()))
                .in(OrderDetail::getStatus, OrderStatus.PAY.getCode()).in(OrderDetail::getCompanyId, companyId);
        return orderDetailService.count(queryWrapper);
    }

    @Override
    public List<Order> getAvailableDateRangeAndStatus(Long storeId, Date start, Date end, String saleUserId, String categoryByCode, Integer... status) {
        Category category = this.categoryService.getOneCategoryByCode(categoryByCode);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getStoreId, storeId)
                .eq(Order::getCategoryId, category.getId())
                .in(Order::getStatus, status)
                .ge(Order::getCreateDate, start)
                .le(Order::getCreateDate, end);
        if (StringUtils.isNotEmpty(saleUserId)) {
            queryWrapper.lambda().eq(Order::getSaleUserId, saleUserId);
        }
        return list(queryWrapper);
    }

    @Override
    public List<Order> getAvailableDateRangeAndStatus(Long storeId, Date start, Date end, String saleUserId, boolean isActive, Integer... status) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        Long categoryId = categoryService.getByCode(SportConstant.FIELD_CATEGORY_CODE);
        Long vipCategoryId = categoryService.getByCode(SportConstant.VIP_CATEGORY_CODE);
        if (isActive) {
            // 激活订单排除体验课
            Long activityId = categoryService.getByCode("activity");
            Long experienceCategoryId = categoryService.getByCode("experience_class");
            queryWrapper.lambda().eq(Order::getStoreId, storeId)
                    .in(Order::getStatus, status)
                    .notIn(Order::getCategoryId, categoryId, vipCategoryId, experienceCategoryId, activityId)
                    .isNotNull(Order::getActiveTime)
                    .ge(Order::getActiveTime, start)
                    .le(Order::getActiveTime, end);
        } else {
            queryWrapper.lambda().eq(Order::getStoreId, storeId)
                    //查询未在指定时间范围内（start～end）激活的订单
                    //状态为未激活
                    //或者激活时间非空，大于等于end时间
//                    .in(Order::getStatus, status)
                    .and(wrapper -> {
                        wrapper.in(Order::getStatus, status)
                                .or(wrapper2 -> wrapper2.isNotNull(Order::getActiveTime).ge(Order::getActiveTime, end));
                    })
                    .notIn(Order::getCategoryId, categoryId, vipCategoryId)
                    .ge(Order::getCreateDate, start)
                    .le(Order::getCreateDate, end);
        }
        if (StringUtils.isNotEmpty(saleUserId)) {
            queryWrapper.lambda().eq(Order::getSaleUserId, saleUserId);
        }
        return list(queryWrapper);
    }

    @Override
    public Order getNewSignatureOrder(Long storeId, Long categoryId, String userId, List<Integer> status, Date startDate) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getStoreId, storeId)
                .in(Order::getStatus, status)
                .eq(Order::getCategoryId, categoryId)
                .eq(Order::getUserId, userId)
                .lt(Order::getCreateDate, startDate)
                .orderByAsc(Order::getCreateDate).last("LIMIT 1");
        return getOne(queryWrapper);
    }

    @Override
    public List<Order> getByUserId(String userId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getUserId, userId);
        return list(queryWrapper);
    }

    @Override
    public boolean checkCodeToUsing(String orderId, String code) {
        Order order = getById(orderId);
        if (order.getStatus().equals(OrderStatus.PAY.getCode())) {
            Order update = new Order();
            update.setId(orderId);
            update.setStatus(OrderStatus.USING.getCode());
            if (StringUtils.isNotEmpty(code) && code.startsWith("CD")) {
                // 设置订单的开始时间
                update.setStartTime(new Date());
            }
            return updateById(update);
        }
        return true;
    }

    @Override
    public Integer getExperienceNumber(Date start, Date end, Long storeId, String saleUserId) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getStoreId, storeId)
                .in(Order::getStatus, OrderStatus.PAY.getCode(), OrderStatus.USING.getCode(),
                        OrderStatus.USED.getCode(), OrderStatus.ORDER_FINISH.getCode())
                .eq(Order::getCategoryId, categoryService.getByCode(ExerciseCategory.EXPERIENCE.getCode()))
                .ge(Order::getCreateDate, start)
                .le(Order::getCreateDate, end);
        if (StringUtils.isNotEmpty(saleUserId)) {
            queryWrapper.lambda().eq(Order::getSaleUserId, saleUserId);
        }
        return count(queryWrapper);
    }

    @Override
    public IPage<UserInfoOrderVO> getExperiential(StoreExperienceOrderForm form) {
        if (form.getPage() == null) {
            form.setPage(1);
        }
        if (form.getSize() == null) {
            form.setSize(10);
        }
        return getBaseMapper().getExperiential(new Page<>(form.getPage(), form.getSize()),
                form.getStart(), form.getEnd(), form.getStoreId(),
                categoryService.getByCode(ExerciseCategory.EXPERIENCE.getCode()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Order createFinishOrder(OrderForm form) {
        Exercise exercise = exerciseService.getById(form.getProductId());
        if (exercise == null) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), form.getProductId() + "活动不存在!");
        }
        String[] phoneArgs = form.getPhoneList().split(",");
        if (phoneArgs.length == 0) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "报名人数不能为空");
        }
        Store store = storeService.getById(exercise.getStoreId());
        Integer count = phoneArgs.length;
        String orderId = IdUtils.getStrNumberId("ED");
        Order order = new Order();
        order.setId(orderId);
        order.setName(exercise.getName());
        order.setCategoryId(exercise.getCategoryId());
        order.setUserId(form.getUserId());
        order.setProductId(form.getProductId());
        order.setCount(count);
        order.setStatus(OrderStatus.USING.getCode());
        order.setRemark(form.getRemark());

        order.setStartTime(exercise.getStartTime());
        order.setEndTime(exercise.getEndTime());
        order.setAddress(store.getAddress());
        order.setStoreId(store.getId());
        order.setCompanyId(store.getCompanyId());

        order.setAmount(exercise.getPrice().multiply(new BigDecimal(count)));
        order.setConsumeAmount(order.getAmount());
        order.setActualAmount(order.getConsumeAmount());

        if (!save(order)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }

        // 创建订单明细
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setCategoryId(order.getCategoryId());
            orderDetail.setOrderId(order.getId());
            orderDetail.setProductId(order.getProductId());
            orderDetail.setProductName(exercise.getName());
            orderDetail.setProductPrice(exercise.getPrice());
            orderDetail.setProductQuantity(1);
            orderDetail.setProductImage(exercise.getImage());
            orderDetail.setStatus(OrderStatus.USING.getCode());
            orderDetail.setUserId(order.getUserId());
            if (phoneArgs.length > 1) {
                orderDetail.setPhone(phoneArgs[i]);
            } else {
                orderDetail.setPhone(phoneArgs[0]);
            }

            orderDetail.setStoreId(exercise.getStoreId());
            orderDetail.setCompanyId(store.getCompanyId());
            orderDetailList.add(orderDetail);
        }
        if (!orderDetailService.saveBatch(orderDetailList)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }
        return order;
    }

    @Override
    public void testOrder(Long exerciseId, int number) {
        Exercise exercise = exerciseService.getById(exerciseId);
        Store store = storeService.getById(exercise.getStoreId());
        List<User> testUserList = userService.getTestUser(number);
        log.info("创建测试订单:" + testUserList.size());
        for (User user : testUserList) {
            String orderId = IdUtils.getStrNumberId("ED");
            Order order = new Order();
            order.setId(orderId);
            order.setName(exercise.getName());
            order.setCategoryId(exercise.getCategoryId());
            order.setUserId(user.getId());
            order.setProductId(exercise.getId() + "");
            order.setCount(1);
            order.setStatus(OrderStatus.ORDER_FINISH.getCode());

            order.setStartTime(exercise.getStartTime());
            order.setEndTime(exercise.getEndTime());
            order.setAddress(store.getAddress());
            order.setStoreId(store.getId());
            order.setCompanyId(store.getCompanyId());

            order.setAmount(exercise.getPrice().multiply(new BigDecimal(1)));
            order.setConsumeAmount(order.getAmount());
            order.setActualAmount(order.getConsumeAmount());

            if (!save(order)) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
            }

            // 创建订单明细
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setCategoryId(order.getCategoryId());
            orderDetail.setOrderId(order.getId());
            orderDetail.setProductId(order.getProductId());
            orderDetail.setProductName(exercise.getName());
            orderDetail.setProductPrice(exercise.getPrice());
            orderDetail.setProductQuantity(1);
            orderDetail.setProductImage(exercise.getImage());
            orderDetail.setStatus(OrderStatus.ORDER_FINISH.getCode());
            orderDetail.setUserId(order.getUserId());
            orderDetail.setPhone(user.getPhone());

            orderDetail.setStoreId(exercise.getStoreId());
            orderDetail.setCompanyId(store.getCompanyId());

            orderDetailService.save(orderDetail);
        }
    }

    @Override
    public List<AnalysisOrderVO> getAnalysisOrderList(Date start, Date end, Long storeId, Integer status, String saleUserId) {
        return getBaseMapper().getAnalysisOrderList(start, end, storeId, status, saleUserId);
    }

    @Override
    public List<AnalysisOrderVO> getOrderListById(List<String> orderIdList) {
        return getBaseMapper().getOrderListById(orderIdList);
    }

    @Override
    public List<AnalysisOrderVO> getRenewalOrderList(Date start, Date end, Long storeId, List<Integer> statusList, String saleUserId) {
        return getBaseMapper().getRenewalOrderList(start, end, storeId, statusList, saleUserId);
    }

    @Override
    public List<InvoiceRequestOrderVO> getInvoiceRequestOrderList(String startStr, String endStr, Long companyId, String username) throws ParseException {
        if(!StringUtils.isEmpty(endStr)) {
            endStr += " 23:59:59";
        }
        List<Integer> statusList = Arrays.asList(OrderStatus.PAY.getCode(), OrderStatus.USING.getCode(), OrderStatus.ORDER_FINISH.getCode(), OrderStatus.USED.getCode());
        List<InvoiceRequestOrderVO> invoiceRequestOrderList = getBaseMapper().getInvoiceRequestOrderList(startStr, endStr, companyId, statusList, userService.getUserIdFromCache(username));
        for (InvoiceRequestOrderVO invoiceRequestOrderVO : invoiceRequestOrderList) {
            if (invoiceRequestOrderVO.getCategoryCode().equals(SportConstant.FIELD_CATEGORY_CODE)) {
                String venueName = "";
                Map<String, Map<String, List<FieldOrderDetailVO>>> fieldOrderDetailMap = new HashMap<>();
                List<FieldOrderDetailVO> orderDetailList = this.orderDetailService.getFieldOrderDetailList(invoiceRequestOrderVO.getOrderId());
                for (FieldOrderDetailVO orderDetail : orderDetailList) {
                    String fieldDay = orderDetail.getFieldDay();
                    if (!fieldOrderDetailMap.containsKey(fieldDay)) {
                        fieldOrderDetailMap.put(fieldDay, new HashMap<>());
                    }
                    venueName = orderDetail.getVenueName();
                    Map<String, List<FieldOrderDetailVO>> fieldMap = fieldOrderDetailMap.get(fieldDay);
                    if (!fieldMap.containsKey(orderDetail.getFieldName())) {
                        fieldMap.put(orderDetail.getFieldName(), new ArrayList<>());
                    }
                    fieldMap.get(orderDetail.getFieldName()).add(orderDetail);
                }
                List<String> dayStrList = new ArrayList<>();
                for (String day : fieldOrderDetailMap.keySet()) {
                    List<String> fieldStrList = new ArrayList<>();
                    Map<String, List<FieldOrderDetailVO>> fieldMap = fieldOrderDetailMap.get(day);
                    for (String fieldKey : fieldMap.keySet()) {
                        List<String> timeStrList = fieldMap.get(fieldKey).stream().map(t -> String.format("[%s - %s]", t.getStartTime(), t.getEndTime())).collect(Collectors.toList());
                        fieldStrList.add(String.format("%s - %s", fieldKey, String.join(" ", timeStrList)));
                    }
                    dayStrList.add(String.format("%s %s", day, String.join("  ", fieldStrList)));
                }
                invoiceRequestOrderVO.setOrderName(String.format("%s  %s", venueName, String.join(" , ", dayStrList)));
            }
        }
        return invoiceRequestOrderList;
    }

    /**
     * 生成订单协议
     *
     * @param userId
     * @param productId
     * @param templateName
     * @param outFile
     * @return
     */
    private boolean createAgreement(String userId, String productId, String templateName, String outFile) {
        Exercise exercise = exerciseService.getById(productId);
        String agreementFile = sportAgreementPath + templateName;
        log.info("协议:" + agreementFile);
        File file = new File(agreementFile);
        if (!file.exists()) {
            log.error("{}协议不存在", agreementFile);
            return false;
        }
        User user = userService.getById(userId);
        List<ExerciseCouponSimpleVO> ecList = exerciseCouponService.getByExerciseId(NumberUtils.toLong(productId));

        Map<String, String> param = new HashMap<>();
        param.put("name", user.getName());
        param.put("phone", user.getPhone());
        param.put("orderName", exercise.getName());
        param.put("signDate", DateFormatUtils.format(new Date(), "yyyy年MM月dd日"));
        if (CollectionUtils.isNotEmpty(ecList)) {
            ExerciseCouponSimpleVO ec = ecList.get(0);
            param.put("couponNumber", ec.getCouponNumber() + "张");
            if (ec.getCouponExpireTime() != null) {
                param.put("validDate", "有效期到" + DateFormatUtils.format(ec.getCouponExpireTime(), "yyyy年MM月dd日"));
            } else {
                param.put("validDate", "激活后" + ec.getCouponExpireDay() + "天有效");
            }
        } else {
            param.put("couponNumber", "");
            param.put("validDate", "");
        }
        try {
            return WordUtil.createAgreement2Oss(agreementFile, param, outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String previewAgreement(String userId, Long categoryId, String productId) {
        boolean isVip = false;
        if (categoryId == 0) {
            isVip = true;
        } else if (categoryId != null) {
            Category category = categoryService.getById(categoryId);
            if (category != null && category.getCode().contains("vip_")) {
                isVip = true;
            }
        }
        String agreementKeyPre = isVip ? RedisConstant.AGREEMENT_VIP_ORDER_KEY : RedisConstant.AGREEMENT_ORDER_KEY;
        String agreementKey = agreementKeyPre + userId + "_" + productId;
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String oldAgreement = valueOperations.get(agreementKey);
        if (StringUtils.isNotEmpty(oldAgreement)) {
            return FileUtil.getAgreementBase64(oldAgreement);
        }
        if (isVip) {
            Long vipCardId = Long.valueOf(productId);
            VipCard vipCard = vipCardService.getById(vipCardId);
            Company company = companyService.getById(vipCard.getCompanyId());
            if (StringUtils.isNotEmpty(company.getVipAgreementTemplate())) {
                String outFile = "doc/preview/" + userId + "_vip_" + vipCardId + ".docx";
                if (createVipAgreement(userId, vipCardId, company.getVipAgreementTemplate(), outFile)) {
                    valueOperations.set(agreementKey, outFile);
                    return FileUtil.getAgreementBase64(outFile);
                }
            }
        } else {
            Exercise exercise = exerciseService.getById(productId);
            if (StringUtils.isNotEmpty(exercise.getAgreementTemplate())) {
                String outFile = "doc/preview/" + userId + "_" + productId + ".docx";
                if (createAgreement(userId, productId, exercise.getAgreementTemplate(), outFile)) {
                    valueOperations.set(agreementKey, outFile);
                    return FileUtil.getAgreementBase64(outFile);
                }
            }
        }
        return null;
    }

    private boolean createVipAgreement(String userId, Long vipCardId, String templateName, String outFile) {
        VipCard vipCard = vipCardService.getById(vipCardId);
        String agreementFile = sportAgreementPath + templateName;
        log.info("协议:" + agreementFile);
        File file = new File(agreementFile);
        if (!file.exists()) {
            log.error("{}协议不存在", agreementFile);
            return false;
        }
        User user = userService.getById(userId);

        Map<String, String> param = new HashMap<>();
        param.put("name", user.getName());
        param.put("phone", user.getPhone());
        param.put("orderName", vipCard.getName());
        param.put("signDate", DateFormatUtils.format(new Date(), "yyyy年MM月dd日"));
        param.put("sellingPrice", vipCard.getSellingPrice().toString());
        param.put("storedPrice", vipCard.getStoredPrice().toString());
        param.put("validDate", "");
        try {
            return WordUtil.createAgreement2Oss(agreementFile, param, outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String previewOrderAgreement(String orderId) {
        Order order = getById(orderId);
        if (order != null) {
            String agreementUrl = order.getAgreementUrl();
            if (StringUtils.isNotEmpty(agreementUrl)) {
                return FileUtil.getAgreementBase64(agreementUrl);
            } else {
                throw new SportException("该订单没有合同信息");
            }
        }
        throw new SportException("订单不存在");
    }

    private int getOrderCountByStatus(Long storeId, Object... values) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Order::getStoreId, storeId)
                .in(Order::getStatus, values);
        return count(queryWrapper);
    }

    @Override
    public OrderNumberVO getOrderNumber(Long storeId) {
        OrderNumberVO orderNumberVO = new OrderNumberVO();
        orderNumberVO.setUnPay(getOrderCountByStatus(storeId, OrderStatus.UN_PAY.getCode()));
        orderNumberVO.setUnUse(getOrderCountByStatus(storeId, OrderStatus.PAY.getCode()));
        orderNumberVO.setUsing(getOrderCountByStatus(storeId, OrderStatus.USING.getCode()));
        orderNumberVO.setFinish(getOrderCountByStatus(storeId, OrderStatus.ORDER_FINISH.getCode()));
        return orderNumberVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized WxMaPayVO createField(FieldForm form) {
        List<Long> fieldIdList = form.getFieldIdList();
        if (CollectionUtils.isEmpty(fieldIdList)) {
            throw new SportException("场地不能为空");
        }
        UserVipCard userVipCard = null;
        VipCard vipCard = null;
        if (form.getUserVipCardId() != null) {
            userVipCard = userVipCardService.getById(form.getUserVipCardId());
            if (userVipCard.getVipCardId() != null) {
                vipCard = vipCardService.getById(userVipCard.getVipCardId());
            }
        }
        StoreVenue storeVenue = this.storeVenueService.getById(form.getVenueId());
        if (!storeVenue.getBookOpen()) {
            throw new SportException("该场馆暂不可预订");
        }

        Company company = companyService.getById(storeVenue.getCompanyId());
        if (StringUtils.isEmpty(company.getMiniAppId())) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "公司参数配置异常");
        }
        if (PayType.Wechat.getCode().equals(company.getPayType())) {
            if (StringUtils.isEmpty(company.getMiniAppPayId())) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "公司参数配置异常");
            }
        } else if (PayType.HF.getCode().equals(company.getPayType())) {
            if (StringUtils.isEmpty(company.getHuiFuFieldId())) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "公司参数配置异常");
            }
        }
        String orderId = IdUtils.getStrNumberId("FD");
        Order order = new Order();
        order.setId(orderId);
        order.setName("订场" + "-" + storeVenue.getName());
        Long categoryId = categoryService.getByCode(SportConstant.FIELD_CATEGORY_CODE);
        order.setCategoryId(categoryId);
        order.setStatus(OrderStatus.UN_PAY.getCode());
        order.setUserId(form.getUserId());
        order.setCount(fieldIdList.size());
        order.setStoreId(storeVenue.getStoreId());
        order.setCompanyId(storeVenue.getCompanyId());
        order.setUserVipCardId(form.getUserVipCardId());

        // 创建订单明细
        List<OrderDetail> orderDetailList = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal vipAmount = BigDecimal.ZERO;
        List<FieldBookUser> fieldBookUserList = new ArrayList<>();
        List<String> errList = new ArrayList<>();
        for (Long fpId : fieldIdList) {
            FieldPlan fp = this.fieldPlanService.getById(fpId);
            VenueField venueField = this.venueFieldService.getById(fp.getFieldId());
            String name = String.format("%s %s %s~%s", venueField.getName(),
                    DateFormatUtils.format(fp.getFieldDay(), "yyyy年MM月dd日"),
                    DateFormatUtils.format(fp.getStartTime(), "HH:mm"),
                    DateFormatUtils.format(fp.getEndTime(), "HH:mm"));
            if (fp.getStatus() != 1) {
                errList.add(String.format("%s已被预定或失效", name));
                continue;
            }
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setStatus(OrderStatus.UN_PAY.getCode());
            orderDetail.setCategoryId(order.getCategoryId());
            orderDetail.setOrderId(order.getId());
            orderDetail.setProductId(fp.getId() + "");
            orderDetail.setProductName(name);
            orderDetail.setProductPrice(fp.getPrice());
            orderDetail.setStartTime(SportDateUtils.combineDateAndTime(fp.getFieldDay(), fp.getStartTime()));
            orderDetail.setEndTime(SportDateUtils.combineDateAndTime(fp.getFieldDay(), fp.getEndTime()));
            // 明细原价
            orderDetail.setAmount(fp.getPrice());
            amount = amount.add(orderDetail.getAmount());
            BigDecimal vipPrice = fp.getVipPrice() != null ? fp.getVipPrice() : fp.getPrice();
            if (vipCard != null) {
                PriceRule priceRule = vipCardService.matchingPriceRule(vipCard, fp);
                if (priceRule != null) {
                    //会员卡特殊订场价格按照1小时价格算，兼容订场半小时
                    // 订场价格单位如果是半小时，取会员卡特殊订场价格/2，
                    // 订场价格单位如果是1小时，取会员卡特殊订场价格
                    Long minute = (fp.getEndTime().getTime() - fp.getStartTime().getTime())/(1000*60);
                    if (minute == 30){
                        vipPrice = priceRule.getVipPrice().divide(BigDecimal.valueOf(2), 2 , RoundingMode.HALF_UP);
                    }else {
                        vipPrice = priceRule.getVipPrice();
                    }
                }
                orderDetail.setActualAmount(BigDecimal.ZERO);
                orderDetail.setCanRefundAmount(BigDecimal.ZERO);
            } else {
                // 没有会员卡 需要实际支付
                orderDetail.setActualAmount(vipPrice);
                orderDetail.setCanRefundAmount(orderDetail.getActualAmount());
            }
            orderDetail.setRefundAmount(BigDecimal.ZERO);
            // 消费金额是实际价格
            orderDetail.setConsumeAmount(vipPrice);
            // 优惠金额
            orderDetail.setCouponAmount(orderDetail.getAmount().subtract(orderDetail.getConsumeAmount()));
            vipAmount = vipAmount.add(vipPrice);
            orderDetail.setProductQuantity(1);
            orderDetail.setUserId(order.getUserId());
            orderDetail.setPhone(form.getPhone());

            orderDetail.setStoreId(venueField.getStoreId());
            orderDetail.setCompanyId(order.getCompanyId());
            orderDetail.setConsumeCode(IdUtils.getConsumeCode("FD"));
            orderDetailList.add(orderDetail);

            FieldBookUser fieldBookUser = new FieldBookUser();
            fieldBookUser.setUserId(order.getUserId());
            fieldBookUser.setFieldPlanId(fp.getId());
            fieldBookUser.setGender(form.getGender());
            fieldBookUser.setUsername(form.getUsername());
            fieldBookUser.setPhone(form.getPhone());
            fieldBookUser.setOrderId(orderId);
            fieldBookUserList.add(fieldBookUser);
        }

        if (!errList.isEmpty()) {
            throw new SportException(String.join(";", errList));
        }

        // 优惠券信息
        Long couponId = form.getUserCouponId();
        BigDecimal couponCost = BigDecimal.ZERO;
        if (couponId != null) {
            SettlementInfo settlementInfo = new SettlementInfo();
            settlementInfo.setUserId(form.getUserId());
            settlementInfo.setEmploy(false);

            List<SettlementInfo.CouponAndTemplateInfo> couponAndTemplateInfos = new ArrayList<>();
            SettlementInfo.CouponAndTemplateInfo ct = new SettlementInfo.CouponAndTemplateInfo();
            ct.setId(couponId);
            UserCoupon coupon = iUserCouponService.getById(couponId);
            if (coupon.getTemplateSDK() != null) {
                ct.setTemplate(coupon.getTemplateSDK());
            } else {
                ct.setTemplate(templateBaseService.getCouponTemplateSdk(coupon.getTemplateId(), null, null));
            }
            couponAndTemplateInfos.add(ct);
            settlementInfo.setCouponAndTemplateInfos(couponAndTemplateInfos);
            BigDecimal price = amount;
            // 优惠券 和 会员卡优惠 同时享受
            if (userVipCard != null && userVipCard.getBalance().compareTo(vipAmount) >= 0) {
                price = vipAmount;
            }
            // 设置商品信息
            List<GoodsInfo> goodsInfos = new ArrayList<>();
            GoodsInfo goodsInfo = new GoodsInfo();
            goodsInfo.setCount(1);
            goodsInfo.setPrice(price.doubleValue());
            goodsInfos.add(goodsInfo);
            settlementInfo.setGoodsInfos(goodsInfos);

            settlementInfo = iUserCouponService.settlement(settlementInfo);
            if (settlementInfo.getCouponCost() != null)
                couponCost = new BigDecimal(settlementInfo.getCouponCost().toString());
        }

        order.setUserCouponId(couponId);
        order.setAmount(amount);

        if (userVipCard == null || userVipCard.getBalance().equals(BigDecimal.ZERO)) {
            // 会员卡没有 或者会员卡余额是 0 微信支付
            order.setConsumeAmount(amount.subtract(couponCost));
            order.setActualAmount(order.getConsumeAmount());
            order.setCanRefundAmount(order.getActualAmount());
            // 更新订单明细 金额信息
            for (OrderDetail od : orderDetailList) {
                BigDecimal odCost = BigDecimal.ZERO;
                if (couponCost.doubleValue() > 0) {
                    if (order.getConsumeAmount().doubleValue() == 0) {
                        odCost = od.getAmount();
                    } else {
                        // 根据单个商品的价格比重分配优惠比例
                        odCost = couponCost.multiply(od.getConsumeAmount().divide(order.getConsumeAmount(), 2, RoundingMode.HALF_UP));
                    }
                }
                od.setConsumeAmount(od.getAmount().subtract(odCost));
                od.setActualAmount(od.getConsumeAmount());
                od.setCanRefundAmount(od.getActualAmount());
                od.setCouponAmount(od.getAmount().subtract(od.getConsumeAmount()));
            }
        } else {
            boolean canPay = userVipCard.getBalance().compareTo(vipAmount) >= 0;
            if (canPay) {
                // 会员卡可以完全支付
                order.setConsumeAmount(vipAmount.subtract(couponCost));
                order.setActualAmount(BigDecimal.ZERO);
                order.setCanRefundAmount(BigDecimal.ZERO);

                // 更新订单明细 金额信息
                for (OrderDetail od : orderDetailList) {
                    BigDecimal odCost = BigDecimal.ZERO;
                    if (couponCost.doubleValue() > 0) {
                        if (order.getConsumeAmount().doubleValue() == 0) {
                            odCost = od.getAmount();
                        } else {
                            // 根据单个商品的价格比重分配优惠比例
                            odCost = couponCost.multiply(od.getConsumeAmount().divide(order.getConsumeAmount(), 2, RoundingMode.HALF_UP));
                        }
                    }
                    od.setConsumeAmount(od.getConsumeAmount().subtract(odCost));
                    od.setActualAmount(BigDecimal.ZERO);
                    od.setCanRefundAmount(BigDecimal.ZERO);
                    od.setCouponAmount(od.getAmount().subtract(od.getConsumeAmount()));
                }
            } else {
                // 减去会员卡余额之后 还需要微信支付
                order.setConsumeAmount(amount.subtract(couponCost));
                order.setActualAmount(order.getConsumeAmount().subtract(userVipCard.getBalance()));
                order.setCanRefundAmount(order.getConsumeAmount().subtract(userVipCard.getBalance()));
                order.setPayType(PayType.MIX.getCode());

                // 更新订单明细 金额信息
                for (OrderDetail od : orderDetailList) {
                    BigDecimal odCost = BigDecimal.ZERO;
                    if (couponCost.doubleValue() > 0) {
                        // 根据单个商品的价格比重分配优惠比例
                        log.info("couponCost:" + couponCost + " amount:" + od.getAmount() + " order-amount:" + order.getConsumeAmount());
                        odCost = couponCost.multiply(od.getAmount().divide(order.getAmount(), 2, RoundingMode.HALF_UP));
                    }
                    od.setPayType(order.getPayType());
                    log.info("odCost:" + odCost);
                    // 49 -
                    od.setConsumeAmount(od.getAmount().subtract(odCost));
                    // 订单明细实付金额 = 订单实付金额 * 订单比重
                    od.setActualAmount(order.getActualAmount().multiply(od.getAmount().divide(order.getAmount(), 2, RoundingMode.HALF_UP)));
                    od.setCanRefundAmount(od.getActualAmount());
                    od.setCouponAmount(od.getAmount().subtract(od.getConsumeAmount()));
                }
            }
        }
        // 订单优惠金额
        order.setCouponAmount(order.getAmount().subtract(order.getConsumeAmount()));

        // 订场用户信息
        if (!fieldBookUserService.saveBatch(fieldBookUserList)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }

        // 修改订单明细 支付方式
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setPayType(order.getActualAmount().doubleValue() > 0 ? company.getPayType(): PayType.VIPCard.getCode());
        }
        if (!save(order)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }
        if (!orderDetailService.saveBatch(orderDetailList)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }
        return fieldPay(order.getId(), company);
    }

    @Override
    public WxMaPayVO fieldPay(String orderId, Company company) {
        Order order = getById(orderId);
        if (!order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单状态异常!");
        }
        WxMaPayVO wxMaPayVO;
        if (order.getActualAmount().doubleValue() > 0) {
            wxMaPayVO = getWxMaPayVO(company, order, "field-wechat-pay", PayCategoryEnum.FIELD.getCode());
        } else {
            wxMaPayVO = new WxMaPayVO();
            Order updateOrder = new Order();
            updateOrder.setId(orderId);
            wxMaPayVO.setOrderId(orderId);
            if (order.getUserVipCardId() != null) {
                updateOrder.setPayType(PayType.VIPCard.getCode());
            } else {
                order.setPayType(PayType.NONE.getCode());
            }
            updateById(updateOrder);
            updateFieldOrder(order.getId(), new Date());
        }
        return wxMaPayVO;
    }

    /**
     * 获取前端支付对象
     * @param company
     * @param order
     * @param wechatNoticeUrl
     * @return
     */
    private WxMaPayVO getWxMaPayVO(Company company, Order order, String wechatNoticeUrl, String payCallCategory) {
        if (company == null) {
            company = companyService.getById(order.getCompanyId());
        }
        WxMaPayVO wxMaPayVO = new WxMaPayVO();
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setAppId(company.getMiniAppId());
        String noticeUrl;
        String tradeType = null;
        if (PayType.HF.getCode().equals(company.getPayType())) {
            if (order.getId().startsWith("FD") || order.getId().startsWith("VI")) {
                // 对于订场 和 会员卡 支付使用订场商户ID
                updateOrder.setPayAppId(company.getHuiFuFieldId());
            } else {
                updateOrder.setPayAppId(company.getHuiFuId());
            }
            noticeUrl = "hf-pay-callback";
            tradeType = "T_MINIAPP";
        } else {
            updateOrder.setPayAppId(company.getMiniAppPayId());
            noticeUrl = wechatNoticeUrl;
        }
        String openId = userOpenIdService.getByAppIdAndUserId(order.getUserId(), company.getMiniAppId(), true);
        try {
            WeChatPayParamVO weChatPayParamVO = getPayParam(order.getUserId(), openId, company.getMiniAppId(),
                    updateOrder.getPayAppId(), order.getId(), order.getName(), order.getActualAmount(), noticeUrl, company.getPayType(), tradeType, payCallCategory, 20);
            BeanUtils.copyProperties(weChatPayParamVO, wxMaPayVO);
            updateOrder.setPrepayId(weChatPayParamVO.getPrepayId());
            updateOrder.setNonceStr(weChatPayParamVO.getNonceStr());
            updateOrder.setPayType(company.getPayType());
            updateById(updateOrder);
        }catch (Exception e) {
            e.printStackTrace();
            throw new SportException("创建订单异常");
        }
        return wxMaPayVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFieldOrder(String orderId, Date payFinishDate) {
        try {
            Order order = getById(orderId);
            if (order == null) {
                throw new SportException(orderId + "订单不存在");
            }
            if (order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
                Date payFinishTime;
                if (payFinishDate == null) {
                    if (order.getActualAmount().doubleValue() > 0) {
                        payFinishTime = checkOrderPayFinishTime(orderId, order.getPayAppId(), order.getPayType(), DateFormatUtils.format(order.getCreateDate(), "yyyyMMdd"));
                    } else {
                        payFinishTime = new Date();
                    }
                } else {
                    payFinishTime = payFinishDate;
                }
                log.info("用户自己查询更新未完成订单{}", orderId);
                boolean checkStatus = updateFieldOrder2Success(orderId, payFinishTime);
                log.info("结果:" + checkStatus);
                return checkStatus;
            } else if (order.getStatus().equals(OrderStatus.PAY.getCode()) || order.getStatus().equals(OrderStatus.USING.getCode())) {
                log.info("用户自己查询更新订单{}已支付完成", orderId);
                return true;
            } else {
                throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单状态异常!订场状态失效或已被预定!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单状态异常!" + e.getMessage());
        } finally {
            log.info("订单:{}, finally执行", orderId);
//                distributedLock.releaseLock(orderId);
        }
    }

    @Override
    public synchronized boolean updateFieldOrder2Success(String orderId, Date payFinishTime) {
        log.info("{}进行状态更新", orderId);
        if (payFinishTime == null) {
            return false;
        }
        // 活动项目订单
        Order order = getById(orderId);
        if (order == null || !order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            return true;
        }
        if (fieldOrderPaySuccess(order, payFinishTime)) {
            return true;
        }
        return false;
    }

    /**
     * 订场订单支付成功操作
     *
     * @param order
     * @param payFinishTime
     * @return
     */
    private boolean fieldOrderPaySuccess(Order order, Date payFinishTime) {
        if (order.getUserVipCardId() != null) {
            boolean payment = userVipCardService.payment(order);
            if (!payment) {
                return false;
            }
        }
        order.setStatus(OrderStatus.PAY.getCode());
        order.setPayFinishTime(payFinishTime);

        if (order.getUserCouponId() != null) {
            List<Long> couponIds = new ArrayList<>();
            couponIds.add(order.getUserCouponId());
            //更新 db
            CouponKafkaMessage couponKafkaMessage = new CouponKafkaMessage(
                    UserCouponStatus.USED.getCode(),
                    couponIds
            );
            kafkaService.changeCouponStatus2Database(couponKafkaMessage);
        }
        Order upOrder = new Order();
        upOrder.setId(order.getId());
        upOrder.setStatus(order.getStatus());
        upOrder.setPayFinishTime(order.getPayFinishTime());
        if (updateById(upOrder)) {
            List<OrderDetail> orderDetailList = orderDetailService.getByOrderId(order.getId());
            if (CollectionUtils.isNotEmpty(orderDetailList)) {
                List<Long> fieldPlanIdList = new ArrayList<>(orderDetailList.size());
                for (OrderDetail orderDetail : orderDetailList) {
                    OrderDetail od = new OrderDetail();
                    od.setId(orderDetail.getId());
                    FieldPlan fieldPlan = this.fieldPlanService.getById(Long.valueOf(orderDetail.getProductId()));
                    if (!Objects.equals(fieldPlan.getStatus(), FieldPlanStatus.Normal.getCode())) {
                        VenueField venueField = this.venueFieldService.getById(fieldPlan.getFieldId());
                        String name = String.format("%s [%s-%s]", venueField.getName(),
                                DateFormatUtils.format(fieldPlan.getFieldDay(), "yyyy-MM-dd"),
                                DateFormatUtils.format(fieldPlan.getStartTime(), "HH:mm"),
                                DateFormatUtils.format(fieldPlan.getEndTime(), "HH:mm"));
                        this.cancelFieldOrder(order, "订场失效，系统自动取消");
                        throw new SportException(String.format("%s已被预定或失效", name));
                    }
                    String phoneLast = orderDetail.getPhone().substring(orderDetail.getPhone().length() - 4);
                    fieldPlan.setStatus(FieldPlanStatus.Predetermine.getCode());
                    fieldPlan.setLockChannel(FieldPlanLockChannels.Current.getCode());
                    fieldPlan.setRemark("尾号" + phoneLast);
                    fieldPlan.setLockRemark("尾号" + phoneLast);
                    fieldPlanService.updateById(fieldPlan);
                    od.setStatus(OrderStatus.PAY.getCode());
                    od.setPayType(order.getPayType());
                    orderDetailService.updateById(od);

                    fieldPlanIdList.add(fieldPlan.getId());

                    // 获取订场的场地名称
                    String[] productNames = orderDetail.getProductName().split(" ");

                    try {
                        // 发送场地完成的延时通知
                        Map<String, Object> fieldFinishMap = new LinkedHashMap<>();
                        fieldFinishMap.put("type", DelayMessageType.FIELD_FINISH.getCode());
                        fieldFinishMap.put("consumeCode", orderDetail.getConsumeCode());
                        String day = DateFormatUtils.format(fieldPlan.getFieldDay(), "yyyy-MM-dd");
                        mqMessageService.sendDelayMsgByDate(fieldFinishMap, DateUtils.parseDate(day + " " + fieldPlan.getStartTime(), "yyyy-MM-dd HH:mm:ss"));

                        // 发送订场成功通知
                        StoreVenue storeVenue = storeVenueService.getById(fieldPlan.getVenueId());
                        Store store = storeService.getById(order.getStoreId());
                        Map<String, Object> msgMap = new HashMap<>();
                        msgMap.put("orderId", order.getId());
                        msgMap.put("userId", order.getUserId());
                        msgMap.put("storeName", store.getName() + " " + storeVenue.getName());
                        msgMap.put("fieldName", productNames[0]);
                        msgMap.put("time", productNames[1] + " " + productNames[2]);
                        String userOpenId = userOpenIdService.getByAppIdAndUserId(order.getUserId(), order.getAppId(), false);
                        msgMap.put("miniOpenId", userOpenId);
                        msgMap.put("user", "尾号" + phoneLast);
                        msgMap.put("fieldDay", productNames[1]);
                        msgMap.put("fieldTime", productNames[2]);
                        msgMap.put("amount", order.getConsumeAmount().setScale(2, RoundingMode.HALF_UP).toString());
                        mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.FIELD_SUCCESS.getCode(), order.getStoreId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                //场地状态变动，发送mq消息通知 用于同步锁场/解锁到第三方平台
                FieldSyncMessage fieldSyncMessage = new FieldSyncMessage();
                fieldSyncMessage.setStatus(FieldPlanStatus.Predetermine.getCode());
                fieldSyncMessage.setChannel(FieldPlanLockChannels.Current.getCode());
                fieldSyncMessage.setFieldPlanIdList(fieldPlanIdList);
                mqMessageService.sendFieldSyncMessage(fieldSyncMessage);
            }
            return true;
        }
        throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单更新失败");
    }

    @Override
    public FieldOrderDetailVO fieldDetail(String orderId) {
        FieldOrderDetailVO fieldOrderDetailVO = new FieldOrderDetailVO();
//        Order order = getById(orderId);
//        BeanUtils.copyProperties(order, fieldOrderDetailVO);
//
//        Store store = storeService.getById(order.getStoreId());
//        fieldOrderDetailVO.setAddress(store.getAddress());
//        fieldOrderDetailVO.setLatitude(store.getLatitude());
//        fieldOrderDetailVO.setLongitude(store.getLongitude());
//        fieldOrderDetailVO.setStoreName(store.getName());
//
//        List<FieldBookUserDTO> fieldBookUserList = fieldPlanService.getFieldBookUserDetail(orderId);
//        if (CollectionUtils.isNotEmpty(fieldBookUserList)) {
//            FieldBookUserDTO fieldBookUserDTO = fieldBookUserList.get(0);
//            fieldOrderDetailVO.setVenueName(fieldBookUserDTO.getVenueName());
//            fieldOrderDetailVO.setFieldList(fieldBookUserList.stream().map(item -> {
//                FieldBookUserDetailVO fu = new FieldBookUserDetailVO();
//                fu.setFieldDay(item.getFieldDay());
//                fu.setFieldName(item.getFieldName());
//                fu.setEndTime(item.getEndTime());
//                fu.setStartTime(item.getStartTime());
//                return fu;
//            }).collect(Collectors.toList()));
//        }
        return fieldOrderDetailVO;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxMaPayVO createVipOrder(VipOrderForm form) {
        VipCard vipCard = vipCardService.getById(form.getVipCardId());
        if (vipCard == null) {
            throw new SportException(String.format("VipCardId %s error", form.getVipCardId()));
        }
        //增加配置开关，vip卡购买是否强制选择销售人员
        StoreConfig storeConfig = storeConfigService.getCompanyStoreConfig(vipCard.getCompanyId(), "BUY_VIP_CARD_CHOOSE_SALES");
        if (storeConfig != null && Boolean.parseBoolean(storeConfig.getValue())) {
           if (StringUtils.isBlank(form.getCoachUserId())){
               throw new SportException(String.format("请选择销售人员！"));
           }
        }

        Company company = companyService.getById(vipCard.getCompanyId());
        if (StringUtils.isEmpty(company.getMiniAppId())) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "公司参数配置异常");
        }
        if (PayType.Wechat.getCode().equals(company.getPayType())) {
            if (StringUtils.isEmpty(company.getMiniAppPayId())) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "公司参数配置异常");
            }
        } else if (PayType.HF.getCode().equals(company.getPayType())) {
            if (StringUtils.isEmpty(company.getHuiFuFieldId())) {
                throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "公司参数配置异常");
            }
        }

        User user = userService.getById(form.getUserId());
        String orderId = IdUtils.getStrNumberId("VI");
        Order order = new Order();
        order.setId(orderId);
        order.setName(vipCard.getName());
        Long categoryId = categoryService.getByCode(SportConstant.VIP_CATEGORY_CODE);
        order.setCategoryId(categoryId);
        order.setStatus(OrderStatus.UN_PAY.getCode());
        order.setUserId(form.getUserId());
        order.setProductId(vipCard.getId() + "");
        order.setSaleUserId(form.getCoachUserId());
        order.setCount(1);
        order.setPayType(company.getPayType());

        // 创建订单明细
        List<OrderDetail> orderDetailList = new ArrayList<>();
        BigDecimal amount = BigDecimal.ZERO;
        amount = amount.add(vipCard.getSellingPrice());
        order.setStoreId(form.getStoreId() != null ? form.getStoreId() : vipCard.getStoreId());
        order.setCompanyId(vipCard.getCompanyId());
        order.setAmount(amount);
        order.setConsumeAmount(order.getAmount());
        order.setActualAmount(order.getConsumeAmount());
        order.setCanRefundAmount(order.getActualAmount());

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStatus(OrderStatus.UN_PAY.getCode());
        orderDetail.setCategoryId(order.getCategoryId());
        orderDetail.setOrderId(order.getId());
        orderDetail.setProductId(vipCard.getId() + "");
        orderDetail.setProductName(vipCard.getName());
        orderDetail.setProductPrice(vipCard.getSellingPrice());
        orderDetail.setProductQuantity(1);
        orderDetail.setUserId(order.getUserId());
        orderDetail.setPhone(user.getPhone());
        orderDetail.setPayType(company.getPayType());
        orderDetail.setStoreId(vipCard.getStoreId());
        orderDetail.setCompanyId(vipCard.getCompanyId());
        orderDetail.setConsumeCode(IdUtils.getConsumeCode("VI"));
        orderDetail.setAmount(order.getAmount());
        orderDetail.setConsumeAmount(order.getConsumeAmount());
        orderDetail.setActualAmount(order.getActualAmount());
        orderDetail.setCanRefundAmount(order.getActualAmount());
        orderDetailList.add(orderDetail);

        if (!save(order)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }
        if (!orderDetailService.saveBatch(orderDetailList)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单创建失败!");
        }
        return vipPay(company, order.getId());
    }

    private WxMaPayVO vipPay(Company company, String orderId) {
        Order order = getById(orderId);
        if (!order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "订单状态异常!");
        }
        if (order.getActualAmount().doubleValue() > 0) {
            return getWxMaPayVO(company, order, "vip-wechat-pay", PayCategoryEnum.VIP.getCode());
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVipOrder(String orderId, Date payFinishDate) {
        try {
            Order order = getById(orderId);
            if (order == null) {
                throw new SportException(orderId + "订单不存在");
            }
            if (order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
                Date payFinishTime;
                if (payFinishDate == null) {
                    if (order.getActualAmount().doubleValue() > 0) {
                        payFinishTime = checkOrderPayFinishTime(orderId, order.getPayAppId(), order.getPayType(), DateFormatUtils.format(order.getCreateDate(), "yyyyMMdd"));
                    } else {
                        payFinishTime = new Date();
                    }
                } else {
                    payFinishTime = payFinishDate;
                }
                log.info("用户自己查询更新未完成订单{}", orderId);
                boolean checkStatus = updateVipOrder2Success(orderId, payFinishTime);
                log.info("结果:" + checkStatus);
                return checkStatus;
            } else if (order.getStatus().equals(OrderStatus.PAY.getCode()) || order.getStatus().equals(OrderStatus.USING.getCode())) {
                log.info("用户自己查询更新订单{}已支付完成", orderId);
                return true;
            } else {
                throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单状态异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单状态异常");
        } finally {
            log.info("订单:{}, finally执行", orderId);
//                distributedLock.releaseLock(orderId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean updateVipOrder2Success(String orderId, Date payFinishTime) {
        log.info("{}进行状态更新", orderId);
        if (payFinishTime == null) {
            return false;
        }
        // 活动项目订单
        Order order = getById(orderId);
        if (order == null || !order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            return true;
        }
        if (vipOrderPaySuccess(order, payFinishTime)) {
            return true;
        }
        return false;
    }

    /**
     * 订单支付成功操作
     *
     * @param order
     * @param payFinishTime
     * @return
     */
    private boolean vipOrderPaySuccess(Order order, Date payFinishTime) {
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(OrderStatus.USING.getCode());
        updateOrder.setPayFinishTime(payFinishTime);
        if (updateById(updateOrder)) {
            List<OrderDetail> orderDetailList = orderDetailService.getByOrderId(order.getId());
            if (CollectionUtils.isNotEmpty(orderDetailList)) {
                for (OrderDetail orderDetail : orderDetailList) {
                    // 解决重复充值的问题
                    if (!order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
                        continue;
                    }
                    OrderDetail od = new OrderDetail();
                    od.setId(orderDetail.getId());
                    od.setStatus(OrderStatus.PAY.getCode());
                    orderDetailService.updateById(od);
                    VipCard vipCard = vipCardService.getById(Long.valueOf(orderDetail.getProductId()));
                    userVipCardService.recharge(order, vipCard, order.getUserId(), orderDetail.getCompanyId(), orderDetail.getStoreId());
                }
            }
            return true;
        }
        throw new SportException(ResultEnum.UN_KNOW_ERROR.getCode(), "订单更新失败");
    }

    @Override
    public BigDecimal autoFinishOrder(String orderId) {
        Order order = getById(orderId);
        if (order.getStatus().equals(OrderStatus.ORDER_FINISH.getCode()) ||
                order.getStatus().equals(OrderStatus.REFUND.getCode()) ||
                order.getStatus().equals(OrderStatus.ORDER_REFUNDING.getCode())) {
            log.info("订单已处理:" + orderId + ", 状态:" + OrderStatus.of(order.getStatus()).getDescription());
            return BigDecimal.ZERO;
        }
        if (order.getStatus().equals(OrderStatus.USING.getCode())) {
            Date startTime = order.getStartTime();
            Date endTime = new Date();
            // 计算两个日期之间的小时差
            long diffMilliSec = endTime.getTime() - startTime.getTime();
            // 分钟差
            long minutes = diffMilliSec / (60 * 1000);
            int count = (int) (minutes / 15);
            if (minutes == 0 || minutes % 15 > 0) {
                count += 1;
            }
            ExerciseCountTemplate exerciseCountTemplate = exerciseCountTemplateService.getByExerciseId(NumberUtils.toLong(order.getProductId()));
            BigDecimal hourPrice = exerciseCountTemplate.getHourPrice();
            BigDecimal payMoney = hourPrice.divide(BigDecimal.valueOf(4)).multiply(BigDecimal.valueOf(count));
            Order update = new Order();
            update.setId(orderId);
            update.setEndTime(endTime);
            update.setStatus(OrderStatus.ORDER_FINISH.getCode());
            update.setActualAmount(payMoney);
            BigDecimal refundAmount = order.getConsumeAmount().subtract(payMoney);
            update.setRefundAmount(refundAmount);
            if (refundAmount.doubleValue() <= 0) {
                // 如果是负数，那就不用退款了
                update.setCanRefundAmount(BigDecimal.ZERO);
                updateById(update);
                return order.getConsumeAmount();
            } else {
                // 需要进行退款
                update.setCategoryId(order.getCategoryId());
                update.setAppId(order.getAppId());
                update.setCanRefundAmount(refundAmount);
                update.setActualAmount(order.getActualAmount());
                update.setUserCouponId(order.getUserCouponId());
                orderRefund(update, "订单完成", false);
                return payMoney;
            }
        } else {
            log.info("订单:" + orderId + ", 状态异常:" + OrderStatus.of(order.getStatus()).getDescription());
            return BigDecimal.ZERO;
        }
    }

    @Override
    public IPage<BasicOrderVO> getBasicOrderList(Map<String, Object> param) {
        int page = NumberUtils.toInt(param.get("page") + "", 1);
        int size = NumberUtils.toInt(param.get("size") + "", 10);
        if (param.get("categoryCode") != null) {
            Long categoryId = categoryService.getByCode(param.get("categoryCode").toString());
            param.put("categoryId", categoryId);
        }
        try {
            Tuple2<Date, Date> startAndEndDate = SportDateUtils.getStartAndEndDate(param);
            param.put("start", startAndEndDate.getT1());
            param.put("end", startAndEndDate.getT2());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBaseMapper().getBasicOrderList(new Page<>(page, size), param);
    }
}
