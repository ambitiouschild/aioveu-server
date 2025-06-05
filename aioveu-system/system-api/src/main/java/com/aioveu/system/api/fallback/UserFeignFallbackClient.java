package com.aioveu.system.api.fallback;

import com.aioveu.system.api.UserFeignClient;
import com.aioveu.system.dto.UserAuthInfo;
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
public class UserFeignFallbackClient implements UserFeignClient {

    @Override
    public UserAuthInfo getUserAuthInfo(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return new UserAuthInfo();
    }
}
