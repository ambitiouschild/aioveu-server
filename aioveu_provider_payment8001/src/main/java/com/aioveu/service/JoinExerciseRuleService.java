package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.JoinExerciseRule;

import java.util.List;

/**
 * 拼单活动规则接口
 * @Author： yao
 * @Date： 2024/10/8 09:08
 * @Describe：
 */
public interface JoinExerciseRuleService extends IService<JoinExerciseRule> {

    /**
     * 根据拼单产品id删除对应的拼单规则
     * @param exerciseId
     * @return
     */
    boolean deleteByExerciseId(String exerciseId);

    /**
     * 通过拼单产品id获取拼单规则
     * @param exerciseId
     * @return
     */
    List<JoinExerciseRule> getByExerciseId(String exerciseId);

    /**
     * 获取拼单产品规则中，最低价格
     * @param exerciseIds
     * @return
     */
    List<JoinExerciseRule> getJoinExerciseLowestPrice(List<String> exerciseIds);
}
