package com.aioveu;

import com.aioveu.common.mail.config.property.MailProperties;
import com.aioveu.system.api.UserFeignClient;
import com.aioveu.ums.api.MemberFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
// 2. 在主应用类中启用配置属性
@EnableConfigurationProperties(MailProperties.class)  // 明确注册
public class aioveu_AuthApplication {
    public static void main(String[] args) {

        SpringApplication.run(aioveu_AuthApplication.class, args);
    }
}