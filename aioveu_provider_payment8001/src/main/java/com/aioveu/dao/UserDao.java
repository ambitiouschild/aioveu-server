package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.entity.User;
import com.aioveu.entity.UserExtensionAccount;
import com.aioveu.vo.user.ManagerUserItemVO;
import com.aioveu.vo.user.StoreUserForm;
import com.aioveu.vo.user.UserItemVO;
import com.aioveu.vo.user.UserVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface UserDao extends BaseMapper<User> {

    /**
     * 用户列表
     * @param page
     * @param role
     * @return
     */
    IPage<UserItemVO> list(IPage<UserItemVO> page, @Param("role") String role, String phone, String keyword);

    /**
     * 从sport_user_open_id查找用户
     * @param openId
     * @return
     */
    User getFromUserOpenId(@Param("openId") String openId);

    /**
     *账户列表 根据创建者id和店铺id查询
     * @param objectPage
     * @param creatorId 创建者id
     * @param storeId 店铺id
     * @return
     */
    IPage<ManagerUserItemVO> getUserByCreatorId(Page<Object> objectPage, String creatorId, Long storeId);

    /**
     * 商户端用户详情
     * @param id
     * @return
     */
    StoreUserForm getStoreUserById(String id);

    /**
     * 查询销售角色的所有用户
     * @return
     */
    IPage<User> getPresale(IPage<User> page,Long id);

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    int deleteUserById(String id);

    /**
     * 分页查询地推人员信息
     * @param page
     * @param username
     * @param id
     * @return
     */
    IPage<UserVo> selExtensionUser(IPage<UserVo> page, String username, String id);

    /**
     * 获取用户推广钱包余额
     *
     * @param userId 用户id
     * @return {@link UserExtensionAccount}
     */
    UserExtensionAccount getUserBalance(String userId);

    /**
     * 修改用户推广钱包余额
     *
     * @param user 用户
     * @return
     */
    Integer updateBalanceById(UserExtensionAccount user);
}
