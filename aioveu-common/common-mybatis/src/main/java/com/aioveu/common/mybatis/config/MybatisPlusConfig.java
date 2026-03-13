package com.aioveu.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.aioveu.common.mybatis.handler.*;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

/**
 * @Description: TODO mybatis-plus 配置类
 * @Author: 雒世松
 * @Date: 2025/6/5 15:48
 * @param
 * @return:
 **/

@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    @Value("${app.db-type:mysql}")
    private String dbType;

    @Autowired(required = false)
    private MyTenantLineHandler myTenantLineHandler;

    /**
     * 分页插件和数据权限插件
     * <p>
     * 如果启用了多租户，则添加多租户插件（必须在最前面）
     * </p>
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 多租户插件（强制启用，必须在最前面）
        if (myTenantLineHandler != null) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(myTenantLineHandler));
        }

        //数据权限
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(new MyDataPermissionHandler()));

        // 分页插件，根据配置动态选择数据库类型
        DbType mpDbType = DbType.MYSQL;
        String type = dbType == null ? "mysql" : dbType.toLowerCase();
        if ("postgres".equals(type) || "postgresql".equals(type)) {
            mpDbType = DbType.POSTGRE_SQL;
        }

        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(mpDbType));

        return interceptor;
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
