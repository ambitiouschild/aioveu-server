package com.aioveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/5/29 3:05
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@EnableDiscoveryClient
@SpringBootApplication
public class Main8401 {
    public static void main(String[] args)
    {
        SpringApplication.run(Main8401.class,args);
    }
}