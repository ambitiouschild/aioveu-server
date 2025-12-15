package com.aioveu.system.service;


import com.aioveu.common.model.Option;
import com.aioveu.common.security.model.UserAuthCredentials;
import com.aioveu.system.model.dto.CurrentUserDTO;
import com.aioveu.system.model.dto.UserExportDTO;
import com.aioveu.system.model.form.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.system.dto.UserAuthInfo;
import com.aioveu.system.model.entity.SysUser;
import com.aioveu.system.model.query.UserPageQuery;
import com.aioveu.system.model.vo.UserExportVO;
import com.aioveu.system.model.vo.UserInfoVO;
import com.aioveu.system.model.vo.UserPageVO;
import com.aioveu.system.model.vo.UserProfileVO;

import java.util.List;

/**
 * @Description: TODO 用户业务接口
 * @Author: 雒世松
 * @Date: 2025/6/5 17:30
 * @param
 * @return:
 **/

public interface SysUserService extends IService<SysUser> {

    /**
     * 用户分页列表
     *
     * @return {@link IPage<UserPageVO>}
     */
    IPage<UserPageVO> getUserPage(UserPageQuery queryParams);


    /**
     * 获取用户表单数据
     *
     * @param userId 用户ID
     * @return {@link UserForm}
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
     * @return {@link Boolean}  是否删除成功
     */
    boolean deleteUsers(String idsStr);


    /**
     * 修改用户密码
     *
     * @param userId   用户ID
     * @param password 用户密码
     * @return {@link Boolean}
     */
    boolean updatePassword(Long userId, String password);

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link UserAuthInfo}
     */

    UserAuthInfo getUserAuthInfo(String username);

    //--------------------

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link UserAuthCredentials}
     */

    UserAuthCredentials getAuthCredentialsByUsername(String username);


    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return {@link List<UserExportVO>}
     */
    List<UserExportVO> listExportUsersOld(UserPageQuery queryParams);


    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return {@link List<UserExportVO>}
     */
    List<UserExportVO> listExportUsers(UserPageQuery queryParams);

    /**
     * 获取登录用户信息
     *
     * @return {@link UserInfoVO}
     */
    UserInfoVO getCurrentUserInfoOld();


    /**
     * 获取登录用户信息
     *
     * @return {@link UserInfoVO}
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
     * 修改用户密码
     *
     * @param userId 用户ID
     * @param data   修改密码表单数据
     * @return {@link Boolean} 是否修改成功
     */
    boolean changePassword(Long userId, PasswordUpdateForm data);


    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 重置后的密码
     * @return {@link Boolean} 是否重置成功
     */
    boolean resetPassword(Long userId, String password);


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
     * @return {@link UserAuthCredentials}
     */

    UserAuthCredentials getAuthCredentialsByOpenId(String openId);


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
     * @return {@link UserAuthCredentials}
     */
    UserAuthCredentials getAuthCredentialsByMobile(String mobile);


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


    /**
     * 注销登出
     *
     * @return {@link Boolean}
     */
    boolean logout();

    /**
     * 注册用户
     *
     * @param userRegisterForm 用户注册表单对象
     * @return {@link Boolean}
     */
    boolean registerUser(UserRegisterForm userRegisterForm);


    /**
     * 发送注册短信验证码
     *
     * @param mobile 手机号
     * @return {@link Boolean} 是否发送成功
     */
    boolean sendRegistrationSmsCode(String mobile);


    /**
     * 获取用户个人中心信息
     *
     * @return {@link UserProfileVO}
     */
    UserProfileVO getUserProfile();
}
