package com.aioveu.auth.oauth2.oidc;

import com.aioveu.system.api.UserFeignClient;
import com.aioveu.system.dto.UserAuthInfo;
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

    private final UserFeignClient userFeignClient;

    public CustomOidcUserInfoService(UserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    public CustomOidcUserInfo loadUserByUsername(String username) {
        UserAuthInfo userAuthInfo = null;
        try {
            userAuthInfo = userFeignClient.getUserAuthInfo(username);
            if (userAuthInfo == null) {
                return null;
            }
            return new CustomOidcUserInfo(createUser(userAuthInfo));
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return null;
        }
    }

    private Map<String, Object> createUser(UserAuthInfo userAuthInfo) {
        return CustomOidcUserInfo.customBuilder()
                .username(userAuthInfo.getUsername())
                .nickname(userAuthInfo.getNickname())
                .status(userAuthInfo.getStatus())
                .phoneNumber(userAuthInfo.getMobile())
                .email(userAuthInfo.getEmail())
                .profile(userAuthInfo.getAvatar())
                .build()
                .getClaims();
    }

}
