package com.aioveu.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.mail.service.MailService;
import com.aioveu.common.model.Option;
import com.aioveu.common.security.model.UserAuthCredentials;
import com.aioveu.common.security.token.TokenManager;
import com.aioveu.common.sms.enmus.SmsTypeEnum;
import com.aioveu.system.enums.DictCodeEnum;
import com.aioveu.system.model.dto.CurrentUserDTO;
import com.aioveu.system.model.dto.UserExportDTO;
import com.aioveu.system.model.entity.SysDict;
import com.aioveu.system.model.entity.SysUserRole;
import com.aioveu.system.model.form.*;
import com.aioveu.system.service.SysDictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.common.constant.GlobalConstants;
import com.aioveu.common.constant.RedisConstants;
import com.aioveu.common.constant.SystemConstants;
import com.aioveu.common.security.service.PermissionService;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.common.sms.property.AliyunSmsProperties;
import com.aioveu.common.sms.service.SmsService;
import com.aioveu.system.converter.UserConverter;
import com.aioveu.system.dto.UserAuthInfo;
import com.aioveu.system.mapper.SysUserMapper;
import com.aioveu.system.model.bo.UserBO;
import com.aioveu.system.model.bo.UserFormBO;
import com.aioveu.system.model.bo.UserProfileBO;
import com.aioveu.system.model.entity.SysUser;
import com.aioveu.system.model.query.UserPageQuery;
import com.aioveu.system.model.vo.UserExportVO;
import com.aioveu.system.model.vo.UserInfoVO;
import com.aioveu.system.model.vo.UserPageVO;
import com.aioveu.system.model.vo.UserProfileVO;
import com.aioveu.system.service.SysRoleService;
import com.aioveu.system.service.SysUserRoleService;
import com.aioveu.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Description: TODO 用户业务实现类
 * @Author: 雒世松
 * @Date: 2025/6/5 17:29
 * @param
 * @return:
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PasswordEncoder passwordEncoder;

    private final SysUserRoleService userRoleService;

    private final SysRoleService roleService;

    private final UserConverter userConverter;

    private final PermissionService permissionService;

    private final SmsService smsService;

    private final AliyunSmsProperties aliyunSmsProperties;

    private final StringRedisTemplate redisTemplate;

    private final SysDictService dictItemService;

    private final TokenManager tokenManager;

    private final MailService mailService;

    /**
     * 获取用户分页列表
     *
     * @param queryParams 查询参数
     * @return {@link UserPageVO}
     */
    @Override
    public IPage<UserPageVO> getUserPage(UserPageQuery queryParams) {

        // 参数构建
        int pageNum = queryParams.getPageNum();
        int pageSize = queryParams.getPageSize();
        Page<UserBO> page = new Page<>(pageNum, pageSize);

        // 查询数据
        Page<UserBO> userBoPage = this.baseMapper.getUserPage(page, queryParams);

        // 实体转换
        return userConverter.bo2Vo(userBoPage);
    }

    /**
     * 获取用户详情
     *
     * @param userId 用户ID
     * @return {@link UserForm}
     */
    @Override
    public UserForm getUserFormData(Long userId) {
        UserFormBO userFormBO = this.baseMapper.getUserDetail(userId);
        // 实体转换po->form
        return userConverter.bo2Form(userFormBO);
    }

    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return true|false
     */
    @Override
    public boolean saveUser(UserForm userForm) {

        String username = userForm.getUsername();

        long count = this.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
        Assert.isTrue(count == 0, "用户名已存在");

        // 实体转换 form->entity
        SysUser entity = userConverter.form2Entity(userForm);

        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode(SystemConstants.DEFAULT_PASSWORD);
        entity.setPassword(defaultEncryptPwd);

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
     * @return true|false 是否更新成功
     */
    @Override
    @Transactional
    public boolean updateUser(Long userId, UserForm userForm) {

        String username = userForm.getUsername();

        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .ne(SysUser::getId, userId)
        );
        Assert.isTrue(count == 0, "用户名已存在");

        // form -> entity
        SysUser entity = userConverter.form2Entity(userForm);

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
     * @return true/false 是否删除成功
     */
    @Override
    public boolean deleteUsers(String idsStr) {
        List<Long> ids = Arrays.stream(idsStr.split(","))
                .map(Long::parseLong).
                collect(Collectors.toList());
        return this.removeByIds(ids);

    }

    /**
     * 根据用户名获取认证凭证信息
     *
     * @param username 用户名
     * @return 用户认证凭证信息 {@link UserAuthCredentials}
     */
    @Override
    public UserAuthCredentials getAuthCredentialsByUsername(String username) {
        UserAuthCredentials userAuthCredentials = this.baseMapper.getAuthCredentialsByUsername(username);
        if (userAuthCredentials != null) {
            Set<String> roles = userAuthCredentials.getRoles();
            // 获取最大范围的数据权限
            Integer dataScope = roleService.getMaxDataRangeDataScope(roles);
            userAuthCredentials.setDataScope(dataScope);
        }
        return userAuthCredentials;
    }


    /**
     * 根据OpenID获取用户认证信息
     *
     * @param openId 微信OpenID
     * @return 用户认证信息
     */
    @Override
    public UserAuthCredentials getAuthCredentialsByOpenId(String openId) {
        if (StrUtil.isBlank(openId)) {
            return null;
        }
        UserAuthCredentials userAuthCredentials = this.baseMapper.getAuthCredentialsByOpenId(openId);
        if (userAuthCredentials != null) {
            Set<String> roles = userAuthCredentials.getRoles();
            // 获取最大范围的数据权限
            Integer dataScope = roleService.getMaxDataRangeDataScope(roles);
            userAuthCredentials.setDataScope(dataScope);
        }
        return userAuthCredentials;
    }

    /**
     * 根据手机号获取用户认证信息
     *
     * @param mobile 手机号
     * @return 用户认证信息
     */
    @Override
    public UserAuthCredentials getAuthCredentialsByMobile(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return null;
        }
        UserAuthCredentials userAuthCredentials = this.baseMapper.getAuthCredentialsByMobile(mobile);
        if (userAuthCredentials != null) {
            Set<String> roles = userAuthCredentials.getRoles();
            // 获取最大范围的数据权限
            Integer dataScope = roleService.getMaxDataRangeDataScope(roles);
            userAuthCredentials.setDataScope(dataScope);
        }
        return userAuthCredentials;
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
        SysUser existUser = this.getOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getOpenid, openId)
        );

        if (existUser != null) {
            // 用户已存在，不需要注册
            return true;
        }

        // 创建新用户
        SysUser newUser = new SysUser();
        newUser.setNickname("微信用户");  // 默认昵称
        newUser.setUsername(openId);      // TODO 后续替换为手机号
        newUser.setOpenid(openId);
        newUser.setGender(0); // 保密
        newUser.setUpdateBy(SecurityUtils.getUserId());
        newUser.setPassword(SystemConstants.DEFAULT_PASSWORD);
        newUser.setCreateTime(LocalDateTime.now());
        newUser.setUpdateTime(LocalDateTime.now());
        this.save(newUser);
        // 为了默认系统管理员角色，这里按需调整，实际情况绑定已存在的系统用户，另一种情况是给默认游客角色，然后由系统管理员设置用户的角色
        SysUserRole userRole = new SysUserRole();
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
        SysUser existingUser = this.getOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getMobile, mobile)
        );

        if (existingUser != null) {
            // 如果存在用户但没绑定openId，则绑定openId
            if (StrUtil.isBlank(existingUser.getOpenid())) {
                return bindUserOpenId(existingUser.getId(), openId);
            }
            // 如果已经绑定了其他openId，则判断是否需要更新
            else if (!openId.equals(existingUser.getOpenid())) {
                return bindUserOpenId(existingUser.getId(), openId);
            }
            // 如果已经绑定了相同的openId，则不需要任何操作
            return true;
        }

        // 不存在用户，创建新用户
        SysUser newUser = new SysUser();
        newUser.setMobile(mobile);
        newUser.setOpenid(openId);
        newUser.setUsername(mobile); // 使用手机号作为用户名
        newUser.setNickname("微信用户_" + mobile.substring(mobile.length() - 4)); // 使用手机号后4位作为昵称
        newUser.setPassword(SystemConstants.DEFAULT_PASSWORD); // 使用加密的openId作为初始密码
        newUser.setGender(0); // 保密
        newUser.setCreateTime(LocalDateTime.now());
        newUser.setUpdateTime(LocalDateTime.now());
        this.save(newUser);
        // 为了默认系统管理员角色，这里按需调整，实际情况绑定已存在的系统用户，另一种情况是给默认游客角色，然后由系统管理员设置用户的角色
        SysUserRole userRole = new SysUserRole();
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
        SysUser existingUser = this.getOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getOpenid, openId)
                        .ne(SysUser::getId, userId)
        );

        if (existingUser != null) {
            log.warn("OpenID {} 已被用户 {} 绑定，无法为用户 {} 绑定", openId, existingUser.getId(), userId);
            return false;
        }

        // 更新用户openId
        boolean updated = this.update(
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getId, userId)
                        .set(SysUser::getOpenid, openId)
                        .set(SysUser::getUpdateTime, LocalDateTime.now())
        );
        return updated ;
    }

    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return {@link UserExportVO}
     */
    @Override
    public List<UserExportVO> listExportUsersOld(UserPageQuery queryParams) {
        return this.baseMapper.listExportUsers(queryParams);
    }

    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return {@link List<UserExportDTO>} 导出用户列表
     */
    @Override
    public List<UserExportVO> listExportUsers(UserPageQuery queryParams) {

        boolean isRoot = SecurityUtils.isRoot();
        queryParams.setIsRoot(isRoot);

        List<UserExportVO> exportUsers = this.baseMapper.listExportUsers(queryParams);
        if (CollectionUtil.isNotEmpty(exportUsers)) {
            //获取性别的字典项
            Map<String, String> genderMap = dictItemService.list(
                            new LambdaQueryWrapper<SysDict>().eq(SysDict::getTypeCode,
                                    DictCodeEnum.GENDER.getValue())
                    ).stream()
                    .collect(Collectors.toMap(SysDict::getValue, SysDict::getName)
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
     * @return {@link UserInfoVO}   用户信息
     */
    @Override
    public UserInfoVO getCurrentUserInfoOld() {
        // 登录用户entity
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, SecurityUtils.getUsername())
                .select(
                        SysUser::getId,
                        SysUser::getNickname,
                        SysUser::getAvatar
                )
        );
        // entity->VO
        UserInfoVO userInfoVO = userConverter.entity2UserInfoVo(user);

        // 获取用户角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfoVO.setRoles(roles);

        // 获取用户权限集合
        if (CollectionUtil.isNotEmpty(roles)) {
            Set<String> perms = permissionService.getRolePermsFormCache(roles);
            userInfoVO.setPerms(perms);
        }

        return userInfoVO;
    }

    /**
     * 获取登录用户信息
     *
     * @return {@link CurrentUserDTO}   用户信息
     */
    @Override
    public CurrentUserDTO getCurrentUserInfo() {

        String username = SecurityUtils.getUsername();

        // 获取登录用户基础信息
        SysUser user = this.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, username)
                .select(
                        SysUser::getId,
                        SysUser::getUsername,
                        SysUser::getNickname,
                        SysUser::getAvatar
                )
        );
        // entity->VO
        CurrentUserDTO userInfoVO = userConverter.toCurrentUserDto(user);

        // 用户角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfoVO.setRoles(roles);

        // 用户权限集合
        if (CollectionUtil.isNotEmpty(roles)) {
            Set<String> perms = permissionService.getRolePermsFormCache(roles);
            userInfoVO.setPerms(perms);
        }
        return userInfoVO;
    }


    /**
     * 获取用户个人中心信息
     *
     * @return {@link UserProfileVO}
     */
    @Override
    public UserProfileVO getUserProfile() {
        Long userId = SecurityUtils.getUserId();
        // 获取用户个人中心信息
        UserProfileBO userProfileBO = this.baseMapper.getUserProfile(userId);
        return userConverter.userProfileBo2Vo(userProfileBO);
    }

    /**
     * 获取个人中心用户信息
     *
     * @param userId 用户ID
     * @return {@link UserProfileVO} 个人中心用户信息
     */
    @Override
    public UserProfileVO getUserProfile(Long userId) {
        UserProfileBO entity = this.baseMapper.getUserProfile(userId);
        return userConverter.userProfileBo2Vo(entity);
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
        SysUser entity = userConverter.toEntity(formData);
        entity.setId(userId);
        return this.updateById(entity);
    }


    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 用户密码
     * @return true|false
     */
    @Override
    public boolean updatePassword(Long userId, String password) {
        return this.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassword, passwordEncoder.encode(password))
        );
    }

    /**
     * 修改用户密码
     *
     * @param userId 用户ID
     * @param data   密码修改表单数据
     * @return true|false
     */
    @Override
    public boolean changePassword(Long userId, PasswordUpdateForm data) {

        SysUser user = this.getById(userId);
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
        if (passwordEncoder.matches(data.getNewPassword(), data.getConfirmPassword())) {
            throw new BusinessException("新密码和确认密码不一致");
        }

        String newPassword = data.getNewPassword();
        boolean result = this.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassword, passwordEncoder.encode(newPassword))
        );

        if (result) {
            // 加入黑名单，重新登录
            String accessToken = SecurityUtils.getTokenFromRequest();
            tokenManager.invalidateToken(accessToken);
        }
        return result;
    }

    /**
     * 重置密码
     *
     * @param userId   用户ID
     * @param password 密码重置表单数据
     * @return true|false
     */
    @Override
    public boolean resetPassword(Long userId, String password) {
        return this.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassword, passwordEncoder.encode(password))
        );
    }

    /**
     * 发送短信验证码(绑定或更换手机号)
     *
     * @param mobile 手机号
     * @return true|false
     */
    @Override
    public boolean sendMobileCode(String mobile) {

        // String code = String.valueOf((int) ((Math.random() * 9 + 1) * 1000));
        // TODO 为了方便测试，验证码固定为 1234，实际开发中在配置了厂商短信服务后，可以使用上面的随机验证码
        String code = "1234";

        Map<String, String> templateParams = new HashMap<>();
        templateParams.put("code", code);
        boolean result = smsService.sendSms(mobile, SmsTypeEnum.CHANGE_MOBILE, templateParams);
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
        SysUser currentUser = this.getById(currentUserId);

        if (currentUser == null) {
            throw new BusinessException("用户不存在");
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
        // 验证完成删除验证码
        redisTemplate.delete(cacheKey);

        // 更新手机号码
        return this.update(
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getId, currentUserId)
                        .set(SysUser::getMobile, mobile)
        );
    }

    /**
     * 发送邮箱验证码（绑定或更换邮箱）
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmailCode(String email) {

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

        SysUser currentUser = this.getById(currentUserId);
        if (currentUser == null) {
            throw new BusinessException("用户不存在");
        }

        // 获取前端输入的验证码
        String inputVerifyCode = form.getCode();

        // 获取缓存的验证码
        String email = form.getEmail();
        String redisCacheKey = RedisConstants.Captcha.EMAIL_CODE + email;
        String cachedVerifyCode = redisTemplate.opsForValue().get(redisCacheKey);

        if (StrUtil.isBlank(cachedVerifyCode)) {
            throw new BusinessException("验证码已过期");
        }

        if (!inputVerifyCode.equals(cachedVerifyCode)) {
            throw new BusinessException("验证码错误");
        }
        // 验证完成删除验证码
        redisTemplate.delete(redisCacheKey);

        // 更新邮箱地址
        return this.update(
                new LambdaUpdateWrapper<SysUser>()
                        .eq(SysUser::getId, currentUserId)
                        .set(SysUser::getEmail, email)
        );
    }

    /**
     * 获取用户选项列表
     *
     * @return {@link List<Option<String>>} 用户选项列表
     */
    @Override
    public List<Option<String>> listUserOptions() {
        List<SysUser> list = this.list(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getStatus, 1)
        );
        return userConverter.toOptions(list);
    }

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return 用户认证信息 {@link UserAuthInfo}
     */
    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        UserAuthInfo userAuthInfo = this.baseMapper.getUserAuthInfo(username);
        if (userAuthInfo != null) {
            Set<String> roles = userAuthInfo.getRoles();
            if (CollectionUtil.isNotEmpty(roles)) {
                // 获取最大范围的数据权限(目前设定DataScope越小，拥有的数据权限范围越大，所以获取得到角色列表中最小的DataScope)
                Integer dataScope = roleService.getMaxDataRangeDataScope(roles);
                userAuthInfo.setDataScope(dataScope);
            }
        }
        return userAuthInfo;
    }






    /**
     * 注销登出
     *
     * @return true|false
     */
    @Override
    public boolean logout() {
        String jti = SecurityUtils.getJti();
        Optional<Long> expireTimeOpt = Optional.ofNullable(SecurityUtils.getExp()); // 使用Optional处理可能的null值

        long currentTimeInSeconds = System.currentTimeMillis() / 1000; // 当前时间（单位：秒）

        expireTimeOpt.ifPresent(expireTime -> {
            if (expireTime > currentTimeInSeconds) {
                // token未过期，添加至缓存作为黑名单，缓存时间为token剩余的有效时间
                long remainingTimeInSeconds = expireTime - currentTimeInSeconds;
                redisTemplate.opsForValue().set(RedisConstants.TOKEN_BLACKLIST_PREFIX + jti, "", remainingTimeInSeconds, TimeUnit.SECONDS);
            }
        });

        if (expireTimeOpt.isEmpty()) {
            // token 永不过期则永久加入黑名单
            redisTemplate.opsForValue().set(RedisConstants.TOKEN_BLACKLIST_PREFIX + jti, "");
        }

        return true;
    }

    /**
     * 注册用户
     *
     * @param userRegisterForm 用户注册表单对象
     * @return true|false 是否注册成功
     */
    @Override
    public boolean registerUser(UserRegisterForm userRegisterForm) {

        String mobile = userRegisterForm.getMobile();
        String code = userRegisterForm.getCode();
        // 校验验证码
        String cacheCode = redisTemplate.opsForValue().get(RedisConstants.REGISTER_SMS_CODE_PREFIX + mobile);
        if (!StrUtil.equals(code, cacheCode)) {
            log.warn("验证码不匹配或不存在: {}", mobile);
            return false; // 验证码不匹配或不存在时返回false
        }
        // 校验通过，删除验证码
        redisTemplate.delete(RedisConstants.REGISTER_SMS_CODE_PREFIX + mobile);

        // 校验手机号是否已注册
        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getMobile, mobile)
                .or()
                .eq(SysUser::getUsername, mobile)
        );
        Assert.isTrue(count == 0, "手机号已注册");

        SysUser entity = new SysUser();
        entity.setUsername(mobile);
        entity.setMobile(mobile);
        entity.setStatus(GlobalConstants.STATUS_YES);

        // 设置默认加密密码
        String defaultEncryptPwd = passwordEncoder.encode(SystemConstants.DEFAULT_PASSWORD);
        entity.setPassword(defaultEncryptPwd);

        // 新增用户，并直接返回结果
        return this.save(entity);
    }

    /**
     * 发送注册短信验证码
     *
     * @param mobile 手机号
     * @return true|false 是否发送成功
     */
    @Override
    public boolean sendRegistrationSmsCode(String mobile) {
        // 获取短信模板代码
        String templateCode = aliyunSmsProperties.getTemplateCodes().get("register");

        // 生成随机4位数验证码
        String code = RandomUtil.randomNumbers(4);

        // 短信模板: 您的验证码：${code}，该验证码5分钟内有效，请勿泄漏于他人。
        // 其中 ${code} 是模板参数，使用时需要替换为实际值。
        String templateParams = JSONUtil.toJsonStr(Collections.singletonMap("code", code));

        boolean result = smsService.sendSmsOld(mobile, templateCode, templateParams);
        if (result) {
            // 将验证码存入redis，有效期5分钟
            redisTemplate.opsForValue().set(RedisConstants.REGISTER_SMS_CODE_PREFIX + mobile, code, 5, TimeUnit.MINUTES);

            // TODO 考虑记录每次发送短信的详情，如发送时间、手机号和短信内容等，以便后续审核或分析短信发送效果。
        }
        return result;
    }



}
