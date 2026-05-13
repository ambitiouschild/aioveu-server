package com.aioveu.pay.aioveu12MqProducerPayment.config.Kafka;


import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: InterceptorKafkaConfig
 * @Description TODO 拦截器配置
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/13 18:38
 * @Version 1.0
 **/
@Configuration
public class InterceptorKafkaConfig {


    @Bean
    public ProducerFactory<String, String> interceptorProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // 添加拦截器
        configProps.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,
                Arrays.asList(
                        "com.yourcompany.kafka.interceptor.MetricsInterceptor",
                        "com.yourcompany.kafka.interceptor.TracingInterceptor"
                ));

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> interceptorKafkaTemplate() {
        return new KafkaTemplate<>(interceptorProducerFactory());
    }
}
