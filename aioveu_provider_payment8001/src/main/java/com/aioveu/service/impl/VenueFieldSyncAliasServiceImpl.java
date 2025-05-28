package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.VenueFieldSyncAliasDao;
import com.aioveu.entity.VenueFieldSyncAlias;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.VenueFieldSyncAliasService;
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
public class VenueFieldSyncAliasServiceImpl extends ServiceImpl<VenueFieldSyncAliasDao, VenueFieldSyncAlias> implements VenueFieldSyncAliasService {


    @Override
    public List<VenueFieldSyncAlias> findByStoreIdAndPlatform(Long storeId, String platformCode) {
        QueryWrapper<VenueFieldSyncAlias> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(VenueFieldSyncAlias::getStoreId, storeId)
                .eq(VenueFieldSyncAlias::getPlatformCode, platformCode)
                .eq(VenueFieldSyncAlias::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }
}
