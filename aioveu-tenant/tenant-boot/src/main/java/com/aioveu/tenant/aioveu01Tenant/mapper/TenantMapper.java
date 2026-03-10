package com.aioveu.tenant.aioveu01Tenant.mapper;

import com.aioveu.tenant.aioveu01Tenant.model.entity.Tenant;
import com.aioveu.tenant.aioveu01Tenant.model.vo.TenantVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName: TenantMapper
 * @Description TODO 租户 Mapper
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:44
 * @Version 1.0
 **/

@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {


    @Select("""
        SELECT DISTINCT u.tenant_id 
        FROM sys_user u
        INNER JOIN sys_tenant t ON u.tenant_id = t.id
        WHERE u.username = #{username}
          AND u.status = 1  -- 用户状态正常
          AND u.is_deleted = 0
        ORDER BY t.tenant_id
    """)
    List<TenantVO> selectTenantsByUsername(@Param("username") String username);

}
