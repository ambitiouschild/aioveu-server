package com.aioveu.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.dao.WxOpenAuthorizerDao;
import com.aioveu.entity.*;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.WxOpenSubmitAuditStatus;
import com.aioveu.exception.SportException;
import com.aioveu.feign.FeignWxOpenAuthClient;
import com.aioveu.feign.form.WxOpenMaSubmitAuditMessageForm;
import com.aioveu.feign.form.WxSetPrivacySettingForm;
import com.aioveu.feign.vo.WxOpenAuthorizationInfoVo;
import com.aioveu.feign.vo.WxOpenAuthorizerAccessTokenVO;
import com.aioveu.feign.vo.WxOpenAuthorizerInfoResultVo;
import com.aioveu.feign.vo.WxOpenMaCodeTemplateVo;
import com.aioveu.service.*;
import com.aioveu.vo.CommonResponse;
import com.aioveu.vo.WxOpenAuthorizerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @Author  luyao
 * @Date: 2025-02-09 10:04:24
 */
@Slf4j
@Service
public class WxOpenAuthorizerServiceImpl extends ServiceImpl<WxOpenAuthorizerDao, WxOpenAuthorizer> implements WxOpenAuthorizerService {

    /**
     * 趣数平台APPID 目前只会有一个平台
     */
    public static String QU_SHU_PLATFORM_APP_ID = "wxb8ab792a9fe1c3f5";

    @Autowired
    private WxOpenAuthorizerDao wxOpenAuthorizerDao;

    @Resource
    private FeignWxOpenAuthClient feignWxOpenAuthClient;

    @Autowired
    private WxOpenSubmitAuditService submitAuditService;

    @Autowired
    private WxOpenOnlineVersionService onlineVersionService;

    @Value(value = "${sport.server.prefix}")
    private String sportServerPrefix;

