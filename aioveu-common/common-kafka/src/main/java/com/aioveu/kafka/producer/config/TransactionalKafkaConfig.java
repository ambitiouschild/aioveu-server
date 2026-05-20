package com.aioveu.kafka.producer.config;


import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: TransactionalKafkaConfig
 * @Description TODO 高级配置：事务、拦截器等
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:37
 * @Version 1.0
 **/


/*
*  TODO     解决方案：
            1.使用@Qualifier明确指定注入哪个Bean
            2.创建缺失的Bean类型（KafkaTemplate<String, Object>）
            3。简化设计，只使用一个KafkaTemplate
            4.标记主Bean，使用@Primary注解
*
* */
@Configuration
@EnableKafka
public class TransactionalKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, String> transactionalProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 事务相关配置
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);

        DefaultKafkaProducerFactory<String, String> factory =
                new DefaultKafkaProducerFactory<>(configProps);

        // 设置事务ID前缀
        factory.setTransactionIdPrefix("tx-");

        return factory;
    }

    @Bean
    public KafkaTemplate<String, String> transactionalKafkaTemplate() {
        return new KafkaTemplate<>(transactionalProducerFactory());
    }

}
