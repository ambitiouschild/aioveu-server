package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ExerciseVipCardDao;
import com.aioveu.entity.ExerciseVipCard;
import com.aioveu.service.ExerciseVipCardService;
import com.aioveu.service.UserVipCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
@Slf4j
@Service
public class ExerciseVipCardServiceImpl extends ServiceImpl<ExerciseVipCardDao, ExerciseVipCard> implements ExerciseVipCardService {

    @Autowired
    private UserVipCardService userVipCardService;


    @Override
    public Long getExerciseVip(Long exerciseId) {
        QueryWrapper<ExerciseVipCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseVipCard::getExerciseId, exerciseId);
        ExerciseVipCard exerciseVipCard = getOne(queryWrapper);
        if (exerciseVipCard != null) {
            return exerciseVipCard.getVipCardId();
        }
        return null;
    }

    @Override
    public boolean receiveVip(Long exerciseId, String userId) {
        Long vipId = getExerciseVip(exerciseId);
        return userVipCardService.receiveVipCard(vipId, userId);
    }
}
