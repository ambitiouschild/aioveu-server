package com.aioveu;

import com.aioveu.system.api.UserFeignClient;
import com.aioveu.ums.api.MemberFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/6/5 13:17
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@EnableFeignClients(basePackageClasses = {UserFeignClient.class, MemberFeignClient.class})
@SpringBootApplication
@EnableDiscoveryClient
public class aioveu_AuthApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_AuthApplication.class, args);
    }
}