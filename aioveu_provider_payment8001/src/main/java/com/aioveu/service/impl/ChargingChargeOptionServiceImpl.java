package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ChargingChargeOptionDao;
import com.aioveu.entity.ChargingChargeOption;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.ChargingChargeOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ChargingChargeOptionServiceImpl extends ServiceImpl<ChargingChargeOptionDao, ChargingChargeOption> implements ChargingChargeOptionService {


    @Override
    public List<ChargingChargeOption> getChargeOptionByCode(String chargingCode) {
        QueryWrapper<ChargingChargeOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChargingChargeOption::getChargingCode, chargingCode)
                .eq(ChargingChargeOption::getStatus, DataStatus.NORMAL.getCode())
                .orderByAsc(ChargingChargeOption::getPrice);
        return list(queryWrapper);
    }
}
