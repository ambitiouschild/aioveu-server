package com.aioveu.registry.aioveu01RegistryTenant.mapper;

import com.aioveu.registry.aioveu01RegistryTenant.model.entity.RegistryTenant;
import com.aioveu.registry.aioveu01RegistryTenant.model.query.RegistryTenantQuery;
import com.aioveu.registry.aioveu01RegistryTenant.model.vo.RegistryTenantVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RegistryTenantMapper
 * @Description TODO 租户注册小程序基本信息Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:30
 * @Version 1.0
 **/
@Mapper
public interface RegistryTenantMapper extends BaseMapper<RegistryTenant> {

    /**
     * 获取租户注册小程序基本信息分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RegistryTenantVo>} 租户注册小程序基本信息分页列表
     */
    Page<RegistryTenantVo> getRegistryTenantPage(Page<RegistryTenantVo> page, RegistryTenantQuery queryParams);
}
