package com.aioveu.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.AuditDao;
import com.aioveu.entity.Audit;
import com.aioveu.entity.StoreCoach;
import com.aioveu.enums.AuditStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.AuditService;
import com.aioveu.service.IApprovedService;
import com.aioveu.service.StoreCoachService;
import com.aioveu.service.UserService;
import com.aioveu.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class AuditServiceImpl extends ServiceImpl<AuditDao, Audit> implements AuditService {

    @Autowired
    private UserService userService;

    @Autowired
    private StoreCoachService storeCoachService;

    @Override
    public IPage<Audit> selAuditByCondition(int page, int size, Integer storeId, String userId, Integer status, Integer auditType) {
        LambdaQueryWrapper<Audit> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Audit::getStoreId, storeId);
        if (!StringUtils.isEmpty(userId)) {
            wrapper.eq(Audit::getAuditUserId, userId);
        }
        if (status != null) {
            wrapper.eq(Audit::getAuditStatus, status);
        }
        if (auditType != null) {
            wrapper.eq(Audit::getAuditType, auditType);
        }
        wrapper.orderByDesc(Audit::getCreateDate);
        IPage<Audit> poolIPage = new Page<>(page, size);
        IPage<Audit> auditIPage = this.baseMapper.selectPage(poolIPage, wrapper);
        for (Audit record : auditIPage.getRecords()) {
            UserVo createUser = userService.getByUserId(record.getCreateUserId());
            if (createUser != null) {
                record.setCreateUserName(createUser.getName());
            }
            if(!StringUtils.isEmpty(record.getAuditUserId())) {
                UserVo auditUser = userService.getByUserId(record.getAuditUserId());
                if (auditUser != null) {
                    record.setAuditUserName(auditUser.getName());
                }
            }
        }
        return auditIPage;
    }

    @Override
    public void submit(Audit audit) {
        if(audit.getAuditType() == 2) {
            JSONObject jsonObject = JSONUtil.parseObj(audit.getJsonVal());
            Audit val = this.baseMapper.findCancelGradeAudit(jsonObject.getLong("gradeId"), audit.getCreateUserId());
            if (val != null) {
                throw new SportException("您已经提交过当前班级的取消申请");
            }
        }
        if (StringUtils.isNotEmpty(audit.getAuditService())) {
            IApprovedService approvedService = (IApprovedService) ApplicationContextGetBeanHelper.getBean(audit.getAuditService());
            if (approvedService == null || !approvedService.submit(audit)) {
                throw new SportException("提交失败!");
            }
        }
        StoreCoach storeCoach = storeCoachService.getByUserIdAndStoreId(audit.getCreateUserId(), audit.getStoreId(), 1);
        if (storeCoach != null) {
            audit.setStoreUsername(storeCoach.getName());
        }
        audit.setAuditStatus(AuditStatus.Submit.getCode());
        this.baseMapper.insert(audit);
    }

    @Override
    public void approved(Audit audit) {
        Audit auditById = this.baseMapper.selectById(audit.getId());
        if (auditById.getAuditStatus() != AuditStatus.Submit.getCode()) {
            throw new SportException("状态必须为提交，才可以操作");
        }
        if (StringUtils.isNotEmpty(auditById.getAuditService())) {
            IApprovedService approvedService = (IApprovedService) ApplicationContextGetBeanHelper.getBean(auditById.getAuditService());
            if (approvedService == null || !approvedService.approved(auditById.getJsonVal())) {
                throw new SportException("审批失败!");
            }
        }
        audit.setAuditStatus(AuditStatus.Approved.getCode());
        audit.setAuditDate(new Date());
        this.baseMapper.updateById(audit);
    }

    @Override
    public void reject(Audit audit) {
        Audit auditById = this.baseMapper.selectById(audit.getId());
        if (auditById.getAuditStatus() != AuditStatus.Submit.getCode()) {
            throw new SportException("状态必须为提交，才可以操作");
        }
        if (StringUtils.isNotEmpty(auditById.getAuditService())) {
            IApprovedService approvedService = (IApprovedService) ApplicationContextGetBeanHelper.getBean(auditById.getAuditService());
            if (approvedService == null || !approvedService.reject(auditById.getJsonVal())) {
                throw new SportException("操作失败!");
            }
        }
        audit.setAuditStatus(AuditStatus.Reject.getCode());
        audit.setAuditDate(new Date());
        this.baseMapper.updateById(audit);
    }
}
