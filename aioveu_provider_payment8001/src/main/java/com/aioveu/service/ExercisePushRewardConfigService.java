package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ExercisePushRewardConfig;
import com.aioveu.form.ExercisePushRewardConfigForm;
import com.aioveu.form.ExerciseTopicForm;
import com.aioveu.vo.ExercisePushRewardConfigVO;
import com.aioveu.vo.TopicBaseVO;
import com.aioveu.vo.TopicExerciseVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ExercisePushRewardConfigService extends IService<ExercisePushRewardConfig> {


    /**
     * 获取活动和主题的奖励配置
     * @param exerciseId
     * @param topicId
     * @return
     */
    List<ExercisePushRewardConfig> getByTopicAndExercise(Long exerciseId, Long topicId);

    /**
     * 创建奖励配置并报名主题
     * @param form)
     * @return
     */
    boolean create(ExerciseTopicForm form);

    /**
     * 插入系统默认奖励配置
     * @param exerciseId
     * @param topicId
     * @return
     */
    boolean createSystemConfig(Long exerciseId, Long topicId);

    /**
     * 下架配置
     * @param exerciseId
     * @param topicId
     * @return
     */
    boolean deleteConfig(Long exerciseId, Long topicId);

    /**
     * 获取指定类型的奖励配置
     * @param exerciseId
     * @param topicId
     * @param type
     * @return
     */
    ExercisePushRewardConfig getRewardConfig(Long exerciseId, Long topicId, Integer type);

    /**
     * 到店核销订单奖励
     * @param exerciseId
     * @param topicId
     * @param userId
     * @param orderId
     * @param storeId
     * @param storeUserId
     * @return
     */
    String orderCheckReward(Long exerciseId, Long topicId, String userId, String orderId, Long storeId, String storeUserId);

    /**
     * 下单的订奖详情励
     * @param exerciseId
     * @param topicId
     * @param userId
     * @param orderId
     * @param storeId
     * @return
     */
    boolean createOrderDetailReward(Long exerciseId, Long topicId, String userId, String orderId, Long storeId);

    /**
     * 获取主题对应活动的到店礼
     * @param exerciseId
     * @param topicId
     * @return
     */
    String getStoreGift(Long exerciseId, Long topicId);

    /**
     * 下单的详情励
     * @param topicId
     * @param userId
     * @param orderId
     * @return
     */
    boolean createOrderReward(Long topicId, String userId, String orderId);

    /**
     * 获取到店礼、奖励金额
     */
    IPage<ExercisePushRewardConfigVO> getExerciseAndReward(int page, int size, Long exerciseId);


    /**
     * 改变奖励
     *
     * @param id            id
     * @param reward        奖励金额
     * @param rewardProduct 奖励产品
     * @return {@link Boolean}
     */
    Boolean changeReward(Long id, BigDecimal reward, String rewardProduct);


    /**
     * 创建奖励配置
     *
     * @param exercisePushRewardConfigForm 运动推动奖励配置形式
     * @return boolean
     */
    boolean createRewardConfig(ExercisePushRewardConfigForm exercisePushRewardConfigForm);

    /**
     * 获得还未设置奖励的活动
     *
     * @param page         页面
     * @param size         大小
     * @param exerciseName 运动名字
     * @return {@link IPage}<{@link ExercisePushRewardConfigVO}>
     */
    IPage<TopicExerciseVO> getUnSetReward(Integer page, Integer size, String exerciseName);
}
