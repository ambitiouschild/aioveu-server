package com.aioveu.tenant.aioveu02User.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.TokenManager.service.TokenManagerService;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.constant.SystemConstants;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.model.Option;
import com.aioveu.common.security.model.RoleDataScope;
import com.aioveu.common.security.model.UserAuthInfoWithTenantId;
import com.aioveu.common.security.service.PermissionService;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.sms.enmus.SmsTypeEnum;
import com.aioveu.common.sms.service.SmsService;
import com.aioveu.common.tenant.TenantContextHolder;
import com.aioveu.tenant.aioveu02User.converter.UserConverter;
import com.aioveu.tenant.aioveu02User.mapper.UserMapper;
import com.aioveu.tenant.aioveu02User.model.bo.UserBO;
import com.aioveu.tenant.aioveu02User.model.dto.CurrentUserDTO;
import com.aioveu.tenant.aioveu02User.model.dto.UserExportDTO;
import com.aioveu.tenant.aioveu02User.model.entity.User;
import com.aioveu.tenant.aioveu02User.model.entity.UserRole;
import com.aioveu.tenant.aioveu02User.model.form.*;
import com.aioveu.tenant.aioveu02User.model.query.UserQuery;
import com.aioveu.tenant.aioveu02User.model.vo.UserPageVO;
import com.aioveu.tenant.aioveu02User.model.vo.UserProfileVO;
import com.aioveu.tenant.aioveu02User.service.UserRoleService;
import com.aioveu.tenant.aioveu02User.service.UserService;
import com.aioveu.tenant.aioveu03Role.service.RoleMenuService;
import com.aioveu.tenant.aioveu03Role.service.RoleService;
import com.aioveu.tenant.aioveu06Dict.enums.DictCodeEnum;
import com.aioveu.tenant.aioveu06Dict.model.entity.DictItem;
import com.aioveu.tenant.aioveu06Dict.service.DictItemService;
import com.aioveu.tenant.aioveu13Mail.service.MailService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.PatternMatchUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName: UserServiceImpl
 * @Description TODO 用户业务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 14:01
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRoleService userRoleService;

    private final RoleService roleService;

    private final PermissionService permissionService;

    private final SmsService smsService;

    private final MailService mailService;

    private final StringRedisTemplate redisTemplate;

    private final TokenManagerService tokenManagerService;

    private final DictItemService dictItemService;

    private final UserConverter userConverter;

    private final RoleMenuService roleMenuService;

    /**
     * 根据用户名查询所有用户ID（跨所有租户）
     *
     * @return {@link List<Long>} 所有用户ID（跨所有租户）列表
     */
    @Override
    public List<Long> getUserIdsByUsername(String username){
        // 查询所有租户下该用户名的用户ID
        List<User> users = this.list(new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getUsername, username)
                .eq(User::getIsDeleted, 0) // 未删除的用户
        );

        // 或者更高效的实现（直接返回ID列表）
        // @Select("SELECT id FROM sys_user WHERE username = #{username} AND isDeleted = 0")

        return users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<UserPageVO>} 用户分页列表
     */
    @Override
    public IPage<UserPageVO> getUserPage(UserQuery queryParams) {

        // 参数构建
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        Page<UserBO> page = new Page<>(pageNum, pageSize);

        boolean isRoot = SecurityUtils.isRoot();
        queryParams.setIsRoot(isRoot);

        // 查询数据
        Page<UserBO> userPage = this.baseMapper.getUserPage(page, queryParams);

        // 实体转换
        return userConverter.toPageVo(userPage);
    }

    /**
     * 获取用户表单数据
     *
     * @param userId 用户ID
     * @return {@link UserForm} 用户表单数据
     */
    @Override
    public UserForm getUserFormData(Long userId) {
        return this.baseMapper.getUserFormData(userId);
    }


    /*
    *   判断是否可以切换租户
    * */
    private boolean resolveCanSwitchTenant(Set<String> roles) {
        if (CollectionUtil.isEmpty(roles)) {
            return false;
        }

        log.info("判断是否可以切换租户");
//        Set<String> perms = permissionService.getRolePermsFormCache(roles);

        Set<String> perms = roleMenuService.getRolePermsByRoleCodes(roles);

        log.info("打印获取到的权限：{}",perms);

        if (CollectionUtil.isEmpty(perms)) {
            return false;
        }
        return perms.stream()
                .filter(Objects::nonNull)
                .anyMatch(perm -> PatternMatchUtils.simpleMatch(perm, SystemConstants.TENANT_SWITCH_PERMISSION));
    }

    /**
     * 根据用户ID查询租户ID
     *
     * @param userId 用户ID
     * @return 租户ID
     */
    @Override
    public Long getTenantIdByUserId(Long userId) {

        // 获取原用户信息
        User user = this.getById(userId);
        Assert.notNull(user, "用户不存在");

        Long tenantId = user.getTenantId();
        log.info("根据用户ID:{}查询租户ID:{}",userId,tenantId);
        return tenantId;

    }


    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return true|false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUser(UserForm userForm) {

        String username = userForm.getUsername();

        // 实体转换 form->entity
        User entity = userConverter.toEntity(userForm);

        // 获取当前操作员的租户ID（新增用户时，租户ID由 MyMetaObjectHandler 自动填充）
        Long tenantId = TenantContextHolder.getTenantId();
        Assert.notNull(tenantId, "租户ID不能为空");

        if (!SystemConstants.DEFAULT_TENANT_ID.equals(tenantId)
                && SystemConstants.PLATFORM_ROOT_USERNAME.equalsIgnoreCase(username)) {
            throw new BusinessException("该租户不允许创建平台保留用户名");
        }

        // 检查同一租户下用户名是否已存在（新设计：用户名在租户内唯一）
        long count = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getTenantId, tenantId));
        Assert.isTrue(count == 0, "该租户下用户名已存在");

        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode(SystemConstants.DEFAULT_PASSWORD);
        entity.setPassword(defaultEncryptPwd);
        entity.setCreateBy(SecurityUtils.getUserId());

        // 注意：租户ID由 MyMetaObjectHandler.insertFill() 自动填充，无需手动设置

        // 新增用户
        boolean result = this.save(entity);

        if (result) {
            // 保存用户角色
            userRoleService.saveUserRoles(entity.getId(), userForm.getRoleIds());
        }
        return result;
    }

    /**
     * 更新用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return true|false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(Long userId, UserForm userForm) {

        String username = userForm.getUsername();

        // 获取原用户信息
        User oldUser = this.getById(userId);
        Assert.notNull(oldUser, "用户不存在");

        Long oldTenantId = oldUser.getTenantId();
        Long currentTenantId = TenantContextHolder.getTenantId();

        // 验证：只能修改当前租户下的用户（防止跨租户修改）
        Assert.isTrue(oldTenantId != null && oldTenantId.equals(currentTenantId),
                "只能修改当前租户下的用户");

        if (!SystemConstants.DEFAULT_TENANT_ID.equals(currentTenantId)
                && SystemConstants.PLATFORM_ROOT_USERNAME.equalsIgnoreCase(username)) {
            throw new BusinessException("该租户不允许使用平台保留用户名");
        }

        // 检查同一租户下用户名是否已存在（排除当前用户）
        long count = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getTenantId, currentTenantId)
                .ne(User::getId, userId)
        );
        Assert.isTrue(count == 0, "该租户下用户名已存在");

        // form -> entity
        User entity = userConverter.toEntity(userForm);
        entity.setUpdateBy(SecurityUtils.getUserId());

        // 保持租户ID不变（不允许跨租户修改用户）
        entity.setTenantId(oldTenantId);

        // 修改用户
        boolean result = this.updateById(entity);

        if (result) {
            // 保存用户角色
            userRoleService.saveUserRoles(entity.getId(), userForm.getRoleIds());
        }
        return result;
    }

    /**
     * 删除用户
     *
     * @param idsStr 用户ID，多个以英文逗号(,)分割
     * @return true|false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUsers(String idsStr) {
        Assert.isTrue(StrUtil.isNotBlank(idsStr), "删除的用户数据为空");
        // 逻辑删除
        List<Long> ids = Arrays.stream(idsStr.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        boolean result = this.removeByIds(ids);

        // 新设计：用户删除时，tenant_id 字段会随用户记录一起逻辑删除，无需额外处理

        return result;
    }

    /**
     * 根据用户名和租户ID获取认证凭证信息  修改为返回单个用户（推荐）
     *
     * @param username 用户名
     * @return 用户认证凭证信息 {@link UserAuthInfoWithTenantId}
     */
    @Override
    public UserAuthInfoWithTenantId getAuthInfoByUsernameAndTenantId(String username, Long tenantId) {

        log.info("根据用户名和租户ID获取认证凭证信息: username={}, tenantId={}", username, tenantId);
        UserAuthInfoWithTenantId userAuthInfoWithTenantId =
                this.baseMapper.getAuthInfoByUsernameAndTenantId(username,tenantId);

        if (userAuthInfoWithTenantId == null) {
            log.warn("用户不存在: username={}, tenantId={}", username, tenantId);
            return null;
        }

        //这里的userAuthInfoWithTenantId是唯一的
        log.info("这里的userAuthInfoWithTenantId是唯一的");

        if (userAuthInfoWithTenantId != null) {
            Set<String> roles = userAuthInfoWithTenantId.getRoles();
            // 获取角色的数据权限列表（支持多角色并集）
            List<RoleDataScope> dataScopes = roleService.getRoleDataScopes(roles);

            // 注意：这里假设所有角色的dataScope是一致的，如果不一致需要根据业务逻辑调整
            if (dataScopes != null && !dataScopes.isEmpty()) {
                // 获取dataScope属性值
                // 这里假设RoleDataScope有getDataScope()方法
                Integer dataScopeValue = dataScopes.get(0).getDataScope();
                userAuthInfoWithTenantId.setDataScope(dataScopeValue);
            }

            log.info("获取到的角色：{}",roles);
            userAuthInfoWithTenantId.setDataScopes(dataScopes);


            userAuthInfoWithTenantId.setCanSwitchTenant(resolveCanSwitchTenant(roles));
            log.info("设置是否可以切换租户:{}",resolveCanSwitchTenant(roles));
        }

        log.info("构建的租户认证信息：{}",userAuthInfoWithTenantId);
        return userAuthInfoWithTenantId;
    }

    /**
     * 根据用户名和租户ID获取认证信息（用于多租户登录）
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return {@link UserAuthInfoWithTenantId}
     */
    @Override
    public UserAuthInfoWithTenantId getAuthInfoByUsernameInTenant(String username, Long tenantId) {
        log.info("查询用户认证信息: username={}, tenantId={}", username, tenantId);

        Long oldTenantId2 = TenantContextHolder.getTenantId();
        log.info("【Tenant-User】TenantContextHolder查询用户oldTenantId2={}", oldTenantId2);

        TenantContextHolder.setTenantId(tenantId);
        log.info("【Tenant-User】TenantContextHolder设置请求TenantId={}", tenantId);
        Long oldTenantId = TenantContextHolder.getTenantId();
        log.info("【Tenant-User】TenantContextHolder再次查询用户oldTenantId={}", oldTenantId);

        boolean oldIgnoreTenant = TenantContextHolder.isIgnoreTenant();
        // 临时忽略租户过滤，查询指定租户下的用户
        TenantContextHolder.setIgnoreTenant(true);

        try {

//            // 先查询用户
//            User user = this.getOne(
//                    new LambdaQueryWrapper<User>()
//                            .eq(User::getUsername, username)
//                            .eq(User::getTenantId, tenantId)
//                            .eq(User::getIsDeleted, 0)
//                            .last("LIMIT 1")
//            );
//            if (user == null) {
//                return null;
//            }
//            log.info("已经根据tenantId：{}进行了过滤，这里的唯一用户User：{}", tenantId, user);
//            // 设置租户上下文，然后查询认证信息（这样会包含该租户下的角色）
//            TenantContextHolder.setIgnoreTenant(false);
//            TenantContextHolder.setTenantId(tenantId);

            return getAuthInfoByUsernameAndTenantId(username,tenantId);
        } finally {
            if (oldTenantId != null) {
                TenantContextHolder.setTenantId(oldTenantId);
                log.info("【Tenant-User】如果上下文租户Id不为空,则确保租户Id为原始租户id");
            } else {
                TenantContextHolder.setTenantId(tenantId);
//                TenantContextHolder.clear();
//                log.info("【Tenant-User】清除当前线程的租户上下文");
                log.info("【Tenant-User】如果上下文租户Id为空,则确保将前端租户Id传递TenantContextHolder");
            }
            TenantContextHolder.setIgnoreTenant(oldIgnoreTenant);
        }
    }

    @Override
    public List<User> listUsersByUsernameAcrossAllTenants(String username) {
        // 临时忽略租户过滤，查询该用户名在所有租户下的账户记录
        TenantContextHolder.setIgnoreTenant(true);
        try {
            return this.list(
                    new LambdaQueryWrapper<User>()
                            .eq(User::getUsername, username)
                            .eq(User::getIsDeleted, 0)
                            .orderByAsc(User::getTenantId)
            );
        } finally {
            TenantContextHolder.setIgnoreTenant(false);
        }
    }

    /**
     * 根据OpenID获取用户认证信息
     *
     * @param openId 微信OpenID
     * @return 用户认证信息
     */
    @Override
    public UserAuthInfoWithTenantId getAuthInfoByOpenId(String openId) {
        if (StrUtil.isBlank(openId)) {
            return null;
        }
        UserAuthInfoWithTenantId userAuthInfoWithTenantId = this.baseMapper.getAuthInfoByOpenId(openId);
        if (userAuthInfoWithTenantId != null) {
            Set<String> roles = userAuthInfoWithTenantId.getRoles();
            // 获取最大范围的数据权限
            Integer dataScope = roleService.getMaximumDataScope(roles);
            userAuthInfoWithTenantId.setDataScope(dataScope);
            userAuthInfoWithTenantId.setCanSwitchTenant(resolveCanSwitchTenant(roles));
        }
        return userAuthInfoWithTenantId;
    }

    /**
     * 根据手机号获取用户认证信息
     *
     * @param mobile 手机号
     * @return 用户认证信息
     */
    @Override
    public UserAuthInfoWithTenantId getAuthInfoByMobile(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return null;
        }
        UserAuthInfoWithTenantId userAuthInfoWithTenantId = this.baseMapper.getAuthInfoByMobile(mobile);
        if (userAuthInfoWithTenantId != null) {
            Set<String> roles = userAuthInfoWithTenantId.getRoles();
            // 获取最大范围的数据权限
            Integer dataScope = roleService.getMaximumDataScope(roles);
            userAuthInfoWithTenantId.setDataScope(dataScope);
            userAuthInfoWithTenantId.setCanSwitchTenant(resolveCanSwitchTenant(roles));
        }
        return userAuthInfoWithTenantId;
    }

    /**
     * 注册或绑定微信用户
     *
     * @param openId 微信OpenID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerOrBindWechatUser(String openId) {
        if (StrUtil.isBlank(openId)) {
            return false;
        }

        // 查询是否已存在该openId的用户
        User existUser = this.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getOpenId, openId)
        );

        if (existUser != null) {
            // 用户已存在，不需要注册
            return true;
        }

        // 创建新用户
        User newUser = new User();
        newUser.setNickname("微信用户");  // 默认昵称
        newUser.setUsername(openId);      // TODO 后续替换为手机号
        newUser.setOpenId(openId);
        newUser.setGender(0); // 保密
        newUser.setUpdateBy(SecurityUtils.getUserId());
        newUser.setPassword(SystemConstants.DEFAULT_PASSWORD);
        newUser.setCreateTime(LocalDateTime.now());
        newUser.setUpdateTime(LocalDateTime.now());
        this.save(newUser);
        // 为了默认系统管理员角色，这里按需调整，实际情况绑定已存在的系统用户，另一种情况是给默认游客角色，然后由系统管理员设置用户的角色
        UserRole userRole = new UserRole();
        userRole.setUserId(newUser.getId());
        userRole.setRoleId(1L);  // TODO 系统管理员
        userRoleService.save(userRole);
        return true;
    }

    /**
     * 根据手机号和OpenID注册用户
     *
     * @param mobile 手机号
     * @param openId 微信OpenID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerUserByMobileAndOpenId(String mobile, String openId) {
        if (StrUtil.isBlank(mobile) || StrUtil.isBlank(openId)) {
            return false;
        }

        // 先查询是否已存在手机号对应的用户
        User existingUser = this.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getMobile, mobile)
        );

        if (existingUser != null) {
            // 如果存在用户但没绑定openId，则绑定openId
            if (StrUtil.isBlank(existingUser.getOpenId())) {
                return bindUserOpenId(existingUser.getId(), openId);
            }
            // 如果已经绑定了其他openId，则判断是否需要更新
            else if (!openId.equals(existingUser.getOpenId())) {
                return bindUserOpenId(existingUser.getId(), openId);
            }
            // 如果已经绑定了相同的openId，则不需要任何操作
            return true;
        }

        // 不存在用户，创建新用户
        User newUser = new User();
        newUser.setMobile(mobile);
        newUser.setOpenId(openId);
        newUser.setUsername(mobile); // 使用手机号作为用户名
        newUser.setNickname("微信用户_" + mobile.substring(mobile.length() - 4)); // 使用手机号后4位作为昵称
        newUser.setPassword(SystemConstants.DEFAULT_PASSWORD); // 使用加密的openId作为初始密码
        newUser.setGender(0); // 保密
        newUser.setCreateTime(LocalDateTime.now());
        newUser.setUpdateTime(LocalDateTime.now());
        this.save(newUser);
        // 为了默认系统管理员角色，这里按需调整，实际情况绑定已存在的系统用户，另一种情况是给默认游客角色，然后由系统管理员设置用户的角色
        UserRole userRole = new UserRole();
        userRole.setUserId(newUser.getId());
        userRole.setRoleId(1L);  // TODO 系统管理员
        userRoleService.save(userRole);
        return true;
    }

    /**
     * 绑定用户微信OpenID
     *
     * @param userId 用户ID
     * @param openId 微信OpenID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean bindUserOpenId(Long userId, String openId) {
        if (userId == null || StrUtil.isBlank(openId)) {
            return false;
        }

        // 检查是否已有其他用户绑定了此openId
        User existingUser = this.getOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getOpenId, openId)
                        .ne(User::getId, userId)
        );

        if (existingUser != null) {
            log.warn("OpenID {} 已被用户 {} 绑定，无法为用户 {} 绑定", openId, existingUser.getId(), userId);
            return false;
        }

        // 更新用户openId
        boolean updated = this.update(
                new LambdaUpdateWrapper<User>()
                        .eq(User::getId, userId)
                        .set(User::getOpenId, openId)
                        .set(User::getUpdateTime, LocalDateTime.now())
        );
        return updated ;
    }

    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return {@link List<UserExportDTO>} 导出用户列表
     */
    @Override
    public List<UserExportDTO> listExportUsers(UserQuery queryParams) {

        boolean isRoot = SecurityUtils.isRoot();
        queryParams.setIsRoot(isRoot);

        List<UserExportDTO> exportUsers = this.baseMapper.listExportUsers(queryParams);
        if (CollectionUtil.isNotEmpty(exportUsers)) {
            //获取性别的字典项
            Map<String, String> genderMap = dictItemService.list(
                            new LambdaQueryWrapper<DictItem>().eq(DictItem::getDictCode,
                                    DictCodeEnum.GENDER.getValue())
                    ).stream()
                    .collect(Collectors.toMap(DictItem::getValue, DictItem::getLabel)
                    );

            exportUsers.forEach(item -> {
                String gender = item.getGender();
                if (StrUtil.isBlank(gender)) {
                    return;
                }

                // 判断map是否为空
                if (genderMap.isEmpty()) {
                    return;
                }

                item.setGender(genderMap.get(gender));
            });
        }
        return exportUsers;
    }

    /**
     * 获取登录用户信息
     *
     * @return {@link CurrentUserDTO}   用户信息
     */
    @Override
    public CurrentUserDTO getCurrentUserInfo() {

        String username = SecurityUtils.getUsername();
        log.info("获取登录用户名：{}",username);

        boolean canSwitchTenant = SecurityUtils.canSwitchTenant();

        log.info("是否可切换租户：{}",canSwitchTenant);

        Long oldTenantId = TenantContextHolder.getTenantId();
        log.info("TenantContextHolder获取当前租户id：{}",oldTenantId);

        Long tenantId = SecurityUtils.getTenantId();
        log.info("SecurityUtils 获取当前租户id：{}",tenantId);
        boolean oldIgnoreTenant = TenantContextHolder.isIgnoreTenant();
        log.info("是否忽略租户：{}",oldIgnoreTenant);

        User user;
        try {
            if (canSwitchTenant) {
                TenantContextHolder.setIgnoreTenant(false);
                TenantContextHolder.setTenantId(SystemConstants.PLATFORM_TENANT_ID);
            }
            //增加租户ID筛选
            log.info("增加租户ID筛选：{}",oldTenantId);
            user = this.getOne(new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, username)
                    .eq(User::getTenantId, tenantId)
                    .select(
                            User::getId,
                            User::getUsername,
                            User::getNickname,
                            User::getAvatar
                    )
            );
        } finally {
            TenantContextHolder.setIgnoreTenant(oldIgnoreTenant);
            if (oldTenantId != null) {
                TenantContextHolder.setTenantId(oldTenantId);
            }
        }

        CurrentUserDTO userInfoVO = userConverter.toCurrentUserDto(user);
        userInfoVO.setCanSwitchTenant(canSwitchTenant);

        // 用户角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfoVO.setRoles(roles);

        // 用户权限集合
        if (CollectionUtil.isNotEmpty(roles)) {
            Set<String> perms;
            if (canSwitchTenant) {
                Long permsOldTenantId = TenantContextHolder.getTenantId();
                boolean permsOldIgnoreTenant = TenantContextHolder.isIgnoreTenant();
                try {
                    TenantContextHolder.setIgnoreTenant(false);
                    TenantContextHolder.setTenantId(SystemConstants.PLATFORM_TENANT_ID);
                    perms = permissionService.getRolePermsFormCache(roles);
                } finally {
                    TenantContextHolder.setIgnoreTenant(permsOldIgnoreTenant);
                    if (permsOldTenantId != null) {
                        TenantContextHolder.setTenantId(permsOldTenantId);
                    }
                }
            } else {
                perms = permissionService.getRolePermsFormCache(roles);
            }
            userInfoVO.setPerms(perms);
        }
        return userInfoVO;
    }

    /**
     * 获取个人中心用户信息
     *
     * @param userId 用户ID
     * @return {@link UserProfileVO} 个人中心用户信息
     */
    @Override
    public UserProfileVO getUserProfile(Long userId) {
        UserBO entity = this.baseMapper.getUserProfile(userId);
        return userConverter.toProfileVo(entity);
    }

    /**
     * 修改个人中心用户信息
     *
     * @param formData 表单数据
     * @return true|false
     */
    @Override
    public boolean updateUserProfile(UserProfileForm formData) {
        Long userId = SecurityUtils.getUserId();

        if (formData.getNickname() == null && formData.getAvatar() == null && formData.getGender() == null) {
            throw new BusinessException("请修改至少一个字段");
        }

        return this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, userId)
                .set(formData.getNickname() != null, User::getNickname, formData.getNickname())
                .set(formData.getAvatar() != null, User::getAvatar, formData.getAvatar())
                .set(formData.getGender() != null, User::getGender, formData.getGender())
        );
    }

    /**
     * 修改指定用户密码
     *
     * @param userId 用户ID
     * @param data   密码修改表单数据
     * @return true|false
     */
    @Override
    public boolean changeUserPassword(Long userId, PasswordUpdateForm data) {

        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        String oldPassword = data.getOldPassword();

        // 校验原密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        // 新旧密码不能相同
        if (passwordEncoder.matches(data.getNewPassword(), user.getPassword())) {
            throw new BusinessException("新密码不能与原密码相同");
        }

        // 判断新密码和确认密码是否一致
        if (!Objects.equals(data.getNewPassword(), data.getConfirmPassword())) {
            throw new BusinessException("新密码和确认密码不一致");
        }

        String newPassword = data.getNewPassword();
        boolean result = this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, userId)
                .set(User::getPassword, passwordEncoder.encode(newPassword))
        );

        if (result) {
            // 密码变更后，使当前用户的所有会话失效，强制重新登录
            tokenManagerService.invalidateUserSessions(userId);
        }
        return result;
    }

    /**
     * 重置指定用户密码
     *
     * @param userId   用户ID
     * @param password 密码重置表单数据
     * @return true|false
     */
    @Override
    public boolean resetUserPassword(Long userId, String password) {
        boolean result = this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, userId)
                .set(User::getPassword, passwordEncoder.encode(password))
        );
        if (result) {
            // 管理员重置用户密码后，使该用户的所有会话失效
            tokenManagerService.invalidateUserSessions(userId);
        }
        return result;
    }

    /**
     * 发送短信验证码(绑定或更换手机号)
     *
     * @param mobile 手机号
     * @return true|false
     */
    @Override
    public boolean sendMobileCode(String mobile) {

        Long currentUserId = SecurityUtils.getUserId();
        long mobileCount = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, mobile)
                .ne(User::getId, currentUserId)
        );
        if (mobileCount > 0) {
            throw new BusinessException("手机号已被其他账号绑定");
        }

        // String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        // TODO 为了方便测试，验证码固定为 1234，实际开发中在配置了厂商短信服务后，可以使用上面的随机验证码
        String code = "1234";

        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("code", code);
        boolean result = smsService.sendSms2(mobile, SmsTypeEnum.CHANGE_MOBILE, templateParams);
        if (result) {
            // 缓存验证码，5分钟有效，用于更换手机号校验
            String redisCacheKey = StrUtil.format(RedisConstants.Captcha.MOBILE_CODE, mobile);
            redisTemplate.opsForValue().set(redisCacheKey, code, 5, TimeUnit.MINUTES);
        }
        return result;
    }

    /**
     * 绑定或更换手机号
     *
     * @param form 表单数据
     * @return true|false
     */
    @Override
    public boolean bindOrChangeMobile(MobileUpdateForm form) {

        Long currentUserId = SecurityUtils.getUserId();
        User currentUser = this.getById(currentUserId);

        if (currentUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(form.getPassword(), currentUser.getPassword())) {
            throw new BusinessException("当前密码错误");
        }

        // 校验验证码
        String inputVerifyCode = form.getCode();
        String mobile = form.getMobile();

        String cacheKey = StrUtil.format(RedisConstants.Captcha.MOBILE_CODE, mobile);

        String cachedVerifyCode = redisTemplate.opsForValue().get(cacheKey);

        if (StrUtil.isBlank(cachedVerifyCode)) {
            throw new BusinessException("验证码已过期");
        }
        if (!inputVerifyCode.equals(cachedVerifyCode)) {
            throw new BusinessException("验证码错误");
        }

        long mobileCount = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, mobile)
                .ne(User::getId, currentUserId)
        );
        if (mobileCount > 0) {
            throw new BusinessException("手机号已被其他账号绑定");
        }

        redisTemplate.delete(cacheKey);

        // 更新手机号码
        return this.update(
                new LambdaUpdateWrapper<User>()
                        .eq(User::getId, currentUserId)
                        .set(User::getMobile, mobile)
        );
    }

    /**
     * 发送邮箱验证码（绑定或更换邮箱）
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmailCode(String email) {

        Long currentUserId = SecurityUtils.getUserId();
        long emailCount = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getId, currentUserId)
        );
        if (emailCount > 0) {
            throw new BusinessException("邮箱已被其他账号绑定");
        }

        // String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        // TODO 为了方便测试，验证码固定为 1234，实际开发中在配置了邮箱服务后，可以使用上面的随机验证码
        String code = "1234";

        mailService.sendMail(email, "邮箱验证码", "您的验证码为：" + code + "，请在5分钟内使用");
        // 缓存验证码，5分钟有效，用于更换邮箱校验
        String redisCacheKey = StrUtil.format(RedisConstants.Captcha.EMAIL_CODE, email);
        redisTemplate.opsForValue().set(redisCacheKey, code, 5, TimeUnit.MINUTES);
    }

    /**
     * 修改当前用户邮箱
     *
     * @param form 表单数据
     * @return true|false
     */
    @Override
    public boolean bindOrChangeEmail(EmailUpdateForm form) {

        Long currentUserId = SecurityUtils.getUserId();

        User currentUser = this.getById(currentUserId);
        if (currentUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (!passwordEncoder.matches(form.getPassword(), currentUser.getPassword())) {
            throw new BusinessException("当前密码错误");
        }

        // 获取前端输入的验证码
        String inputVerifyCode = form.getCode();

        // 获取缓存的验证码
        String email = form.getEmail();
        String redisCacheKey = StrUtil.format(RedisConstants.Captcha.EMAIL_CODE, email);
        String cachedVerifyCode = redisTemplate.opsForValue().get(redisCacheKey);

        if (StrUtil.isBlank(cachedVerifyCode)) {
            throw new BusinessException("验证码已过期");
        }

        if (!inputVerifyCode.equals(cachedVerifyCode)) {
            throw new BusinessException("验证码错误");
        }

        long emailCount = this.count(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, email)
                .ne(User::getId, currentUserId)
        );
        if (emailCount > 0) {
            throw new BusinessException("邮箱已被其他账号绑定");
        }

        redisTemplate.delete(redisCacheKey);

        // 更新邮箱地址
        return this.update(
                new LambdaUpdateWrapper<User>()
                        .eq(User::getId, currentUserId)
                        .set(User::getEmail, email)
        );
    }

    @Override
    public boolean unbindMobile(PasswordVerifyForm form) {

        Long currentUserId = SecurityUtils.getUserId();
        User currentUser = this.getById(currentUserId);

        if (currentUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (StrUtil.isBlank(currentUser.getMobile())) {
            throw new BusinessException("当前账号未绑定手机号");
        }

        if (!passwordEncoder.matches(form.getPassword(), currentUser.getPassword())) {
            throw new BusinessException("当前密码错误");
        }

        return this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, currentUserId)
                .set(User::getMobile, null)
        );
    }

    @Override
    public boolean unbindEmail(PasswordVerifyForm form) {

        Long currentUserId = SecurityUtils.getUserId();
        User currentUser = this.getById(currentUserId);

        if (currentUser == null) {
            throw new BusinessException("用户不存在");
        }

        if (StrUtil.isBlank(currentUser.getEmail())) {
            throw new BusinessException("当前账号未绑定邮箱");
        }

        if (!passwordEncoder.matches(form.getPassword(), currentUser.getPassword())) {
            throw new BusinessException("当前密码错误");
        }

        return this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getId, currentUserId)
                .set(User::getEmail, null)
        );
    }

    /**
     * 获取用户选项列表
     *
     * @return {@link List<Option<String>>} 用户选项列表
     */
    @Override
    public List<Option<String>> listUserOptions() {
        List<User> list = this.list(new LambdaQueryWrapper<User>()
                .eq(User::getStatus, 1)
        );
        return userConverter.toOptions(list);
    }
}
