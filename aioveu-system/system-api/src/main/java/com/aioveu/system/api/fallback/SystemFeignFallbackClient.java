package com.aioveu.system.api.fallback;

import com.aioveu.common.core.result.Result;
import com.aioveu.common.security.core.model.dto.UserAuthCredentials;
import com.aioveu.system.api.SystemFeignClient;  // 确保导入正确的接口

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO 系统用户服务远程调用异常后的降级处理类
 * @Author: 雒世松
 * @Date: 2025/6/5 16:45
 * @param
 * @return:
 **/

@Component
@Slf4j
public class SystemFeignFallbackClient implements SystemFeignClient { // 必须实现 UserFeignClient


    @Override
    public Result<UserAuthCredentials>  getUserAuthInfo(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法- getUserAuthInfo, username: {}", username);
        return Result.failed("根据用户名获取认证信息失败");
    }




}
