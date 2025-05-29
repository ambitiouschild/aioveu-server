package com.aioveu.gateway.model;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author 雒世松
 */
@ConfigurationProperties(prefix = "oauth2.cloud.sys.parameter")
@Data
public class SysParameterConfig {
    /**
     * 白名单
     */
    private List<String> ignoreUrls;
}
