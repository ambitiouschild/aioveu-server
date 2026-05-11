package com.aioveu.oms.aioveu09MqDeadLetter.converter;


import com.aioveu.oms.aioveu09MqDeadLetter.model.entity.MqDeadLetter;
import com.aioveu.oms.aioveu09MqDeadLetter.model.form.MqDeadLetterForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: MqDeadLetterConverter
 * @Description TODO MQ死信队列对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:51
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface MqDeadLetterConverter {

    MqDeadLetterForm toForm(MqDeadLetter entity);

    MqDeadLetter toEntity(MqDeadLetterForm formData);
}
