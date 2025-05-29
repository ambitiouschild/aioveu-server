package com.aioveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/5/29 4:35
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@SpringBootApplication
@EnableDiscoveryClient //服务注册和发现
public class GateWay9527 {
    public static void main(String[] args)
    {
        SpringApplication.run(GateWay9527.class,args);
    }
}