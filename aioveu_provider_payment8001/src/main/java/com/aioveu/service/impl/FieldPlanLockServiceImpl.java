package com.aioveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.FieldPlanLockDao;
import com.aioveu.entity.*;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FieldPlanLockServiceImpl extends ServiceImpl<FieldPlanLockDao, FieldPlanLock> implements FieldPlanLockService {

    @Override
    public List<FieldPlanLock> getByStoreId(Long storeId, Long venueId, String name) {
        List<FieldPlanLock> list = this.getBaseMapper().getByStoreId(storeId, venueId, name);
        if (CollectionUtils.isNotEmpty(list)) {
            for (FieldPlanLock item : list) {
            }
        }
        return list;
    }

    @Override
    public List<FieldPlanLock> getByVenueId(Long venueId, Date fieldPlanDate) {
        QueryWrapper<FieldPlanLock> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FieldPlanLock::getVenueId, venueId)
                .ge(FieldPlanLock::getExpiryDate, fieldPlanDate)
                .eq(FieldPlanLock::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }

    @Override
    public boolean changeStatus(Long id, Integer status) {
        FieldPlanLock gt = new FieldPlanLock();
        gt.setId(id);
        gt.setStatus(status);
        return updateById(gt);
    }

    @Override
    public boolean create(FieldPlanLock item) {
        try {
            item.setExpiryDate(DateUtils.parseDate(item.getExpiryDay(), "yyyy-MM-dd"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (!CollectionUtils.isEmpty(item.getLockRules())) {
            item.setLockRule(JSONObject.toJSONString(item.getLockRules()));
        } else {
            item.setLockRule(null);
        }
        if (item.getId() == null) {
            save(item);
        } else {
            updateById(item);
        }
        return true;
    }
}
