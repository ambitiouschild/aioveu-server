package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.SysConfigDao;
import com.aioveu.entity.SysConfig;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.SysConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/02/19 11:41
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfig> implements SysConfigService {

    @Override
    public SysConfig getByCode(String code) {
        LambdaQueryWrapper<SysConfig> eq = Wrappers.lambdaQuery(SysConfig.class)
                .eq(SysConfig::getCode, code)
                .eq(SysConfig::getStatus, DataStatus.NORMAL.getCode());
        return getOne(eq);
    }

    @Override
    public List<SysConfig> getCategoryConfigList(String categoryCode) {
        LambdaQueryWrapper<SysConfig> eq = Wrappers.lambdaQuery(SysConfig.class)
                .eq(SysConfig::getCategoryCode, categoryCode)
                .eq(SysConfig::getStatus, DataStatus.NORMAL.getCode())
                .orderByAsc(SysConfig::getCreateDate);
        return list(eq);
    }
}
