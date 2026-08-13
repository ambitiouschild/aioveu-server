这是一个典型的**Redis数据污染/冲突问题**，在微服务架构中很常见。

🔍 **问题原因分析**

1. **键命名冲突（最主要原因）**

```
# 两个微服务都使用相同的缓存键
微服务A: 存储权限数据，键为 "permission:user:1001"
微服务B: 也存储其他数据，但使用了相同的键名模式
# 结果：A服务的数据被B服务覆盖
```

2. **Redis序列化不兼容**

```
// 场景：两个微服务使用不同的序列化方式
微服务A: 使用JSON序列化
微服务B: 使用Jdk序列化（默认）
// 结果：读取时反序列化失败，显示为null
```

3. **缓存配置不隔离**

```
# 默认情况下，所有服务共享Redis的db0
# 没有做数据库隔离
spring.redis.database=0  # 两个服务都用了db0
```

## ✅ **解决方案**

### **方案1：为每个微服务添加命名空间（推荐）**

```
@Configuration
public class RedisCacheConfig {
    
    @Value("${spring.application.name:unknown-app}")
    private String appName;
    
    @Bean
    public RedisCacheManager redisCacheManager(
            RedisConnectionFactory redisConnectionFactory, 
            CacheProperties cacheProperties) {
        
        RedisCacheConfiguration defaultConfig = redisCacheConfiguration(cacheProperties);
        
        return RedisCacheManager.builder(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
            .cacheDefaults(defaultConfig)
            .build();
    }
    
    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        
        // 关键：为每个微服务添加应用名前缀
        config = config.computePrefixWith(cacheName -> 
            appName + ":" + cacheName + ":"
        );
        
        // 其他配置...
        return config;
    }
}
```

方案2：使用不同的Redis数据库

```
# application.yml 微服务A
spring:
  application:
    name: auth-service
  redis:
    database: 0  # 权限服务用db0
    host: localhost
    port: 6379

# application.yml 微服务B
spring:
  application:
    name: order-service
  redis:
    database: 1  # 订单服务用db1
    host: localhost
    port: 6379
```

## 🛡️ **权限数据特殊处理建议**

对于权限这种**系统级别**的数据，建议独立处理：

方案1：使用独立的Redis实例/集群

```
# 权限服务专用Redis
permission:
  redis:
    host: redis-auth.xxx.com
    port: 6379
    database: 0

# 业务服务Redis
spring:
  redis:
    host: redis-biz.xxx.com
    port: 6379
    database: 0
```

🔧 **诊断工具：查看Redis中实际数据**

创建一个诊断工具，查看Redis中存储的实际数据：

```
@Component
public class RedisDataDiagnoser {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    public void diagnosePermissionData(Long userId) {
        // 查看所有可能的键
        Set<String> keys = redisTemplate.keys("*permission*");
        System.out.println("权限相关键：" + keys);
        
        // 查看具体的权限数据
        String keyPattern = "*:permission:user:" + userId;
        keys = redisTemplate.keys(keyPattern);
        
        for (String key : keys) {
            Object value = redisTemplate.opsForValue().get(key);
            System.out.println("键：" + key);
            System.out.println("值类型：" + (value != null ? value.getClass() : "null"));
            System.out.println("值内容：" + value);
        }
    }
}
```

