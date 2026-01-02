package com.aioveu.lss.api;

import com.aioveu.lss.api.fallback.LssFeignFallbackClient;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.lss.api.dto.UserAuthCredentials;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @ClassName: UserFeignClient
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 21:52
 * @Version 1.0
 **/

@FeignClient(value = "aioveu-lss",
        fallback = LssFeignFallbackClient.class,
        configuration = {FeignDecoderConfig.class})
public interface LssFeignClient {

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link UserAuthCredentials}
     */
    @GetMapping("/api/v1/users/{username}/UserAuthCredentials")
    UserAuthCredentials getAuthCredentialsByUsername(@PathVariable  String username);
}
