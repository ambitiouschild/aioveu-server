package com.aioveu;


import com.aioveu.order.api.OrderFeignClient;
import com.aioveu.pms.api.SkuFeignClient;
import com.aioveu.ums.api.MemberFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

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
@EnableFeignClients(basePackages = {
        "com.aioveu.ums.api",
        "com.aioveu.order.api",
        "com.aioveu.pms.api",
        "com.aioveu.tenant.api"
})
// 在启动类添加 @EnableScheduling
@EnableScheduling
public class aioveu_PayApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_PayApplication.class, args);
        System.out.println("支付微服务系统启动 Hello, World!");
    }
}