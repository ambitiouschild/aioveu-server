package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ExerciseCouponDao;
import com.aioveu.entity.CouponTemplate;
import com.aioveu.entity.ExerciseCoupon;
import com.aioveu.entity.Order;
import com.aioveu.entity.Store;
import com.aioveu.enums.MsgOptionEnum;
import com.aioveu.enums.PeriodType;
import com.aioveu.service.*;
import com.aioveu.vo.ExerciseCouponSimpleVO;
import com.aioveu.vo.ExerciseCouponVO;
import com.aioveu.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ExerciseCouponServiceImpl extends ServiceImpl<ExerciseCouponDao, ExerciseCoupon> implements ExerciseCouponService {

    @Lazy
    @Autowired
    private IUserCouponService userCouponService;

    @Autowired
    private ITemplateBaseService templateBaseService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ExerciseCouponDao exerciseCouponDao;

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private MQMessageService mqMessageService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendAndNoticeUserCouponByExerciseId(Long exerciseId, String userId, Long categoryId, String orderId, String orderName) {
        Map<String, String> receiveCouponResult = sendUserCouponByExerciseId(exerciseId, userId, categoryId, orderId);
        if (receiveCouponResult != null) {
            // 更新订单激活时间
            Order order = new Order();
            order.setId(orderId);
            order.setActiveTime(new Date());
            orderService.updateById(order);
            log.info("订单:{}发送领取优惠券模板消息", orderId);
            Map<String, Object> msgMap = new HashMap<>();
            if (receiveCouponResult.get("couponName") == null) {
                msgMap.put("couponName", orderName + "优惠券");
            } else {
                msgMap.put("couponName", receiveCouponResult.get("couponName"));
            }
            try {
                Store store = exerciseService.getCompanyIdById(exerciseId);
                msgMap.put("couponName", msgMap.get("couponName") + " " + receiveCouponResult.get("couponCount"));
                msgMap.put("validityTime", receiveCouponResult.get("validityTime"));
                String appId = companyService.getMiniAppPayIdById(store.getCompanyId());
                String userOpenId = userOpenIdService.getByAppIdAndUserId(userId, appId, true);
                msgMap.put("miniOpenId", userOpenId);
                msgMap.put("couponType", "课券");
                mqMessageService.sendNoticeMessage(msgMap, MsgOptionEnum.RECEIVE_COUPON_MSG.getCode(), store.getId());
            }catch (Exception e) {
                e.printStackTrace();
                log.error("领券微信小程序消息发送失败:{}", userId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendCoupon(Long couponTemplateId, Integer count, String userId, Long exerciseId,
                           Long categoryId, BigDecimal amount, String orderId) {
        QueryWrapper<ExerciseCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseCoupon::getExerciseId, exerciseId).
                eq(ExerciseCoupon::getCouponTemplateId, couponTemplateId)
                .eq(ExerciseCoupon::getCategoryId, categoryId);

        ExerciseCoupon item = getOne(queryWrapper);
        if (item == null) {
            log.error("用户:{} 活动:{}未配置优惠券:{}", userId, exerciseId, couponTemplateId);
            return;
        }
        // 每个优惠券的均价
        BigDecimal singleCouponAmount = amount.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_DOWN);
        // 领取单个优惠券
        TemplateRule.Expiration expiration = getCouponExpiration(item);
        List<Long> ids = userCouponService.acquireByCouponTemplateId(userId, item.getCouponTemplateId(),
                orderId, singleCouponAmount, count, expiration, true);
        log.info("领取优惠券数量:" + ids.size());
    }

    @Override
    public boolean sendCouponByExercise(Long exerciseId, String userId, Long categoryId) {
        QueryWrapper<ExerciseCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseCoupon::getExerciseId, exerciseId).
                eq(ExerciseCoupon::getStatus, 1)
                .eq(ExerciseCoupon::getCategoryId, categoryId);
        List<ExerciseCoupon> exerciseCoupons = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(exerciseCoupons)) {
            for (ExerciseCoupon item : exerciseCoupons) {
                // 领取单个优惠券
                TemplateRule.Expiration expiration = getCouponExpiration(item);
                userCouponService.acquireByCouponTemplateId(userId, item.getCouponTemplateId(),
                        null, null, item.getCouponNumber(), expiration, true);
            }
            return true;
        }
        return false;
    }

    @Override
    public Map<String, String> sendUserCouponByExerciseId(Long exerciseId, String userId, Long categoryId, String orderId) {
        // Integer orderLimit, TemplateRule.Expiration expiration
        QueryWrapper<ExerciseCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseCoupon::getExerciseId, exerciseId).
                eq(ExerciseCoupon::getStatus, 1)
                .eq(ExerciseCoupon::getCategoryId, categoryId);
        List<ExerciseCoupon> exerciseCoupons = list(queryWrapper);
        // 活动关联的优惠券
        if (CollectionUtils.isNotEmpty(exerciseCoupons)) {
            // 计算每张优惠券的实际价格 用于后续退款需求
            Order order = orderService.getById(orderId);
            int couponTotalCount = 0;
            // 计算本次领取优惠券的数量
            for (ExerciseCoupon item : exerciseCoupons) {
                if (item.getCouponNumber() != null) {
                    couponTotalCount += item.getCouponNumber();
                }
            }
            // 每个优惠券的均价
            BigDecimal singleCouponAmount = order.getActualAmount().divide(BigDecimal.valueOf(couponTotalCount), 2, BigDecimal.ROUND_HALF_DOWN);
            // 领取优惠券的总数量
            int couponNumber = 0;
            for (ExerciseCoupon item : exerciseCoupons) {
                // 领取单个优惠券
                TemplateRule.Expiration expiration = getCouponExpiration(item);
                List<Long> ids = userCouponService.acquireByCouponTemplateId(userId, item.getCouponTemplateId(),
                        orderId, singleCouponAmount, item.getCouponNumber(), expiration, true);
                couponNumber += ids.size();
            }
            try {
                Map<String, String> result = new HashMap<>();
                result.put("couponCount", couponNumber + "张");
                CouponTemplate couponTemplate = templateBaseService.buildTemplateInfo(exerciseCoupons.get(0).getCouponTemplateId());
                TemplateRule.Expiration expiration = getCouponExpiration(exerciseCoupons.get(0));
                if (expiration.getPeriod() != null) {
                    couponTemplate.getRule().setExpiration(expiration);
                }
                result.put("couponName", couponTemplate.getName());
                Date start = new Date();
                Date end = null;
                if (couponTemplate.getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())
                    && couponTemplate.getRule().getExpiration().getDeadline() != null) {
                    end = new Date(couponTemplate.getRule().getExpiration().getDeadline());
                } else if (couponTemplate.getRule().getExpiration().getGap() != null){
                    end = DateUtils.addDays(start, couponTemplate.getRule().getExpiration().getGap());
                }
                if (end != null) {
                    result.put("validityTime", DateFormatUtils.format(end, "yyyy年MM月dd日"));
                } else {
                    result.put("validityTime", "");
                }
                return result;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean batchSave(Long exerciseId, Long categoryId, List<Long> couponTemplateIds) {
        List<ExerciseCoupon> exerciseCoupons = couponTemplateIds.stream().map(id -> {
            ExerciseCoupon exerciseCoupon = new ExerciseCoupon();
            exerciseCoupon.setExerciseId(exerciseId);
            exerciseCoupon.setCouponTemplateId(id);
            exerciseCoupon.setCategoryId(categoryId);
            return exerciseCoupon;
        }).collect(Collectors.toList());
        return saveBatch(exerciseCoupons);
    }


    @Override
    public IPage<ExerciseCouponVO> getManagerAll(int page, int size, Long exerciseId) {
        return exerciseCouponDao.getManagerAll(new Page<>(page, size),exerciseId);
    }

    @Override
    public boolean deleteExercise(Long id) {
        return removeById(id);
    }

    @Override
    public boolean deleteByExerciseId(Long exerciseId) {
        QueryWrapper<ExerciseCoupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseCoupon::getExerciseId, exerciseId);
        return remove(queryWrapper);
    }

    @Override
    public List<ExerciseCouponSimpleVO> getByExerciseId(Long exerciseId) {
        return getBaseMapper().getByExerciseId(exerciseId);
    }

    /**
     * 获取活动的优惠券过期配置
     * @param exerciseCoupon
     * @return
     */
    @Override
    public TemplateRule.Expiration getCouponExpiration(ExerciseCoupon exerciseCoupon) {
        TemplateRule.Expiration expiration = new TemplateRule.Expiration();
        if (exerciseCoupon.getCouponExpireDay() != null) {
            expiration.setGap(exerciseCoupon.getCouponExpireDay());
            expiration.setPeriod(PeriodType.SHIFT.getCode());
        } else if (exerciseCoupon.getCouponExpireTime() != null) {
            expiration.setGap(exerciseCoupon.getCouponExpireDay());
            expiration.setPeriod(PeriodType.REGULAR.getCode());
            expiration.setDeadline(exerciseCoupon.getCouponExpireTime().getTime());
        }
        return expiration;
    }

    @Override
    public boolean batchSave(List<ExerciseCoupon> exerciseCouponFormList) {
        return saveBatch(exerciseCouponFormList);
    }
}
