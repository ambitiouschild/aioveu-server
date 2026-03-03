package com.aioveu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

//@SpringBootApplication已经包含了 @ComponentScan，不需要重复声明。
@SpringBootApplication
@EnableDiscoveryClient
// 确保扫描到 FilePathGenerator 所在的包
//原因：IDEA 需要在控制台输出中检测到端口启动日志，才会自动在运行配置名称后添加端口号。
public class aioveu_LssApplication {
    public static void main(String[] args) {


        System.out.println("Hello, World!");


        // 创建应用
        SpringApplication app = new SpringApplication(aioveu_LssApplication.class);

        // 添加初始化器以查看配置（仅在应用启动前执行）
        app.addInitializers((ApplicationContextInitializer<ConfigurableApplicationContext>) context -> {
            ConfigurableEnvironment env = context.getEnvironment();

            // 打印当前配置
            System.out.println("\n=== 调试信息（应用启动前）===");
            System.out.println("Nacos Config Server Addr: " +
                    env.getProperty("spring.cloud.nacos.config.server-addr"));
            System.out.println("Nacos Discovery Server Addr: " +
                    env.getProperty("spring.cloud.nacos.discovery.server-addr"));
            System.out.println("Config Import: " +
                    env.getProperty("spring.config.import"));
            System.out.println("Application Name: " +
                    env.getProperty("spring.application.name"));

            // 查看所有配置源
            System.out.println("\n配置源列表:");
            for (PropertySource<?> source : env.getPropertySources()) {
                System.out.println("  - " + source.getName());
            }
        });

        // 运行应用（只运行一次！）
        app.run(args);

    }
}