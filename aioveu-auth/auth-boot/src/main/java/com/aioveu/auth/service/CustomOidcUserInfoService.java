package com.aioveu.auth.service;

import com.aioveu.auth.oauth2.oidc.CustomOidcUserInfo;
import com.aioveu.common.core.result.Result;
import com.aioveu.common.security.core.model.dto.UserAuthCredentials;
import com.aioveu.system.api.SystemFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: TODO 自定义 OIDC 用户信息服务
 * @Author: 雒世松
 * @Date: 2025/6/5 17:51
 * @param
 * @return:
 **/

@Service
@Slf4j
public class CustomOidcUserInfoService {

    private final SystemFeignClient systemFeignClient;

    public CustomOidcUserInfoService(SystemFeignClient systemFeignClient) {
        this.systemFeignClient = systemFeignClient;
    }

    public CustomOidcUserInfo loadUserByUsername(String username) {
        UserAuthCredentials userAuthCredentials = null;
        try {
            userAuthCredentials = extractData(systemFeignClient.getUserAuthInfo(username));
            if (userAuthCredentials == null) {
                return null;
            }
            return new CustomOidcUserInfo(createUser(userAuthCredentials));
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return null;
        }
    }

    private Map<String, Object> createUser(UserAuthCredentials userAuthCredentials) {
        return CustomOidcUserInfo.customBuilder()
                .username(userAuthCredentials.getUsername())
                .nickname(userAuthCredentials.getNickname())
                .status(userAuthCredentials.getStatus())
//                .phoneNumber(userAuthCredentials.getMobile())
//                .email(userAuthCredentials.getEmail())
//                .profile(userAuthCredentials.getAvatar())
                .build()
                .getClaims();
    }


    /**
     * 安全地从 Result<T> 中提取数据
     */
    private <T> T extractData(Result<T> result) {
        if (result == null || !result.isSuccess(result) || result.getData() == null) {
            return null;
        }
        return result.getData();
    }

}
