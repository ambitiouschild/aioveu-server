package com.aioveu;


import com.aioveu.pms.api.SkuFeignClient;
import com.aioveu.ums.api.MemberFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 支付微服务系统启动
 * @Date  2026/2/2 16:55
 * @Param
 * @return
 **/

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@EnableFeignClients(basePackageClasses = { MemberFeignClient.class, SkuFeignClient.class})
public class aioveu_PayApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_PayApplication.class, args);
        System.out.println("支付微服务系统启动 Hello, World!");
    }
}