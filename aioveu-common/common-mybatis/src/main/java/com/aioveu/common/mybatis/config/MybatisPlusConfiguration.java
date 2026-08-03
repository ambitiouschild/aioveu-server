package com.aioveu.common.mybatis.config;

import com.aioveu.common.mybatis.config.property.MybatisExtProperties;
import com.aioveu.common.mybatis.config.property.TenantMybatisProperties;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
/*
* 👉 只依赖接口
👉 resource 实现接口
👉 Spring 负责组装
✅ 这是 IoC 的核心思想
* */
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.aioveu.common.mybatis.handler.*;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;
import java.util.Properties;

/**
 * @Description: TODO mybatis-plus 配置类
 * @Author: 雒世松
 * @Date: 2025/6/5 15:48
 * @param
 * @return:
 **/

@Slf4j
@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
@EnableConfigurationProperties({
        TenantMybatisProperties.class,
        MybatisExtProperties.class
})
public class MybatisPlusConfiguration {

    private final TenantMybatisProperties tenantMybatisProperties;
    private final MybatisExtProperties mybatisProperties;


    @PostConstruct
    public void init() {
        log.info("=== MybatisPlusConfig 初始化检查 ===");
        log.info("tenantEnabled: {}", mybatisProperties.isTenantEnabled());
        log.info("dataPermissionEnabled: {}", mybatisProperties.isDataPermissionEnabled());
        log.info("dbType: {}", mybatisProperties.getDbType());
    }

    /*
    *   ✅ 启动顺序可控
        ✅ 自动装配语义清晰
        ✅ 不会被“误扫描”
    *
    * */
    @Bean
    @ConditionalOnMissingBean
    public TenantLineHandler tenantLineHandler() {
        log.info("=== MyTenantLineHandler 注册 ===");
        return new MyTenantLineHandler(tenantMybatisProperties);
    }


    /**
     * 分页插件和数据权限插件
     * <p>
     * 如果启用了多租户，则添加多租户插件（必须在最前面）
     * </p>
     */
    //这里会获取资源服务器里的MyDataPermissionHandler  看到 Optional<T>= 安全
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(
            Optional<TenantLineHandler> tenantLineHandler,
            Optional<DataPermissionHandler> dataPermissionHandler

    ) {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // ✅✅✅ 1. 乐观锁插件 —— 必须在最前面！ （永远启用）
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());


        // 2. 多租户插件（强制启用，必须在最前面）
        // 多租户插件（非auth服务才启用）
        if (mybatisProperties.isTenantEnabled() && tenantLineHandler.isPresent()) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantLineHandler.get()));
            log.info("【MybatisPlus】TenantLineInnerInterceptor enabled,已启用多租户插件");
        }


        // 3. 数据权限（配置驱动 + SPI）
        // 数据权限（非auth服务才启用，如果auth服务也需要可以保留）
        if (mybatisProperties.isDataPermissionEnabled() && dataPermissionHandler.isPresent()) {
            interceptor.addInnerInterceptor(new DataPermissionInterceptor(dataPermissionHandler.get()));
            log.info("【MybatisPlus】DataPermissionInterceptor enabled,已启用数据权限插件");
        }

        // 分页插件，根据配置动态选择数据库类型 （永远最后）
        DbType dbType = resolveDbType(mybatisProperties.getDbType());
        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(dbType));

        return interceptor;
    }

    private DbType resolveDbType(String dbType) {
        if ("postgresql".equalsIgnoreCase(dbType)
                || "postgres".equalsIgnoreCase(dbType)) {
            return DbType.POSTGRE_SQL;
        }
        return DbType.MYSQL;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            // 全局注册自定义TypeHandler
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            typeHandlerRegistry.register(String[].class, JdbcType.OTHER, StringArrayJsonTypeHandler.class);
            typeHandlerRegistry.register(Long[].class, JdbcType.OTHER, LongArrayJsonTypeHandler.class);
            typeHandlerRegistry.register(Integer[].class, JdbcType.OTHER, IntegerArrayJsonTypeHandler.class);
        };
    }

    /**
     * 自动填充数据库创建人、创建时间、更新人、更新时间
     */
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new MyMetaObjectHandler());
        return globalConfig;
    }

    /**
     * 数据库类型自动识别
     */
    @Bean
    public DatabaseIdProvider databaseIdProvider() {
        DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("MySQL", "mysql");
        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }

}
