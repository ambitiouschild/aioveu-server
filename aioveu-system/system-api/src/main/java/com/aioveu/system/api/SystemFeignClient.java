package com.aioveu.system.api;

import com.aioveu.common.result.Result;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.system.api.fallback.SystemFeignFallbackClient;
import com.aioveu.system.dto.UserAuthInfo;

import io.swagger.v3.oas.annotations.Operation;
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
     * @return {@link UserAuthInfo}
     */
    @GetMapping("/api/v1/users/{username}/authInfo")
    UserAuthInfo getUserAuthInfo(@PathVariable("username") String username);





}
