package com.aioveu.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;


/**
 * @Description: TODO Redis 配置
 *                   用于自定义和配置 Redis 相关的 Bean
 *                  这个配置是 Redis 在 Spring Boot 项目中最佳实践的配置方式，兼顾了性能、可读性和兼容性。
 * @Author: 雒世松
 * @Date: 2025/6/5 16:04
 * @param
 * @return:
 **/
@Slf4j
@Configuration   // 标记此类为 Spring 配置类，Spring 容器启动时会自动加载此类
public class RedisConfig {


    /**
     *       TODO           创建并配置 RedisTemplate Bean
     *                  主要作用：修改 Redis 的默认序列化方式，从默认的 JDK 序列化改为更高效的序列化方式
     *                  默认的 JdkSerializationRedisSerializer 存在的问题：
     *                  1. 序列化后的数据可读性差（二进制格式）
     *                  2. 序列化后的数据体积较大
     *                  3. 不同 JVM 版本之间可能存在兼容性问题
     *                  配置说明：
     *                  1. Key 使用 StringRedisSerializer：将 String 键序列化为字节数组，提高可读性和兼容性
     *                  2. Value 使用 GenericJackson2JsonRedisSerializer：将对象序列化为 JSON 格式
     *                     优势：可读性好、体积小、跨语言兼容
     *
     * @param redisConnectionFactory Redis 连接工厂，由 Spring Boot 自动注入
     *        负责创建和管理 Redis 连接
     * @return 配置好的 RedisTemplate 实例
     */
    @Bean  // 声明此方法返回的对象是一个 Spring Bean，Bean 名称默认为方法名 "redisTemplate"
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        log.info("创建 RedisTemplate 实例，指定键类型为 String，值类型为 Object");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        log.info("设置 Redis 连接工厂，这是 RedisTemplate 与 Redis 服务器通信的基础");
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        log.info("配置键的序列化器");
        log.info("使用 StringRedisSerializer 序列化 String 类型的键");
        log.info("序列化结果：字符串 -> 字节数组 (UTF-8编码)");
        redisTemplate.setKeySerializer(RedisSerializer.string());

        log.info("配置值的序列化器");
        log.info("使用 JSON 序列化器，将对象序列化为 JSON 字符串");
        log.info("支持各种 Java 对象到 JSON 的转换");
        redisTemplate.setValueSerializer(RedisSerializer.json());


        log.info("配置 Hash 数据结构的键序列化器");
        log.info("Hash 的键也使用 String 序列化");
        redisTemplate.setHashKeySerializer(RedisSerializer.string());

        log.info("配置 Hash 数据结构的值的序列化器");
        log.info("Hash 的值也使用 JSON 序列化");
        redisTemplate.setHashValueSerializer(RedisSerializer.json());


        log.info("初始化模板属性");
        log.info("这个方法必须调用，用于应用所有的配置设置");
        log.info("在设置完所有属性后调用，确保配置生效");
        redisTemplate.afterPropertiesSet();

        log.info("返回配置好的 RedisTemplate Bean");
        return redisTemplate;
    }


}
