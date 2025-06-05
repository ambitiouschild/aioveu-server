package com.aioveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/6/5 18:55
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@SpringBootApplication
@EnableDiscoveryClient
public class aioveu_UmsApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_UmsApplication.class, args);
    }
}