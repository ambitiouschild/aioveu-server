package com.aioveu.system.api;

import com.aioveu.common.core.result.Result;
import com.aioveu.common.security.core.model.dto.UserAuthCredentials;
import com.aioveu.feign.config.FeignDecoderConfig;
import com.aioveu.system.api.fallback.SystemFeignFallbackClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "aioveu-system",
        fallback = SystemFeignFallbackClient.class,
        configuration = {FeignDecoderConfig.class})
public interface SystemFeignClient {



    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link UserAuthCredentials}
     */
    @GetMapping("/api/v1/users/{username}/authInfo")
    Result<UserAuthCredentials> getUserAuthInfo(@PathVariable("username") String username);





}
