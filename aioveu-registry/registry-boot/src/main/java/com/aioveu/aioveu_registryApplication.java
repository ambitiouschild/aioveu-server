package com.aioveu;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class aioveu_registryApplication {

    public static void main(String[] args) {

        SpringApplication.run(aioveu_registryApplication.class, args);
        System.out.printf("微信小程序注册备案服务~ Hello and welcome!");
    }


}