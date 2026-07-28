package com.aioveu.common.security.core.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.core.constant.JwtClaimConstants;
import com.aioveu.common.core.constant.SecurityConstants;
import com.aioveu.common.core.constant.SystemConstants;
import com.aioveu.common.core.model.RoleDataScope;
import com.aioveu.common.core.tenant.TenantContextHolder;
import com.aioveu.common.security.core.model.SysUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;
import java.util.stream.Collectors;

import static cn.hutool.core.convert.Convert.toInt;

/**
 * @Description: TODO Spring Security 工具类  瘦身后的 SecurityUtils（core 版）
 * @Author: 雒世松
 * @Date: 2025/7/28 15:13
 * @param
 * @return:
 **/

@Slf4j
public class SecurityUtils {


    /**
     * 获取认证对象的详细信息
     *
     * @返回值:
     *   - Authentication Spring Security认证对象
     *   - 包含主体、凭证、权限等完整信息
     *
     * @高级用途:
     *   - 自定义权限验证逻辑
     *   - 获取认证提供者信息
     *   - 访问原始认证细节
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    /**
     * 获取当前登录人信息
     *
     * @return Optional<SysUserDetails>
     */
    public static Optional<SysUserDetails> getUser() {

//        log.info("开始获取用户信息");
        Authentication authentication = getAuthentication();

        if (authentication == null) {
            log.warn("⚠️ Authentication 为 null");
            return Optional.empty();
        }

//        log.info("Authentication 类型: {}", authentication.getClass().getName());
//        log.info("Authentication 名称: {}", authentication.getName());
//        log.info("是否已认证: {}", authentication.isAuthenticated());
//
//        Object principal = authentication.getPrincipal();
//        log.info("Principal 类型: {}", principal.getClass().getName());
//        log.info("Principal 值: {}", principal);

        if (authentication.getPrincipal() instanceof SysUserDetails userDetails) {
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    /**
     * 从JWT令牌中获取当前用户ID
     *
     * @原理说明:
     *   - 从SecurityContext中获取Authentication认证对象
     *   - 提取JWT令牌中的claims声明信息
     *   - 从声明中获取userId字段并转换为Long类型
     *
     * @返回值:
     *   - Long 用户ID，如果未认证或用户ID不存在返回null
     *
     * @使用示例:
     *   Long userId = SecurityUtils.getUserId();
     *   if (userId != null) {
     *       // 基于用户ID进行业务操作
     *   }
     *
     * @注意: 需要在Spring Security认证上下文中调用，如Controller、Service层
     */


    /**
     * 获取用户ID
     *
     * @return Long
     */
    public static Long getUserId() {
        return getUser().map(SysUserDetails::getUserId).orElse(null);
    }

    /**
     * 获取当前认证用户的用户名（通常是登录账号）
     *
     * @原理说明:
     *   - 直接从Authentication对象的getName()方法获取
     *   - 对应JWT令牌中的"sub" (subject) 声明
     *
     * @返回值:
     *   - String 用户名，未认证时返回null
     *
     * @与getUserId()区别:
     *   - 用户名可能变化（如改名），用户ID唯一不变
     *   - 用户名用于显示，用户ID用于数据关联
     */
    public static String getUsername() {
        return getUser().map(SysUserDetails::getUsername).orElse(null);
    }



    /**
     * 获取当前用户拥有的角色编码集合
     *
     * @原理说明:
     *   - Spring Security将权限信息存储在Authentication的Authorities中
     *   - 角色通常以"ROLE_"前缀表示，如"ROLE_ADMIN"
     *   - 此方法提取所有权限并转换为角色编码集合
     *
     * @返回值:
     *   - Set<String> 角色编码集合（如["ADMIN", "USER"]），未认证返回null
     *   - 返回不可修改的集合，防止意外修改
     *
     * @使用场景:
     *   - 基于角色的访问控制（RBAC）
     *   - 菜单权限过滤
     *   - 接口权限验证
     *
     * @示例输出: ["ADMIN", "MANAGER", "USER"]
     */
    public static Set<String> getRoles1() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return AuthorityUtils.authorityListToSet(authentication.getAuthorities())
                    .stream()

                    // 筛选角色权限，使用 Hutool 工具类（推荐）
                    .filter(authority -> StrUtil.startWith(authority, SecurityConstants.ROLE_PREFIX))
                    // 移除 ROLE_ 前缀
                    .map(authority -> StrUtil.removePrefix(authority, SecurityConstants.ROLE_PREFIX))

                    // 转换为不可修改集合，确保线程安全
                    .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
        }
        return Collections.emptySet();  // 返回空集合而不是 null，避免空指针
    }

