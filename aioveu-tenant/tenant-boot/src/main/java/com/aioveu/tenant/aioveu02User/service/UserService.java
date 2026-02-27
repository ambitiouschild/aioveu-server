package com.aioveu.tenant.aioveu02User.service;

import com.aioveu.common.model.Option;
import com.aioveu.common.security.model.UserAuthCredentials;
import com.aioveu.common.security.model.UserAuthInfoWithTenantId;
import com.aioveu.tenant.aioveu02User.model.dto.CurrentUserDTO;
import com.aioveu.tenant.aioveu02User.model.dto.UserExportDTO;
import com.aioveu.tenant.aioveu02User.model.entity.User;
import com.aioveu.tenant.aioveu02User.model.form.*;
import com.aioveu.tenant.aioveu02User.model.query.UserQuery;
import com.aioveu.tenant.aioveu02User.model.vo.UserPageVO;
import com.aioveu.tenant.aioveu02User.model.vo.UserProfileVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @ClassName: UserService
 * @Description TODO 用户业务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 23:27
 * @Version 1.0
 **/
public interface UserService extends IService<User> {


    /**
     * 根据用户名查询所有用户ID（跨所有租户）
     *
     * @return {@link List<Long>} 所有用户ID（跨所有租户）列表
     */
    List<Long> getUserIdsByUsername(String username);

    /**
     * 用户分页列表
     *
     * @return {@link IPage<UserPageVO>} 用户分页列表
     */
    IPage<UserPageVO> getUserPage(UserQuery queryParams);

    /**
     * 获取用户表单数据
     *
     * @param userId 用户ID
     * @return {@link UserForm} 用户表单数据
     */
    UserForm getUserFormData(Long userId);


    /**
     * 新增用户
     *
     * @param userForm 用户表单对象
     * @return {@link Boolean} 是否新增成功
     */
    boolean saveUser(UserForm userForm);

    /**
     * 修改用户
     *
     * @param userId   用户ID
     * @param userForm 用户表单对象
     * @return {@link Boolean} 是否修改成功
     */
    boolean updateUser(Long userId, UserForm userForm);


    /**
     * 删除用户
     *
     * @param idsStr 用户ID，多个以英文逗号(,)分割
     * @return {@link Boolean} 是否删除成功
     */
    boolean deleteUsers(String idsStr);


    /**
     * 获取用户认证信息
     *
     * @param username 用户名
     * @return {@link UserAuthInfoWithTenantId}
     */
    UserAuthInfoWithTenantId getAuthInfoByUsername(String username);

    /**
     * 根据用户名和租户ID获取认证信息（用于多租户登录）
     *
     * @param username 用户名
     * @param tenantId 租户ID
     * @return {@link UserAuthInfoWithTenantId}
     */
    UserAuthInfoWithTenantId getAuthInfoByUsernameInTenant(String username, Long tenantId);

    /**
     * 跨租户查询用户账户列表
     * <p>
     * 查询该用户名在所有租户下的账户记录，用于多租户登录时判断是否需要选择租户
     * </p>
     *
     * @param username 用户名
     * @return 用户账户列表（每个租户一条记录）
     */
    List<User> listUsersByUsernameAcrossAllTenants(String username);


    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return {@link List<UserExportDTO>} 导出用户列表
     */
    List<UserExportDTO> listExportUsers(UserQuery queryParams);


    /**
     * 获取登录用户信息
     *
     * @return {@link CurrentUserDTO} 登录用户信息
     */
    CurrentUserDTO getCurrentUserInfo();

    /**
     * 获取个人中心用户信息
     *
     * @return {@link UserProfileVO} 个人中心用户信息
     */
    UserProfileVO getUserProfile(Long userId);

    /**
     * 修改个人中心用户信息
     *
     * @param formData 表单数据
     * @return {@link Boolean} 是否修改成功
     */
    boolean updateUserProfile(UserProfileForm formData);

    /**
     * 修改指定用户密码
     *
     * @param userId 用户ID
     * @param data   修改密码表单数据
     * @return {@link Boolean} 是否修改成功
     */
    boolean changeUserPassword(Long userId, PasswordUpdateForm data);

    /**
     * 重置指定用户密码
     *
     * @param userId   用户ID
     * @param password 重置后的密码
     * @return {@link Boolean} 是否重置成功
     */
    boolean resetUserPassword(Long userId, String password);

    /**
     * 发送短信验证码(绑定或更换手机号)
     *
     * @param mobile 手机号
     * @return {@link Boolean} 是否发送成功
     */
    boolean sendMobileCode(String mobile);

    /**
     * 修改当前用户手机号
     *
     * @param data 表单数据
     * @return {@link Boolean} 是否修改成功
     */
    boolean bindOrChangeMobile(MobileUpdateForm data);

    boolean unbindMobile(PasswordVerifyForm data);

    /**
     * 发送邮箱验证码(绑定或更换邮箱)
     *
     * @param email 邮箱
     */
    void sendEmailCode(String email);

    /**
     * 绑定或更换邮箱
     *
     * @param data 表单数据
     * @return {@link Boolean} 是否绑定成功
     */
    boolean bindOrChangeEmail(EmailUpdateForm data);

    boolean unbindEmail(PasswordVerifyForm data);

    /**
     * 获取用户选项列表
     *
     * @return {@link List<Option<String>>} 用户选项列表
     */
    List<Option<String>> listUserOptions();

    /**
     * 根据 openid 获取用户认证信息
     *
     * @param openId 用户名
     * @return {@link UserAuthInfoWithTenantId}
     */

    UserAuthInfoWithTenantId getAuthInfoByOpenId(String openId);

    /**
     * 根据微信 OpenID 注册或绑定用户
     *
     * @param openId 微信 OpenID
     */
    boolean registerOrBindWechatUser(String openId);

    /**
     * 根据手机号获取用户认证信息
     *
     * @param mobile 手机号
     * @return {@link UserAuthInfoWithTenantId}
     */
    UserAuthInfoWithTenantId getAuthInfoByMobile(String mobile);

    /**
     * 根据手机号和OpenID注册用户
     *
     * @param mobile 手机号
     * @param openId 微信OpenID
     * @return 是否成功
     */
    boolean registerUserByMobileAndOpenId(String mobile, String openId);

    /**
     * 绑定用户微信OpenID
     *
     * @param userId 用户ID
     * @param openId 微信OpenID
     * @return 是否成功
     */
    boolean bindUserOpenId(Long userId, String openId);
}
