package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.dto.ExerciseVipDTO;
import com.aioveu.entity.VipCard;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
@Repository
public interface VipCardDao extends BaseMapper<VipCard> {

    /**
     * 获取活动的会员卡
     * @param exerciseIdList
     * @return
     */
    List<ExerciseVipDTO> getExerciseVipCard(List<Long> exerciseIdList);


}
