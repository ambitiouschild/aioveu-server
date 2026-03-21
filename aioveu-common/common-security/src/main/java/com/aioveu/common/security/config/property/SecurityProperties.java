package com.aioveu.common.security.config.property;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: SecurityProperties
 * @Description TODO  专门处理属性绑定
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/21 15:38
 * @Version 1.0
 **/
@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {

    /**
     * 白名单路径列表 - 从配置文件动态注入
     * 配置示例：
     * security:
     *   whitelist-paths:
     *     - "/api/v1/public/**"
     *     - "/health"
     *     - "/actuator/info"
     */
//    @Setter
    private List<String> whitelistPaths;
}
