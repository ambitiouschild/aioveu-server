package com.aioveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName: $ Nacos 启动入口  http://localhost:8000/
 * @Author: 雒世松
 * @Date: 2025/5/29 12:10
 * @Param:
 * @Return:
 * @Description: TODO
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class Nacos8000 {
    public static void main(String[] args) {

        SpringApplication.run(Nacos8000.class, args);
    }
}