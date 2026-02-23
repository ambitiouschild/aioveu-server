package com.aioveu.tenant.aioveu03Role.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aioveu.common.tenant.TenantContextHolder;
import com.aioveu.tenant.aioveu03Role.mapper.RoleDeptMapper;
import com.aioveu.tenant.aioveu03Role.model.entity.RoleDept;
import com.aioveu.tenant.aioveu03Role.service.RoleDeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName: RoleDeptServiceImpl
 * @Description TODO 角色部门关联服务实现
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 13:25
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RoleDeptServiceImpl extends ServiceImpl<RoleDeptMapper, RoleDept> implements RoleDeptService {

    @Override
    public List<Long> getDeptIdsByRoleId(Long roleId) {
        if (roleId == null) {
            return Collections.emptyList();
        }
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return Collections.emptyList();
        }
        return this.baseMapper.getDeptIdsByRoleId(tenantId, roleId);
    }

    @Override
    public List<Long> getDeptIdsByRoleCodes(List<String> roleCodes) {
        if (CollectionUtil.isEmpty(roleCodes)) {
            return Collections.emptyList();
        }
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return Collections.emptyList();
        }
        return this.baseMapper.getDeptIdsByRoleCodes(tenantId, roleCodes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleDepts(Long roleId, List<Long> deptIds) {
        if (roleId == null || CollectionUtil.isEmpty(deptIds)) {
            return;
        }
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return;
        }

        this.remove(new LambdaQueryWrapper<RoleDept>()
                .eq(RoleDept::getTenantId, tenantId)
                .eq(RoleDept::getRoleId, roleId));

        List<RoleDept> roleDepts = deptIds.stream()
                .map(deptId -> new RoleDept(tenantId, roleId, deptId))
                .toList();
        this.saveBatch(roleDepts);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        if (roleId == null) {
            return;
        }
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId == null) {
            return;
        }
        this.remove(new LambdaQueryWrapper<RoleDept>()
                .eq(RoleDept::getTenantId, tenantId)
                .eq(RoleDept::getRoleId, roleId));
    }
}
