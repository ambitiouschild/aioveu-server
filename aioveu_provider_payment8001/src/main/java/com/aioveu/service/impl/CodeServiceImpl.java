package com.aioveu.service.impl;

import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.*;
import com.aioveu.enums.*;
import com.aioveu.exception.SportException;
import com.aioveu.feign.WxMaUserClient;
import com.aioveu.feign.form.MpBindUserForm;
import com.aioveu.feign.form.WeChatCodeUnLimitForm;
import com.aioveu.service.*;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import com.aioveu.vo.CheckOrderVO;
import com.aioveu.vo.FileVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class CodeServiceImpl implements CodeService {

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CompanyStoreUserService companyStoreUserService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private IUserCouponService iUserCouponService;

    @Autowired
    private IBuildTemplateService iBuildTemplateService;

    @Autowired
    @Resource
    private WxMaUserClient wxMaUserClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CouponVerifyService couponVerifyService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseCouponService exerciseCouponService;

    @Autowired
    private UserService userService;

    @Autowired
    private MQMessageService mqMessageService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CheckOrderVO checkCode(String userId, String code) {
        if (code.startsWith("OD") || code.startsWith("CD")) {
            return checkOrderCode(userId, code, true);
        } else if (code.startsWith("FD")) {
            return checkFieldOrderCode(userId, code, true);
        } else {
            return checkCouponCode(userId, code, true);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean checkCodeMine(String orderId, String username) {
        String userId = userService.getUserIdFromCache(username);
        List<OrderDetail> orderDetailList = orderDetailService.getByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SportException("未查询到订单信息!");
        }
        for (OrderDetail od : orderDetailList) {
            if (OrderStatus.PAY.getCode().equals(od.getStatus()) && od.getUserId().equals(userId)) {
                checkOrderCode(userId, od.getConsumeCode(), false);
            }
        }
        return true;
    }
    @Override
    public Boolean helpActiveOrder(String orderId) {
        List<OrderDetail> orderDetailList = orderDetailService.getByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new SportException("未查询到订单信息!");
        }
        for (OrderDetail od : orderDetailList) {
            if (OrderStatus.PAY.getCode().equals(od.getStatus())) {
                checkOrderCode(OauthUtils.getCurrentUserId(), od.getConsumeCode(), true);
            }
        }
        return true;
    }

    /**
     * 仅仅是核销券
     *
     * @param userId
     * @param code
     * @param businessCheck
     * @return
     */
    @Override
    public CheckOrderVO checkCouponCode(String userId, String code, boolean businessCheck) {
        //1. 检查核销码
        UserCoupon userCoupon = iUserCouponService.findUsableCoupon(code);
        CouponTemplate couponTemplate = iBuildTemplateService.getById(userCoupon.getTemplateId());
        if (businessCheck) {
            // 2.检查权限
            if (!companyStoreUserService.hasStorePermission(userId, couponTemplate.getStoreId(), couponTemplate.getCompanyId())) {
                throw new SportException("您没有该优惠券的核销权限!");
            }
        }
        CheckOrderVO checkOrderVO = new CheckOrderVO();
        checkOrderVO.setOrderId(code);
        checkOrderVO.setName(couponTemplate.getName());
        // 兑换券
        if (userCoupon.getTemplateSDK().getCategory().equals(CouponCategory.DUIHUAN.getCode())) {
            // 更新状态
            userCoupon.setStatus(UserCouponStatus.USED);
            if (iUserCouponService.updateById(userCoupon)) {
                if (StringUtils.isNotEmpty(userCoupon.getOrderId())) {
                    // 更新订单状态
                    mqMessageService.changeOrderStatus(userCoupon.getOrderId(), userId);
                }
                couponVerifyService.recordCouponVerify(userId, couponTemplate.getStoreId(), userCoupon.getId(), "商家核销");
                return checkOrderVO;
            } else {
                throw new SportException("优惠券核销失败!");
            }
        } else if (userCoupon.getTemplateSDK().getCategory().equals(CouponCategory.UNLIMIT_TIMES.getCode())) {
            // 无限次券，在有效期内 直接核销成功，保存核销记录
            couponVerifyService.recordCouponVerify(userId, couponTemplate.getStoreId(), userCoupon.getId(), "商家核销");
            return checkOrderVO;
        } else {
            throw new SportException("该优惠券不可核销");
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckOrderVO checkFieldOrderCode(String userId, String code, boolean businessCheck) {
        // 1.检查核销码
        OrderDetail orderDetail = orderDetailService.getByConsumeCode(code);
        if (orderDetail == null) {
            throw new SportException(code + "不存在, 请检查核销码是否正确");
        }
        if (orderDetail.getStatus().equals(OrderStatus.ORDER_FINISH.getCode())) {
            log.info("订单已核销:{}", orderDetail.getOrderId());
            CheckOrderVO checkOrderVO = new CheckOrderVO();
            checkOrderVO.setName(orderDetail.getProductName());
            checkOrderVO.setOrderId(orderDetail.getOrderId());
            checkOrderVO.setCost(orderDetail.getProductPrice());
            return checkOrderVO;
        }
        if (!StringUtils.isEmpty(userId)) {
            // 2.检查权限
            if (businessCheck) {
                if (!companyStoreUserService.hasStorePermission(userId, orderDetail.getStoreId(), orderDetail.getCompanyId())) {
                    throw new SportException("您没有该订单的核销权限!");
                }
            } else {
                if (!orderDetail.getUserId().equals(userId)) {
                    throw new SportException("您没有该订单的核销权限!");
                }
            }
        }
        if (orderDetail.getStatus().equals(OrderStatus.ORDER_FINISH.getCode())) {
            throw new SportException(code + "已核销");
        }
        if (orderDetailService.changeStatus(orderDetail.getId(), OrderStatus.ORDER_FINISH.getCode())) {
            Integer count = orderDetailService.getUnUseOrderDetailCount(orderDetail.getOrderId());
            if (count == 0) {
                Order order = orderService.getById(orderDetail.getOrderId());
                if (Objects.equals(order.getStatus(), OrderStatus.PAY.getCode()) || Objects.equals(order.getStatus(), OrderStatus.USING.getCode())) {
                    Order updateOrder = new Order();
                    updateOrder.setId(orderDetail.getOrderId());
                    updateOrder.setStatus(OrderStatus.ORDER_FINISH.getCode());
                    orderService.updateById(updateOrder);
                }
            }
            //TODO 2025 应该增加一个主订单使用中的状态，用户端的订场订单也要加 使用中状态
            CheckOrderVO checkOrderVO = new CheckOrderVO();
            checkOrderVO.setName(orderDetail.getProductName());
            checkOrderVO.setOrderId(orderDetail.getOrderId());
            checkOrderVO.setCost(orderDetail.getProductPrice());
            return checkOrderVO;
        } else {
            throw new SportException("订单核销失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckOrderVO checkOrderCode(String userId, String code, boolean businessCheck) {
        // 1.检查核销码
        OrderDetail orderDetail = orderDetailService.getByConsumeCode(code);
        if (orderDetail == null) {
            throw new SportException(code + "不存在, 请检查核销码是否正确");
        }
        // 防止多次核销或者激活 校验
        if (orderDetail.getStatus().equals(OrderStatus.USED.getCode())) {
            log.info("订单已核销:{}", orderDetail.getOrderId());
            CheckOrderVO checkOrderVO = new CheckOrderVO();
            checkOrderVO.setName(orderDetail.getProductName());
            checkOrderVO.setOrderId(orderDetail.getOrderId());
            checkOrderVO.setCost(orderDetail.getProductPrice().multiply(BigDecimal.valueOf(orderDetail.getProductQuantity())));
            return checkOrderVO;
        }
        // 2.检查权限
        if (businessCheck) {
            if (!companyStoreUserService.hasStorePermission(userId, orderDetail.getStoreId(), orderDetail.getCompanyId())) {
                throw new SportException("您没有该订单的核销权限!");
            }
        } else {
            if (!orderDetail.getUserId().equals(userId)) {
                throw new SportException("您没有该订单的核销权限!");
            }
        }
        // 对于按次活动的订单二维码 会扫描两次
        if (code.startsWith("CD") && orderDetail.getStatus().equals(OrderStatus.USED.getCode())) {
            // TODO 开门
            BigDecimal payMoney = orderService.autoFinishOrder(orderDetail.getOrderId());

            CheckOrderVO checkOrderVO = new CheckOrderVO();
            checkOrderVO.setName(orderDetail.getProductName());
            checkOrderVO.setOrderId(orderDetail.getOrderId());
            checkOrderVO.setCost(payMoney);
            return checkOrderVO;
        }
        // 3.核销
        if (orderService.checkCodeToUsing(orderDetail.getOrderId(), code) && orderDetailService.changeStatus(orderDetail.getId(), OrderStatus.USED.getCode())) {
            CheckOrderVO checkOrderVO = new CheckOrderVO();
            checkOrderVO.setName(orderDetail.getProductName());
            checkOrderVO.setOrderId(orderDetail.getOrderId());
            checkOrderVO.setCost(orderDetail.getProductPrice().multiply(BigDecimal.valueOf(orderDetail.getProductQuantity())));

            // 判断商品是否是核销领券
            Exercise exercise = exerciseService.getById(orderDetail.getProductId());
            if (exercise.getCouponReceiveType() == 2) {
                // 用户领取优惠券
                exerciseCouponService.sendAndNoticeUserCouponByExerciseId(NumberUtils.toLong(orderDetail.getProductId()),
                        orderDetail.getUserId(), orderDetail.getCategoryId(), orderDetail.getOrderId(),
                        orderDetail.getProductName());
            }
            //检查是否赠送余额
            if (exercise.getSendBalance() != null && exercise.getSendBalance().doubleValue() > 0) {
                userService.addBalance(orderDetail.getUserId(), exercise.getSendBalance(),
                        orderDetail.getOrderId(), "余额增加", orderDetail.getProductName() + "赠送");
            }
            if (code.startsWith("CD")) {
                Order order = orderService.getById(orderDetail.getOrderId());
                sendOrderAutoFinishMessage(orderDetail.getOrderId(), order.getEndTime());
            } else {
                // 更新订单状态
                mqMessageService.changeOrderStatus(orderDetail.getOrderId(), userId);
            }
            return checkOrderVO;
        } else {
            throw new SportException("订单核销失败");
        }
    }

    @Override
    public String exercisePageCode(Long id) {
        String key = "MINI_CODE_EXERCISE_DETAIL_" + id;
        String fileName = "mini-code/exercise-detail/" + id + ".png";
        String appId = exerciseService.getAppIdByExerciseId(id);
        return miniPageCode(id + "", key, "pages/exercise/pages/detail/detail", fileName, appId, true);
    }

    @Override
    public FileVO createCustomExercisePageCode(Long id) {
        Exercise exercise = exerciseService.getById(id);
        if (exercise != null) {
            String key = "MINI_CODE_CUSTOM_EXERCISE_DETAIL_" + id;
            String fileName = "mini-code/custom-exercise-detail/" + id + ".png";
            String appId = exerciseService.getAppIdByExerciseId(id);
            String filePath = miniPageCode(id + "", key, "pages/shop/pages/flexible/flexible", fileName, appId, true);
            FileVO fileVO = new FileVO();
            fileVO.setPath(filePath);
            fileVO.setName(exercise.getName());
            return fileVO;
        }
        return null;
    }

    @Override
    public String miniPageCode(String scene, String key, String page, String fileName, String appId, boolean needCache) {
        String codePath = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(codePath) && needCache) {
            return codePath;
        }
        if (StringUtils.isEmpty(appId)) {
            throw new SportException("活动店铺小程序AppId未配置:" + scene);
        }
        WeChatCodeUnLimitForm form = new WeChatCodeUnLimitForm();
        form.setAppId(appId);
        form.setPage(page);
        form.setScene(scene);
        log.info("appId:" + appId + " page:" + page);
        byte[] response = wxMaUserClient.createWxMaBarCodeUnLimit(form);
        if (response != null) {
            if (OssUtil.uploadSingleImage(fileName, response)) {
                stringRedisTemplate.opsForValue().set(key, fileName);
                return fileName;
            }
        }
        return null;
    }

    @Override
    public String topicPageCode(Long id) {
        String key = "MINI_CODE_TOPIC_DETAIL_" + id;
        String page = "pages/project/pages/projectDetail/projectDetail";
        String fileName = "mini-code/topic-detail/" + id + ".png";
        return miniPageCode(id + "", key, page, fileName, "wxd6d275b4e626447d", true);
    }

    @Override
    public String formEnrollPageCode(Long id) {
        String key = "MINI_CODE_FORM_ENROLL_DETAIL_" + id;
        String fileName = "mini-code/form-enroll-detail/" + id + ".png";
        String appId = exerciseService.getAppIdByExerciseId(id);
        return miniPageCode(id + "", key, "pages/formEnroll/formEnroll", fileName, appId, true);
    }

    @Override
    public String toStoreCode(Long id) {
        String key = "MINI_CODE_TO_STORE_" + id;
        String page = "pages/reach-shop/reach-shop";
        String fileName = "mini-code/to-store/" + id + ".png";
        return miniPageCode(id + "", key, page, fileName, "wxd6d275b4e626447d", true);
    }

    @Override
    public String bindMpQrcode(String userId, String appId) {
        String key = "SUBSCRIBE_" +  appId + ":" + userId;
        String codePath = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isNotEmpty(codePath)) {
            return codePath;
        }
        log.info("appId:" + appId + " 绑定用户:" + userId);
        MpBindUserForm form = new MpBindUserForm();
        form.setUserId(userId);
        form.setAppId(appId);
        form.setExpireSeconds(3600 * 24);
        byte[] response = wxMaUserClient.getUserBindMpQrcode(form);
        if (response != null) {
            String fileName = "mp-subscribe/" + appId + "/" + userId + "/" + System.currentTimeMillis() + ".png";
            if (OssUtil.uploadSingleImage(fileName, response)) {
                String qrCode = FileUtil.getImageFullUrl(fileName);
                stringRedisTemplate.opsForValue().set(key, qrCode, form.getExpireSeconds() - 60, TimeUnit.SECONDS);
                return qrCode;
            }
        }
        return "";
    }

    private void sendOrderAutoFinishMessage(String orderId, Date noticeTime) {
        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("orderId", orderId);
        msgMap.put("type", DelayMessageType.COUNT_EXERCISE_FINISH.getCode());
        log.info("按次活动订单{}开始消费,订单自动完成时间:{}", orderId, DateFormatUtils.format(noticeTime, "yyyy-MM-dd HH:mm:ss"));
        mqMessageService.sendDelayMsgByDate(msgMap, noticeTime);
    }
}
