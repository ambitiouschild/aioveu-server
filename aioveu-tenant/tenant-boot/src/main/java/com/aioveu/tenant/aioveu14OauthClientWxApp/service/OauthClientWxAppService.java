package com.aioveu.tenant.aioveu14OauthClientWxApp.service;

import com.aioveu.tenant.aioveu14OauthClientWxApp.model.entity.OauthClientWxApp;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.form.OauthClientWxAppForm;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.query.OauthClientWxAppQuery;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.vo.OauthClientWxAppVo;
import com.aioveu.tenant.aioveu14OauthClientWxApp.model.vo.TenantWxAppInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: OauthClientWxAppService
 * @Description TODO OAuth2客户端与微信小程序映射服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/19 16:51
 * @Version 1.0
 **/

public interface OauthClientWxAppService extends IService<OauthClientWxApp> {

    /**
     *OAuth2客户端与微信小程序映射分页列表
     *
     * @return {@link IPage<OauthClientWxAppVo>} OAuth2客户端与微信小程序映射分页列表
     */
    IPage<OauthClientWxAppVo> getOauthClientWxAppPage(OauthClientWxAppQuery queryParams);

    /**
     * 获取OAuth2客户端与微信小程序映射表单数据
     *
     * @param id OAuth2客户端与微信小程序映射ID
     * @return OAuth2客户端与微信小程序映射表单数据
     */
    OauthClientWxAppForm getOauthClientWxAppFormData(Long id);

    /**
     * 新增OAuth2客户端与微信小程序映射
     *
     * @param formData OAuth2客户端与微信小程序映射表单对象
     * @return 是否新增成功
     */
    boolean saveOauthClientWxApp(OauthClientWxAppForm formData);

    /**
     * 修改OAuth2客户端与微信小程序映射
     *
     * @param id   OAuth2客户端与微信小程序映射ID
     * @param formData OAuth2客户端与微信小程序映射表单对象
     * @return 是否修改成功
     */
    boolean updateOauthClientWxApp(Long id, OauthClientWxAppForm formData);

    /**
     * 删除OAuth2客户端与微信小程序映射
     *
     * @param ids OAuth2客户端与微信小程序映射ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOauthClientWxApps(String ids);

    /**
     * 通过 clientId 获取租户和小程序信息
     *
     * @param clientId
     * @return TenantWxAppInfo
     */
    TenantWxAppInfo getTenantWxAppInfoByClientId(String clientId);
}
