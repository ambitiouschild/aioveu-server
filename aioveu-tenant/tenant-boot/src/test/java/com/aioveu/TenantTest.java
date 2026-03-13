package com.aioveu;

import com.aioveu.common.security.model.UserAuthInfoWithTenantId;
import com.aioveu.common.tenant.TenantContextHolder;
import com.aioveu.tenant.aioveu02User.service.UserService;
import com.aioveu.tenant.aioveu04Menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @ClassName: com.aioveu.TenantTest
 * @Description TODO 测试数据隔离
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/13 20:44
 * @Version 1.0
 **/
//@SpringBootTest(classes = aioveu_TenantApplication.class)  // 自动扫描配置
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // 使用测试配置文件
public class TenantTest {

    @Autowired
    private UserService userService; // 字段注入

    @Autowired
    private MenuService menuService;

//    @Test
//    public void debugBean() {
//        // 查看所有Bean
//        String[] beanNames = applicationContext.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
//    }

    @Test
    public void testContextLoads() {
        // 简单测试，验证Spring上下文加载成功
        System.out.println("Spring Boot上下文加载成功！");
        System.out.println("userService: " + userService);
        System.out.println("menuService: " + menuService);
    }

    @Test
    public void testTenantIsolation() {

        String admin = "admin";
        // 设置租户1
        TenantContextHolder.setTenantId(1L);
        Long tenantId1 =TenantContextHolder.getTenantId();
        UserAuthInfoWithTenantId userAuthInfo1 = userService.getAuthInfoByUsernameAndTenantId(admin,tenantId1);

        // 设置租户2
        TenantContextHolder.setTenantId(2L);
        Long tenantId2 =TenantContextHolder.getTenantId();
        UserAuthInfoWithTenantId userAuthInfo2 = userService.getAuthInfoByUsernameAndTenantId(admin,tenantId2);

        // 验证两个租户看到的数据不同
        assertNotEquals(userAuthInfo1, userAuthInfo2);
        // 验证租户数据隔离
        // 这里可以添加断言验证
    }

    @Test
    public void testWithoutTenant() {
        // 测试不设置租户的情况
        TenantContextHolder.clear();

        try {
            userService.list();
            // 如果没有抛出异常，说明多租户插件配置有问题
            System.err.println("警告：多租户插件可能未正确配置！");
        } catch (Exception e) {
            System.out.println("正确捕获异常（未设置租户ID）：" + e.getMessage());
        }
    }
}
