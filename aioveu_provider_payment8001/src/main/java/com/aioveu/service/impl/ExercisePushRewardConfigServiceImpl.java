package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ExercisePushRewardConfigDao;
import com.aioveu.entity.*;
import com.aioveu.enums.PushRewardType;
import com.aioveu.exception.SportException;
import com.aioveu.form.ExercisePushRewardConfigForm;
import com.aioveu.form.ExerciseTopicForm;
import com.aioveu.service.*;
import com.aioveu.vo.ExercisePushRewardConfigVO;
import com.aioveu.vo.TopicExerciseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ExercisePushRewardConfigServiceImpl extends ServiceImpl<ExercisePushRewardConfigDao, ExercisePushRewardConfig> implements ExercisePushRewardConfigService {

    @Autowired
    private TopicExerciseService topicExerciseService;

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Autowired
    private IUserCouponService iUserCouponService;

    @Autowired
    private UserService userService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public List<ExercisePushRewardConfig> getByTopicAndExercise(Long exerciseId, Long topicId) {
        QueryWrapper<ExercisePushRewardConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExercisePushRewardConfig::getExerciseId, exerciseId).
                eq(ExercisePushRewardConfig::getTopicId, topicId);
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(ExerciseTopicForm form) {
        List<ExercisePushRewardConfig> configList = form.getConfigList();
        if (CollectionUtils.isEmpty(configList)) {
            throw new SportException("奖励配置不能为空");
        }
        for (ExercisePushRewardConfig item : configList) {
            if (StringUtils.isEmpty(item.getName())) {
                throw new SportException("名称不能为空");
            }
            if (item.getTopicId() == null) {
                throw new SportException("主题id不能为空");
            }
            if (item.getExerciseId() == null) {
                throw new SportException("活动id不能为空");
            }
            if (item.getRewardType() == null) {
                throw new SportException("奖励类型不能为空");
            }
            if (item.getReward() == null || item.getReward().doubleValue() <= 0) {
                throw new SportException(item.getName() + "奖励设置错误");
            }
            if (!saveOrUpdate(item)) {
                throw new SportException("系统错误");
            }
        }
        // 活动报名到主题
        if (topicExerciseService.create(configList.get(0).getExerciseId(), configList.get(0).getTopicId(), form.getCategoryId())) {
            return true;
        }
        throw new SportException("参加主题失败!");
    }

    @Override
    public boolean createSystemConfig(Long exerciseId, Long topicId) {
        List<ExercisePushRewardConfig> list = new ArrayList<>(3);

//        ExercisePushRewardConfig orderReward = new ExercisePushRewardConfig();
//        orderReward.setReward(new BigDecimal("10"));
//        orderReward.setName("预约奖励");
//        orderReward.setExerciseId(exerciseId);
//        orderReward.setTopicId(topicId);
//        orderReward.setRewardType(PushRewardType.ORDER_DETAIL.getCode());
//
//        ExercisePushRewardConfig checkReward = new ExercisePushRewardConfig();
//        checkReward.setReward(new BigDecimal("20"));
//        checkReward.setName("到店奖励");
//        checkReward.setExerciseId(exerciseId);
//        checkReward.setTopicId(topicId);
//        checkReward.setRewardType(PushRewardType.CHECK.getCode());
//
        ExercisePushRewardConfig storeReward = new ExercisePushRewardConfig();
//        storeReward.setCouponTemplateId(12L);
        storeReward.setName("到店礼");
        storeReward.setRewardProduct("运动背包");
        storeReward.setReward(new BigDecimal(0));
        storeReward.setExerciseId(exerciseId);
        storeReward.setTopicId(topicId);
        storeReward.setRewardType(PushRewardType.TO_STORE.getCode());

//        list.add(orderReward);
//        list.add(checkReward);
        list.add(storeReward);

        return saveBatch(list);
    }

    @Override
    public boolean deleteConfig(Long exerciseId, Long topicId) {
        UpdateWrapper<ExercisePushRewardConfig> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(ExercisePushRewardConfig::getExerciseId, exerciseId).
                eq(ExercisePushRewardConfig::getTopicId, topicId)
                .set(ExercisePushRewardConfig::getStatus,0);
        return update(updateWrapper);
    }

    @Override
    public ExercisePushRewardConfig getRewardConfig(Long exerciseId, Long topicId, Integer type) {
        QueryWrapper<ExercisePushRewardConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExercisePushRewardConfig::getTopicId, topicId)
                .eq(ExercisePushRewardConfig::getRewardType, type);
        if (exerciseId != null) {
            queryWrapper.lambda().eq(ExercisePushRewardConfig::getExerciseId, exerciseId);
        }
        return getOne(queryWrapper);
    }

    @Override
    public boolean createOrderDetailReward(Long exerciseId, Long topicId, String userId, String orderId, Long storeId) {
        ExercisePushRewardConfig rewardConfig = getRewardConfig(exerciseId, topicId, PushRewardType.ORDER_DETAIL.getCode());
        if (rewardConfig != null) {
            Store store = storeService.getById(storeId);
            return userService.addBalance(userId, rewardConfig.getReward(), "给" + store.getName() + "推广",
                    "用户下单奖励", orderId);
        } else {
            log.warn("主题:{}, 活动:{}未配置下单奖励, 用户:{}", topicId, exerciseId, userId);
        }
        return true;
    }

    @Override
    public String orderCheckReward(Long exerciseId, Long topicId, String userId, String orderId, Long storeId, String storeUserId) {
        ExercisePushRewardConfig rewardConfig = getRewardConfig(exerciseId, topicId, PushRewardType.CHECK.getCode());
        if (rewardConfig != null) {
            Store store = storeService.getById(storeId);
            User storeUser = userService.getById(storeUserId);
            userService.addBalance(userId, rewardConfig.getReward(), store.getName() +
                    "店" + storeUser.getName() + "新客到店", "用户到店奖励", orderId);
        } else {
            log.warn("主题:{}, 活动:{}未配置到店奖励, 用户:{}", topicId, exerciseId, userId);
        }
        ExercisePushRewardConfig storeConfig = getRewardConfig(exerciseId, topicId, PushRewardType.TO_STORE.getCode());
        if (storeConfig != null) {
            if (storeConfig.getCouponTemplateId() != null) {
                CouponTemplate couponTemplate = couponTemplateService.getById(storeConfig.getCouponTemplateId());
                iUserCouponService.acquireByCouponTemplateId(userId, couponTemplate.getId(),
                        orderId, null, 1, null, false);
                return couponTemplate.getName();
            } else {
                return storeConfig.getRewardProduct();
            }
        } else {
            log.warn("主题:{}, 活动:{}未配置到店礼, 用户:{}", topicId, exerciseId, userId);
        }
        return null;
    }

    @Override
    public String getStoreGift(Long exerciseId, Long topicId) {
        ExercisePushRewardConfig storeConfig = getRewardConfig(exerciseId, topicId, PushRewardType.TO_STORE.getCode());
        if (storeConfig != null) {
            if (storeConfig.getCouponTemplateId() != null) {
                CouponTemplate couponTemplate = couponTemplateService.getById(storeConfig.getCouponTemplateId());
                return couponTemplate.getName();
            } else {
                return storeConfig.getRewardProduct();
            }
        }
        return null;
    }

    @Override
    public boolean createOrderReward(Long topicId, String userId, String orderId) {
        List<OrderDetail> byOrderId = orderDetailService.getByOrderId(orderId);
        ExercisePushRewardConfig rewardConfig = getRewardConfig(Long.valueOf(byOrderId.get(0).getProductId()), topicId, PushRewardType.ORDER_DETAIL.getCode());
        if (rewardConfig == null) {
            log.error("主题订单奖励未配置:{}, 订单:{}", topicId, orderId);
            return true;
        }
        User user = userService.getById(userId);
        return userService.addBalance(userId, rewardConfig.getReward(), user.getName() + "下单",
                "用户下单奖励", orderId);
    }

    @Override
    public IPage<ExercisePushRewardConfigVO> getExerciseAndReward(int page, int size, Long exerciseId) {
        return getBaseMapper().getExerciseAndRewardOrExerciseId(new Page<>(page, size), exerciseId);
    }

    @Override
    public Boolean changeReward(Long id, BigDecimal reward, String rewardProduct) {
        if (Objects.isNull(id)){
            throw new SportException("id不能为空");
        }
        if (Objects.nonNull(reward) && Objects.nonNull(rewardProduct)){
            throw new SportException("到店奖励商品和奖励金额不能同时存在");
        }
        ExercisePushRewardConfig exercisePushRewardConfig = new ExercisePushRewardConfig();
        exercisePushRewardConfig.setId(id);
        if (Objects.isNull(rewardProduct)){
            exercisePushRewardConfig.setReward(reward);
            if(getBaseMapper().updateById(exercisePushRewardConfig)>0){
                return true;
            }
        }
        exercisePushRewardConfig.setRewardProduct(rewardProduct);
        exercisePushRewardConfig.setReward(new BigDecimal(0));
        if (getBaseMapper().updateById(exercisePushRewardConfig)>0){
            return true;
        }
        throw new SportException("修改奖励失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRewardConfig(ExercisePushRewardConfigForm exercisePushRewardConfigForm) {
        List<ExercisePushRewardConfig> list = new ArrayList<>(3);

        ExercisePushRewardConfig orderReward = new ExercisePushRewardConfig();
        orderReward.setReward(exercisePushRewardConfigForm.getMakeAnAppointmentReward());
        orderReward.setName("预约奖励");
        orderReward.setExerciseId(exercisePushRewardConfigForm.getExerciseId());
        orderReward.setTopicId(exercisePushRewardConfigForm.getTopicId());
        orderReward.setRewardType(PushRewardType.ORDER_DETAIL.getCode());

        ExercisePushRewardConfig checkReward = new ExercisePushRewardConfig();
        checkReward.setReward(exercisePushRewardConfigForm.getReachReward());
        checkReward.setName("到店奖励");
        checkReward.setExerciseId(exercisePushRewardConfigForm.getExerciseId());
        checkReward.setTopicId(exercisePushRewardConfigForm.getTopicId());
        checkReward.setRewardType(PushRewardType.CHECK.getCode());

        ExercisePushRewardConfig storeReward = new ExercisePushRewardConfig();
//        storeReward.setCouponTemplateId(12L);
        storeReward.setName("到店礼");
        storeReward.setRewardProduct(exercisePushRewardConfigForm.getRewardProduct());
        storeReward.setReward(new BigDecimal(0));
        storeReward.setExerciseId(exercisePushRewardConfigForm.getExerciseId());
        storeReward.setTopicId(exercisePushRewardConfigForm.getTopicId());
        storeReward.setRewardType(PushRewardType.TO_STORE.getCode());

        list.add(orderReward);
        list.add(checkReward);
        list.add(storeReward);

        return saveBatch(list);
    }

    @Override
    public IPage<TopicExerciseVO> getUnSetReward(Integer page, Integer size, String exerciseName) {
        return  getBaseMapper().getUnSetReward(new Page<>(page,size),exerciseName);
    }
}
