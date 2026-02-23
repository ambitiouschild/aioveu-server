package com.aioveu.tenant.aioveu02User.mapper;

import com.aioveu.tenant.aioveu02User.model.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: UserRoleMapper
 * @Description TODO 用户角色访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:56
 * @Version 1.0
 **/

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 获取用户ID绑定的角色集合
     *
     * @param userId 用户ID
     * @return 角色ID集合
     */
    List<Long> listRoleIdsByUserId(@Param("userId") Long userId);  // 添加 @Param

    /**
     * 获取角色绑定的用户数
     *
     * @param roleId 角色ID
     */
    int countUsersByRoleId(@Param("roleId")Long roleId);   // 添加 @Param

    /**
     * 获取角色绑定的用户ID集合
     *
     * @param roleId 角色ID
     * @return 用户ID集合
     * XML 中引用了 #{userId}，但接口方法参数名是 userId，在 Java 编译后参数名会丢失。
     */

    List<Long> listUserIdsByRoleId(@Param("roleId")Long roleId); // 添加 @Param


}
