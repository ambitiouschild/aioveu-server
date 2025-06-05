package com.aioveu;

import com.aioveu.pms.api.SkuFeignClient;
import com.aioveu.ums.api.MemberFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName: $ {NAME}
 * @Author: 雒世松
 * @Date: 2025/6/5 17:58
 * @Param:
 * @Return:
 * @Description: TODO
 **/

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = { MemberFeignClient.class, SkuFeignClient.class})
public class aioveu_OmsApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_OmsApplication.class, args);
    }
}