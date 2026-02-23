package com.aioveu.tenant.aioveu03Role.mapper;

import com.aioveu.common.security.model.RoleDataScope;
import com.aioveu.tenant.aioveu03Role.model.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @ClassName: RoleMapper
 * @Description TODO 角色持久层接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:35
 * @Version 1.0
 **/

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取最大范围的数据权限
     *
     * @param roles 角色编码集合
     * @return {@link Integer} – 数据权限范围
     */
    Integer getMaximumDataScope(Set<String> roles);

    /**
     * 获取角色的数据权限列表
     *
     * @param roleCodes 角色编码集合
     * @return 数据权限列表
     */
    List<RoleDataScope> getRoleDataScopes(@Param("roleCodes") Set<String> roleCodes);
}
