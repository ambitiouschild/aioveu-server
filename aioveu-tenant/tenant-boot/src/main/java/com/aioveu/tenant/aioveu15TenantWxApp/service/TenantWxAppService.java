package com.aioveu.tenant.aioveu15TenantWxApp.service;

import com.aioveu.tenant.aioveu15TenantWxApp.model.entity.TenantWxApp;
import com.aioveu.tenant.aioveu15TenantWxApp.model.form.TenantWxAppForm;
import com.aioveu.tenant.aioveu15TenantWxApp.model.query.TenantWxAppQuery;
import com.aioveu.tenant.aioveu15TenantWxApp.model.vo.TenantWxAppVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: TenantWxAppService
 * @Description TODO 租户与微信小程序关联服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 17:07
 * @Version 1.0
 **/

public interface TenantWxAppService extends IService<TenantWxApp> {

    /**
     *租户与微信小程序关联分页列表
     *
     * @return {@link IPage<TenantWxAppVo>} 租户与微信小程序关联分页列表
     */
    IPage<TenantWxAppVo> getTenantWxAppPage(TenantWxAppQuery queryParams);

    /**
     * 获取租户与微信小程序关联表单数据
     *
     * @param id 租户与微信小程序关联ID
     * @return 租户与微信小程序关联表单数据
     */
    TenantWxAppForm getTenantWxAppFormData(Long id);

    /**
     * 新增租户与微信小程序关联
     *
     * @param formData 租户与微信小程序关联表单对象
     * @return 是否新增成功
     */
    boolean saveTenantWxApp(TenantWxAppForm formData);

    /**
     * 修改租户与微信小程序关联
     *
     * @param id   租户与微信小程序关联ID
     * @param formData 租户与微信小程序关联表单对象
     * @return 是否修改成功
     */
    boolean updateTenantWxApp(Long id, TenantWxAppForm formData);

    /**
     * 删除租户与微信小程序关联
     *
     * @param ids 租户与微信小程序关联ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteTenantWxApps(String ids);


    /**
     * 通过 wxAppid 获取tenantId
     *
     * @param wxAppid
     * @return tenantId
     */
    Long getTenantIdByWxAppid(String wxAppid);


    /**
     * 通过 wxAppid 获取租户微信配置
     *
     * @param wxAppid
     * @return TenantWxApp
     */
    TenantWxAppVo  getConfigByWxAppid(String wxAppid);

}
