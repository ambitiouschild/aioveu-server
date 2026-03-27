package com.aioveu.registry.aioveu01RegistryTenant.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.registry.aioveu01RegistryTenant.converter.RegistryTenantConverter;
import com.aioveu.registry.aioveu01RegistryTenant.mapper.RegistryTenantMapper;
import com.aioveu.registry.aioveu01RegistryTenant.model.entity.RegistryTenant;
import com.aioveu.registry.aioveu01RegistryTenant.model.form.RegistryTenantForm;
import com.aioveu.registry.aioveu01RegistryTenant.model.query.RegistryTenantQuery;
import com.aioveu.registry.aioveu01RegistryTenant.model.vo.RegistryTenantVo;
import com.aioveu.registry.aioveu01RegistryTenant.service.RegistryTenantService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RegistryTenantServiceImpl
 * @Description TODO 租户注册小程序基本信息服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 16:33
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class RegistryTenantServiceImpl extends ServiceImpl<RegistryTenantMapper, RegistryTenant> implements RegistryTenantService {

    private final RegistryTenantConverter registryTenantConverter;

    /**
     * 获取租户注册小程序基本信息分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RegistryTenantVo>} 租户注册小程序基本信息分页列表
     */
    @Override
    public IPage<RegistryTenantVo> getRegistryTenantPage(RegistryTenantQuery queryParams) {
        Page<RegistryTenantVo> page = this.baseMapper.getRegistryTenantPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取租户注册小程序基本信息表单数据
     *
     * @param id 租户注册小程序基本信息ID
     * @return 租户注册小程序基本信息表单数据
     */
    @Override
    public RegistryTenantForm getRegistryTenantFormData(Long id) {
        RegistryTenant entity = this.getById(id);
        return registryTenantConverter.toForm(entity);
    }

    /**
     * 新增租户注册小程序基本信息
     *
     * @param formData 租户注册小程序基本信息表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRegistryTenant(RegistryTenantForm formData) {
        RegistryTenant entity = registryTenantConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新租户注册小程序基本信息
     *
     * @param id   租户注册小程序基本信息ID
     * @param formData 租户注册小程序基本信息表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRegistryTenant(Long id,RegistryTenantForm formData) {
        RegistryTenant entity = registryTenantConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除租户注册小程序基本信息
     *
     * @param ids 租户注册小程序基本信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRegistryTenants(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的租户注册小程序基本信息数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
