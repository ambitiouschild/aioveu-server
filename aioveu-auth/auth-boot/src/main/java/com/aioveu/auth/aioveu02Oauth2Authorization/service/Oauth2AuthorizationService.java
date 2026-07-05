package com.aioveu.auth.aioveu02Oauth2Authorization.service;

import com.aioveu.auth.aioveu02Oauth2Authorization.model.entity.Oauth2Authorization;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.form.Oauth2AuthorizationForm;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.query.Oauth2AuthorizationQuery;
import com.aioveu.auth.aioveu02Oauth2Authorization.model.vo.Oauth2AuthorizationVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: Oauth2AuthorizationService
 * @Description TODO OAuth2授权信息，存储所有的授权记录、令牌和状态信息服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 13:59
 * @Version 1.0
 **/

public interface Oauth2AuthorizationService extends IService<Oauth2Authorization> {

    /**
     *OAuth2授权信息，存储所有的授权记录、令牌和状态信息分页列表
     *
     * @return {@link IPage<Oauth2AuthorizationVo>} OAuth2授权信息，存储所有的授权记录、令牌和状态信息分页列表
     */
    IPage<Oauth2AuthorizationVo> getOauth2AuthorizationPage(Oauth2AuthorizationQuery queryParams);

    /**
     * 获取OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单数据
     *
     * @param id OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID
     * @return OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单数据
     */
    Oauth2AuthorizationForm getOauth2AuthorizationFormData(Long id);

    /**
     * 新增OAuth2授权信息，存储所有的授权记录、令牌和状态信息
     *
     * @param formData OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单对象
     * @return 是否新增成功
     */
    boolean saveOauth2Authorization(Oauth2AuthorizationForm formData);

    /**
     * 修改OAuth2授权信息，存储所有的授权记录、令牌和状态信息
     *
     * @param id   OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID
     * @param formData OAuth2授权信息，存储所有的授权记录、令牌和状态信息表单对象
     * @return 是否修改成功
     */
    boolean updateOauth2Authorization(Long id, Oauth2AuthorizationForm formData);

    /**
     * 删除OAuth2授权信息，存储所有的授权记录、令牌和状态信息
     *
     * @param ids OAuth2授权信息，存储所有的授权记录、令牌和状态信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOauth2Authorizations(String ids);
}
