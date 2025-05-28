package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.exception.SportException;
import com.aioveu.feign.vo.WxOpenMaCodeTemplateVo;
import com.aioveu.service.WxOpenAuthorizerService;
import com.aioveu.vo.WxOpenAuthorizerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author： yao
 * @Date： 2025/2/9 10:52
 * @Describe：
 */
@Slf4j
@RequestMapping("/api/v1/open")
@RestController
public class WxOpenAuthorizerController {

    @Autowired
    private WxOpenAuthorizerService wxOpenAuthorizerService;

    @Value(value = "${sport.web-manager.prefix}")
    private String sportWebManagerPrefix;

    @GetMapping("/getMyByPage")
    public IPage<WxOpenAuthorizerVo> getMyByPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(required = false, defaultValue = "10") Integer size){
        return wxOpenAuthorizerService.getAllByPage(page, size, OauthUtils.getCurrentUserId());
    }
    @GetMapping("/getListByPage")
    public IPage<WxOpenAuthorizerVo> getListByPage(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(required = false, defaultValue = "10") Integer size){
        return wxOpenAuthorizerService.getAllByPage(page, size,null);
    }

    @GetMapping("/auth/pre-auth-url")
    public String gotoPreAuthUrl(){
        return wxOpenAuthorizerService.gotoPreAuthUrl();
    }

    @GetMapping("/auth/jump")
    public void jump(@RequestParam("auth_code") String authorizationCode, @RequestParam("userId") String userId, HttpServletResponse response) throws IOException {
        // http://frp.highyundong.com/sp/api/api/v1/open/auth/jump?userId=w1fade6af1535617cbf10d27b1c87d3t&auth_code=queryauthcode@@@lU8btlCzxuYg5y-793oB03wkK56agQzTdnShysCwGnGMoEcWdBOCNmKwPifm8zVqiyW3_C0Ms4vMxvps-06ytQ&expires_in=3600
        String redirectUrl;
        if (wxOpenAuthorizerService.saveAuthorization(authorizationCode, userId)) {
            redirectUrl =  sportWebManagerPrefix + "/#/wechat/my-mini-app?result=success";
        } else {
            redirectUrl =  sportWebManagerPrefix + "/#/wechat/my-mini-app?result=fail";
        }
        // 设置重定向的 URL
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/latest-template")
    public WxOpenMaCodeTemplateVo getLatestTemplate(@RequestParam(required = false, defaultValue = "0") Integer templateType){
        try {
            return wxOpenAuthorizerService.getLatestTemplate(templateType);
        } catch (Exception e) {
            log.error("getTemplateList:{}", e);
            throw new SportException("getTemplateList error"+e.getMessage());
        }
    }

    @GetMapping("/template-list")
    public List<WxOpenMaCodeTemplateVo> getTemplateList(@RequestParam(required = false, defaultValue = "0") Integer templateType){
        try {
            return wxOpenAuthorizerService.getTemplateList(templateType);
        } catch (Exception e) {
            log.error("getTemplateList:{}", e);
            throw new SportException("getTemplateList error"+e.getMessage());
        }
    }

    @PutMapping("/code-commit")
    public Boolean codeCommit(@RequestParam String openAppIds,
                           @RequestParam(required = false) Long templateId,
                           @RequestParam String userVersion,
                           @RequestParam String userDesc,
                           @RequestParam Object extJsonObject){
        return wxOpenAuthorizerService.codeCommit(openAppIds,templateId,userVersion,userDesc,extJsonObject);
    }

    @PutMapping("/{appId}/submit-audit")
    public Boolean submitAudit(@PathVariable String appId,
                               @RequestParam String versionDesc){
        return wxOpenAuthorizerService.submitAudit(appId, versionDesc);
    }

    /**
     * 发版
     * @param appId
     * @return
     */
    @PutMapping("/{appId}/release")
    public Boolean release(@PathVariable String appId){
        return wxOpenAuthorizerService.versionPublish(appId);
    }

    @PutMapping("/domain-config/{appId}")
    public Boolean domainConfig(@PathVariable String appId){
        return wxOpenAuthorizerService.domainConfig(appId);
    }

    @PutMapping("/webview-config/{appId}")
    public Boolean webviewConfig(@PathVariable String appId){
        return wxOpenAuthorizerService.webviewConfig(appId);
    }

    /**
     * 代码回退
     * @param appId
     * @return
     */
    @GetMapping("/{appId}/revert")
    public Boolean revert(@PathVariable String appId){
        return wxOpenAuthorizerService.revert(appId);
    }

    @PostMapping("/{appId}/privacy-setting")
    public Boolean privacySetting(@PathVariable String appId){
        return wxOpenAuthorizerService.privacySetting(appId);
    }


}
