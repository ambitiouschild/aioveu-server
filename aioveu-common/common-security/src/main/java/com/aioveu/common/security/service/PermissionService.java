package com.aioveu.common.security.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;

import java.util.*;

/**
 * @Description: TODO SpringSecurity 权限校验
 * @Author: 雒世松
 * @Date: 2025/6/5 16:11
 * @param
 * @return:
 **/

@Service("ss")
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 判断当前登录用户是否拥有操作权限
     *
     * @param requiredPerm 所需权限
     * @return 是否有权限
     */
    public boolean hasPerm(String requiredPerm) {

        if (StrUtil.isBlank(requiredPerm)) {
            log.info("权限检查：权限字符串为空");
            return false;
        }

        // 记录开始检查
        log.info("开始检查权限：{}", requiredPerm);


        // 超级管理员放行
        if (SecurityUtils.isRoot()) {
            log.info("超级管理员，跳过权限检查：{}", requiredPerm);
            return true;
        }

        // 获取当前登录用户的角色编码集合
        Set<String> roleCodes = SecurityUtils.getRoles();
        if (CollectionUtil.isEmpty(roleCodes)) {
            log.info("用户没有分配角色，权限检查失败：{}", requiredPerm);
            return false;
        }

        log.info("用户角色：{}", roleCodes);

        // 获取当前登录用户的所有角色的权限列表
        Set<String> rolePerms = this.getRolePermsFormCache(roleCodes);
        if (CollectionUtil.isEmpty(rolePerms)) {
            log.info("角色没有分配权限，权限检查失败：{}，角色：{}", requiredPerm, roleCodes);
            return false;
        }

        log.info("用户权限列表（{}个）：{}", rolePerms.size(), rolePerms);


        // 判断当前登录用户的所有角色的权限列表中是否包含所需权限
        boolean hasPermission = rolePerms.stream()
                .anyMatch(rolePerm ->
                        // 匹配权限，支持通配符(* 等)
                        PatternMatchUtils.simpleMatch(rolePerm, requiredPerm)
                );

        if (hasPermission) {
            log.info("权限检查通过：{}", requiredPerm);
        } else {
            // 记录更详细的失败信息
            log.info("用户无操作权限，所需权限：{}，用户角色：{}，用户权限：{}",
                    requiredPerm, roleCodes, rolePerms);
        }
        return hasPermission;
    }


    /**
     * 从缓存中获取角色权限列表
     *
     * @param roleCodes 角色编码集合
     * @return 角色权限列表
     */
    public Set<String> getRolePermsFormCache(Set<String> roleCodes) {
        // 检查输入是否为空

        log.info("从缓存中获取角色权限列表，步骤1：参数验证");
        log.info("检查角色编码集合是否为空");
        log.info("如果为空，返回空的不可修改集合，避免后续空指针异常");

        if (CollectionUtil.isEmpty(roleCodes)) {
            // 返回Collections.emptySet()比new HashSet<>()更优，因为：
            // 1. 不可修改，防止调用方意外修改
            // 2. 内存占用更小
            // 3. 明确表示"无权限"的意图
            return Collections.emptySet();
        }


        log.info("从缓存中获取角色权限列表，步骤2：准备返回结果");
        // 使用HashSet存储合并后的权限，因为：
        // 1. HashSet自动去重，避免同一权限在不同角色中重复
        // 2. 查找效率O(1)，适合后续的权限检查
        // 3. 不保证顺序，但权限检查通常不需要顺序
        Set<String> perms = new HashSet<>();


        log.info("从缓存中获取角色权限列表，步骤3：数据类型转换");
        // 将Set<String>转换为Collection<Object>，因为：
        // 1. RedisTemplate.opsForHash().multiGet()要求Collection<Object>类型
        // 2. 需要支持不同类型的序列化器
        // 注意：这里创建了一个新的ArrayList，有一定内存开销
        // 但考虑到角色数量通常不多（一般少于10个），开销可接受
        Collection<Object> roleCodesAsObjects = new ArrayList<>(roleCodes);


        log.info("从缓存中获取角色权限列表，步骤4：批量从Redis获取权限");
        // 使用multiGet一次性获取所有角色的权限，优势：
        // 1. 减少网络往返次数，从O(n)降到O(1)
        // 2. 原子性操作，确保数据一致性
        // 3. 降低Redis服务器压力
        //
        // RedisConstants.ROLE_PERMS_PREFIX 通常是类似 "auth:role:perms" 的字符串
        // 这个Hash结构存储了所有角色的权限映射
        //
        // 返回值rolePermsList的顺序与roleCodesAsObjects的顺序一致
        // 但这里我们不需要顺序，只需要合并所有权限
//        List<Object> rolePermsList = redisTemplate.opsForHash().multiGet(
//                RedisConstants.ROLE_PERMS_PREFIX,   // Redis Hash的key
//                roleCodesAsObjects);    // 要获取的字段列表

        List<Object> rolePermsList = redisTemplate.opsForHash().multiGet(
                RedisConstants.System.ROLE_PERMS,   // Redis Hash的key
                roleCodesAsObjects);    // 要获取的字段列表


        log.info("从缓存中获取角色权限列表，步骤5：处理返回结果");
        // 遍历获取到的每个角色的权限集合
        for (Object rolePermsObj : rolePermsList) {

            // 检查返回的对象是否为null
            // 可能为null的情况：
            // 1. Redis中不存在该角色的权限缓存
            // 2. 该角色的权限集合为空
            // 3. Redis中的数据过期被删除
            if (rolePermsObj == null) {
                // 记录调试日志（可选）
                // log.debug("Redis中未找到角色的权限缓存");
                continue; // 跳过null值，继续处理下一个
            }

            // 类型检查和转换
            // 这里假设Redis中存储的是Set<String>类型
            // 使用instanceof进行类型检查，避免ClassCastException
            if (rolePermsObj instanceof Set) {
                @SuppressWarnings("unchecked")
                Set<String> rolePerms = (Set<String>) rolePermsObj;
                perms.addAll(rolePerms);
            }
        }


        log.info("从缓存中获取角色权限列表，步骤6：返回结果：{}", perms);
        // 返回合并后的权限集合
        // 注意：返回的是可修改的HashSet
        // 如果调用方需要不可修改的视图，可以返回Collections.unmodifiableSet(perms)
        return perms;
    }
}
