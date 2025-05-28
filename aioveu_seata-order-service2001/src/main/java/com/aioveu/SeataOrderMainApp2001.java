package main.java.com.aioveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

/**
 *@ClassName: $ {NAME}
 *@Author: 雒世松
 *@Date: 2025/5/29 3:39
 *@Param:
 *@Return:
 *@Description: TODO
 **/

@SpringBootApplication
@MapperScan("com.aioveu.mapper") //import tk.mybatis.spring.annotation.MapperScan;
@EnableDiscoveryClient //服务注册和发现
@EnableFeignClients
public class SeataOrderMainApp2001 {

    public static void main(String[] args)
    {
        SpringApplication.run(SeataOrderMainApp2001.class,args);
    }
}
