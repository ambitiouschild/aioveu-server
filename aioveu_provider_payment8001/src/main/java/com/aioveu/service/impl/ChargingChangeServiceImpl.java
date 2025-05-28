package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ChargingChangeDao;
import com.aioveu.entity.ChargingChange;
import com.aioveu.entity.StoreChargingOption;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.ChargingChangeService;
import com.aioveu.service.StoreChargingOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ChargingChangeServiceImpl extends ServiceImpl<ChargingChangeDao, ChargingChange> implements ChargingChangeService {

    @Autowired
    private StoreChargingOptionService storeChargingOptionService;

    @Resource
    private RedisLockRegistry redisLockRegistry;

    @Override
    public boolean change(ChargingChange chargingChange) {
        StoreChargingOption storeChargingOption;
        if (chargingChange.getStoreId() == null) {
            storeChargingOption = storeChargingOptionService.getStoreChargingByCompanyId(chargingChange.getCompanyId(), chargingChange.getChargingCode());
        } else {
            storeChargingOption = storeChargingOptionService.getByStoreIdAndCode(chargingChange.getStoreId(), chargingChange.getChargingCode());
        }
        if (storeChargingOption == null) {
            log.error("未查询到店铺:{}增值服务:{}", chargingChange.getStoreId(), chargingChange.getChargingCode());
            return false;
        }
        if (chargingChange.getStoreId() == null) {
            chargingChange.setStoreId(storeChargingOption.getStoreId());
        } else if (chargingChange.getCompanyId() == null) {
            chargingChange.setCompanyId(storeChargingOption.getCompanyId());
        }
        if (storeChargingOption.getTotal() == null) {
            storeChargingOption.setTotal(0);
        }
        String key = "STORE_CHARGING_CHANGE_" + storeChargingOption.getId();
        Lock lock = redisLockRegistry.obtain(key);
        lock.lock();
        try {
            // 增加增值服务项目次数
            StoreChargingOption updateStoreChargingOption = new StoreChargingOption();
            updateStoreChargingOption.setId(storeChargingOption.getId());
            if (chargingChange.getChangeType() == 1) {
                updateStoreChargingOption.setTotal(storeChargingOption.getTotal() + chargingChange.getCount());
            } else {
                updateStoreChargingOption.setTotal(storeChargingOption.getTotal() - chargingChange.getCount());
            }
            storeChargingOptionService.updateById(updateStoreChargingOption);
            chargingChange.setRemainCount(updateStoreChargingOption.getTotal());
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        //TODO 2025 后续增加操作日志和 次数不足提醒
        // 保存增值服务变动记录
        return save(chargingChange);
    }

    @Override
    public IPage<ChargingChange> getChangeList(Long storeId, String code, Integer changeType, int page, int size) {
        QueryWrapper<ChargingChange> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ChargingChange::getStoreId, storeId)
                .eq(ChargingChange::getChargingCode, code)
                .eq(ChargingChange::getStatus, DataStatus.NORMAL.getCode());
        if (changeType != null && changeType != -1) {
            queryWrapper.lambda().eq(ChargingChange::getChangeType, changeType);
        }
        queryWrapper.lambda().orderByDesc(ChargingChange::getCreateDate);
        return page(new Page<>(page, size), queryWrapper);
    }
}
