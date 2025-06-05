package com.aioveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/6/5 18:42
 * @Param:
 * @Return:
 * @Description: TODO
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class aioveu_SmsApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_SmsApplication.class, args);
    }
}