package com.aioveu.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Description: TODO Redis缓存配置
 *                      用于配置 Spring Cache 与 Redis 集成，提供分布式缓存功能
 *                      主要功能：
 *                      1. 启用 Spring 缓存注解支持
 *                      2. 自定义 Redis 缓存序列化方式
 *                      3. 配置缓存过期时间、空值缓存等策略
 *                      此配置提供了生产级的 Redis 缓存配置，兼顾了性能、可维护性和安全性。
 * @Author: 雒世松
 * @Date: 2025/6/5 16:04
 * @param
 * @return:
 **/

@Slf4j
@EnableCaching   // 启用 Spring 的缓存注解功能，允许在方法上使用 @Cacheable、@CacheEvict 等注解
@EnableConfigurationProperties(CacheProperties.class) // 启用缓存配置属性，使得 application.yml 中的 spring.cache.* 配置生效
@Configuration  // 标记此类为 Spring 配置类
@ConditionalOnProperty(name = "spring.cache.enabled") // 条件装配：只有当配置文件中 spring.cache.enabled=true 时，此配置类才会生效
public class RedisCacheConfig {


    /**
     *          TODO            创建并配置 RedisCacheManager Bean
     *                      RedisCacheManager 是 Spring Cache 抽象与 Redis 之间的桥梁
     *                      负责管理所有缓存的创建、获取、删除等操作
     *                      配置说明：
     *                      1. 使用无锁的 RedisCacheWriter 提高性能
     *                      2. 应用自定义的 RedisCacheConfiguration
     *                      3. 支持多个缓存区域（Cache Names）的不同配置
     *
     * @param redisConnectionFactory Redis 连接工厂，自动注入
     *        用于创建与 Redis 服务器的连接
     * @param cacheProperties 缓存配置属性，自动注入
     *        包含 application.yml 中 spring.cache.redis.* 的配置
     * @return 配置好的 RedisCacheManager 实例
     */
    @Bean
    public RedisCacheManager redisCacheManager(
            RedisConnectionFactory redisConnectionFactory,
            CacheProperties cacheProperties){

        log.info("构建 RedisCacheManager");
        return RedisCacheManager.builder(

                        // 创建 RedisCacheWriter，负责实际的缓存读写操作
                        // nonLockingRedisCacheWriter：无锁实现，适合高并发场景
                        // 与 lockingRedisCacheWriter 的区别：
                        // 1. 无锁版本：性能更高，适合缓存数据不常更新的场景
                        // 2. 加锁版本：保证缓存一致性，适合缓存数据频繁更新的场景
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory)
                )
                // 设置默认的缓存配置
                .cacheDefaults(redisCacheConfiguration(cacheProperties))

                // 可选的额外配置（示例）：
                // .withInitialCacheConfigurations(customConfigs)  // 为特定缓存名设置不同配置
                // .transactionAware()  // 启用事务支持
                .build();
    }

    /**
     *       TODO               创建并配置 RedisCacheConfiguration
     *                      定义 Redis 缓存的核心配置，包括：
     *                      1. 序列化方式
     *                      2. 过期时间
     *                      3. 空值缓存策略
     *                      4. 键前缀策略
     *                      默认序列化问题说明：
     *                      如果不自定义配置，Spring Boot 会使用 JdkSerializationRedisSerializer
     *                      会导致 Redis 中存储的数据不可读，且占用空间较大
     *
     * @param cacheProperties 缓存配置属性
     *        包含 spring.cache.redis 下的所有配置
     * @return 配置好的 RedisCacheConfiguration 实例
     */
    @Bean
    RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {

        log.info("获取 Spring Data Redis 的默认缓存配置");
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

        log.info("配置键的序列化方式");
        log.info("使用 StringRedisSerializer 序列化缓存键");
        log.info("好处：可读性好，便于在 Redis CLI 中查看和管理");
        config = config.serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                        RedisSerializer.string()
                )
        );

        log.info("配置值的序列化方式");
        log.info("使用 JSON 序列化器序列化缓存值");
        log.info("好处：数据可读，跨语言兼容，支持复杂对象");
        config = config.serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                        RedisSerializer.json()
                )
        );
        log.info("获取 Redis 特定的配置");
        CacheProperties.Redis redisProperties = cacheProperties.getRedis();

        log.info("配置缓存过期时间（TTL）");
        log.info("从配置文件读取 spring.cache.redis.time-to-live");
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }

        log.info("配置是否缓存空值");
        log.info("从配置文件读取 spring.cache.redis.cache-null-values");
        log.info("默认不缓存 null 值，避免缓存穿透");
        if (!redisProperties.isCacheNullValues()) {
            config = config.disableCachingNullValues();
        }

        log.info("配置是否使用键前缀");
        log.info("从配置文件读取 spring.cache.redis.use-key-prefix");
        log.info("键前缀格式：缓存名::键名");
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        log.info("自定义键前缀生成策略");
        log.info("默认格式是 \"缓存名::键名\"，这里修改为 \"缓存名:键名\"");
        log.info(" computePrefixWith 会覆盖默认的 CacheKeyPrefix.prefixed() 实现");
        config = config.computePrefixWith(name -> name + ":");//覆盖默认key双冒号  CacheKeyPrefix#prefixed
        return config;
    }


}
