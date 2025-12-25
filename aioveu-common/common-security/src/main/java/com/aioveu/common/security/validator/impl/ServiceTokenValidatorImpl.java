package com.aioveu.common.security.validator.impl;

import com.aioveu.common.security.validator.ServiceTokenValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: ServiceTokenValidatorImpl
 * @Description TODO    服务令牌验证器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/21 23:07
 * @Version 1.0
 **/

@Slf4j
@Component
public class ServiceTokenValidatorImpl implements ServiceTokenValidator {

    // 从配置文件中读取有效的服务令牌
    @Value("${service.auth.tokens:aioveu-internal-token-2025,aioveu-auth-token,aioveu-system-token}")
    private String configuredTokens;

    @Value("${service.auth.enabled:true}")
    private boolean enabled;

    // 缓存的令牌列表
    private List<String> validTokens;

    @Override
    public boolean validate(String token) {
        // 如果禁用验证，直接返回true
        if (!enabled) {
            log.debug("服务令牌验证已禁用，跳过验证");
            return true;
        }

        // 检查令牌是否为空
        if (token == null || token.trim().isEmpty()) {
            log.warn("服务令牌为空");
            return false;
        }

        String trimmedToken = token.trim();

        // 获取有效令牌列表
        List<String> tokens = getValidTokens();

        // 验证令牌
        boolean isValid = tokens.contains(trimmedToken);

        if (isValid) {
            log.debug("服务令牌验证成功");
        } else {
            log.warn("服务令牌验证失败，有效令牌: {}", String.join(", ", tokens));
        }

        return isValid;
    }

    @Override
    public ValidationResult validateWithDetails(String token) {
        boolean isValid = validate(token);
        String message = isValid ? "服务令牌验证成功" : "无效的服务令牌";
        String serviceName = detectServiceName(token);

        return new ValidationResult(isValid, message, serviceName);
    }

    /**
     * 获取有效的令牌列表
     */
    private List<String> getValidTokens() {
        if (validTokens == null) {
            // 解析配置的令牌
            validTokens = Arrays.asList(configuredTokens.split(","));
            log.info("加载有效服务令牌: {}", validTokens);
        }
        return validTokens;
    }

    /**
     * 根据令牌检测服务名称
     */
    private String detectServiceName(String token) {
        if (token == null) return "unknown";

        if (token.contains("auth")) return "auth-service";
        if (token.contains("system")) return "system-service";
        if (token.contains("internal")) return "internal-service";
        if (token.contains("admin")) return "admin-service";

        return "unknown-service";
    }
}
