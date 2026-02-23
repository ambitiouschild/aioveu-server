package com.aioveu.tenant.aioveu01Tenant.mapper;

import com.aioveu.tenant.aioveu01Tenant.model.entity.Tenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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
}
