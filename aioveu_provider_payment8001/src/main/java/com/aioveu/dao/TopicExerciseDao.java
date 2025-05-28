package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.Exercise;
import com.aioveu.entity.TopicExercise;
import com.aioveu.vo.ExerciseTopicItemVO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface TopicExerciseDao extends BaseMapper<TopicExercise> {

    /**
     * 主题详情获取活动列表
     * @param page
     * @param params
     * @return
     */
    IPage<ExerciseTopicItemVO> getExerciseList(IPage<ExerciseTopicItemVO> page, @Param("params") Map<String, Object> params);

    /**
     * 根据店铺活动对应的参加活动
     * @param storeId
     * @return
     */
    List<TopicExercise> getByStoreId(Long storeId);

    /**
     * 获取用户已购买的活动
     * @param topicId
     * @param productCategoryId
     * @param userId
     * @return
     */
    List<Long> getUserBuyExercise(Long topicId, Long productCategoryId, String userId);

    /**
     * 根据id查询活动
     * @param ids
     * @return
     */
    List<ExerciseTopicItemVO> getByIdList(List<Long> ids);

    /**
     * 查询店铺下参加的活动
     * @param storeId
     * @param topicId
     * @return
     */
    Exercise getByStoreAndTopic(Long storeId, Long topicId);

    /**
     * 获取店铺的主题颜色
     * @param storeId
     * @return
     */
    List<String> getStoreTopicColor(Long storeId);

    /**
     * 更新活动的置顶
     * @param exerciseId
     * @param id
     * @param priority
     * @return
     */
    int updateExerciseTop(Long exerciseId, Long id, Integer priority);

}
