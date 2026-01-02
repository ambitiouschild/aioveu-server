package com.aioveu;


import com.aioveu.lss.api.LssFeignClient;
import com.aioveu.system.api.SystemFeignClient;
import com.aioveu.ums.api.MemberFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/6/5 13:17
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@EnableFeignClients(basePackageClasses = {
        SystemFeignClient.class,
        LssFeignClient.class,
        MemberFeignClient.class})
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {
        "com.aioveu.auth",           // 当前模块
        "com.aioveu.common",          // 显式扫描 common 模块
})
// 2. 在主应用类中启用配置属性
public class aioveu_AuthApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_AuthApplication.class, args);
    }
}