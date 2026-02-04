package com.aioveu.pay.aioveu03PayChannelConfig.converter;

import com.aioveu.pay.aioveu03PayChannelConfig.model.entity.PayChannelConfig;
import com.aioveu.pay.aioveu03PayChannelConfig.model.form.PayChannelConfigForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayChannelConfigConverter
 * @Description TODO 支付渠道配置对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:02
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface PayChannelConfigConverter {

    PayChannelConfigForm toForm(PayChannelConfig entity);

    PayChannelConfig toEntity(PayChannelConfigForm formData);
}
