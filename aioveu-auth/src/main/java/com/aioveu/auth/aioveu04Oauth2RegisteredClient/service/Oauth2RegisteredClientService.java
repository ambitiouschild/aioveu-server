package com.aioveu.auth.aioveu04Oauth2RegisteredClient.service;

import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.entity.Oauth2RegisteredClient;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.form.Oauth2RegisteredClientForm;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.query.Oauth2RegisteredClientQuery;
import com.aioveu.auth.aioveu04Oauth2RegisteredClient.model.vo.Oauth2RegisteredClientVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: Oauth2RegisteredClientService
 * @Description TODO OAuth2注册客户端，存储所有已注册的客户端应用信息服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/22 15:14
 * @Version 1.0
 **/

public interface Oauth2RegisteredClientService extends IService<Oauth2RegisteredClient> {

    /**
     *OAuth2注册客户端，存储所有已注册的客户端应用信息分页列表
     *
     * @return {@link IPage<Oauth2RegisteredClientVo>} OAuth2注册客户端，存储所有已注册的客户端应用信息分页列表
     */
    IPage<Oauth2RegisteredClientVo> getOauth2RegisteredClientPage(Oauth2RegisteredClientQuery queryParams);

    /**
     * 获取OAuth2注册客户端，存储所有已注册的客户端应用信息表单数据
     *
     * @param id OAuth2注册客户端，存储所有已注册的客户端应用信息ID
     * @return OAuth2注册客户端，存储所有已注册的客户端应用信息表单数据
     */
    Oauth2RegisteredClientForm getOauth2RegisteredClientFormData(Long id);

    /**
     * 新增OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param formData OAuth2注册客户端，存储所有已注册的客户端应用信息表单对象
     * @return 是否新增成功
     */
    boolean saveOauth2RegisteredClient(Oauth2RegisteredClientForm formData);

    /**
     * 修改OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param clientId   OAuth2注册客户端，存储所有已注册的客户端应用信息ID
     * @param formData OAuth2注册客户端，存储所有已注册的客户端应用信息表单对象
     * @return 是否修改成功
     */
    boolean updateOauth2RegisteredClient(String clientId, Oauth2RegisteredClientForm formData);

    /**
     * 删除OAuth2注册客户端，存储所有已注册的客户端应用信息
     *
     * @param ids OAuth2注册客户端，存储所有已注册的客户端应用信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOauth2RegisteredClients(String ids);


    /*
    * 根据ID获取客户端
    * */
    Oauth2RegisteredClientVo getClient(String clientId);


    /*
     * 重置客户端密钥
     * */
    Oauth2RegisteredClientVo resetClientSecret(String clientId);

    /*
     * 修改客户端状态
     * */
    void toggleClientStatus(String clientId, boolean enabled);
}
