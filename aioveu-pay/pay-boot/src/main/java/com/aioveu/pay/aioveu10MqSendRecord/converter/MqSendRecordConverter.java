package com.aioveu.pay.aioveu10MqSendRecord.converter;


import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: MqSendRecordConverter
 * @Description TODO MQ消息发送记录对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:46
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface MqSendRecordConverter {

    MqSendRecordForm toForm(MqSendRecord entity);

    MqSendRecord toEntity(MqSendRecordForm formData);
}
