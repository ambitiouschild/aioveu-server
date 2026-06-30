package com.aioveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *@ClassName: $ {NAME}
 *@Author: 雒世松
 *@Date: 2025/6/5 13:14
 *@Param:
 *@Return:
 *@Description: TODO
 **/

@SpringBootApplication
@EnableDiscoveryClient
//删掉所有 Feign 相关（重要）
public class aioveu_GatewayApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_GatewayApplication.class, args);
    }
}