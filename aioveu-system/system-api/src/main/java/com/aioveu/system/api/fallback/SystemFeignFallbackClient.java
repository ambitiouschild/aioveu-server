package com.aioveu.system.api.fallback;

import com.aioveu.common.result.Result;
import com.aioveu.system.api.SystemFeignClient;  // 确保导入正确的接口
import com.aioveu.system.dto.UserAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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
    public UserAuthInfo getUserAuthInfo(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法- getUserAuthInfo, username: {}", username);
        return new UserAuthInfo();  // 返回默认值
    }




}
