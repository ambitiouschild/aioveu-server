package com.aioveu.oms.aioveu08MqConsumeRecord.converter;


import com.aioveu.oms.aioveu08MqConsumeRecord.model.entity.MqConsumeRecord;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.form.MqConsumeRecordForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: MqConsumeRecordConverter
 * @Description TODO MQ消息消费记录对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:31
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface MqConsumeRecordConverter {

    MqConsumeRecordForm toForm(MqConsumeRecord entity);

    MqConsumeRecord toEntity(MqConsumeRecordForm formData);
}
