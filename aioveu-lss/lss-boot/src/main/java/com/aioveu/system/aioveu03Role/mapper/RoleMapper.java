package com.aioveu.system.aioveu03Role.mapper;

import com.aioveu.system.aioveu03Role.model.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * @ClassName: RoleMapper
 * @Description TODO 角色持久层接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:03
 * @Version 1.0
 **/

@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取最大范围的数据权限
     *
     * @param roles 角色编码集合
     * @return
     */
    Integer getMaximumDataScope(Set<String> roles);
}
