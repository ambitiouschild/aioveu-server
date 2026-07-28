package com.aioveu.oms.aioveu01Order.config;


import com.aioveu.common.core.enums.pay.PaymentChannelEnum;
import com.aioveu.common.core.enums.pay.PaymentMethodEnum;

import com.aioveu.common.core.util.PaymentChannelEnumOmsTypeHandler;
import com.aioveu.common.core.util.PaymentMethodEnumOmsTypeHandler;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: OmsMybatisConfig
 * @Description TODO 各自服务注册 TypeHandler
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/25 19:13
 * @Version 1.0
 **/
@Configuration
public class OmsMybatisConfig {

    @Bean
    public ConfigurationCustomizer omsTypeHandlerConfig() {
        return configuration -> {
            configuration.getTypeHandlerRegistry()
                    .register(PaymentMethodEnum.class, PaymentMethodEnumOmsTypeHandler.class);
            configuration.getTypeHandlerRegistry()
                    .register(PaymentChannelEnum.class, PaymentChannelEnumOmsTypeHandler.class);
        };
    }
}
