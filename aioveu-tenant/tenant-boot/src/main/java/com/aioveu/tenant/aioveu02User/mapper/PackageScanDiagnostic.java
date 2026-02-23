package com.aioveu.tenant.aioveu02User.mapper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: PackageScanDiagnostic
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 18:06
 * @Version 1.0
 **/
@Component
public class PackageScanDiagnostic implements CommandLineRunner {

    private final ApplicationContext context;

    public PackageScanDiagnostic(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== 包扫描诊断 ===");

        // 1. 检查启动类包
        System.out.println("\n1. 启动类信息:");
        try {
            Class<?> appClass = Class.forName("com.aioveu.AioveuTenantApplication");
            System.out.println("   启动类: " + appClass.getName());
            System.out.println("   包: " + appClass.getPackage().getName());

            // 检查注解
            StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(appClass);
            System.out.println("   注解:");
            for (String annotation : metadata.getAnnotationTypes()) {
                System.out.println("     - " + annotation);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("   启动类不存在");
        }

        // 2. 检查自动配置包
        System.out.println("\n2. 自动配置包:");
        try {
            List<String> packages = AutoConfigurationPackages.get(context);
            System.out.println("   自动配置包: " + packages);
        } catch (Exception e) {
            System.err.println("   无法获取自动配置包: " + e.getMessage());
        }

        // 3. 检查 Mapper 是否在扫描范围内
        System.out.println("\n3. Mapper 扫描检查:");
        String mapperPackage = "com.aioveu.tenant.aioveu02User.mapper";
        String appPackage = "com.aioveu";

        System.out.println("   启动类包: " + appPackage);
        System.out.println("   Mapper 包: " + mapperPackage);

        boolean isInSubPackage = mapperPackage.startsWith(appPackage + ".");
        System.out.println("   Mapper 是启动类的子包: " + isInSubPackage);

        if (!isInSubPackage) {
            System.err.println("   ❌ Mapper 不在启动类的扫描范围内！");
            System.err.println("   解决方案：");
            System.err.println("   1. 移动启动类到 com.aioveu 包");
            System.err.println("   2. 或使用 @ComponentScan 指定包");
            System.err.println("   3. 或使用 @MapperScan 指定包");
        }

        // 4. 检查所有注册的 Mapper
        System.out.println("\n4. 已注册的 Mapper Bean:");
        String[] mapperBeans = context.getBeanNamesForType(
                org.apache.ibatis.annotations.Mapper.class);

        if (mapperBeans.length == 0) {
            System.err.println("   没有注册任何 Mapper Bean");

            // 检查所有 Bean
            System.out.println("\n5. 所有 Bean 数量:");
            String[] allBeans = context.getBeanDefinitionNames();
            System.out.println("   总 Bean 数: " + allBeans.length);

            // 查找 Mapper 相关的
            int mapperRelated = 0;
            for (String bean : allBeans) {
                if (bean.toLowerCase().contains("mapper")) {
                    System.out.println("   找到 Mapper 相关: " + bean);
                    mapperRelated++;
                }
            }
            System.out.println("   Mapper 相关 Bean: " + mapperRelated);
        } else {
            System.out.println("   注册了 " + mapperBeans.length + " 个 Mapper:");
            Arrays.sort(mapperBeans);
            for (String name : mapperBeans) {
                System.out.println("   - " + name);
            }
        }
    }
}
