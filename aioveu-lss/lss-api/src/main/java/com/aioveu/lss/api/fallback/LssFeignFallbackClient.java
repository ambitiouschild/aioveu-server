package com.aioveu.lss.api.fallback;

import com.aioveu.lss.api.LssFeignClient;
import com.aioveu.lss.api.dto.UserAuthCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName: LssFeignFallbackClient
 * @Description TODO  系统用户服务远程调用异常后的降级处理类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 21:53
 * @Version 1.0
 **/

@Component
@Slf4j
public class LssFeignFallbackClient implements LssFeignClient {

    /**
     * 根据用户名获取认证信息
     *
     * @param username 用户名
     * @return {@link UserAuthCredentials}
     */
    @Override
    public UserAuthCredentials getAuthCredentialsByUsername(String username) {
        log.error("feign远程调用系统用户服务异常后的降级方法");
        return new UserAuthCredentials();
    }

}
