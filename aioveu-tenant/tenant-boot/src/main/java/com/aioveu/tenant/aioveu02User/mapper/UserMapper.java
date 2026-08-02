package com.aioveu.tenant.aioveu02User.mapper;

import com.aioveu.common.core.annotation.DataPermission;
import com.aioveu.common.security.core.model.dto.UserAuthCredentials;

import com.aioveu.tenant.aioveu02User.model.bo.UserBO;
import com.aioveu.tenant.aioveu02User.model.dto.UserExportDTO;
import com.aioveu.tenant.aioveu02User.model.entity.User;
import com.aioveu.tenant.aioveu02User.model.form.UserForm;
import com.aioveu.tenant.aioveu02User.model.query.UserQuery;
import com.aioveu.tenant.dto.TenantVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName: UserMapper
 * @Description TODO 用户持久层接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:46
 * @Version 1.0
 **/
@Mapper
public interface UserMapper  extends BaseMapper<User> {

    /**
     * 获取用户分页列表
     *
     * @param page        分页参数
     * @param queryParams 查询参数
     * @return 用户分页列表
     */
    @DataPermission(deptAlias = "u", userAlias = "u")
    Page<UserBO> getUserPage(Page<UserBO> page, @Param("queryParams") UserQuery queryParams);

    /**
     * 获取用户表单详情
     *
     * @param userId 用户ID
     * @return 用户表单详情
     */
    UserForm getUserFormData(Long userId);

    /**
     * 根据用户名和租户ID获取认证信息
     *
     * @param username 用户名
     * @return 认证信息
     */
    UserAuthCredentials getAuthInfoByUsernameAndTenantId(String username, Long tenantId);

    /**
     * 根据微信openid获取用户认证信息
     *
     * @param openid 微信openid
     * @return 认证信息
     */
    UserAuthCredentials getAuthInfoByOpenId(String openid);

    /**
     * 根据手机号获取用户认证信息
     *
     * @param mobile 手机号
     * @return 认证信息
     */
    UserAuthCredentials getAuthInfoByMobile(String mobile);

    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return 导出用户列表
     */
    @DataPermission(deptAlias = "u", userAlias = "u")
    List<UserExportDTO> listExportUsers(UserQuery queryParams);

    /**
     * 获取用户个人中心信息
     *
     * @param userId 用户ID
     * @return 用户个人中心信息
     */
    UserBO getUserProfile(Long userId);



    /**
     * 根据用户名查询所有用户ID（跨所有租户）
     *
     * @return {@link List<Long>} 所有用户ID（跨所有租户）列表
     */
    @Select("""
    SELECT id
    FROM sys_user
    WHERE username = #{username}
      AND is_deleted = 0
    """)
    List<Long> getUserIdsByUsernameAcrossTenants(String username);

    @Select("""
    SELECT
        t.id,
        t.username,
        t.status
    FROM sys_user u
    JOIN sys_tenant t ON u.tenant_id = t.id
    WHERE u.username = #{username}
      AND u.is_deleted = 0
      AND t.is_deleted = 0
      AND t.status = 1
    """)
    List<TenantVO> getAccessibleTenantsByUsername(String username);
}
