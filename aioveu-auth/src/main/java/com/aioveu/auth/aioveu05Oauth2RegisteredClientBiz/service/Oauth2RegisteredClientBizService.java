package com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.service;


import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.entity.Oauth2RegisteredClientBiz;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.form.Oauth2RegisteredClientBizForm;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.query.Oauth2RegisteredClientBizQuery;
import com.aioveu.auth.aioveu05Oauth2RegisteredClientBiz.model.vo.Oauth2RegisteredClientBizVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: Oauth2RegisteredClientBizService
 * @Description TODO OAuth2 客户端业务状态（auth 服务本地校验用）服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/11 17:35
 * @Version 1.0
 **/

public interface Oauth2RegisteredClientBizService extends IService<Oauth2RegisteredClientBiz> {

    /**
     *OAuth2 客户端业务状态（auth 服务本地校验用）分页列表
     *
     * @return {@link IPage<Oauth2RegisteredClientBizVo>} OAuth2 客户端业务状态（auth 服务本地校验用）分页列表
     */
    IPage<Oauth2RegisteredClientBizVo> getOauth2RegisteredClientBizPage(Oauth2RegisteredClientBizQuery queryParams);

    /**
     * 获取OAuth2 客户端业务状态（auth 服务本地校验用）表单数据
     *
     * @param id OAuth2 客户端业务状态（auth 服务本地校验用）ID
     * @return OAuth2 客户端业务状态（auth 服务本地校验用）表单数据
     */
    Oauth2RegisteredClientBizForm getOauth2RegisteredClientBizFormData(Long id);

    /**
     * 新增OAuth2 客户端业务状态（auth 服务本地校验用）
     *
     * @param formData OAuth2 客户端业务状态（auth 服务本地校验用）表单对象
     * @return 是否新增成功
     */
    boolean saveOauth2RegisteredClientBiz(Oauth2RegisteredClientBizForm formData);

    /**
     * 修改OAuth2 客户端业务状态（auth 服务本地校验用）
     *
     * @param id   OAuth2 客户端业务状态（auth 服务本地校验用）ID
     * @param formData OAuth2 客户端业务状态（auth 服务本地校验用）表单对象
     * @return 是否修改成功
     */
    boolean updateOauth2RegisteredClientBiz(Long id, Oauth2RegisteredClientBizForm formData);

    /**
     * 删除OAuth2 客户端业务状态（auth 服务本地校验用）
     *
     * @param ids OAuth2 客户端业务状态（auth 服务本地校验用）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOauth2RegisteredClientBizs(String ids);
}
