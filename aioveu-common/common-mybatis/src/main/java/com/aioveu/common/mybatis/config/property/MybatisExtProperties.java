package com.aioveu.common.mybatis.config.property;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName: MybatisExtProperties
 * @Description TODO  多租户配置属性
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/13 20:35
 * @Version 1.0
 **/

@Data
@ConfigurationProperties(prefix = "mybatis")
public class MybatisExtProperties {

    /**
     * 是否启用多租户插件
     */
    private boolean tenantEnabled = false;

    /**
     * 是否启用数据权限插件
     */
    private boolean dataPermissionEnabled = false;

    /**
     * 数据库类型
     */
    private String dbType = "mysql";
}
