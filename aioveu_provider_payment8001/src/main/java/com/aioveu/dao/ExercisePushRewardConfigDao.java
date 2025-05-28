package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.entity.ExercisePushRewardConfig;
import com.aioveu.vo.ExercisePushRewardConfigVO;
import com.aioveu.vo.TopicExerciseVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface ExercisePushRewardConfigDao extends BaseMapper<ExercisePushRewardConfig> {

    IPage<ExercisePushRewardConfigVO> getExerciseAndRewardOrExerciseId(Page<ExercisePushRewardConfigVO> Page, Long exerciseId);

    IPage<TopicExerciseVO> getUnSetReward(Page<TopicExerciseVO> Page, String exerciseName);
}
