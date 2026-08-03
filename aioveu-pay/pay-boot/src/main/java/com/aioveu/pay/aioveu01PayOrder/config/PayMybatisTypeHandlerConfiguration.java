package com.aioveu.pay.aioveu01PayOrder.config;


import com.aioveu.common.core.enums.pay.PaymentChannelEnum;
import com.aioveu.common.core.enums.pay.PaymentMethodEnum;

import com.aioveu.common.core.util.PaymentChannelEnumPayTypeHandler;
import com.aioveu.common.core.util.PaymentMethodEnumPayTypeHandler;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: PayMybatisConfig
 * @Description TODO 各自服务注册 TypeHandler
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/25 19:13
 * @Version 1.0
 **/
@Configuration
public class PayMybatisTypeHandlerConfiguration {

    @Bean
    public ConfigurationCustomizer payTypeHandlerConfig() {
        return configuration -> {
            configuration.getTypeHandlerRegistry()
                    .register(PaymentMethodEnum.class, PaymentMethodEnumPayTypeHandler.class);
            configuration.getTypeHandlerRegistry()
                    .register(PaymentChannelEnum.class, PaymentChannelEnumPayTypeHandler.class);
        };
    }
}
