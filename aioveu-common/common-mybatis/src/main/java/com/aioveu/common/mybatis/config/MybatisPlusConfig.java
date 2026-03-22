package com.aioveu.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.aioveu.common.mybatis.handler.*;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    /*
    * 配置了 @Value("${app.db-type:mysql}")，但 Spring 在启动时会初始化这个配置类，
    * 需要从配置文件中读取 app.db-type属性。如果配置文件中没有这个属性，
    * 可能会导致配置类初始化失败。
    * */
//    @Value("${app.db-type:mysql}")
    private String dbType;

    @Autowired  //(required = false)
    private MyTenantLineHandler myTenantLineHandler;

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @PostConstruct
    public void init() {
        log.info("=== MybatisPlusConfig 初始化检查 ===");
        log.info("当前服务名称: {}", applicationName);
        log.info("MyTenantLineHandler: {}", myTenantLineHandler);

        // 检查是否是auth服务
        boolean isAuthService = isAuthService();
        log.info("当前是否为auth服务: {}", isAuthService);
    }

    /**
     * 判断当前是否为auth微服务
     */
    private boolean isAuthService() {
        // 方法1：通过应用名判断（推荐）
        if (applicationName != null && (applicationName.contains("auth") || applicationName.contains("auth-service"))) {
            return true;
        }

        // 方法2：通过检查启动类判断
        try {
            String mainClassName = getMainClassName();
            if (mainClassName != null && mainClassName.toLowerCase().contains("auth")) {
                return true;
            }
        } catch (Exception e) {
            // 忽略异常
        }

        return false;
    }

    /**
     * 获取主启动类名
     */
    private String getMainClassName() {
        try {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            for (StackTraceElement element : stackTrace) {
                String className = element.getClassName();
                if (className.contains("Application") && !className.startsWith("org.springframework") && !className.contains("$")) {
                    return className;
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }
        return null;
    }

    /**
     * 分页插件和数据权限插件
     * <p>
     * 如果启用了多租户，则添加多租户插件（必须在最前面）
     * </p>
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {

        log.info("【mybatis-plus 配置类】开始工作！==================");

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        boolean isAuthService = isAuthService();


        // 多租户插件（强制启用，必须在最前面）// 多租户插件（非auth服务才启用）
        if (myTenantLineHandler != null && !isAuthService) {
            interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(myTenantLineHandler));
            log.info("已启用多租户插件（当前非auth服务）");
        }else if (isAuthService) {
            log.info("当前为auth服务，跳过多租户插件");
        } else {
            log.info("未找到MyTenantLineHandler，跳过多租户插件");
        }
        log.info("【MyTenantLineHandler】也在处理租户过滤！");


        //数据权限
        // 数据权限（非auth服务才启用，如果auth服务也需要可以保留）
        if (!isAuthService) {
            interceptor.addInnerInterceptor(new DataPermissionInterceptor(new MyDataPermissionHandler()));
            log.info("已启用数据权限插件（当前非auth服务）");
        } else {
            log.info("当前为auth服务，跳过数据权限插件");
        }

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
