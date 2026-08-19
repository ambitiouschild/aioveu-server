package com.aioveu.auth.service;

import com.aioveu.common.core.enums.StatusEnum;
import com.aioveu.common.core.result.Result;
import com.aioveu.common.core.tenant.TenantContextHolder;
import com.aioveu.common.security.core.model.dto.UserAuthCredentials;
import com.aioveu.lss.api.LssFeignClient;
import com.aioveu.auth.model.LoginUserInfo;
import com.aioveu.common.security.core.model.SysUserDetails;
import com.aioveu.system.api.SystemFeignClient;
import com.aioveu.tenant.api.TenantFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO 系统用户信息加载实现类  系统用户信息加载实现类 - Spring Security用户详情服务
 *                    负责从远程用户服务加载系统用户（管理员、后台用户）的认证和授权信息
 *                      * 核心功能：
 *                      * 1. 实现Spring Security的UserDetailsService接口
 *                      * 2. 通过Feign客户端调用用户服务获取用户认证信息
 *                      * 3. 验证用户状态和权限
 *                      * 4. 构建Spring Security所需的UserDetails对象
 *                      Spring Security集成架构
 *                      用户登录请求 → Spring Security → loadUserByUsername() → 远程用户服务 → 返回UserDetails
 * @Author: 雒世松
 * @Date: 2025/6/5 17:52
 * @param
 * @return:
 **/
@Slf4j
@Service  // 标记为Spring服务组件，由Spring容器管理
@RequiredArgsConstructor  // Lombok注解，自动生成包含final字段的构造函数，实现依赖注入
public class SysUserDetailsService implements UserDetailsService {


    // 用户服务Feign客户端，用于远程调用用户微服务获取用户认证信息
    private final SystemFeignClient systemFeignClient;

    // 用户服务Feign客户端，用于远程调用用户微服务获取用户认证信息
    private final LssFeignClient lssFeignClient;

    // 用户服务Feign客户端，用于远程调用用户微服务获取用户认证信息
    private final TenantFeignClient tenantFeignClient;

    /**
     * 根据用户名获取用户信息(用户名、密码和角色权限)
     * <p>
     * 用户名、密码用于后续认证，认证成功之后将权限授予用户
     *
     * @param username 用户名
     * @return {@link  SysUserDetails}
     */

