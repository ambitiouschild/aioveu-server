package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ExerciseVipCard;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
public interface ExerciseVipCardService extends IService<ExerciseVipCard> {

    /**
     * 活动是否有vip
     * @param exerciseId
     * @return
     */
    Long getExerciseVip(Long exerciseId);

    /**
     * 领取会员卡
     * @param exerciseId
     * @param userId
     * @return
     */
    boolean receiveVip(Long exerciseId, String userId);


}
