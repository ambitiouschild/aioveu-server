package com.aioveu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author: 雒世松
 * @Date: 2025/5/28 22:23
 * @Param:
 * @Return:
 * @Description:
 **/

@SpringBootApplication
@MapperScan("com.atguigu.cloud.mapper") //import tk.mybatis.spring.annotation.MapperScan;
public class Main8001 {

    public static void main(String[] args)
    {
        SpringApplication.run(Main8001.class,args);
    }
}