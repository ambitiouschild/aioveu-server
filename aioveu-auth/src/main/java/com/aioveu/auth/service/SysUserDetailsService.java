package com.aioveu.auth.service;

import cn.hutool.core.lang.Assert;
import com.aioveu.auth.model.LoginUserInfo;
import com.aioveu.auth.model.SysUserDetails;
import com.aioveu.common.enums.StatusEnum;
import com.aioveu.system.api.SystemFeignClient;
import com.aioveu.system.dto.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

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
     */
    @Override
    public UserDetails loadUserByUsername(String username) {


        // 添加空值检查
        if (username == null || username.trim().isEmpty()) {
            throw new UsernameNotFoundException("用户名不能为空");
        }

        // 确保用户名有效
        String trimmedUsername = username.trim();
        log.info("调用systemFeignClient微服务查询用户名和加密密码: {}", trimmedUsername);
        log.info("正在查询用户认证信息: {}", trimmedUsername);


        // 打印 Feign 客户端类信息  systemFeignClient
        log.info("Feign 客户端类: {}", systemFeignClient.getClass().getName());
        log.info("Feign 客户端父类: {}", systemFeignClient.getClass().getSuperclass().getName());
        log.info("Feign 客户端接口: {}", Arrays.toString(systemFeignClient.getClass().getInterfaces()));

        // 通过Feign客户端调用远程用户服务，根据用户名获取用户认证信息
        // 注意：这里可能会抛出Feign异常（如服务不可用、网络超时等）
        UserAuthInfo userAuthInfo = systemFeignClient.getUserAuthInfo(username);

        // 使用断言验证用户是否存在，如果为null则抛出异常并提示"用户不存在"
        Assert.isTrue(userAuthInfo != null, "用户不存在");


        // 检查用户状态：如果用户被禁用，抛出DisabledException异常
        if (!StatusEnum.ENABLE.getValue().equals(userAuthInfo.getStatus())) {
            throw new DisabledException("该账户已被禁用!");
        }

        // 构建Spring Security所需的UserDetails实现对象
        // SysUserDetails包含：用户ID、用户名、密码、部门ID、数据权限、角色权限列表等
        return new SysUserDetails(userAuthInfo);
    }



    /**
     * 获取当前登录用户的基本信息（演示方法）
     * 注意：此方法目前是硬编码实现，实际项目中应该从安全上下文中获取当前用户信息
     *
     * @return LoginUserInfo 登录用户基本信息
     *
     * TODO: 实际实现应该从SecurityContextHolder中获取当前认证用户
     * 示例：
     * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     * if (authentication != null && authentication.getPrincipal() instanceof SysUserDetails) {
     *     SysUserDetails userDetails = (SysUserDetails) authentication.getPrincipal();
     *     // 构建LoginUserInfo
     * }
     */
    public LoginUserInfo getLoginUserInfo() {
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        loginUserInfo.setId(123L);
        return loginUserInfo;
    }
}
