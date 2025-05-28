package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.SportPlanPriceDao;
import com.aioveu.entity.SportPlanPrice;
import com.aioveu.service.SportPlanPriceService;
import org.springframework.stereotype.Service;

@Service
public class SportPlanPriceServiceImpl extends ServiceImpl<SportPlanPriceDao, SportPlanPrice> implements SportPlanPriceService {

    public boolean maintainPlanPrice(Integer planId, Integer membershipPrice, Integer normalPrice) {
        SportPlanPrice price = new SportPlanPrice();
        price.setPlanId(planId);
        price.setMembershipPrice(membershipPrice);
        price.setNormalPrice(normalPrice);
        QueryWrapper<SportPlanPrice> qw = new QueryWrapper<>();
        qw.eq("PLAN_ID", planId);
        return this.saveOrUpdate(price, qw);
    }

}
