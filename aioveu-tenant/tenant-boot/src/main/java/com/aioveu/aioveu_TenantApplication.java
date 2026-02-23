package com.aioveu;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableConfigurationProperties  // 启用配置属性扫描
// 确保扫描到正确的包
@MapperScan("com.aioveu.tenant.**.mapper") // 2. 加上这行注解
public class aioveu_TenantApplication {


    public static void main(String[] args) {

        SpringApplication.run(

                aioveu_TenantApplication.class, args);
    }

}