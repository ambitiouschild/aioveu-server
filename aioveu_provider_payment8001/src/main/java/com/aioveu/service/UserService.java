package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.User;
import com.aioveu.entity.UserExtensionAccount;
import com.aioveu.form.UserCashForm;
import com.aioveu.vo.UserExtendVO;
import com.aioveu.vo.UserRealTimeVO;
import com.aioveu.vo.UserStoreBalanceVO;
import com.aioveu.vo.WebUserVo;
import com.aioveu.vo.user.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserService extends IService<User> {

    /**
     * web端创建临时账户
     * @param
     * @return
     */
    Map<String, String> createQuShuTempAccounts();


    /**
     * 创建用户
     * @param baseUserForm {@link BaseUserForm} 待创建的用户信息
     * @return userVo {@link UserVo} 创建后的用户信息
     */
    UserVo create(BaseUserForm baseUserForm);

    /**
     * web端创建或者更新用户
     * @param sportUserForm
     * @return
     */
    boolean webManagerCreateOrUpdate(SportUserForm sportUserForm);

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @return {@link UserVo} 用户
     */
    UserVo findByUsername(String username);

    /**
     * 通过openId或者UnionId认证
     * @param openId
     * @param providerId
     * @return
     */
    UserVo getByIdAndProvider(String openId, String providerId);

    /**
     * 根据用户id查找
     * @param id
     * @return
     */
    UserVo getByUserId(String id);


    /**
     * 获取用户列表
     * @param page
     * @param size
     * @param role
     * @param phone
     * @param keyword
     * @return
     */
    IPage<UserItemVO> getAll(int page, int size, String role, String phone, String keyword);

    /**
     * 获取管理平台用户详情
     * @param id
     * @return
     */
    SportUserForm getManagerUserById(String id);

    /**
     * 重置密码
     * @param id
     * @param password
     * @return
     */
    boolean resetPassword(String id, String password);

    /**
     * 检查用户是否注册
     * @param checkType 0 珊瑚报名 1 第三方小程序
     * @param openId
     * @param unionId
     * @return
     */
    UserVo checkRegister(Integer checkType, String openId, String unionId);

    /**
     * 根据用户名查找用户实时数据
     * @param username
     * @return
     */
    UserRealTimeVO getRealTimeByUsername(String username, Long companyId);

    /**
     * 用户提现
     * @param userCashForm
     * @return
     * @throws Exception
     */
    boolean userCash(UserCashForm userCashForm);

    /**
     * 增加用户余额
     * @param userId
     * @param amount
     * @param name
     * @param description
     * @param orderId
     * @return
     */
    boolean addBalance(String userId, BigDecimal amount, String name, String description, String orderId);

    /**
     * 减去用户余额
     * @param userId
     * @param amount
     * @param name
     * @param description
     * @param orderId
     * @param negative 是否可以为负数
     * @return
     */
    boolean reduceBalance(String userId, BigDecimal amount, String name, String description, String orderId, boolean negative);

    /**
     * 从缓存中获取userId
     * @param username
     * @return
     */
    String getUserIdFromCache(String username);

    /**
     * 通过手机号码查找用户
     * @param phone
     * @return
     */
    User getByUserPhone(String phone);

    /**
     * 修改用户的手机号
     * @param username 用户名
     * @param newPhone 新手机号码
     * @param code 验证码
     * @return
     */
    boolean updateUserPhone(String username, String newPhone, String code);

    /**
     * 获取账户列表 根据创建者id和店铺id查询
     * @param page
     * @param size
     * @param creatorId 创建者id
     * @param storeId 店铺id
     * @return
     */
    IPage<ManagerUserItemVO> getUserByCreatorId(Integer page, Integer size, String creatorId, Long storeId);

    /**
     * 商户小程序端创建用户
     * @param user
     * @return
     */
    boolean createUser4Store(StoreUserForm user);

    /**
     * 修改小程序商户端用户
     * @param user
     * @return
     */
    boolean updateUser(StoreUserForm user);

    /**
     * 删除小程序商户端用户
     * @param userId
     * @return
     */
    boolean deleteStoreUser(String userId);

    /**
     * 商户端用户详情
     * @param id
     * @return
     */
    StoreUserForm getStoreUserById(String id);

    /**
     * 商户端用户详情
     * @param id
     * @return
     */
    StoreUserForm getStoreUserById(String id, Long storeId);

    /**
     * 获取用户在该门店对应的角色
     * @param id
     * @param storeId
     * @return
     */
    StoreUserForm getStoreRoleByUserId(String id, Long storeId);

    /**
     * 查询销售角色的所有用户
     * @return
     */
    IPage<User> getPresale(int page, int size,Long id);

    /**
     * 通过用户名查找用户
     * @param username 用户名
     * @param appId
     * @return {@link UserVo} 用户
     */
    UserVo findByUsernameAndAppId(String username, String appId);

    /**
     * username查找用户
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 通过id删除用户
     * @param userId
     * @return
     */
    boolean deleteById(String userId);

    /**
     * 手机号码创建用户
     * @param phone
     * @param name
     * @return
     */
    String createByPhone(String phone, String name);

    /**
     * 获取测试用户
     * @param number
     * @return
     */
    List<User> getTestUser(int number);

    /**
     * 根据用户余额
     * @param username
     * @return
     */
    UserStoreBalanceVO getStoreBalance(String username);


    /**
     * 获取用户推广钱包余额
     *
     * @param userId 用户id
     * @return {@link UserExtensionAccount}
     */
    UserExtensionAccount getUserBalance(String userId);

    /**
     * 修改推广钱包余额
     *
     * @param user 用户
     * @return {@link Boolean}
     */
    Boolean updateBalanceById(UserExtensionAccount user);

    UserExtendVO getUserExtendedInfo(Long companyId);

    UserExtendVO getUserExtendedInfoByAppId(String appId);

    /**
     * 获取web端用户信息
     * @param id
     * @return
     */
    WebUserVo getWebUserById(String id);

    /**
     * 修改用户状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(String id, Integer status);

    /**
     * 访客端，设置用户基本信息，比如头像、别名、性别
     * @param currentUserId
     * @param name
     * @param head
     * @param gender
     * @return
     */
    boolean updateUserBaseInfo(String currentUserId, String name, String head, Integer gender);

    /**
     * 通过openId查找用户
     * @param openId
     * @return
     */
    User getByOpenId(String openId);

    /**
     * 手机号快速注册
     * @param phone
     * @return
     */
    User quickRegisterByPhone(String phone, String password);
}