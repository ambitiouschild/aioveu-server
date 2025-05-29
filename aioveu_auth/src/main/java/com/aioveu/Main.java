package com.aioveu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/5/29 13:09
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.high.sport.auth.dao")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}