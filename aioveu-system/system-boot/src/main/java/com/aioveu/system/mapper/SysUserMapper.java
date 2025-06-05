package com.aioveu.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.common.mybatis.annotation.DataPermission;
import com.aioveu.system.dto.UserAuthInfo;
import com.aioveu.system.model.bo.UserBO;
import com.aioveu.system.model.bo.UserFormBO;
import com.aioveu.system.model.bo.UserProfileBO;
import com.aioveu.system.model.entity.SysUser;
import com.aioveu.system.model.query.UserPageQuery;
import com.aioveu.system.model.vo.UserExportVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: TODO 用户持久层
 * @Author: 雒世松
 * @Date: 2025/6/5 17:17
 * @param
 * @return:
 **/

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 获取用户分页列表
     *
     * @param page        分页参数
     * @param queryParams 查询参数
     * @return {@link List<UserBO>}
     */
    @DataPermission(deptAlias = "u")
    Page<UserBO> getUserPage(Page<UserBO> page, UserPageQuery queryParams);

    /**
     * 获取用户表单详情
     *
     * @param userId 用户ID
     * @return {@link UserFormBO}
     */
    UserFormBO getUserDetail(Long userId);

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link UserAuthInfo}
     */
    UserAuthInfo getUserAuthInfo(String username);

    /**
     * 获取导出用户列表
     *
     * @param queryParams 查询参数
     * @return {@link List<UserExportVO>}
     */
    @DataPermission(deptAlias = "u")
    List<UserExportVO> listExportUsers(UserPageQuery queryParams);

    /**
     * 获取用户个人中心信息
     *
     * @param userId 用户ID
     * @return {@link UserProfileBO}
     */
    UserProfileBO getUserProfile(Long userId);
}
