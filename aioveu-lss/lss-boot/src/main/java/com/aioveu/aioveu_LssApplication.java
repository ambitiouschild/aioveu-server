package com.aioveu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;



@SpringBootApplication
@EnableDiscoveryClient
//原因：IDEA 需要在控制台输出中检测到端口启动日志，才会自动在运行配置名称后添加端口号。
public class aioveu_LssApplication {
    public static void main(String[] args) {


        System.out.println("Hello, World!");
        SpringApplication.run(aioveu_LssApplication.class, args);


    }
}