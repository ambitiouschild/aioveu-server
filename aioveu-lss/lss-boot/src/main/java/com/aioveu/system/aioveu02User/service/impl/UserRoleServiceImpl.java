package com.aioveu.system.aioveu02User.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.aioveu.common.security.service.TokenService;
import com.aioveu.system.aioveu02User.mapper.UserRoleMapper;
import com.aioveu.system.aioveu02User.model.entity.UserRole;
import com.aioveu.system.aioveu02User.service.UserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.aioveu.common.security.util.SecurityUtils;
/**
 * @ClassName: UserRoleServiceImpl
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:13
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private final TokenService tokenService;

    /**
     * 保存用户角色
     *
     * @param userId 用户ID
     * @param roleIds 选择的角色ID集合
     * @return
     */
    @Override
    public void saveUserRoles(Long userId, List<Long> roleIds) {
        if (userId == null || CollectionUtil.isEmpty(roleIds)) {
            return ;
        }

        // 获取现有角色
        List<Long> userRoleIds = this.list(new LambdaQueryWrapper<UserRole>()
                        .select(UserRole::getRoleId)
                        .eq(UserRole::getUserId, userId))
                .parallelStream()
                .map(UserRole::getRoleId)
                .toList();

        // 使用Set提升对比效率
        Set<Long> oldRoles = new HashSet<>(userRoleIds);
        Set<Long> newRoles = new HashSet<>(roleIds);

        // 计算变更集
        Set<Long> addedRoles = new HashSet<>(newRoles);
        addedRoles.removeAll(oldRoles);

        Set<Long> removedRoles = new HashSet<>(oldRoles);
        removedRoles.removeAll(newRoles);

        boolean rolesChanged = !addedRoles.isEmpty() || !removedRoles.isEmpty();

        // 批量保存新增角色
        if (!addedRoles.isEmpty()) {
            this.saveBatch(addedRoles.stream()
                    .map(roleId -> new UserRole(userId, roleId))
                    .collect(Collectors.toList()));
        }

        // 删除废弃角色
        if (!removedRoles.isEmpty()) {
            this.remove(new LambdaQueryWrapper<UserRole>()
                    .eq(UserRole::getUserId, userId)
                    .in(UserRole::getRoleId, removedRoles));
        }

        // 当权限变更时清除登录态
        if (rolesChanged) {
            // 获取用户所有有效token（根据实际token存储实现）
            String accessToken = SecurityUtils.getTokenFromAuthentication();

            log.info("获取用户所有有效token（根据实际token存储实现）", accessToken);

            log.info("当权限变更时清除登录态,将令牌加入黑名单");
            tokenService.invalidateToken(accessToken);
        }
    }

    /**
     * 判断角色是否存在绑定的用户
     *
     * @param roleId 角色ID
     * @return true：已分配 false：未分配
     */
    @Override
    public boolean hasAssignedUsers(Long roleId) {
        int count = this.baseMapper.countUsersForRole(roleId);
        return count > 0;
    }
}
