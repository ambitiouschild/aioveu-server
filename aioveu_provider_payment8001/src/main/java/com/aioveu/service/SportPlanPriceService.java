package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.SportPlanPrice;

public interface SportPlanPriceService extends IService<SportPlanPrice> {

    boolean maintainPlanPrice(Integer planId, Integer membershipPrice, Integer normalPrice);

}