    @Override
    public IPage<WxOpenAuthorizerVo> getAllByPage(int page, int size, String authorizerUserId) {
        IPage<WxOpenAuthorizerVo> listByPage = wxOpenAuthorizerDao.getListByPage(new Page<>(page, size), authorizerUserId);
        List<WxOpenAuthorizerVo> records = listByPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)){
            Set<Long> codeCommitIdSet = new HashSet<>();
            records.stream().forEach(item -> {
                if (item.getOnlineCodeCommitId() != null){
                    codeCommitIdSet.add(item.getOnlineCodeCommitId());
                }
                if (item.getAuditCodeCommitId() != null) {
                    codeCommitIdSet.add(item.getAuditCodeCommitId());
                }
            });
            Map<Long, WxOpenCodeCommit> wxOpenCodeCommitMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(codeCommitIdSet)){
                List<WxOpenCodeCommit> codeCommits = codeCommitService.getListByIds(new ArrayList<>(codeCommitIdSet));
                wxOpenCodeCommitMap = codeCommits.stream().collect(Collectors.toMap(WxOpenCodeCommit::getId, item -> item));
            }
            for(WxOpenAuthorizerVo item : records){
                item.setOnlineInfo(wxOpenCodeCommitMap.get(item.getOnlineCodeCommitId()));
                item.setAuditInfo(wxOpenCodeCommitMap.get(item.getAuditCodeCommitId()));
            }
        }
        return listByPage;
    }

    @Override
    public WxOpenAuthorizer getOne(String componentAppId, String authorizerAppId) {
        LambdaQueryWrapper<WxOpenAuthorizer> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(WxOpenAuthorizer::getComponentAppId, componentAppId)
                .eq(WxOpenAuthorizer::getAuthorizerAppId, authorizerAppId);
        return getOne(wrapper);
    }

    @Override
    public List<WxOpenAuthorizer> getAuthList(String componentAppId) {
        LambdaQueryWrapper<WxOpenAuthorizer> wrapper = Wrappers.lambdaQuery();
        if (componentAppId != null){
            wrapper.eq(WxOpenAuthorizer::getComponentAppId, componentAppId);
        }
        wrapper.eq(WxOpenAuthorizer::getStatus, DataStatus.NORMAL.getCode());
        return list(wrapper);
    }

    @Override
    public Boolean updateStatus(String componentAppId, String authorizerAppId, Integer status) {
        LambdaUpdateWrapper<WxOpenAuthorizer> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(WxOpenAuthorizer::getStatus, status)
                .eq(WxOpenAuthorizer::getComponentAppId, componentAppId)
                .eq(WxOpenAuthorizer::getAuthorizerAppId, authorizerAppId);
        return update(wrapper);
    }

    @Override
    public Boolean revokeAuth(String componentAppId, String authorizerAppId) {
        LambdaUpdateWrapper<WxOpenAuthorizer> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(WxOpenAuthorizer::getStatus, 2)
                .set(WxOpenAuthorizer::getFuncInfo, null)
                .eq(WxOpenAuthorizer::getComponentAppId, componentAppId)
                .eq(WxOpenAuthorizer::getAuthorizerAppId, authorizerAppId);
        return update(wrapper);
    }

    @Override
    public String gotoPreAuthUrl() {
        String url = sportServerPrefix + "/api/v1/open/auth/jump?userId=" + OauthUtils.getCurrentUserId();
        CommonResponse<Map<String, String>> stringCommonResponse = feignWxOpenAuthClient.gotoPreAuthUrl(url);
        return stringCommonResponse.getData().get("url");
    }

    @Override
    public Boolean saveAuthorization(String authCode, String userId) {
        CommonResponse<WxOpenAuthorizationInfoVo> response = feignWxOpenAuthClient.getAuthInfo(authCode);
        WxOpenAuthorizationInfoVo data = response.getData();
        WxOpenAuthorizer entity = new WxOpenAuthorizer();
        BeanUtils.copyProperties(data, entity);

        CommonResponse<WxOpenAuthorizerInfoResultVo> authorizerInfoResponse = feignWxOpenAuthClient.getAuthorizerInfo(data.getAuthorizerAppid());
        WxOpenAuthorizerInfoResultVo authorizerInfo = authorizerInfoResponse.getData();
        entity.setFuncInfo(Joiner.on(",").join(data.getFuncInfo()));
        entity.setNickName(authorizerInfo.getAuthorizerInfo().getNickName());
        entity.setHeadImg(authorizerInfo.getAuthorizerInfo().getHeadImg());
        entity.setAuthorizerAppId(data.getAuthorizerAppid());
        entity.setPrincipalName(authorizerInfo.getAuthorizerInfo().getPrincipalName());
        entity.setSignature(authorizerInfo.getAuthorizerInfo().getSignature());
        entity.setAuthorizerRefreshDate(new Date());
        entity.setAuthorizerDate(new Date());
        entity.setAuthorizerUserId(userId);

        WxOpenAuthorizer one = getOne(entity.getComponentAppId(), entity.getAuthorizerAppId());
        if (one == null){
            return this.save(entity);
        }else {
            entity.setId(one.getId());
            entity.setStatus(1);
            return this.updateById(entity);
        }
    }

    @Autowired
    private WxOpenCodeCommitService codeCommitService;

    @Override
    public Boolean codeCommit(String openAppIds, Long templateId, String userVersion, String userDesc, Object extJsonObject) {
        String[] openAppIdArr = openAppIds.split(",");
        List<WxOpenCodeCommit> list = new ArrayList<>();
        for (String openAppId : openAppIdArr) {
            JSONObject json = new JSONObject();
            json.put("extAppid", openAppId);
//        json.put("ext",new JSONObject());
//        json.put("extPages",new JSONObject());
//        json.put("window",new JSONObject());
//        json.put("tabBar",new JSONObject());
            CommonResponse<Boolean> response = feignWxOpenAuthClient.codeCommit(openAppId, templateId, userVersion, userDesc, json);
            if (response.isSuccess() && response.getData()){
                WxOpenCodeCommit codeCommit = new WxOpenCodeCommit();
                codeCommit.setAppId(openAppId);
                codeCommit.setExtJson(JSONObject.toJSONString(json));
                codeCommit.setDescribe(userDesc);
                codeCommit.setTemplateId(templateId);
                codeCommit.setVersion(userVersion);
                codeCommit.setCommitStatus(1);
                codeCommit.setCreateUserId(OauthUtils.getCurrentUserId());
                codeCommit.setCreateUserName(OauthUtils.getCurrentUsername());
                list.add(codeCommit);
//                codeCommitService.save(codeCommit);
            }
        }
        codeCommitService.saveBatch(list);
         return true;
    }

    @Override
    public WxOpenMaCodeTemplateVo getLatestTemplate(Integer templateType) {
        List<WxOpenMaCodeTemplateVo> templateList = getTemplateList(templateType);
        if (CollectionUtils.isEmpty(templateList)){
            return null;
        }

        return templateList.get(templateList.size()-1);
    }

    @Override
    public List<WxOpenMaCodeTemplateVo> getTemplateList(Integer templateType) {
        CommonResponse<List<WxOpenMaCodeTemplateVo>> templateList = feignWxOpenAuthClient.getTemplateList(templateType);
        List<WxOpenMaCodeTemplateVo> data = templateList.getData();
        if (CollectionUtils.isEmpty(data)){
            return null;
        }
        return data;
    }

    @Override
    public Boolean submitAudit(String appId, String versionDesc) {
        WxOpenCodeCommit latestCodeCommit = codeCommitService.getLatestCodeCommit(appId);
        if (latestCodeCommit == null){
            throw new SportException("请先上传代码");
        }
        WxOpenMaSubmitAuditMessageForm form = new WxOpenMaSubmitAuditMessageForm();
        form.setVersionDesc(versionDesc);
        form.setPrivacyApiNotUse(true);
        CommonResponse<Long> response = feignWxOpenAuthClient.submitAudit(appId, form);
        if (response.isSuccess()) {
            //先把其他审核的信息删了，在新增审核记录
            submitAuditService.updateStatusByAppId(appId,DataStatus.DELETE.getCode());

            WxOpenSubmitAudit submitAudit = new WxOpenSubmitAudit();
            submitAudit.setAppId(appId);
            submitAudit.setCodeCommitId(latestCodeCommit.getId());
            submitAudit.setAuditId(response.getData());
            submitAudit.setAuditStatus(WxOpenSubmitAuditStatus.weapp_audit.getCode());
            submitAudit.setDescribe(versionDesc);
            submitAudit.setPrivacyApiNotUse(form.getPrivacyApiNotUse() ? 1 : 0);
            submitAuditService.save(submitAudit);
            return true;
        }
        throw new SportException(response.getMessage());
    }

    @Override
    public Boolean versionPublish(String appId) {
        WxOpenSubmitAudit submitAudit = submitAuditService.getLatestSubmitAudit(appId);
        if (submitAudit == null){
            throw new SportException("请先提交审核");
        }
        CommonResponse<Boolean> release = feignWxOpenAuthClient.release(appId);
        if (release.isSuccess()) {
            // 删除历史的版本提交信息
            onlineVersionService.deleteByAppId(appId, DataStatus.DELETE.getCode());
            // 保存本次 在线版本信息
            WxOpenOnlineVersion onlineVersion = new WxOpenOnlineVersion();
            onlineVersion.setAppId(appId);
            onlineVersion.setCodeCommitId(submitAudit.getCodeCommitId());
            onlineVersion.setSubmitAuditId(submitAudit.getId());
            onlineVersionService.save(onlineVersion);
            // 删除历史的审核信息
            submitAuditService.updateStatusByAppId(appId, DataStatus.DELETE.getCode());
            return release.getData();
        }
        return false;
    }

    @Override
    public Boolean refreshAuthToken(String componentAppId,String appId) {
        LambdaQueryWrapper<WxOpenAuthorizer> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(componentAppId)){
            wrapper.eq(WxOpenAuthorizer::getComponentAppId, componentAppId);
        }
        if (StringUtils.isNotBlank(appId)){
            wrapper.eq(WxOpenAuthorizer::getAuthorizerAppId, appId);
        }
        wrapper.eq(WxOpenAuthorizer::getStatus, DataStatus.NORMAL.getCode());
        List<WxOpenAuthorizer> authList = list(wrapper);
        if (CollectionUtils.isEmpty(authList)){
            return true;
        }
        for (WxOpenAuthorizer authorizer: authList){
            try {
                //表示还有10分钟内过期，需要刷新
                if ((new Date().getTime()-authorizer.getAuthorizerRefreshDate().getTime())/1000 >= (authorizer.getExpiresIn() - 600)) {
                    CommonResponse<WxOpenAuthorizerAccessTokenVO> response = feignWxOpenAuthClient.refreshAuthorizerAccessToken(authorizer.getAuthorizerAppId());
                    if (response.isSuccess()) {
                        WxOpenAuthorizerAccessTokenVO data = response.getData();
                        authorizer.setAuthorizerRefreshDate(data.getAuthorizerRefreshDate());
                        authorizer.setAuthorizerRefreshToken(data.getAuthorizerRefreshToken());
                        updateById(authorizer);
                    }
                }
            }catch (Exception e){
                log.error("appid={} refreshAuthToken error:{}",authorizer.getAuthorizerAppId(), e.getMessage());
            }
        }
        return true;
    }

    @Override
    public Boolean domainConfig(String appId) {
        WxOpenAuthorizer wxOpenAuthorizer = getOne(QU_SHU_PLATFORM_APP_ID, appId);
        if (wxOpenAuthorizer.getDomainConfig()) {
            throw new SportException("域名已配置");
        }
        CommonResponse<Boolean> response = feignWxOpenAuthClient.domainConfig(appId);
        if (response.isSuccess() && response.getData()) {
            WxOpenAuthorizer wa = new WxOpenAuthorizer();
            wa.setId(wxOpenAuthorizer.getId());
            wa.setDomainConfig(true);
            return updateById(wa);
        }
        throw new SportException(response.getMessage());
    }

    @Override
    public Boolean webviewConfig(String appId) {
        WxOpenAuthorizer wxOpenAuthorizer = getOne(QU_SHU_PLATFORM_APP_ID, appId);
        if (wxOpenAuthorizer.getWebviewConfig()) {
            throw new SportException("webview域名已配置");
        }
        CommonResponse<Map<String, String>> response = feignWxOpenAuthClient.webViewDomainConfig(appId);
        if (response.isSuccess()) {
            WxOpenAuthorizer wa = new WxOpenAuthorizer();
            wa.setId(wxOpenAuthorizer.getId());
            wa.setWebviewConfig(true);
            return updateById(wa);
        }
        throw new SportException(response.getMessage());
    }


    @Override
    public Boolean revert(String appId) {
        CommonResponse<Boolean> commonResponse = feignWxOpenAuthClient.revertRelease(appId);
        if (commonResponse.isSuccess()) {
            // 删除历史的在线版本信息
            onlineVersionService.deleteByAppId(appId, DataStatus.DELETE.getCode());
            // 默认回滚到上一个版本 获取历史提交的版本
            WxOpenCodeCommit backCommit = codeCommitService.getBackCommit(appId);

            // 保存本次 在线版本信息
            WxOpenOnlineVersion onlineVersion = new WxOpenOnlineVersion();
            onlineVersion.setAppId(appId);
            if (backCommit != null) {
                onlineVersion.setCodeCommitId(backCommit.getId());
            } else {
                onlineVersion.setCodeCommitId(0L);
            }
            onlineVersion.setSubmitAuditId(0L);
            onlineVersionService.save(onlineVersion);

            return commonResponse.getData();
        }
        return false;
    }

    @Override
    public Boolean privacySetting(String appId) {
        WxOpenAuthorizer wxOpenAuthorizer = getOne(QU_SHU_PLATFORM_APP_ID, appId);
        if (wxOpenAuthorizer.getPrivacySetting()) {
            throw new SportException("隐私设置已配置");
        }
        WxSetPrivacySettingForm settingForm = new WxSetPrivacySettingForm();
        settingForm.setPrivacyVer(2);
        settingForm.setContactPhone("17621190028");
        settingForm.setNoticeMethod("小程序弹窗");
        Map<String,String> setting = new HashMap<>();
        setting.put("UserInfo", "用户注册完成后展示用户基本信息");
        setting.put("Location", "店铺列表定位获取最近的店铺");
        setting.put("PhoneNumber", "获取手机号码快速登录");
        settingForm.setSetting(setting);
        CommonResponse<Boolean> commonResponse = feignWxOpenAuthClient.privacySetting(appId, settingForm);
        if (commonResponse.isSuccess()) {
            WxOpenAuthorizer wa = new WxOpenAuthorizer();
            wa.setId(wxOpenAuthorizer.getId());
            wa.setPrivacySetting(true);
            return updateById(wa);
        }
        return false;
    }
}
