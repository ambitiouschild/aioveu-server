package com.aioveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.VipCardDao;
import com.aioveu.dto.ExerciseVipDTO;
import com.aioveu.entity.FieldPlan;
import com.aioveu.entity.VipCard;
import com.aioveu.enums.DataStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.FieldPlanService;
import com.aioveu.service.VipCardService;
import com.aioveu.utils.PriceRuleUtils;
import com.aioveu.vo.PriceRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
@Slf4j
@Service
public class VipCardServiceImpl extends ServiceImpl<VipCardDao, VipCard> implements VipCardService {

    @Autowired
    private FieldPlanService fieldPlanService;

    @Override
    public List<ExerciseVipDTO> getExerciseVipCard(List<Long> exerciseIdList) {
        return getBaseMapper().getExerciseVipCard(exerciseIdList);
    }

    @Override
    public List<VipCard> getActiveList(String pCategoryCode, Long companyId) {
        QueryWrapper<VipCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VipCard::getStatus, DataStatus.NORMAL.getCode())
                .eq(VipCard::getProductCategoryCode, pCategoryCode)
                .eq(VipCard::getCompanyId, companyId)
                .orderByAsc(VipCard::getSellingPrice);
        return list(queryWrapper);
    }

    @Override
    public List<VipCard> getAllList(String pCategoryCode, Long companyId) {
        QueryWrapper<VipCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(VipCard::getStatus, DataStatus.NORMAL.getCode(), DataStatus.LOW.getCode())
                .eq(VipCard::getProductCategoryCode, pCategoryCode)
                .eq(VipCard::getCompanyId, companyId)
                .orderByDesc(VipCard::getCreateDate);
        return list(queryWrapper);
    }

    @Override
    public void saveVipCard(VipCard vipCard) {
        if (CollectionUtils.isNotEmpty(vipCard.getPriceRules())) {
            vipCard.setPriceRule(JSONObject.toJSONString(vipCard.getPriceRules()));
        }
        if (vipCard.getId() != null) {
            this.updateById(vipCard);
        } else {
            this.save(vipCard);
        }
    }

    @Override
    public PriceRule matchingPriceRule(VipCard vipCard, FieldPlan fieldPlan) {
        try {
            String dayStr = DateFormatUtils.format(fieldPlan.getFieldDay(), "yyyy-MM-dd");
            Date startDate = DateUtils.parseDate(dayStr + " " + fieldPlan.getStartTime(), "yyyy-MM-dd HH:mm:ss");
            return PriceRuleUtils.matchingPriceRule(vipCard.getPriceRules(), startDate, null, fieldPlan.getVenueId());
        } catch (Exception e) {
            throw new SportException(e.getMessage());
        }
    }

    @Override
    public BigDecimal getFieldVipPrice(Long vipCardId, List<Long> fieldPlanIds) {
        VipCard vipCard = null;
        if (vipCardId != null) {
            vipCard = this.getById(vipCardId);
        }
        BigDecimal vipAllPrice = BigDecimal.ZERO;
        for (Long id : fieldPlanIds) {
            FieldPlan fieldPlan = this.fieldPlanService.getById(id);
            BigDecimal vipPrice = fieldPlan.getVipPrice();
            if (vipCard != null) {
                PriceRule priceRule = this.matchingPriceRule(vipCard, fieldPlan);
                if (priceRule != null) {
                    //会员卡特殊订场价格按照1小时价格算，兼容订场半小时
                    // 订场价格单位如果是半小时，取会员卡特殊订场价格/2，
                    // 订场价格单位如果是1小时，取会员卡特殊订场价格
                    Long minute = (fieldPlan.getEndTime().getTime() - fieldPlan.getStartTime().getTime())/(1000*60);
                    if (minute == 30){
                        vipPrice = priceRule.getVipPrice().divide(BigDecimal.valueOf(2), 2 ,BigDecimal.ROUND_HALF_UP);
                    }else {
                        vipPrice = priceRule.getVipPrice();
                    }
                }
            }
            vipAllPrice = vipAllPrice.add(vipPrice);
        }
        return vipAllPrice;
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        VipCard gt = new VipCard();
        gt.setId(id);
        gt.setStatus(status);
        return updateById(gt);
    }
}