    /**
     * Spring Security核心方法：根据用户名加载用户详细信息
     * 这个方法在用户登录认证时被Spring Security自动调用
     *
     *  TODO 执行流程：
     *   1. 接收用户名 → 2. 远程查询用户信息 → 3. 验证用户状态 → 4. 构建UserDetails对象
     *   认证服务需要用户服务提供的数据
     *           // ↓ 认证所需的核心数据 ↓
     *         return new SysUserDetails(
     *             userAuthInfo.getUserId(),      // 用户ID（用于JWT声明）
     *             userAuthInfo.getUsername(),    // 用户名（用于认证）
     *             userAuthInfo.getPassword(),    // 加密密码（用于密码验证）
     *             userAuthInfo.getStatus(),      // 用户状态（是否禁用）
     *             userAuthInfo.getDeptId(),      // 部门ID（用于数据权限）
     *             userAuthInfo.getDataScope(),   // 数据范围（权限控制）
     *             userAuthInfo.getRoles(),       // 角色列表（权限控制）
     *             userAuthInfo.getPermissions()  // 权限列表（接口权限）
     *      认证流程中的数据依赖
     *      密码模式认证流程
     *                  用户输入用户名密码
     *                          ↓
     *                  认证服务接收请求
     *                      ↓
     *                  认证服务 → 调用用户服务 → 查询用户信息（包括加密密码）
     *                      ↓
     *                  认证服务验证密码（bcrypt对比）
     *                      ↓
     *                  认证服务生成JWT令牌（包含用户信息）
     *     技术栈独立性
     *               # 认证服务可以使用更适合安全的技术栈
     *              auth-service:
     *               技术栈: Spring Security + OAuth2 + JWT
     *               数据库: Redis（令牌存储）+ 少量配置表
     *
     *              # 用户服务可以使用更适合业务的技术栈
     *                  user-service:
     *               技术栈: Spring Boot + MyBatis Plus
     *               数据库: MySQL（用户主数据）
     *               功能: 用户管理、权限管理、组织架构
     *              TODO 将三个微服务的用户认证逻辑按优先级依次尝试，直到找到有效的用户信息
     *                  直接使用 SysUserDetails变量：不需要中间的 UserAuthInfo转换
     *                  在每个分支中直接创建 SysUserDetails：利用已有的三个构造函数
     *                  正确的条件判断：使用 sysUserDetails == null来判断是否找到用户
     *                  简化逻辑：找到用户后直接返回，不再进行不必要的转换
     *
     *
     * @param username 用户名（用户登录时输入的用户名）
     * @return UserDetails Spring Security用户详情对象，包含用户名、密码、权限等信息
    //     * @throws UsernameNotFoundException 当用户不存在时抛出
     * @throws DisabledException 当用户被禁用时抛出
     *
     * 使用场景：
     * - 密码模式认证时，Spring Security会调用此方法验证用户
     * - 其他需要获取用户详情的认证流程
     * Spring Security 的 UserDetailsService.loadUserByUsername方法只能传一个参数（用户名）。这是 Spring Security 的设计约束。
     */
    @Override
    public SysUserDetails loadUserByUsername(String username) {


        // 添加空值检查
        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        // 确保用户名有效
        String trimmedUsername = username.trim();
        Long tenantId = TenantContextHolder.getTenantId();

        log.info("加载用户，用户名: {}, 租户ID: {}", trimmedUsername, tenantId);

        UserAuthCredentials credentials = fetchUserCredentials(trimmedUsername, tenantId);


        if (credentials == null) {

            log.error("用户不存在，用户名: {}, 租户ID: {}", trimmedUsername, tenantId);
            throw new UsernameNotFoundException("用户不存在: " + trimmedUsername);
        }


        // ✅ 状态校验（必须）
        if (!StatusEnum.ENABLE.getValue().equals(credentials.getStatus())) {
            log.error("账户已禁用，用户名: {}", trimmedUsername);
            throw new DisabledException("该账户已被禁用");
        }

        SysUserDetails userDetails = new SysUserDetails(credentials);

        log.info("成功构建用户详情，用户名: {}, 租户ID: {}, 来源: {},credentials:{}",
                trimmedUsername, tenantId, credentials.getSource(),credentials);

        return userDetails;

    }

    /**
     * 统一获取用户认证信息
     * tenantId 决定路由，而不是在 auth 层兜底
     */
    private UserAuthCredentials fetchUserCredentials(String username, Long tenantId) {
        try {
            if (tenantId != null) {
                // ✅ 多租户场景：只查 tenant-service
                log.info("「认证接口参数驱动」不仅是主流，而且是事实上的工业级标准");
                return extractData(
                        tenantFeignClient.getUserAuthCredentialsByUsernameAndTenantId(username, tenantId)
                );
            }

            // ✅ 非租户场景：按业务优先级
            UserAuthCredentials credentials = extractData(
                    lssFeignClient.getAuthCredentialsByUsername(username)
            );
            if (credentials != null) {
                return credentials;
            }

            return extractData(
                    systemFeignClient.getUserAuthInfo(username)
            );

        } catch (FeignException.NotFound e) {
            log.warn("用户不存在，用户名: {}", username);
            return null;
        } catch (FeignException e) {
            log.error("调用用户服务异常，用户名: {}", username, e);
            throw new UsernameNotFoundException("用户认证服务异常");
        }
    }

    /**
     * 安全地从 Result<T> 中提取数据
     */
    private <T> T extractData(Result<T> result) {
        if (result == null || !result.isSuccess(result) || result.getData() == null) {
            return null;
        }
        return result.getData();
    }



}
