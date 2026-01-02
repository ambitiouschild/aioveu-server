package com.aioveu.system.aioveu02User.mapper;

import com.aioveu.common.mybatis.annotation.DataPermission;
import com.aioveu.common.security.model.UserAuthCredentials;
import com.aioveu.system.aioveu02User.model.entity.User;
import com.aioveu.system.aioveu02User.model.form.UserForm;
import com.aioveu.system.aioveu02User.model.query.UserPageQuery;
import com.aioveu.system.aioveu02User.model.vo.UserBO;
import com.aioveu.system.aioveu02User.model.vo.UserExportDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: UserMapper
 * @Description TODO  用户持久层接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/30 22:35
 * @Version 1.0
 **/

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取用户分页列表
     *
     * @param page        分页参数
     * @param queryParams 查询参数
     * @return 用户分页列表
     */
    @DataPermission(
            deptAlias = "u",  // 部门字段的表别名，用于标记 SQL 中部门字段的来源表
            deptIdColumnName = "dept_id", // 部门字段的列名，默认为 dept_id
            userAlias = "u",// 用户字段的表别名，用于标记 SQL 中用户字段的来源表
            userIdColumnName = "create_by" // 用户字段的列名，默认为 create_by
    )
    Page<UserBO> getUserPage(Page<UserBO> page, UserPageQuery queryParams);

    /**
     * 获取用户表单详情
     *
     * @param userId 用户ID
     * @return 用户表单详情
     */
    UserForm getUserFormData(Long userId);

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return 认证信息
     */
    UserAuthCredentials getAuthCredentialsByUsername(String username);

    /**
     * 根据微信openid获取用户认证信息
     *
     * @param openid 微信openid
     * @return 认证信息
     */
    UserAuthCredentials getAuthCredentialsByOpenId(String openid);

    /**
     * 根据手机号获取用户认证信息
     *
     * @param mobile 手机号
     * @return 认证信息
     */
    UserAuthCredentials getAuthCredentialsByMobile(String mobile);

    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return 导出用户列表
     */
    @DataPermission(deptAlias = "u", userAlias = "u")
    List<UserExportDTO> listExportUsers(UserPageQuery queryParams);

    /**
     * 获取用户个人中心信息
     *
     * @param userId 用户ID
     * @return 用户个人中心信息
     */
    UserBO getUserProfile(Long userId);
}
