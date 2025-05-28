package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.WxOpenSubmitAuditDao;
import com.aioveu.entity.WxOpenSubmitAudit;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.WxOpenSubmitAuditStatus;
import com.aioveu.service.WxOpenSubmitAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author： yao
 * @Date： 2025/2/14 15:34
 * @Describe：
 */
@Slf4j
@Service
public class WxOpenSubmitAuditServiceImpl extends ServiceImpl<WxOpenSubmitAuditDao, WxOpenSubmitAudit> implements WxOpenSubmitAuditService {
    @Override
    public WxOpenSubmitAudit getLatestSubmitAudit(String appId) {
        LambdaQueryWrapper<WxOpenSubmitAudit> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WxOpenSubmitAudit::getAppId, appId)
                .eq(WxOpenSubmitAudit::getStatus,DataStatus.NORMAL.getCode())
                .eq(WxOpenSubmitAudit::getAuditStatus, WxOpenSubmitAuditStatus.weapp_audit_success.getCode())
                .orderByDesc(WxOpenSubmitAudit::getCreateDate)
                .last("limit 1");;
        return getOne(wrapper);
    }

    @Override
    public Boolean updateStatus(Long id,Integer status) {
        WxOpenSubmitAudit submitAudit = new WxOpenSubmitAudit();
        submitAudit.setId(id);
        submitAudit.setStatus(status);
        return updateById(submitAudit);
    }

    @Override
    public Boolean updateStatusByAppId(String appId, Integer status) {
        LambdaUpdateWrapper<WxOpenSubmitAudit> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(WxOpenSubmitAudit::getStatus, status)
                .eq(WxOpenSubmitAudit::getAppId, appId)
                .eq(WxOpenSubmitAudit::getStatus, DataStatus.NORMAL.getCode());
        return update(wrapper);
    }

    @Override
    public Boolean updateAuditResultByAppId(String appId, WxOpenSubmitAudit entity) {
        LambdaUpdateWrapper<WxOpenSubmitAudit> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(WxOpenSubmitAudit::getAppId, appId)
                .eq(WxOpenSubmitAudit::getStatus, DataStatus.NORMAL.getCode())
                .set(WxOpenSubmitAudit::getAuditStatus, entity.getAuditStatus())
                .set(WxOpenSubmitAudit::getResultDate, new Date());
        if (StringUtils.isNotBlank(entity.getFailReason())){
            wrapper.set(WxOpenSubmitAudit::getFailReason, entity.getFailReason());
        }
        return update(wrapper);
    }
}
