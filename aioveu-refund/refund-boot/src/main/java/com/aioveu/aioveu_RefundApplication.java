package com.aioveu;


import com.aioveu.pms.api.SkuFeignClient;
import com.aioveu.ums.api.MemberFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;



/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  退款微服务启动
 * @Date  2026/1/31 11:39
 * @Param
 * @return
 **/



@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = { MemberFeignClient.class, SkuFeignClient.class})
public class aioveu_RefundApplication {
    public static void main(String[] args) {


        System.out.println("退款微服务启动 Hello, World!");

        SpringApplication.run(aioveu_RefundApplication.class, args);
    }
}