package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.VenueSyncAliasDao;
import com.aioveu.entity.VenueSyncAlias;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.VenueSyncAliasService;
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
public class VenueSyncAliasServiceImpl extends ServiceImpl<VenueSyncAliasDao, VenueSyncAlias> implements VenueSyncAliasService {


    @Override
    public List<VenueSyncAlias> findByStoreIdAndPlatform(Long storeId, String platformCode) {
        QueryWrapper<VenueSyncAlias> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VenueSyncAlias::getStoreId, storeId)
                .eq(VenueSyncAlias::getPlatformCode, platformCode)
                .eq(VenueSyncAlias::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }
}
