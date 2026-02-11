使用的是 **Nacos 命名空间（Namespace）** 来做环境隔离，而不是配置文件分组。这是更标准的做法

## Nacos 命名空间环境隔离方案

### 当前的配置问题

您希望：`本地环境配置`→ `对应Nacos命名空间`

比如：

- `dev`profile → `dev`namespace
- `prod`profile → `prod`namespace

### 正确的配置方式

**`application.yml`**（主配置）：

```
spring:
  application:
    name: aioveu-lss
  profiles:
    active: dev
  
  config:
    import:
      - classpath:codegen.yml
      - optional:nacos:${spring.application.name}.yaml?namespace=${spring.cloud.nacos.config.namespace}
```

**application-dev.yml**（开发环境）：

```
server:
  port: 9999

spring:
  cloud:
    nacos:
      # 注册中心
      discovery:
        server-addr: localhost:8848
        namespace: dev-namespace-id  # 开发命名空间ID
        group: DEFAULT_GROUP
        metadata:
          version: 1.0
          zone: zone1
      
      # 配置中心
      config:
        server-addr: localhost:8848
        file-extension: yaml
        namespace: dev-namespace-id  # 开发命名空间ID
        group: DEFAULT_GROUP
        import-check:
          enabled: false
```

**application-prod.yml**（生产环境）：

```
server:
  port: 8080

spring:
  cloud:
    nacos:
      discovery:
        server-addr: nacos.prod.com:8848
        namespace: prod-namespace-id  # 生产命名空间ID
        group: DEFAULT_GROUP
        metadata:
          version: 1.0
          zone: prod-zone
      
      config:
        server-addr: nacos.prod.com:8848
        namespace: prod-namespace-id  # 生产命名空间ID
        group: DEFAULT_GROUP
        import-check:
          enabled: false
```

## 获取 Nacos 命名空间 ID

### 方法1：通过控制台获取

1. 访问 Nacos 控制台
2. 左侧菜单点击"命名空间"
3. 复制命名空间的 **命名空间ID**（不是名称）

方法2：通过 API 获取

```
curl -X GET "http://localhost:8848/nacos/v1/console/namespaces"
```

## 动态命名空间配置

### 方案一：通过配置文件映射

**`application.yml`**：

```
spring:
  application:
    name: aioveu-lss
  profiles:
    active: dev
  
  config:
    import:
      - classpath:codegen.yml
      - optional:nacos:${spring.application.name}.yaml?namespace=${nacos.namespace.id}
  
  # 命名空间映射
  nacos:
    namespace:
      dev: dev-namespace-id
      test: test-namespace-id
      prod: prod-namespace-id
```

**application-dev.yml：**

```
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        namespace: ${spring.nacos.namespace.dev}
      config:
        server-addr: localhost:8848
        namespace: ${spring.nacos.namespace.dev}
```

### 方案二：通过 Profile 自动选择

**`bootstrap.yml`**（如果需要）或 `application.yml`：

## 完整的 Nacos 命名空间配置

### 1. 在 Nacos 中创建命名空间





复制

```
命名空间ID        命名空间名        描述
dev-namespace-id   dev            开发环境
test-namespace-id  test           测试环境
prod-namespace-id  prod           生产环境
```

### 2. 完整的配置文件

**`application.yml`**：

```
spring:
  application:
    name: aioveu-lss
  profiles:
    active: @activatedProperties@
  
  # 延迟数据源初始化
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
  
  config:
    import:
      - classpath:codegen.yml
      - optional:nacos:${spring.application.name}.yaml?namespace=${nacos.namespace.id}
  
  # 命名空间映射
  nacos:
    namespace:
      id: ${spring.nacos.namespace.${spring.profiles.active}}
      dev: dev-namespace-id
      test: test-namespace-id
      prod: prod-namespace-id
  
  main:
    allow-circular-references: true
  mvc:
    path-match:
      matching-strategy: ant_path_matcher
```

**`application-dev.yml`**：

```
server:
  port: 9999

spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: ${spring.nacos.namespace.dev}
        group: DEFAULT_GROUP
      
      config:
        server-addr: 127.0.0.1:8848
        namespace: ${spring.nacos.namespace.dev}
        group: DEFAULT_GROUP
        import-check:
          enabled: false
  
  mail:
    host: smtp.qq.com
    port: 587
    username: dev@example.com
    password: xxx
    properties:
      mail:
        smtp:
          auth: true
          ssl: true
          starttls:
            enable: true
    from: dev@example.com
```

**`application-prod.yml`**：

```
server:
  port: 8080

spring:
  cloud:
    nacos:
      discovery:
        server-addr: nacos.prod.com:8848
        namespace: ${spring.nacos.namespace.prod}
        group: DEFAULT_GROUP
      
      config:
        server-addr: nacos.prod.com:8848
        namespace: ${spring.nacos.namespace.prod}
        group: DEFAULT_GROUP
        import-check:
          enabled: false
  
  mail:
    host: smtp.exmail.qq.com
    port: 465
    username: prod@company.com
    password: xxx
    properties:
      mail:
        smtp:
          auth: true
          ssl: true
    from: prod@company.com
```

这样就能根据本地 profile 切换到对应的 Nacos 命名空间了。请告诉我您的 Nacos 命名空间 ID，