    /**
     * 获取角色集合
     *
     * @return 角色集合
     */
    public static Set<String> getRoles() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getAuthorities)
                .filter(CollectionUtil::isNotEmpty)
                .stream()
                .flatMap(Collection::stream)
                .map(GrantedAuthority::getAuthority)
                // 筛选角色,authorities 中的角色都是以 ROLE_ 开头
                .filter(authority -> authority.startsWith(SecurityConstants.ROLE_PREFIX))
                .map(authority -> StrUtil.removePrefix(authority, SecurityConstants.ROLE_PREFIX))
                .collect(Collectors.toSet());
    }

    /**
     * 获取数据权限列表
     *
     * @return 数据权限列表
     */
    public static List<RoleDataScope> getDataScopes() {
        return getUser().map(SysUserDetails::getDataScopes).orElse(Collections.emptyList());
    }

    /**
     * 获取部门ID
     */
    /**
     * 获取当前用户所属的部门ID
     *
     * @原理说明:
     *   - 从JWT声明的"deptId"字段获取
     *   - 用于数据权限控制，限制用户只能访问本部门数据
     *
     * @返回值:
     *   - Long 部门ID，未设置部门返回null
     *
     * @数据权限应用:
     *   - 查询过滤: WHERE dept_id = #{deptId}
     *   - 数据隔离: 不同部门数据相互不可见
     *   - 审计追踪: 操作记录关联部门信息
     */
    public static Long getDeptId() {
        return getUser().map(SysUserDetails::getDeptId).orElse(null);
    }

    /*
    * 获取租户ID
    * */
    public static Long getTenantId() {
        // 1️JWT 中的（已认证）
        Long jwtTenantId = getUser().map(SysUserDetails::getTenantId).orElse(null);
        if (jwtTenantId != null) {
            return jwtTenantId;
        }
        // 2️公共接口 / 系统态
        return TenantContextHolder.getTenantId();
    }

    /*
     * 获取openId
     * */
    public static String getOpenId() {
        return getUser().map(SysUserDetails::getOpenId).orElse(null);
    }




    /**
     * 是否可切换租户
     *
     * @return true 表示可切换租户
     */
    public static boolean canSwitchTenant() {
        return getUser().map(SysUserDetails::getCanSwitchTenant).orElse(false);
    }




    /**
     * 判断当前用户是否为超级管理员（最高权限角色）
     *
     * @原理说明:
     *   - 检查用户角色集合是否包含预设的超级管理员角色编码
     *   - 超级管理员通常拥有系统所有权限，不受数据权限限制
     *
     * @返回值:
     *   - boolean true-是超级管理员, false-不是或未认证
     *
     * @超级管理员特权:
     *   - 绕过所有数据权限限制
     *   - 访问系统所有功能和数据
     *   - 进行系统级管理操作
     *
     * @配置参考: SystemConstants.ROOT_ROLE_CODE 定义超级管理员角色编码
     */
    public static boolean isRoot() {
        Set<String> roles = getRoles();
        return roles != null && roles.contains(SystemConstants.ROOT_ROLE_CODE);
    }


    /**
     * 获取当前用户的数据权限范围
     *
     * @原理说明:
     *   - 数据权限控制用户能看到的数据范围
     *   - 从JWT声明的"dataScope"字段获取权限级别
     *
     * @返回值:
     *   - Integer 数据权限范围代码，未设置返回null
     *
     * @数据权限级别（通常定义）:
     *   - 1: 仅本人数据（SELF）
     *   - 2: 本部门数据（DEPT）
     *   - 3: 本部门及子部门数据（DEPT_AND_CHILD）
     *   - 4: 全部数据（ALL）
     *   - 5: 自定义数据范围（CUSTOM）
     *
     * @参考枚举: DataScopeEnum 定义具体的数据权限级别
     *
     * @SQL应用示例:
     *   - 仅本人: WHERE create_by = #{userId}
     *   - 本部门: WHERE dept_id = #{deptId}
     *   - 全部: 无过滤条件
     */
    public static Integer getDataScope() {
        return getUser().map(SysUserDetails::getDataScope).orElse(null);
    }

    /**
     * 获取会员ID（适用于多租户或会员体系）
     *
     * @原理说明:
     *   - 在会员制系统中，用户可能关联会员信息
     *   - 从JWT声明的"memberId"字段获取会员标识
     *
     * @返回值:
     *   - Long 会员ID，非会员用户返回null
     *
     * @使用场景:
     *   - 多租户数据隔离
     *   - 会员等级权益控制
     *   - 跨系统用户映射
     *
     * @与userId区别:
     *   - userId: 系统内部用户标识（认证主体）
     *   - memberId: 业务层面会员标识（可能一对一或一对多）
     */
    public static Long getMemberId() {
        return getUser().map(SysUserDetails::getMemberId).orElse(null);
    }

    /**
     * 安全检查：验证当前上下文是否有认证信息
     *
     * @返回值:
     *   - boolean true-已认证, false-匿名访问
     *
     * @使用场景:
     *   - 在非强制认证的接口中检查用户状态
     *   - 区分认证用户和匿名用户的业务逻辑
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }


}
