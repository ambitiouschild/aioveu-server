package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.TopicExercise;
import com.aioveu.form.TopicExerciseForm;
import com.aioveu.form.TopicExercisePrePayForm;
import com.aioveu.vo.ExerciseTopicItemVO;
import com.aioveu.vo.ExerciseTopicPrePayVO;
import com.aioveu.vo.TopicExerciseJoinDetailVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface TopicExerciseService extends IService<TopicExercise> {

    /**
     * 活动主题报名
     * @param exerciseId
     * @param topicId
     * @param categoryId
     * @return
     */
    boolean create(Long exerciseId, Long topicId, Long categoryId);

    /**
     * 商家主题报名列表
     * @param topicId
     * @param storeId
     * @return
     */
    TopicExerciseJoinDetailVO joinList(Long topicId, Long storeId);

    /**
     * 获取主题的活动列表
     * @param form
     * @return
     */
    IPage<ExerciseTopicItemVO> exerciseList(TopicExerciseForm form);

    /**
     * 活动参加主题
     * @param topicExercise
     * @return
     */
    boolean join(TopicExercise topicExercise);

    /**
     * 活动退出主题
     * @param topicExercise
     * @return
     */
    boolean unJoin(TopicExercise topicExercise);

    /**
     * 根据店铺活动对应的参加活动
     * @param storeId
     * @return
     */
    List<TopicExercise> getByStoreId(Long storeId);

    /**
     * 预支付
     * @param form
     * @return
     */
    ExerciseTopicPrePayVO prePay(TopicExercisePrePayForm form);

    /**
     * 获取用户已购买的活动
     * @param topicId
     * @param userId
     * @return
     */
    List<Long> getUserBuyExercise(Long topicId, String userId);

    /**
     * 根据主题id和活动id查询分类id
     * @return
     */
    TopicExercise getByExerciseAndTopicId(Long exerciseId, Long topicId);

    /**
     * 获取店铺的主题颜色
     * @param storeId
     * @return
     */
    List<String> getStoreTopicColor(Long storeId);

    /**
     * 活动置顶算法更新
     * @param topicId
     */
    void exerciseTop(Long topicId);

    /**
     * 获取主题对应的店铺
     * @param topicId
     * @return
     */
    List<Long> getStoreIdByTopic(Long topicId);

    /**
     * 查询活动绑定的主题
     * @param exerciseId
     * @return
     */
    List<TopicExercise> getByExercise(Long exerciseId);
}
