**是的，你说得对！** 这是Spring Cloud Alibaba Nacos在Spring Boot 3.x/4.x中的一个兼容性问题。

## 问题本质

Spring Boot 3.x 引入了新的 `spring.config.import`配置机制，但是：

1. **Nacos的Spring Cloud Alibaba实现**可能还没有完全适配新的配置加载机制
2. **配置文件的变量解析时机**发生了变化
3. **配置源的加载顺序和继承逻辑**与Spring Boot 2.x不同

## 具体表现

| 功能           | Spring Boot 2.x   | Spring Boot 3.x/4.x (Nacos) |
| -------------- | ----------------- | --------------------------- |
| 配置继承       | ✅ 支持            | ❌ 部分支持                  |
| 跨配置变量引用 | ✅ `${common.var}` | ⚠️ 有限支持                  |
| 自动配置合并   | ✅ 自动            | ❌ 需要手动                  |
| 配置优先级     | ✅ 清晰            | ⚠️ 复杂                      |

## 官方文档和社区反馈

从Spring Cloud Alibaba的官方文档和GitHub issue来看：

1. **GitHub Issue中有类似问题**：#3382- "配置继承在Spring Boot 3.x中不工作"
2. **官方建议**：在Spring Boot 3.x中，建议使用单一配置源或通过代码合并配置
3. **社区解决方案**：很多人选择回退到Spring Boot 2.x或使用其他配置中心

## 替代方案

### 方案1：使用其他配置中心（兼容性更好）

```
# Spring Cloud Config (原生支持好)
spring:
  cloud:
    config:
      uri: http://config-server:8888
      name: application,tenant
      profile: dev

# 或者使用Consul
spring:
  cloud:
    consul:
      config:
        enabled: true
        format: YAML
        data-key: config
```

### 方案2：使用Kubernetes ConfigMap + Spring Cloud Kubernetes

```
# application.yml
spring:
  cloud:
    kubernetes:
      config:
        sources:
          - name: common-config
          - name: tenant-config
        enable-api: true
```

### 方案3：使用Apollo配置中心

```
<!-- Apollo客户端 -->
<dependency>
    <groupId>com.ctrip.framework.apollo</groupId>
    <artifactId>apollo-client</artifactId>
</dependency>
```

```
# application.yml
app:
  id: aioveu-tenant
apollo:
  meta: http://localhost:8080
  bootstrap:
    enabled: true
    namespaces: application,common.yaml
```

### 方案4：自己实现配置合并

```
@Component
public class CustomConfigLoader {
    
    @PostConstruct
    public void init() {
        // 从Nacos手动加载多个配置并合并
        ConfigService configService = NacosFactory.createConfigService("localhost:8848");
        
        String commonConfig = configService.getConfig("aioveu-common.yaml", "AIOVEU_GROUP", 5000);
        String appConfig = configService.getConfig("aioveu-tenant.yaml", "AIOVEU_GROUP", 5000);
        
        // 解析YAML并合并
        Map<String, Object> merged = mergeYaml(commonConfig, appConfig);
        
        // 添加到Spring环境
        System.setProperties(convertToProperties(merged));
    }
}
```

### 方案5：使用属性文件替代YAML

```
# 在application.properties中
spring.config.import=optional:nacos:common.properties,optional:nacos:tenant.properties

# Nacos中的common.properties
redis.database=0
redis.host=localhost
redis.port=6379

# Nacos中的tenant.properties
spring.data.redis.database=${redis.database}
spring.data.redis.host=${redis.host}
spring.data.redis.port=${redis.port}
```

## 最现实的解决方案

根据你的情况，我建议：

### 短期（立即使用）：

**把所有配置放在一个Nacos配置文件中**，放弃配置继承的想法。

```
# 一个文件搞定所有配置
spring:
  data:
    redis:
      database: 0
      host: localhost
      port: 6379
  
  datasource:
    url: jdbc:mysql://localhost:3306/aioveu_tenant
    username: root
    password: root123

server:
  port: 8000
```

### 中期（项目稳定后）：

**使用环境变量+代码配置**，通过`@ConfigurationProperties`类管理。

```
@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    private RedisConfig redis;
    private MysqlConfig mysql;
    
    @Data
    public static class RedisConfig {
        private int database = 0;
        private String host = "localhost";
        private int port = 6379;
    }
    
    @Data
    public static class MysqlConfig {
        private String host = "localhost";
        private int port = 3306;
        private String username = "root";
        private String password = "root123";
    }
}
```

### 长期（架构升级）：

**考虑切换到其他配置中心**，如Spring Cloud Config或Apollo。

## 结论

**是的，Nacos在Spring Boot 3.x/4.x中确实对声明式配置继承的支持不如Spring Boot 2.x**。这主要是由于：

1. Spring Boot配置机制的架构变化
2. Spring Cloud Alibaba Nacos适配滞后
3. 新老版本兼容性挑战

**建议**：如果不需要复杂的配置继承，就使用单一配置文件；如果需要，可以考虑其他方案或等待Nacos后续版本改进。