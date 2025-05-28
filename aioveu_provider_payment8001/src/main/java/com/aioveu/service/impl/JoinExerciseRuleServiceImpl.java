package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.JoinExerciseRuleDao;
import com.aioveu.entity.JoinExerciseRule;
import com.aioveu.service.JoinExerciseRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author： yao
 * @Date： 2024/10/8 09:09
 * @Describe：
 */
@Slf4j
@Service
public class JoinExerciseRuleServiceImpl extends ServiceImpl<JoinExerciseRuleDao, JoinExerciseRule> implements JoinExerciseRuleService {
    @Override
    public boolean deleteByExerciseId(String exerciseId) {
        QueryWrapper<JoinExerciseRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(JoinExerciseRule::getExerciseId, exerciseId);
        return remove(queryWrapper);
    }

    @Override
    public List<JoinExerciseRule> getByExerciseId(String exerciseId) {
        QueryWrapper<JoinExerciseRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(JoinExerciseRule::getExerciseId, exerciseId)
                .orderByAsc(JoinExerciseRule::getJoinNumber);
        return list(queryWrapper);
    }

    @Override
    public List<JoinExerciseRule> getJoinExerciseLowestPrice(List<String> exerciseIds) {
        QueryWrapper<JoinExerciseRule> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("exercise_id","min(join_price) as join_price")
                .lambda().in(JoinExerciseRule::getExerciseId, exerciseIds)
//                .orderByAsc(JoinExerciseRule::getJoinNumber)
                .groupBy(JoinExerciseRule::getExerciseId);
        return list(queryWrapper);
    }


}
