package com.aioveu.registry.aioveu01RegistryTenant.service;

import com.aioveu.registry.aioveu01RegistryTenant.model.entity.RegistryTenant;
import com.aioveu.registry.aioveu01RegistryTenant.model.form.RegistryTenantForm;
import com.aioveu.registry.aioveu01RegistryTenant.model.query.RegistryTenantQuery;
import com.aioveu.registry.aioveu01RegistryTenant.model.vo.RegistryTenantVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RegistryTenantService
 * @Description TODO 租户注册小程序基本信息服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:32
 * @Version 1.0
 **/

public interface RegistryTenantService extends IService<RegistryTenant> {

    /**
     *租户注册小程序基本信息分页列表
     *
     * @return {@link IPage<RegistryTenantVo>} 租户注册小程序基本信息分页列表
     */
    IPage<RegistryTenantVo> getRegistryTenantPage(RegistryTenantQuery queryParams);

    /**
     * 获取租户注册小程序基本信息表单数据
     *
     * @param id 租户注册小程序基本信息ID
     * @return 租户注册小程序基本信息表单数据
     */
    RegistryTenantForm getRegistryTenantFormData(Long id);

    /**
     * 新增租户注册小程序基本信息
     *
     * @param formData 租户注册小程序基本信息表单对象
     * @return 是否新增成功
     */
    boolean saveRegistryTenant(RegistryTenantForm formData);

    /**
     * 修改租户注册小程序基本信息
     *
     * @param id   租户注册小程序基本信息ID
     * @param formData 租户注册小程序基本信息表单对象
     * @return 是否修改成功
     */
    boolean updateRegistryTenant(Long id, RegistryTenantForm formData);

    /**
     * 删除租户注册小程序基本信息
     *
     * @param ids 租户注册小程序基本信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRegistryTenants(String ids);
}
