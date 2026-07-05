package com.aioveu.auth.api.fallback;

import com.aioveu.auth.api.AuthFeignClient;
import com.aioveu.auth.model.TenantClientInitDTO;
import com.aioveu.common.annotation.Log;
import com.aioveu.common.enums.LogModuleEnum;
import com.aioveu.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @ClassName: TenantFeignFallbackClient
 * @Description TODO 多租户服务远程调用异常后的降级处理类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 20:47
 * @Version 1.0
 **/
@Component
@Slf4j
public class AuthFeignFallbackClient implements FallbackFactory<AuthFeignClient> {


    @Override
    public AuthFeignClient create(Throwable cause) {

        return new AuthFeignClient() {

            @Override
            public Result<Boolean> initClientByTenant(@RequestBody TenantClientInitDTO dto) {
                log.error("Feign fallback: initClientByTenant, dto={}", dto, cause);
                log.error("根据租户名初始化客户端");
                return Result.failed("根据租户名初始化客户端失败"); // ✅ 绝不能 return null
            }




        };
    }

}
