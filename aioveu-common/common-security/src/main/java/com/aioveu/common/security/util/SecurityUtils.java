package com.aioveu.common.security.util;

import cn.hutool.core.convert.Convert;
import com.aioveu.common.constant.SystemConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description: TODO Spring Security 工具类  这个工具类是Spring Security集成中的核心组件，为业务系统提供了统一的安全信息访问入口。
 *                      提供从SecurityContext中获取当前认证用户信息的便捷方法
 *  *                   基于OAuth2 JWT令牌认证，支持获取用户ID、角色、权限范围等信息
 *                          * @功能特性:
 *                          *   - 用户身份信息获取（用户ID、用户名）
 *                          *   - 角色权限信息获取（角色集合、数据权限范围）
 *                          *   - JWT令牌属性提取（令牌ID、过期时间等）
 *                          *   - 权限验证辅助方法（超级管理员判断）
 *                          *
 *                          * @使用场景:
 *                          *   - 业务服务中获取当前登录用户信息
 *                          *   - 权限验证和访问控制
 *                          *   - 数据权限过滤（基于部门ID、数据范围）
 *                          *   - 审计日志记录用户操作
 *                          *
 *                          * @依赖组件:
 *                          *   - Spring Security Context Holder
 *                          *   - OAuth2 JWT Authentication Token
 *                          *   - Hutool类型转换工具
 *                          *
 *                          * @线程安全: 是 - 所有方法都是静态方法，无状态操作
 * @Author: 雒世松
 * @Date: 2025/6/5 16:12
 * @param
 * @return:
 **/

public class SecurityUtils {

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
    public static Long getUserId() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toLong(tokenAttributes.get("userId"));
        }
        return null;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * 获取JWT令牌的所有声明属性（Claims）
     *
     * @原理说明:
     *   - JWT令牌包含三部分：头部、载荷（声明）、签名
     *   - 此方法返回载荷部分的所有声明键值对
     *
     * @常见声明字段:
     *   - sub: 主题（用户名）
     *   - userId: 用户ID
     *   - roles: 角色列表
     *   - deptId: 部门ID
     *   - exp: 过期时间
     *   - iat: 签发时间
     *   - jti: 令牌唯一标识
     *
     * @返回值:
     *   - Map<String, Object> JWT声明映射，未认证返回null
     *
     * @安全注意: 声明信息来自可信的认证服务器，客户端不可篡改
     */
    public static Map<String, Object> getTokenAttributes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 检查是否为JWT认证令牌
        if (authentication instanceof JwtAuthenticationToken jwtAuthenticationToken) {
            return jwtAuthenticationToken.getTokenAttributes();
        }
        return null;
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
    public static Set<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return AuthorityUtils.authorityListToSet(authentication.getAuthorities())
                    .stream()
                    // 转换为不可修改集合，确保线程安全
                    .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
        }
        return null;
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
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toLong(tokenAttributes.get("deptId"));
        }
        return null;
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
     * 获取JWT令牌的唯一标识符（JTI）
     *
     * @原理说明:
     *   - JTI是JWT标准声明字段，确保每个令牌唯一性
     *   - 用于令牌黑名单、会话管理、防止重放攻击
     *
     * @返回值:
     *   - String 令牌唯一标识，未认证返回null
     *
     * @安全应用:
     *   - 令牌注销: 将JTI加入黑名单
     *   - 防止重放: 验证JTI是否已使用过
     *   - 会话追踪: 关联用户登录会话
     */
    public static String getJti() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return String.valueOf(tokenAttributes.get("jti"));
        }
        return null;
    }


    /**
     * 获取JWT令牌的过期时间（Expiration Time）
     *
     * @原理说明:
     *   - exp是JWT标准声明，表示令牌失效的时间戳
     *   - 时间格式为Unix时间戳（秒级）
     *
     * @返回值:
     *   - Long 过期时间戳（秒），未认证返回null
     *
     * @使用场景:
     *   - 令牌有效期检查
     *   - 自动刷新令牌逻辑
     *   - 会话超时提示
     */
    public static Long getExp() {
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toLong(tokenAttributes.get("exp"));
        }
        return null;
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
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toInt(tokenAttributes.get("dataScope"));
        }
        return null;
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
        Map<String, Object> tokenAttributes = getTokenAttributes();
        if (tokenAttributes != null) {
            return Convert.toLong(tokenAttributes.get("memberId"));
        }
        return null;
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
}
