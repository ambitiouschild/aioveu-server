package com.aioveu;

import com.aioveu.ums.api.MemberFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *@ClassName: $ {NAME}
 *@Author: 雒世松
 *@Date: 2025/6/5 18:24
 *@Param:
 *@Return:
 *@Description: TODO
 **/

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {MemberFeignClient.class})
public class aioveu_PmsApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_PmsApplication.class, args);
    }
}