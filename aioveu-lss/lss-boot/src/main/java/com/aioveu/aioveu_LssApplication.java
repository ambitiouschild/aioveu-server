package com.aioveu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;


@SpringBootApplication
@EnableDiscoveryClient
//原因：IDEA 需要在控制台输出中检测到端口启动日志，才会自动在运行配置名称后添加端口号。
public class aioveu_LssApplication {
    public static void main(String[] args) {


        System.out.println("Hello, World!");
        SpringApplication.run(aioveu_LssApplication.class, args);

        // 打印当前配置
        System.out.println("=== 调试信息 ===");
        // 创建应用
        SpringApplication app = new SpringApplication(aioveu_LssApplication.class);

        // 添加初始化器查看配置
        app.addInitializers((ApplicationContextInitializer<ConfigurableApplicationContext>) context -> {
            ConfigurableEnvironment env = context.getEnvironment();

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

        app.run(args);

    }
}