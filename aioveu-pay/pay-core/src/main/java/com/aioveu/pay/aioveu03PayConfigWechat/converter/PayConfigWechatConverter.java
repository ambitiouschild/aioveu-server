package com.aioveu.pay.aioveu03PayConfigWechat.converter;

import com.aioveu.pay.aioveu03PayConfigWechat.model.entity.PayConfigWechat;
import com.aioveu.pay.aioveu03PayConfigWechat.model.form.PayConfigWechatForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: PayConfigWechatConverter
 * @Description TODO 微信支付配置对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:16
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayConfigWechatConverter {

    PayConfigWechatForm toForm(PayConfigWechat entity);

    PayConfigWechat toEntity(PayConfigWechatForm formData);
}
