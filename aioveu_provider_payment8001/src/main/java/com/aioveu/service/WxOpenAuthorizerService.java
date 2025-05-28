package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.WxOpenAuthorizer;
import com.aioveu.feign.vo.WxOpenMaCodeTemplateVo;
import com.aioveu.vo.WxOpenAuthorizerVo;

import java.util.List;

/**
 * @description
 * @Author  luyao
 * @Date: 2025-02-09 10:04:24
 */
public interface WxOpenAuthorizerService extends IService<WxOpenAuthorizer> {

    /**
     * 获取授权二维码
     * @return
     */
    String gotoPreAuthUrl();

    /**
     * 授权成功后，保存授权信息
     * @param authorizationCode  授权码, 会在授权成功时返回给第三方平台
     * @param userId
     */
    Boolean saveAuthorization(String authorizationCode, String userId);

    /**
     * 获取列表
     * @param page
     * @param size
     * @param authorizerUserId
     * @return
     */
    IPage<WxOpenAuthorizerVo> getAllByPage(int page, int size, String authorizerUserId);

    /**
     * 根据第三方平台app和授权appid查询记录
     * @param componentAppId
     * @param authorizerAppId
     * @return
     */
    WxOpenAuthorizer getOne(String componentAppId,String authorizerAppId);

    /**
     * 获取授权的小程序
     * @return
     */
    List<WxOpenAuthorizer> getAuthList(String componentAppId);

    /**
     * 刷新已授权程序的
     * @param componentAppId
     * @param appId
     * @return
     */
    Boolean refreshAuthToken(String componentAppId, String appId);

    /**
     * 更新状态
     * @param componentAppId
     * @param authorizerAppId
     * @param status
     * @return
     */
    Boolean updateStatus(String componentAppId,String authorizerAppId, Integer status);

    /**
     * 解除授权
     * @param componentAppId
     * @param authorizerAppId
     * @return
     */
    Boolean revokeAuth(String componentAppId,String authorizerAppId);

    /**
     * 提交代码到审核，并生成体验版
     * @param openAppIds  支持多个
     * @param templateId
     * @param userVersion
     * @param userDesc
     * @param extJsonObject
     * @return
     */
    Boolean codeCommit(String openAppIds, Long templateId, String userVersion, String userDesc, Object extJsonObject);
    /**
     * 获取第三方代码模版,最新的一个
     * @param templateType
     * @return
     */
    WxOpenMaCodeTemplateVo getLatestTemplate(Integer templateType);
    /**
     * 获取第三方代码模版
     * @param templateType
     * @return
     */
    List<WxOpenMaCodeTemplateVo> getTemplateList(Integer templateType);

    /**
     * 提交代码审核
     * @param appId
     * @param versionDesc  小程序版本说明和功能解释
     * @return
     */
    Boolean submitAudit(String appId,String versionDesc);

    /**
     * 发布已通过审核的小程序
     * @param appId
     * @return
     */
    Boolean versionPublish(String appId);

    /**
     * 域名配置
     * @param appId
     * @return
     */
    Boolean domainConfig(String appId);

    /**
     * 版本回退
     * @param appId
     * @return
     */
    Boolean revert(String appId);

    /**
     * 隐私配置
     * @param appId
     * @return
     */
    Boolean privacySetting(String appId);

    /**
     * webview域名配置
     * @param appId
     * @return
     */
    Boolean webviewConfig(String appId);
}
