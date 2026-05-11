package com.aioveu.oms.aioveu10MqConsumeIdempotent.converter;


import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.entity.MqConsumeIdempotent;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.form.MqConsumeIdempotentForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: MqConsumeIdempotentConverter
 * @Description TODO MQ消费幂等性对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 18:20
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface MqConsumeIdempotentConverter {

    MqConsumeIdempotentForm toForm(MqConsumeIdempotent entity);

    MqConsumeIdempotent toEntity(MqConsumeIdempotentForm formData);
}
