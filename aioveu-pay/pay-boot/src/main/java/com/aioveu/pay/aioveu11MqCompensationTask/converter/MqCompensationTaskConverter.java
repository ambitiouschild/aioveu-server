package com.aioveu.pay.aioveu11MqCompensationTask.converter;


import com.aioveu.pay.aioveu11MqCompensationTask.model.entity.MqCompensationTask;
import com.aioveu.pay.aioveu11MqCompensationTask.model.form.MqCompensationTaskForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: MqCompensationTaskConverter
 * @Description TODO MQ补偿任务对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:53
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface MqCompensationTaskConverter {

    MqCompensationTaskForm toForm(MqCompensationTask entity);

    MqCompensationTask toEntity(MqCompensationTaskForm formData);
}
