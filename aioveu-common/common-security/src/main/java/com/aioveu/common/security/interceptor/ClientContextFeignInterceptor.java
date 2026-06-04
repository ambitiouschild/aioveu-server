package com.aioveu.common.security.interceptor;

import com.aioveu.common.tenant.ClientContextHolder;
import feign.RequestInterceptor;
/**
 * @ClassName: ClientContextFeignInterceptor
 * @Description TODO Feign 拦截器（微服务透传 ✅）
 *                      即使你现在不用，将来一定用得上
 *                      ✅ 订单 → 库存 → 支付
 *                      ✅ 链路不断
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 22:42
 * @Version 1.0
 **/

//只定义
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign 请求拦截器（透传 clientId）
 */
@Slf4j
public class ClientContextFeignInterceptor implements RequestInterceptor {

    private static final String HEADER_CLIENT_ID = "X-Client-Id";

    @Override
    public void apply(RequestTemplate template) {

        String clientId = ClientContextHolder.getClientId();
        if (clientId != null) {
            template.header(HEADER_CLIENT_ID, clientId);
            log.debug("Feign 透传 clientId = {}", clientId);
        }
    }
}
