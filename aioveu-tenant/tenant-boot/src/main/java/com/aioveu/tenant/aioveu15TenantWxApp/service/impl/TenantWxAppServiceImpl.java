package com.aioveu.tenant.aioveu15TenantWxApp.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.tenant.aioveu15TenantWxApp.converter.TenantWxAppConverter;
import com.aioveu.tenant.aioveu15TenantWxApp.mapper.TenantWxAppMapper;
import com.aioveu.tenant.aioveu15TenantWxApp.model.entity.TenantWxApp;
import com.aioveu.tenant.aioveu15TenantWxApp.model.form.TenantWxAppForm;
import com.aioveu.tenant.aioveu15TenantWxApp.model.query.TenantWxAppQuery;
import com.aioveu.tenant.aioveu15TenantWxApp.model.vo.TenantWxAppVo;
import com.aioveu.tenant.aioveu15TenantWxApp.service.TenantWxAppService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: TenantWxAppServiceImpl
 * @Description TODO 租户与微信小程序关联服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 17:07
 * @Version 1.0
 **/
@Service
@RequiredArgsConstructor
public class TenantWxAppServiceImpl extends ServiceImpl<TenantWxAppMapper, TenantWxApp> implements TenantWxAppService {

    private final TenantWxAppConverter tenantWxAppConverter;

    /**
     * 获取租户与微信小程序关联分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<TenantWxAppVo>} 租户与微信小程序关联分页列表
     */
    @Override
    public IPage<TenantWxAppVo> getTenantWxAppPage(TenantWxAppQuery queryParams) {
        Page<TenantWxAppVo> page = this.baseMapper.getTenantWxAppPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取租户与微信小程序关联表单数据
     *
     * @param id 租户与微信小程序关联ID
     * @return 租户与微信小程序关联表单数据
     */
    @Override
    public TenantWxAppForm getTenantWxAppFormData(Long id) {
        TenantWxApp entity = this.getById(id);
        return tenantWxAppConverter.toForm(entity);
    }

    /**
     * 新增租户与微信小程序关联
     *
     * @param formData 租户与微信小程序关联表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveTenantWxApp(TenantWxAppForm formData) {
        TenantWxApp entity = tenantWxAppConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新租户与微信小程序关联
     *
     * @param id   租户与微信小程序关联ID
     * @param formData 租户与微信小程序关联表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateTenantWxApp(Long id,TenantWxAppForm formData) {
        TenantWxApp entity = tenantWxAppConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除租户与微信小程序关联
     *
     * @param ids 租户与微信小程序关联ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteTenantWxApps(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的租户与微信小程序关联数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
