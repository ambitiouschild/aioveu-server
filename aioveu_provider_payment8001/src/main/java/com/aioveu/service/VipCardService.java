package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.dto.ExerciseVipDTO;
import com.aioveu.entity.FieldPlan;
import com.aioveu.entity.VipCard;
import com.aioveu.vo.FieldPlanVO;
import com.aioveu.vo.PriceRule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
public interface VipCardService extends IService<VipCard> {

    /**
     * 获取活动的会员卡
     * @param exerciseIdList
     * @return
     */
    List<ExerciseVipDTO> getExerciseVipCard(List<Long> exerciseIdList);


    List<VipCard> getActiveList(String pCategoryCode, Long companyId);

    List<VipCard> getAllList(String pCategoryCode, Long companyId);

    void saveVipCard(VipCard vipCard);

    PriceRule matchingPriceRule(VipCard vipCard, FieldPlan fieldPlan);

    BigDecimal getFieldVipPrice(Long vipCardId, List<Long> fieldPlanIds);

    boolean changeStatus(Long id, Integer status);
}
