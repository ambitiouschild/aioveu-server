package com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.service;

import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.entity.Oauth2AuthorizationConsent;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.form.Oauth2AuthorizationConsentForm;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.query.Oauth2AuthorizationConsentQuery;
import com.aioveu.auth.aioveu03Oauth2AuthorizationConsent.model.vo.Oauth2AuthorizationConsentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: Oauth2AuthorizationConsentService
 * @Description TODO OAuth2授权同意，记录用户对每个客户端的授权同意情况服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 14:28
 * @Version 1.0
 **/

public interface Oauth2AuthorizationConsentService extends IService<Oauth2AuthorizationConsent> {

    /**
     *OAuth2授权同意，记录用户对每个客户端的授权同意情况分页列表
     *
     * @return {@link IPage<Oauth2AuthorizationConsentVo>} OAuth2授权同意，记录用户对每个客户端的授权同意情况分页列表
     */
    IPage<Oauth2AuthorizationConsentVo> getOauth2AuthorizationConsentPage(Oauth2AuthorizationConsentQuery queryParams);

    /**
     * 获取OAuth2授权同意，记录用户对每个客户端的授权同意情况表单数据
     *
     * @param id OAuth2授权同意，记录用户对每个客户端的授权同意情况ID
     * @return OAuth2授权同意，记录用户对每个客户端的授权同意情况表单数据
     */
    Oauth2AuthorizationConsentForm getOauth2AuthorizationConsentFormData(Long id);

    /**
     * 新增OAuth2授权同意，记录用户对每个客户端的授权同意情况
     *
     * @param formData OAuth2授权同意，记录用户对每个客户端的授权同意情况表单对象
     * @return 是否新增成功
     */
    boolean saveOauth2AuthorizationConsent(Oauth2AuthorizationConsentForm formData);

    /**
     * 修改OAuth2授权同意，记录用户对每个客户端的授权同意情况
     *
     * @param id   OAuth2授权同意，记录用户对每个客户端的授权同意情况ID
     * @param formData OAuth2授权同意，记录用户对每个客户端的授权同意情况表单对象
     * @return 是否修改成功
     */
    boolean updateOauth2AuthorizationConsent(Long id, Oauth2AuthorizationConsentForm formData);

    /**
     * 删除OAuth2授权同意，记录用户对每个客户端的授权同意情况
     *
     * @param ids OAuth2授权同意，记录用户对每个客户端的授权同意情况ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOauth2AuthorizationConsents(String ids);
}
